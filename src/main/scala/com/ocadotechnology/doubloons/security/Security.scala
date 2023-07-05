package com.ocadotechnology.doubloons.security

import cats.effect.IO
import cats.implicits.*
import com.ocadotechnology.doubloons.security.Cryptography
import com.ocadotechnology.doubloons.security.Cryptography.DecryptionResult
import com.ocadotechnology.doubloons.security.UserSession
import com.ocadotechnology.doubloons.user.Email
import com.ocadotechnology.sttp.oauth2.AuthorizationCodeProvider
import com.ocadotechnology.sttp.oauth2.OAuth2TokenResponse
import com.ocadotechnology.sttp.oauth2.Secret
import com.ocadotechnology.sttp.oauth2.TokenIntrospection
import com.ocadotechnology.sttp.oauth2.json.circe.instances.*
import eu.timepit.refined.api.*
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.refined.*
import io.circe.syntax.*
import mouse.all.*
import sttp.model.Uri.*
import sttp.model.*

import java.util.UUID
import cats.data.EitherT
import com.ocadotechnology.doubloons.user.UserService
import io.github.arainko.ducktape.*
import com.ocadotechnology.doubloons.user.User

trait Security[F[_]] {
  def decodeAndIntrospectToken(
      cookieValue: Secret[String]
  ): F[Either[SecurityError, UserSession]]

  def issueCookieAndRedirect(
      accessCode: String
  ): F[Either[SecurityError, (Uri, Secret[String])]]

  def ssoRedirect(): F[Uri]
}

object Security {
  def ioInstance(
      crypto: Cryptography[IO],
      cryptoSecret: Secret[String],
      oidc: OIDC[IO],
      userService: UserService
  ): Security[IO] =
    new Security[IO] {

      private def introspectToken(
          token: Secret[String]
      ): IO[Either[SecurityError, UserSession]] = ???

      override def ssoRedirect(): IO[Uri] =
        // todo - left as an IO as we might want to generate CSRF validation later
        IO.pure(oidc.ssoRedirect)

      override def issueCookieAndRedirect(
          accessCode: String
      ): IO[Either[SecurityError, (Uri, Secret[String])]] = {
        for {
          token <- oidc.authCodeToToken(accessCode).liftEitherT
          userData <- oidc.userInfo(token.access_token).liftEitherT
          _ <- registerUserOnFirstLogin(userData).liftEitherT
          jsonUserData = userData.asJson.noSpaces
          encryptedUserData <- crypto
            .encrypt(jsonUserData, cryptoSecret.value)
            .liftEitherT[SecurityError]
          cookieValue = Secret(encryptedUserData)
          redirectUri = uri"/"
        } yield (redirectUri, cookieValue)
      }.value

      override def decodeAndIntrospectToken(
          cookieValue: Secret[String]
      ): IO[Either[SecurityError, UserSession]] = {
        for {
          session <- crypto
            .decrypt(cookieValue.value, cryptoSecret.value)
            .map(convertDecryptionResult(_))
            .liftEitherT
          introspected <- oidc.userInfo(session.token.value).liftEitherT
        } yield introspected
      }.value

      private def convertDecryptionResult(decryptionResult: DecryptionResult) =
        decryptionResult match {
          case DecryptionResult.Failed(error) =>
            Left(
              SecurityError.Unauthenticated(
                "Failed to process session cookie",
                UUID
                  .randomUUID()
                  .toString // todo replace with safe UUID generation later
              )
            )
          case DecryptionResult.Successful(result) =>
            io.circe.parser.decode[UserSession](result).leftMap { error =>
              SecurityError.Unauthenticated(
                "Failed to decode session cookie",
                UUID
                  .randomUUID()
                  .toString // todo replace with safe UUID generation later
              )
            }
        }

      private def registerUserOnFirstLogin(user: UserSession): IO[Unit] =
        userService.getUserByEmail(user.email.value).flatMap {
          case Left(value) =>
            IO.println(
              s"User ${user.email} first login, creating internal account"
            ) *>
              userService
                .createUser(
                  user
                    .into[User]
                    .transform(
                      Field
                        .const(_.password, Refined.unsafeApply("irrelevant")),
                      Field.const(_.avatar, None),
                      Field.const(_.teamId, None)
                    )
                )
                .void
          case Right(value) => IO.unit
        }
    }
}

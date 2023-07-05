package com.ocadotechnology.doubloons.security

import com.ocadotechnology.sttp.oauth2.Secret
import cats.effect.IO
import sttp.client3.*
import sttp.model.*
import sttp.client3.circe.*
import io.circe.Codec
import java.util.UUID
import eu.timepit.refined.api.*

trait OIDC[F[_]] {
  def userInfo(
      token: String
  ): F[Either[SecurityError, UserSession]]

  def authCodeToToken(
      code: String
  ): F[Either[SecurityError, OIDC.TokenResponse]]

  def ssoRedirect: Uri
}

object OIDC {
  def ioInstance(
      baseUrl: Uri,
      redirectUri: Uri,
      clientId: String,
      clientSecret: Secret[String],
      scope: String
  )(sttpBackend: SttpBackend[IO, Any]) = new OIDC[IO] {

    val loginPath = List("oauth2", "v2.0", "authorize")

    val logoutPath = List("oauth2", "v2.0", "logout")

    val tokenPath = List("oauth2", "v2.0", "token")

    override def ssoRedirect: Uri =
      uri"$baseUrl/oauth2/v2.0/authorize?response_type=code&redirect_uri=$redirectUri&response_mode=query&state=12345&scope=$scope&client_id=$clientId"

    override def authCodeToToken(
        code: String
    ): IO[Either[SecurityError, OIDC.TokenResponse]] = {

      val tokenRequestParams = Map(
        "grant_type" -> "authorization_code",
        "client_id" -> clientId,
        "client_secret" -> clientSecret.value,
        "redirect_uri" -> redirectUri.toString,
        "scope" -> scope,
        "code" -> code
      )

      basicRequest
        .post(baseUrl.addPath(tokenPath))
        .body(tokenRequestParams)
        .response(asJson[TokenResponse])
        .header(HeaderNames.Accept, "application/json")
        .send(sttpBackend)
        .map {
          case Response(Right(tokenResponse), _, _, _, _, _) =>
            Right(
              tokenResponse
            )
          case response @ Response(Left(error), code, statusText, _, _, _) =>
            Left(
              SecurityError.TechnicalError(
                s"Failed to token due to error from OIDC provider: $error",
                UUID
                  .randomUUID()
                  .toString
              )
            )
        }
    }

    override def userInfo(
        token: String
    ): IO[Either[SecurityError, UserSession]] = {
      basicRequest.auth
        .bearer(token)
        .get(uri"https://graph.microsoft.com/oidc/userinfo")
        .response(asJson[UserInfoResponse])
        .send(sttpBackend)
        .map {
          case Response(Right(userInfo), _, _, _, _, _) =>
            Right(
              UserSession(
                // let's say we trust OIDC for now
                Refined.unsafeApply(userInfo.email),
                Refined.unsafeApply(userInfo.given_name),
                Refined.unsafeApply(userInfo.family_name),
                Secret(token)
              )
            )
          case response @ Response(_, code, statusText, _, _, _) =>
            Left(
              SecurityError.TechnicalError(
                s"Failed to retrieve user info due to $statusText from OIDC provider",
                UUID
                  .randomUUID()
                  .toString
              )
            )
        }
    }

  }

  case class TokenResponse(
      access_token: String // leaky abstraction with this underscore, apply json field rename later
  ) derives Codec.AsObject

  private case class UserInfoResponse(
      family_name: String,
      given_name: String,
      email: String
  ) derives Codec.AsObject
}

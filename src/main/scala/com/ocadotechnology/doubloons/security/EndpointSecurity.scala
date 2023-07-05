package com.ocadotechnology.doubloons.security

import com.ocadotechnology.sttp.oauth2.Secret
import sttp.tapir.*
import sttp.tapir.codec.refined.TapirCodecRefined
import sttp.tapir.codec.refined.*
import sttp.tapir.generic.auto.*
import com.ocadotechnology.doubloons.user.Email
import eu.timepit.refined.types.string.NonEmptyString
import sttp.tapir.EndpointInput.AuthType.ApiKey
import sttp.tapir.EndpointInput.Auth

object EndpointSecurity {

  private val errorMessage =
    s"Missing token cookie ${AuthEndpoints.sessionCookieName}"

  implicit val tapirCodecPlain
      : sttp.tapir.Codec[String, Secret[String], CodecFormat.TextPlain] =
    Codec.string.map(Secret(_))(_.value)

  val optionalSessionCookie: Auth[Option[Secret[String]], ApiKey] =
    auth.apiKey(cookie[Option[Secret[String]]](AuthEndpoints.sessionCookieName))

  val sessionCookie: Auth[Secret[String], ApiKey] =
    auth
      .apiKey(
        cookie[Option[Secret[String]]](AuthEndpoints.sessionCookieName)
          .mapDecode {
            case None =>
              DecodeResult.Error(
                errorMessage,
                new IllegalStateException(errorMessage)
              )
            case Some(token) => DecodeResult.Value(token)
          }(Some(_))
      )

  val securedEndpoint: Endpoint[Secret[String], Unit, Unit, Unit, Any] =
    endpoint.securityIn(EndpointSecurity.sessionCookie)
}

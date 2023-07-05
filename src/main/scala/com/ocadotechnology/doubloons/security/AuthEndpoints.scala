package com.ocadotechnology.doubloons.security

import sttp.tapir.*
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.generic.auto.*
import sttp.tapir.codec.refined.TapirCodecRefined
import sttp.tapir.codec.refined.*
import io.circe.generic.auto.*
import io.circe.refined.*
import sttp.tapir.generic.auto.*
import sttp.model.*
import sttp.model.headers.CookieValueWithMeta
import com.ocadotechnology.sttp.oauth2.Secret
import com.ocadotechnology.doubloons.security.EndpointSecurity.sessionCookie

object AuthEndpoints {

  val sessionCookieName = "SESSION"

  val login: PublicEndpoint[Unit, Unit, Uri, Any] =
    endpoint
      .in("auth" / "login")
      .summary("Redirect user to login page")
      .get
      .out(header[Uri]("Location"))
      .out(statusCode(StatusCode.SeeOther))

  private val sevenDaysInSeconds = 7 * 24 * 60 * 60

  private val setSessionCookie =
    setCookie(sessionCookieName).map[Secret[String]](cookie =>
      Secret(cookie.value)
    )(secret =>
      CookieValueWithMeta.unsafeApply(
        secret.value,
        maxAge = Some(sevenDaysInSeconds),
        secure = true,
        httpOnly = true,
        path = Some("/"),
        expires = None,
        domain = None,
        otherDirectives = Map.empty
      )
    )

  val authorize
      : PublicEndpoint[String, SecurityError, (Uri, Secret[String]), Any] =
    endpoint
      .in("auth" / "authorize")
      .in(query[String]("code"))
      .summary("Redirect user to login page")
      .get
      .out(header[Uri]("Location"))
      .out(statusCode(StatusCode.SeeOther))
      .out(setSessionCookie)
      .errorOut(SecurityError.variants.all)

}

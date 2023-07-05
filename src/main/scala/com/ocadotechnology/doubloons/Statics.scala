package com.ocadotechnology.doubloons

import sttp.tapir.*
import sttp.tapir.server.*
import sttp.model.*
import cats.effect.IO
import com.ocadotechnology.doubloons.security.EndpointSecurity
import com.ocadotechnology.doubloons.security.Security
import com.ocadotechnology.sttp.oauth2.Secret
import scala.io.Source

class Statics(security: Security[IO]) {

  private val classLoader = this.getClass.getClassLoader

  def static: ServerEndpoint[Any, IO] =
    resourcesGetServerEndpoint[IO]("static")(classLoader, "web/static")

  private def indexEndpoint
      : Endpoint[Option[Secret[String]], Unit, Uri, String, Any] =
    endpoint
      .in("")
      .get
      .out(htmlBodyUtf8)
      .securityIn(EndpointSecurity.optionalSessionCookie)
      .errorOut(
        header[Uri]("Location")
      )
      .errorOut(statusCode(sttp.model.StatusCode.SeeOther))

  private def rateTeamEndpoint
      : Endpoint[Option[Secret[String]], Unit, Uri, String, Any] =
    endpoint
      .in("rate-team")
      .get
      .out(htmlBodyUtf8)
      .securityIn(EndpointSecurity.optionalSessionCookie)
      .errorOut(
        header[Uri]("Location")
      )
      .errorOut(statusCode(sttp.model.StatusCode.SeeOther))

  private def yourResultsEndpoint
      : Endpoint[Option[Secret[String]], Unit, Uri, String, Any] =
    endpoint
      .in("your-results")
      .get
      .out(htmlBodyUtf8)
      .securityIn(EndpointSecurity.optionalSessionCookie)
      .errorOut(
        header[Uri]("Location")
      )
      .errorOut(statusCode(sttp.model.StatusCode.SeeOther))

  private val validateCookieOrRedirect
      : Option[Secret[String]] => IO[Either[Uri, Unit]] = {
    case None => security.ssoRedirect().map(Left(_))
    case Some(value) =>
      security.decodeAndIntrospectToken(value).flatMap {
        case Left(value)  => security.ssoRedirect().map(Left(_))
        case Right(value) => IO.pure(Right(()))
      }
  }

  private val indexBody =
    IO.delay(Source.fromResource("web/index.html").getLines().mkString)

  val index: ServerEndpoint.Full[Option[
    Secret[String]
  ], Unit, Unit, Uri, String, Any, IO] =
    indexEndpoint
      .serverSecurityLogic { validateCookieOrRedirect }
      .serverLogicSuccess { _ => _ => indexBody }

  val rateTeam = rateTeamEndpoint
    .serverSecurityLogic(validateCookieOrRedirect)
    .serverLogicSuccess { _ => _ => indexBody }
  val yourResults = yourResultsEndpoint
    .serverSecurityLogic(validateCookieOrRedirect)
    .serverLogicSuccess { _ => _ => indexBody }

  def endpoints: List[ServerEndpoint[Any, cats.effect.IO]] =
    List(static, index, rateTeam, yourResults)

}

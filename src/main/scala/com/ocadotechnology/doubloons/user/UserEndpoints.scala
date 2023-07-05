package com.ocadotechnology.doubloons.user

import sttp.tapir.*
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.generic.auto.*
import sttp.tapir.codec.refined.TapirCodecRefined
import sttp.tapir.codec.refined.*
import io.circe.generic.auto.*
import io.circe.refined.*
import sttp.tapir.generic.auto.*
import com.ocadotechnology.doubloons.EndpointsExamples
import com.ocadotechnology.doubloons.security.EndpointSecurity
import com.ocadotechnology.doubloons.security.SecurityError
import com.ocadotechnology.doubloons.BusinessError
import com.ocadotechnology.sttp.oauth2.Secret
import com.ocadotechnology.doubloons.security.UserSession

object UserEndpoints {

  given Schema[Secret[String]] = Schema.string[Secret[String]]

  val userInfo: Endpoint[
    Secret[String],
    Unit,
    SecurityError | BusinessError,
    UserView,
    Any
  ] =
    EndpointSecurity.securedEndpoint
      .in("api" / "user" / "info")
      .get
      .out(jsonBody[UserView])
      .errorOut(
        SecurityError.variants.and(oneOfDefaultVariant(jsonBody[BusinessError]))
      )

  val getUserByEmail: Endpoint[
    Secret[String],
    String,
    SecurityError | BusinessError,
    UserView,
    Any
  ] =
    EndpointSecurity.securedEndpoint.get
      .in(
        "api" / "users" / path[String]("email").example(EndpointsExamples.email)
      )
      .description("Get user by email")
      .tag("Users")
      .out(jsonBody[UserView])
      .errorOut(
        SecurityError.variants.and(oneOfDefaultVariant(jsonBody[BusinessError]))
      )

  val getUsersByTeamId
      : Endpoint[Secret[String], Unit, SecurityError | BusinessError, List[
        UserView
      ], Any] =
    EndpointSecurity.securedEndpoint.get
      .in("api" / "users" / "team")
      .description("Get list of users in the team where user is assigned")
      .tag("Users")
      .out(jsonBody[List[UserView]])
      .errorOut(
        SecurityError.variants.and(oneOfDefaultVariant(jsonBody[BusinessError]))
      )

  val joinTeam: Endpoint[
    Secret[String],
    String,
    SecurityError | BusinessError,
    Unit,
    Any
  ] =
    EndpointSecurity.securedEndpoint.get
      .in("api" / "users" / "team" / "join" / path[String]("teamId"))
      .description("Assign user to the team")
      .tag("Users")
      .errorOut(
        SecurityError.variants.and(oneOfDefaultVariant(jsonBody[BusinessError]))
      )

}

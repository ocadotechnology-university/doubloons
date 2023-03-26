package com.ocadotechnology


import sttp.tapir.*
import sttp.tapir.json.circe.{jsonBody, *}
import sttp.tapir.generic.auto.*
import eu.timepit.refined.types.string.NonEmptyString
import eu.timepit.refined.auto.*
import io.circe.generic.auto.*
import io.circe.refined.*
import com.ocadotechnology.repositories.UserRepository
import com.ocadotechnology.models.{User, UserView}
import sttp.tapir.codec.refined.TapirCodecRefined



object Endpoints:
  val getUserByEmail: PublicEndpoint[String, String, User, Any] = endpoint.get
    .in("user" / path[String]("email").example("admin@example.com"))
    .description("TODO")
    .out(jsonBody[User])
    .errorOut(jsonBody[String])

val getUsersByTeamId: PublicEndpoint[String, String, List[UserView], Any] = endpoint.get
  .in("users" / path[String]("teamId").example("1"))
  .description("TODO")
  .out(jsonBody[List[UserView]])
  .errorOut(jsonBody[String])



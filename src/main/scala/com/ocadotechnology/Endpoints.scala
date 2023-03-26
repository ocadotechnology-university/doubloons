package com.ocadotechnology


import sttp.tapir.*
import sttp.tapir.json.circe.*
import sttp.tapir.generic.auto.*
import eu.timepit.refined.types.string.NonEmptyString
import eu.timepit.refined.auto.*
import io.circe.generic.auto.*
import io.circe.refined.*
import com.ocadotechnology.repositories.UserRepository
import com.ocadotechnology.models.{User, UserView}



object Endpoints:
  val getUserByEmail: PublicEndpoint[String, String, User, Any] = endpoint.get
    .in("user" / path[String]("email").example("admin@example.com"))
    .description("TODO")
    .out(jsonBody[User])
    .errorOut(jsonBody[String])





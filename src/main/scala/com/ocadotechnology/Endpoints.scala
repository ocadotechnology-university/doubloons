package com.ocadotechnology

import sttp.tapir.*
import cats.effect.IO
import com.ocadotechnology.models.User
import com.ocadotechnology.repositories.UserRepository
import io.circe.{Decoder, Encoder, Json}
import io.circe.generic.auto.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.*
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.swagger.bundle.SwaggerInterpreter



object Endpoints:

  val getUserByEmail: PublicEndpoint[String, String, User, Any] = endpoint.get
    .in("user" / path[String]("email").example("admin@example.com"))
    .description("TODO")
    .out(jsonBody[User])
    .errorOut(jsonBody[String])


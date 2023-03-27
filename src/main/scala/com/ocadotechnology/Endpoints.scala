package com.ocadotechnology

import sttp.tapir.*
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.generic.auto.*
import sttp.tapir.codec.refined.TapirCodecRefined
import sttp.tapir.codec.refined.*
import io.circe.generic.auto.*
import io.circe.refined.*

import com.ocadotechnology.models.*


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



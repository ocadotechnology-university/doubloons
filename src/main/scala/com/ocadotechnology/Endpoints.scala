package com.ocadotechnology

import sttp.tapir.*
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.generic.auto.*
import sttp.tapir.codec.refined.TapirCodecRefined
import sttp.tapir.codec.refined.*
import io.circe.generic.auto.*
import io.circe.refined.*
import com.ocadotechnology.models.*
import io.circe.JsonObject


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

  val createUser: PublicEndpoint[User, String, String, Any] = endpoint.post
    .in("user-create")
    .in(jsonBody[User])
    .description("TODO")
    .out(jsonBody[String])
    .errorOut(jsonBody[String])



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
  val getUserByEmail: PublicEndpoint[String, String, UserView, Any] = endpoint.get
    .in("user" / path[String]("email").example("admin@example.com"))
    .description("Get user data from the database")
    .out(jsonBody[UserView])
    .errorOut(jsonBody[String])
  
  val getUsersByTeamId: PublicEndpoint[String, String, List[UserView], Any] = endpoint.get
    .in("users" / path[String]("teamId").example("1"))
    .description("Get list of users from the database")
    .out(jsonBody[List[UserView]])
    .errorOut(jsonBody[String])

  val createUser: PublicEndpoint[User, String, Unit, Any] = endpoint.post
    .in("user-create")
    .in(jsonBody[User])
    .description("Insert a new user into the database")
    .errorOut(jsonBody[String])



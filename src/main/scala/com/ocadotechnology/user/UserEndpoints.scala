package com.ocadotechnology.user

import sttp.tapir.*
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.generic.auto.*
import sttp.tapir.codec.refined.TapirCodecRefined
import sttp.tapir.codec.refined.*
import io.circe.generic.auto.*
import io.circe.refined.*
import sttp.tapir.generic.auto.*
import com.ocadotechnology.EndpointsExamples

object UserEndpoints {
  val getUserByEmail: PublicEndpoint[String, String, UserView, Any] = endpoint.get
    .in("api" / "users" / path[String]("email").example(EndpointsExamples.email))
    .description("Get user data from the database - requires email")
    .tag("Users")
    .out(jsonBody[UserView])
    .errorOut(jsonBody[String])

  val getUsersByTeamId: PublicEndpoint[String, String, List[UserView], Any] = endpoint.get
    .in("api" / "users" / "getByTeamId" / path[String]("teamId").example("1"))
    .description("Get list of users from the database - requires teamId")
    .tag("Users")
    .out(jsonBody[List[UserView]])
    .errorOut(jsonBody[String])

  val createUser: PublicEndpoint[User, String, Unit, Any] = endpoint.post
    .in("api" / "users" / "create")
    .in(jsonBody[User].example(EndpointsExamples.user))
    .description("Insert a new user into the database - requires User object")
    .tag("Users")
    .errorOut(jsonBody[String])
}

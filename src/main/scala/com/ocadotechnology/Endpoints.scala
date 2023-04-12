package com.ocadotechnology

import sttp.tapir.*
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.generic.auto.*
import sttp.tapir.codec.refined.TapirCodecRefined
import sttp.tapir.codec.refined.*
import io.circe.generic.auto.*
import io.circe.refined.*
import com.ocadotechnology.models.*
//import eu.timepit.refined.api.Refined
//import eu.timepit.refined.types.numeric.PosInt
//import eu.timepit.refined.types.string.NonEmptyString
//
//val testUser = User(
//  Refined.unsafeApply("admin@example.com"),
//  Some(PosInt.unsafeFrom(1)),
//  NonEmptyString.unsafeFrom("Mike"),
//  NonEmptyString.unsafeFrom("Wazowski"),
//  NonEmptyString.unsafeFrom("secret"),
//  Some(NonEmptyString.unsafeFrom("avatars.example/avatar1.jpg")),
//)
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



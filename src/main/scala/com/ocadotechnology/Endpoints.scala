package com.ocadotechnology

import com.ocadotechnology.doubloon.Doubloon
import sttp.tapir.*
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.generic.auto.*
import sttp.tapir.codec.refined.TapirCodecRefined
import sttp.tapir.codec.refined.*
import io.circe.generic.auto.*
import io.circe.refined.*
import com.ocadotechnology.user.{User, UserView}
import eu.timepit.refined.api.Refined
import eu.timepit.refined.types.numeric.PosInt
import eu.timepit.refined.types.string.NonEmptyString


object Endpoints {
  private object Examples {
    val user: User = User(
      Refined.unsafeApply("admin@example.com"),
      Some(NonEmptyString.unsafeFrom("1")),
      NonEmptyString.unsafeFrom("Mike"),
      NonEmptyString.unsafeFrom("Wazowski"),
      NonEmptyString.unsafeFrom("secret"),
      Some(NonEmptyString.unsafeFrom("avatars.example/avatar1.jpg")),
    )
  }


  val getUserByEmail: PublicEndpoint[String, String, UserView, Any] = endpoint.get
    .in("api" / "user" / path[String]("email").example("admin@example.com"))
    .description("Get user data from the database")
    .out(jsonBody[UserView])
    .errorOut(jsonBody[String])

  val getUsersByTeamId: PublicEndpoint[String, String, List[UserView], Any] = endpoint.get
    .in("api" / "users" / path[String]("teamId").example("1"))
    .description("Get list of users from the database")
    .out(jsonBody[List[UserView]])
    .errorOut(jsonBody[String])

  val createUser: PublicEndpoint[User, String, Unit, Any] = endpoint.post
    .in("api" / "user-create")
    .in(jsonBody[User].example(Examples.user))
    .description("Insert a new user into the database")
    .errorOut(jsonBody[String])

  val getCurrentSpentDoubloons: PublicEndpoint[String, String, List[Doubloon], Any] = endpoint.get
    .in("api" / "doubloons" / "current" / path[String]("email").example("admin@example.com"))
    .description("Get list of doubloons spent in current time span for provided user by email")
    .out(jsonBody[List[Doubloon]])
    .errorOut(jsonBody[String])

  val createDoubloon: PublicEndpoint[Doubloon, String, Unit, Any] = endpoint.post
    .in("api" / "create-doubloon")
    .in(jsonBody[Doubloon])
    .description("Insert a new doubloon into the database")
    .errorOut(jsonBody[String])

  val updateDoubloon: PublicEndpoint[Doubloon, String, Unit, Any] = endpoint.post
    .in("api" / "update-doubloon")
    .in(jsonBody[Doubloon])
    .description("Update doubloon amount")
    .errorOut(jsonBody[String])

  val deleteDoubloon: PublicEndpoint[Doubloon, String, Unit, Any] = endpoint.post
    .in("api" / "delete-doubloon")
    .in(jsonBody[Doubloon])
    .description("Delete doubloon")
    .errorOut(jsonBody[String])

}

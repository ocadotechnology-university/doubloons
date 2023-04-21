package com.ocadotechnology

import com.ocadotechnology.comment.Comment
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

import java.time.LocalDate
import java.time.format.DateTimeFormatter


object Endpoints {
  private object Examples {
    private val monthYearFormatter = DateTimeFormatter.ofPattern("MM-yyyy")
    private val currentDateFormatted = LocalDate.now().format(monthYearFormatter)

    val email = "admin@example.com"

    val user: User = User(
      Refined.unsafeApply("admin@example.com"),
      Some(NonEmptyString.unsafeFrom("1")),
      NonEmptyString.unsafeFrom("Mike"),
      NonEmptyString.unsafeFrom("Wazowski"),
      NonEmptyString.unsafeFrom("secret"),
      Some(NonEmptyString.unsafeFrom("avatars.example/avatar1.jpg")),
    )

    val doubloon: Doubloon = Doubloon(
      PosInt.unsafeFrom(1),
      PosInt.unsafeFrom(1),
      NonEmptyString.unsafeFrom("user@example.com"),
      NonEmptyString.unsafeFrom("admin@example.com"),
      PosInt.unsafeFrom(1),
      NonEmptyString.unsafeFrom(currentDateFormatted)
    )

    val comment: Comment = Comment(
      NonEmptyString.unsafeFrom(currentDateFormatted),
      Refined.unsafeApply("user@example.com"),
      Refined.unsafeApply("admin@example.com"),
      NonEmptyString.unsafeFrom("This is a example body of the comment")
    )
  }


  val getUserByEmail: PublicEndpoint[String, String, UserView, Any] = endpoint.get
    .in("api" / "user" / path[String]("email").example(Examples.email))
    .description("Get user data from the database - requires email")
    .out(jsonBody[UserView])
    .errorOut(jsonBody[String])

  val getUsersByTeamId: PublicEndpoint[String, String, List[UserView], Any] = endpoint.get
    .in("api" / "users" / path[String]("teamId").example("1"))
    .description("Get list of users from the database - requires teamId")
    .out(jsonBody[List[UserView]])
    .errorOut(jsonBody[String])

  val createUser: PublicEndpoint[User, String, Unit, Any] = endpoint.post
    .in("api" / "user-create")
    .in(jsonBody[User].example(Examples.user))
    .description("Insert a new user into the database - requires User object")
    .errorOut(jsonBody[String])

  val getCurrentSpentDoubloons: PublicEndpoint[String, String, List[Doubloon], Any] = endpoint.get
    .in("api" / "doubloons" / "current" / path[String]("email").example(Examples.email))
    .description("Get list of doubloons spent in current time span by the user - requires email")
    .out(jsonBody[List[Doubloon]])
    .errorOut(jsonBody[String])

  val createDoubloon: PublicEndpoint[Doubloon, String, Unit, Any] = endpoint.post
    .in("api" / "create-doubloon")
    .in(jsonBody[Doubloon].example(Examples.doubloon))
    .description("Insert a new doubloon into the database - requires Doubloon object")
    .errorOut(jsonBody[String])

  val updateDoubloon: PublicEndpoint[Doubloon, String, Unit, Any] = endpoint.post
    .in("api" / "update-doubloon")
    .in(jsonBody[Doubloon].example(Examples.doubloon))
    .description("Update doubloon amount - requires Doubloon object")
    .errorOut(jsonBody[String])

  val deleteDoubloon: PublicEndpoint[Doubloon, String, Unit, Any] = endpoint.post
    .in("api" / "delete-doubloon")
    .in(jsonBody[Doubloon].example(Examples.doubloon))
    .description("Delete doubloon - requires Doubloon object")
    .errorOut(jsonBody[String])

  val getCurrentCommentsByEmail: PublicEndpoint[String, String, List[Comment], Any] = endpoint.get
    .in("api" / "comments" / "current" / path[String]("email").example(Examples.email))
    .description("Get list of comments created in current time span by the user - requires email")
    .out(jsonBody[List[Comment]])
    .errorOut(jsonBody[String])

  val createComment: PublicEndpoint[Comment, String, Unit, Any] = endpoint.post
    .in("api" / "comment" / "create")
    .description("Create a comment - requires Comment object")
    .in(jsonBody[Comment].example(Examples.comment))
    .errorOut(jsonBody[String])

  val updateComment: PublicEndpoint[Comment, String, Unit, Any] = endpoint.post
    .in("api" / "comment" / "update")
    .description("Update the comment - requires Comment object")
    .in(jsonBody[Comment].example(Examples.comment))
    .errorOut(jsonBody[String])

  val deleteComment: PublicEndpoint[Comment, String, Unit, Any] = endpoint.post
    .in("api" / "comment" / "delete")
    .description("Delete the comment - requires Comment object")
    .in(jsonBody[Comment].example(Examples.comment))
    .errorOut(jsonBody[String])

}

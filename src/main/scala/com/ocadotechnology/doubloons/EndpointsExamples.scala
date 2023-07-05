package com.ocadotechnology.doubloons

import com.ocadotechnology.doubloons.category.Category
import com.ocadotechnology.doubloons.comment.Comment
import com.ocadotechnology.doubloons.comment.DTO.CommentSummary
import com.ocadotechnology.doubloons.comment.DTO.CommentUpdate
import com.ocadotechnology.doubloons.comment.DTO.DeleteCommentDTO
import com.ocadotechnology.doubloons.common.DTO.GetSummary
import com.ocadotechnology.doubloons.doubloon.DTO.DoubloonSummary
import com.ocadotechnology.doubloons.doubloon.DTO.DoubloonUpdate
import com.ocadotechnology.doubloons.doubloon.DTO.GetSpentByOthers
import com.ocadotechnology.doubloons.doubloon.DTO.SpentByOthers
import com.ocadotechnology.doubloons.doubloon.Doubloon
import com.ocadotechnology.doubloons.team.Team
import com.ocadotechnology.doubloons.user.User
import com.ocadotechnology.doubloons.user.UserView
import eu.timepit.refined.api.Refined
import eu.timepit.refined.types.numeric.PosInt
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.generic.auto.*
import io.circe.refined.*
import sttp.tapir.*
import sttp.tapir.codec.refined.TapirCodecRefined
import sttp.tapir.codec.refined.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.jsonBody

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object EndpointsExamples {

  private val monthYearFormatter = DateTimeFormatter.ofPattern("MM-yyyy")
  val currentDateFormatted = LocalDate.now().format(monthYearFormatter)

  val email = "admin@example.com"

  val user: User = User(
    Refined.unsafeApply("admin@example.com"),
    Some(NonEmptyString.unsafeFrom("1")),
    NonEmptyString.unsafeFrom("Mike"),
    NonEmptyString.unsafeFrom("Wazowski"),
    NonEmptyString.unsafeFrom("secret"),
    Some(NonEmptyString.unsafeFrom("avatars.example/avatar1.jpg"))
  )

  val doubloon: Doubloon = Doubloon(
    PosInt.unsafeFrom(1),
    PosInt.unsafeFrom(1),
    NonEmptyString.unsafeFrom("user@example.com"),
    Refined.unsafeApply("admin@example.com"),
    PosInt.unsafeFrom(1),
    NonEmptyString.unsafeFrom(currentDateFormatted)
  )

  val doubloonUpdate: DoubloonUpdate = DoubloonUpdate(
    PosInt.unsafeFrom(1),
    PosInt.unsafeFrom(1),
    NonEmptyString.unsafeFrom("admin@example.com"),
    PosInt.unsafeFrom(1),
    NonEmptyString.unsafeFrom(currentDateFormatted)
  )

  val spentByOthersDTO: GetSpentByOthers = GetSpentByOthers(
    Refined.unsafeApply("user@example.com"),
    NonEmptyString.unsafeFrom("1"),
    NonEmptyString.unsafeFrom(currentDateFormatted)
  )

  val resultDTO: GetSummary = GetSummary(
    Refined.unsafeApply("user@example.com"),
    NonEmptyString.unsafeFrom(currentDateFormatted)
  )

  val comment: Comment = Comment(
    NonEmptyString.unsafeFrom(currentDateFormatted),
    Refined.unsafeApply("user@example.com"),
    Refined.unsafeApply("admin@example.com"),
    NonEmptyString.unsafeFrom("This is a example body of the comment")
  )

  val commentUpdate: CommentUpdate = CommentUpdate(
    NonEmptyString.unsafeFrom(currentDateFormatted),
    Refined.unsafeApply("admin@example.com"),
    NonEmptyString.unsafeFrom("This is a example body of the comment")
  )

  val deleteComment: DeleteCommentDTO = DeleteCommentDTO(
    NonEmptyString.unsafeFrom(currentDateFormatted),
    Refined.unsafeApply("user@example.com"),
    Refined.unsafeApply("admin@example.com")
  )

}

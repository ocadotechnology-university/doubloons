package com.ocadotechnology

import com.ocadotechnology.category.Category
import com.ocadotechnology.comment.Comment
import com.ocadotechnology.comment.DTO.{CommentSummary, DeleteCommentDTO}
import com.ocadotechnology.common.DTO.GetSummary
import com.ocadotechnology.doubloon.DTO.{DoubloonSummary, GetSpentByOthers, SpentByOthers}
import com.ocadotechnology.doubloon.Doubloon
import com.ocadotechnology.team.Team
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
  
  val deleteComment: DeleteCommentDTO = DeleteCommentDTO(
    NonEmptyString.unsafeFrom(currentDateFormatted),
    Refined.unsafeApply("user@example.com"),
    Refined.unsafeApply("admin@example.com"),
  )
  
}

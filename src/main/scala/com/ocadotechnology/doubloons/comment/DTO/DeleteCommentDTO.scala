package com.ocadotechnology.doubloons.comment.DTO

import eu.timepit.refined.types.all.PosInt
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Codec
import io.circe.generic.semiauto.*
import io.circe.refined.*
import com.ocadotechnology.doubloons.user.Email

case class DeleteCommentDTO(
    monthAndYear: NonEmptyString,
    givenTo: Email,
    givenBy: Email
)

object DeleteCommentDTO {
  given Codec[DeleteCommentDTO] = deriveCodec
}

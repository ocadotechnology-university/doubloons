package com.ocadotechnology.doubloons.comment.DTO

import eu.timepit.refined.types.all.PosInt
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Codec
import io.circe.generic.semiauto.*
import io.circe.refined.*
import com.ocadotechnology.doubloons.user.Email

case class CommentUpdate(
    monthAndYear: NonEmptyString,
    givenTo: Email,
    comment: NonEmptyString
)

object CommentUpdate {
  given Codec[CommentUpdate] = deriveCodec
}

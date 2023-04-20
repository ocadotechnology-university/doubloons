package com.ocadotechnology.comment

import eu.timepit.refined.types.all.PosInt
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Codec
import io.circe.generic.semiauto.*
import io.circe.refined.*
import com.ocadotechnology.user.Email

case class Comment (
                      monthAndYear: NonEmptyString,
                      givenTo: Email,
                      givenBy: Email,
                      comment: NonEmptyString,
                   )


object Comment {
  given Codec[Comment] = deriveCodec
}

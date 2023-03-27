package com.ocadotechnology.models

import eu.timepit.refined.*
import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.MatchesRegex
import eu.timepit.refined.types.numeric.PosInt
import eu.timepit.refined.types.string.NonEmptyString

import io.circe.Codec // Codec type
import io.circe.generic.semiauto._ // Derivation of codecs
import io.circe.refined._ // Derivations for refined types

/**
 * Safe User model without password
 */
case class UserView (
                      email: Email,
                      teamId: Option[PosInt],
                      firstName: NonEmptyString,
                      lastName: NonEmptyString,
                      avatar: Option[NonEmptyString],
                      leadingTeam: Option[Boolean],
                    )

object UserView {
  implicit val codec: Codec[User] = deriveCodec
}
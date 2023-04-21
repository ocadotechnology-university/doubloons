package com.ocadotechnology.user

import eu.timepit.refined.*
import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.MatchesRegex
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Codec
import io.circe.generic.semiauto.*
import io.circe.refined.* // Derivations for refined types

/**
 * Safe User model without password
 */
case class UserView (
                      email: Email,
                      teamId: Option[NonEmptyString],
                      firstName: NonEmptyString,
                      lastName: NonEmptyString,
                      avatar: Option[NonEmptyString],
                    )

object UserView {
  given Codec[UserView] = deriveCodec
}
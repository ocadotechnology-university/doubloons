package com.ocadotechnology.doubloons.user

import com.ocadotechnology.doubloons.user.User
import eu.timepit.refined.*
import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.MatchesRegex
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Codec
import io.circe.generic.semiauto.*
import io.circe.refined.* // Derivations for refined types

type Email = String Refined
  MatchesRegex["^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"]

case class User(
    email: Email,
    teamId: Option[NonEmptyString],
    firstName: NonEmptyString,
    lastName: NonEmptyString,
    password: NonEmptyString,
    avatar: Option[NonEmptyString]
)
object User {
  given Codec[User] = deriveCodec
}

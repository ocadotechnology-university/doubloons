package com.ocadotechnology.doubloons.doubloon.DTO

import com.ocadotechnology.doubloons.user.Email
import eu.timepit.refined.*
import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.MatchesRegex
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Codec
import io.circe.generic.semiauto.*
import io.circe.refined.* // Derivations for refined types

case class GetSpentByOthers(
    email: Email,
    teamId: NonEmptyString, // not necessary
    monthAndYear: NonEmptyString
)

object GetSpentByOthers {
  given Codec[GetSpentByOthers] = deriveCodec
}

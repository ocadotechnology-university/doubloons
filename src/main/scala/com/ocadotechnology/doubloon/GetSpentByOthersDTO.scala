package com.ocadotechnology.doubloon

import com.ocadotechnology.user.Email

import eu.timepit.refined.*
import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.MatchesRegex
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Codec
import io.circe.generic.semiauto.*
import io.circe.refined.* // Derivations for refined types

case class GetSpentByOthersDTO(
    email: Email,
    teamId: NonEmptyString,
    monthAndDate: NonEmptyString,
                            )

object GetSpentByOthersDTO {
  given Codec[GetSpentByOthersDTO] = deriveCodec
}

package com.ocadotechnology.doubloons.team

import eu.timepit.refined.*
import eu.timepit.refined.types.all.PosInt
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Codec
import io.circe.generic.semiauto.*
import io.circe.refined.*

case class Team(
    teamId: NonEmptyString,
    teamName: NonEmptyString,
    teamDescription: Option[NonEmptyString]
)

object Team {
  given Codec[Team] = deriveCodec
}

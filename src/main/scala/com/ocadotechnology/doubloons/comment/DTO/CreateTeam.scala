package com.ocadotechnology.doubloons.comment.DTO

import eu.timepit.refined.*
import eu.timepit.refined.api.Refined
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Codec
import io.circe.generic.semiauto.*
import io.circe.refined.*

case class CreateTeam(
    name: NonEmptyString,
    description: NonEmptyString
) derives Codec.AsObject

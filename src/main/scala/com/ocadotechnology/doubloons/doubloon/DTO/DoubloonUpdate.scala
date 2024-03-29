package com.ocadotechnology.doubloons.doubloon.DTO

import eu.timepit.refined.*
import eu.timepit.refined.types.all.PosInt
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Codec
import io.circe.generic.semiauto.*
import io.circe.refined.*

case class DoubloonUpdate(
    doubloonId: PosInt,
    categoryId: PosInt,
    givenTo: NonEmptyString,
    amount: PosInt,
    monthAndYear: NonEmptyString
)

object DoubloonUpdate {
  given Codec[DoubloonUpdate] = deriveCodec
}

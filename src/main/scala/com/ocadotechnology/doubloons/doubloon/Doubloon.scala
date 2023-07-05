package com.ocadotechnology.doubloons.doubloon

import eu.timepit.refined.*
import eu.timepit.refined.types.all.PosInt
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Codec
import io.circe.generic.semiauto.*
import io.circe.refined.*
import com.ocadotechnology.doubloons.user.Email

case class Doubloon(
    doubloonId: PosInt,
    categoryId: PosInt,
    givenTo: NonEmptyString,
    givenBy: Email,
    amount: PosInt,
    monthAndYear: NonEmptyString
)

object Doubloon {
  given Codec[Doubloon] = deriveCodec
}

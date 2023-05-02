package com.ocadotechnology.category

import eu.timepit.refined.*
import eu.timepit.refined.types.all.PosInt
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Codec
import io.circe.generic.semiauto.*
import io.circe.refined.*

case class Category (
    categoryId: PosInt,
    categoryName: NonEmptyString,
    categoryDescription: NonEmptyString,
                    )

object Category {
  given Codec[Category] = deriveCodec
}
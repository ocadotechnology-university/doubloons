package com.ocadotechnology.doubloons

import io.circe.Codec
import cats.Show

final case class BusinessError(reason: String) derives Codec.AsObject

object BusinessError {
  given Show[BusinessError] = Show.fromToString
}

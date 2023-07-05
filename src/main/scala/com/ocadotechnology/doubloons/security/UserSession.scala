package com.ocadotechnology.doubloons.security

import com.ocadotechnology.doubloons.user.Email
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Codec
import io.circe.refined.*
import com.ocadotechnology.sttp.oauth2.Secret
import cats.implicits.*

final case class UserSession(
    email: Email,
    firstName: NonEmptyString,
    lastName: NonEmptyString,
    token: Secret[String]
) derives Codec.AsObject

object UserSession {
  val stringCodec: io.circe.Codec[String] = io.circe.Codec
    .from(io.circe.Decoder.decodeString, io.circe.Encoder.encodeString)

  given Codec[Secret[String]] =
    stringCodec.iemap(Secret(_).asRight)(_.value)
}

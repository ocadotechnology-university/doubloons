package com.ocadotechnology.models

import eu.timepit.refined.auto._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.string._
import eu.timepit.refined.types.string._
import eu.timepit.refined.types.numeric._

import io.circe.Codec // Codec type
import io.circe.generic.semiauto._ // Derivation of codecs
import io.circe.refined._ // Derivations for refined types
import io.circe.syntax._ // for `.toJson` syntax
import io.circe.parser._ // for parsing json



// Case class representing User entity without password

/**
 * Safe User model without password
 */
case class UserView (
                      email: Email,
                      teamId: Option[PosInt],
                      firstName: NonEmptyString,
                      lastName: NonEmptyString,
                      avatar: Option[NonEmptyString],
                      leadingTeam: Option[Boolean],
                    )
/*
case class UserView (
                      email: Email,
                      teamId: Option[PosInt],
                      firstName: NonEmptyString,
                      lastName: NonEmptyString,
                      avatar: Option[NonEmptyString],
                      leadingTeam: Option[Boolean],
                    )

case class UserView (
                      email: String,
                      teamId: Option[Int],
                      firstName: String,
                      lastName: String,
                      avatar: Option[String],
                      leadingTeam: Option[Boolean],
                    )
*/

object UserView {
  implicit val codec: Codec[User] = deriveCodec
}
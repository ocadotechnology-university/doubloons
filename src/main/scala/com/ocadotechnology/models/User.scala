package com.ocadotechnology.models

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}





//type Email = String Refined MatchesRegex["""[a-z0-9]+@[a-z0-9]+\\.[a-z0-9]{2,}"""]

//case class User (email: NonEmptyString, teamID: PosInt, firstName: NonEmptyString, lastName: NonEmptyString, password: NonEmptyString, picture: String)

case class User (
                  email: String,
                  teamId: Option[Int],
                  firstName: String,
                  lastName: String,
                  password: String,
                  avatar: Option[String],
                  leadingTeam: Option[Boolean],
                )
object User {

  implicit val userEncoder: Encoder[User] = deriveEncoder
  implicit val userDecoder: Decoder[User] = deriveDecoder
}

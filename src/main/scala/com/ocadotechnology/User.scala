package com.ocadotechnology


//import eu.timepit.refined.api.Refined
//import sun.security.util.Password
//import eu.timepit.refined.auto.*
//import eu.timepit.refined.string.MatchesRegex
//import eu.timepit.refined.types.string.*
//import eu.timepit.refined.types.numeric.*
//import io.circe.Codec
//import io.circe.generic.semiauto.deriveCodec

import cats.effect.IO
import io.circe.*
import io.circe.generic.semiauto.*





// type Email = String Refined MatchesRegex[W.`"""[a-z0-9]+@[a-z0-9]+\\.[a-z0-9]{2,}"""`.T]

//case class User (email: NonEmptyString, teamID: PosInt, firstName: NonEmptyString, lastName: NonEmptyString, password: NonEmptyString, picture: String)

case class User (email: String, teamID: Option[Int], firstName: String, lastName: String, password: String, picture: Option[String])
object User {
  //User(NonEmptyString.unsafeFrom("admin@ocado.com"), PosInt.unsafeFrom(1), NonEmptyString.unsafeFrom("Admin"), NonEmptyString.unsafeFrom("Adminowy"), NonEmptyString.unsafeFrom("admin"), "bleble")
  //import io.circe.generic.semiauto._
  //implicit val codec: Codec[User] = deriveCodec

  implicit val userEncoder: Encoder[User] = deriveEncoder
  implicit val userDecoder: Decoder[User] = deriveDecoder
}

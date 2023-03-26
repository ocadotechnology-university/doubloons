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


type Email = String Refined MatchesRegex["""[a-z0-9]+@[a-z0-9]+\\.[a-z0-9]{2,}"""]

/*
Some unexpected behaviour spotted: when building the project, it sometimes produces error:


Could not find Schema for type String Refined
  eu.timepit.refined.string.MatchesRegex[
    ("[a-z0-9]+@[a-z0-9]+\\\\.[a-z0-9]{2,}" : String)
  ].
Schemas can be derived automatically by adding: `import sttp.tapir.generic.auto._`, or manually using `Schema.derived[T]`.
The latter is also useful for debugging derivation errors.
You can find more details in the docs: https://tapir.softwaremill.com/en/latest/endpoint/schemas.html
When integrating with a third-party datatype library remember to import respective schemas/codecs as described in https://tapir.softwaremill.com/en/latest/endpoint/integrations.html.
I found:

    sttp.tapir.Schema.derivedEnumerationValue[Enumeration#Value]

But method derivedEnumerationValue in trait SchemaCompanionMacros does not match type sttp.tapir.Schema[String Refined
  eu.timepit.refined.string.MatchesRegex[
    ("[a-z0-9]+@[a-z0-9]+\\\\.[a-z0-9]{2,}" : String)
  ]
].

One of the following imports might make progress towards fixing the problem:

  import shapeless.~?>.idKeyWitness
  import shapeless.~?>.idValueWitness
  import shapeless.~?>.witness


    .out(jsonBody[User])




The only working solution I found is to switch back to generic types, build the project and then switch to refined types and build the project again
Turns out that every change in Endpoints.scala (even adding a new line) results in throwing this error.
*/
case class User (
                  email: Email,
                  teamId: Option[PosInt],
                  firstName: NonEmptyString,
                  lastName: NonEmptyString,
                  password: NonEmptyString,
                  avatar: Option[NonEmptyString],
                  leadingTeam: Option[Boolean],
                )

/*
case class User (
                  email: Email,
                  teamId: Option[PosInt],
                  firstName: NonEmptyString,
                  lastName: NonEmptyString,
                  password: NonEmptyString,
                  avatar: Option[NonEmptyString],
                  leadingTeam: Option[Boolean],
                )

case class User (
                  email: String,
                  teamId: Option[Int],
                  firstName: String,
                  lastName: String,
                  password: String,
                  avatar: Option[String],
                  leadingTeam: Option[Boolean],
                )
*/
object User {

  implicit val codec: Codec[User] = deriveCodec

}

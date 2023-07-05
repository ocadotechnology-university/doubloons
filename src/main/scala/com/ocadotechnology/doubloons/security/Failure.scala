package com.ocadotechnology.doubloons.security

import io.circe.Codec
import sttp.model.StatusCode
import sttp.tapir.EndpointIO.Example
import sttp.tapir.EndpointOutput.OneOfVariant
import sttp.tapir.*
import sttp.tapir.json.circe.*
import sttp.tapir.generic.auto.*

import cats.Show
import cats.data.NonEmptyList
import io.circe.Encoder
import io.circe.Decoder

enum SecurityError(reason: String, errorId: String)
    derives Encoder.AsObject,
      Decoder {
  case Unauthorized(reason: String, errorId: String)
      extends SecurityError(reason, errorId)
  case Unauthenticated(reason: String, errorId: String)
      extends SecurityError(reason, errorId)
  case TechnicalError(reason: String, errorId: String)
      extends SecurityError(reason, errorId)
}

object SecurityError {

  object variants {

    def unauthenticated: OneOfVariant[SecurityError] =
      oneOfVariantValueMatcher(
        StatusCode.Unauthorized,
        jsonBody[SecurityError].example(
          Unauthenticated(
            "Roles didn't match",
            "96ca844e-da6d-11eb-a26c-2b028f5e9bf8"
          )
        )
      ) { case _: Unauthenticated => true }

    def unauthorized: OneOfVariant[SecurityError] =
      oneOfVariantValueMatcher(
        StatusCode.Forbidden,
        jsonBody[SecurityError].example(
          Unauthorized(
            "Missing session cookie",
            "96ca844e-da6d-11eb-a26c-2b028f5e9bf8"
          )
        )
      ) { case _: Unauthorized => true }

    def technicalError: OneOfVariant[SecurityError] =
      oneOfVariantValueMatcher(
        StatusCode.InternalServerError,
        jsonBody[SecurityError]
          .example(
            TechnicalError(
              "Something went wrong",
              "96ca844e-da6d-11eb-a26c-2b028f5e9bf8"
            )
          )
      ) { case TechnicalError(_, _) => true }

    def all: EndpointOutput.OneOf[SecurityError, SecurityError] =
      oneOf(unauthenticated, unauthorized, technicalError)

    def and[T: Codec: Schema: Show](
        businessErrorVariant: OneOfVariant[T]
    ): EndpointOutput.OneOf[SecurityError | T, SecurityError | T] =
      oneOf(unauthenticated, unauthorized, technicalError, businessErrorVariant)

  }
}

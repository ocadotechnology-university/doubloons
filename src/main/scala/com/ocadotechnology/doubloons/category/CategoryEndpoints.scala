package com.ocadotechnology.doubloons.category

import sttp.tapir.*
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.generic.auto.*
import sttp.tapir.codec.refined.TapirCodecRefined
import sttp.tapir.codec.refined.*
import io.circe.generic.auto.*
import io.circe.refined.*
import com.ocadotechnology.sttp.oauth2.Secret
import com.ocadotechnology.doubloons.security.EndpointSecurity
import com.ocadotechnology.doubloons.security.SecurityError
import com.ocadotechnology.doubloons.BusinessError
import com.ocadotechnology.sttp.oauth2.TokenIntrospection
import cats.data.EitherT
import com.ocadotechnology.doubloons.security.Security

object CategoryEndpoints {

  val getCategories
      : Endpoint[Secret[String], Unit, SecurityError | BusinessError, List[
        Category
      ], Any] =
    EndpointSecurity.securedEndpoint.get
      .in("api" / "categories")
      .description("Get the list of category objects")
      .tag("Categories")
      .out(jsonBody[List[Category]])
      .errorOut(
        SecurityError.variants.and(oneOfDefaultVariant(jsonBody[BusinessError]))
      )

}

package com.ocadotechnology.doubloons.doubloon

import com.ocadotechnology.doubloons.BusinessError
import com.ocadotechnology.doubloons.EndpointsExamples
import com.ocadotechnology.doubloons.common.DTO.GetSummary
import com.ocadotechnology.doubloons.doubloon.DTO.DoubloonSummary
import com.ocadotechnology.doubloons.doubloon.DTO.DoubloonUpdate
import com.ocadotechnology.doubloons.doubloon.DTO.GetSpentByOthers
import com.ocadotechnology.doubloons.doubloon.DTO.SpentByOthers
import com.ocadotechnology.doubloons.security.EndpointSecurity
import com.ocadotechnology.doubloons.security.SecurityError
import com.ocadotechnology.sttp.oauth2.Secret
import io.circe.generic.auto.*
import io.circe.refined.*
import sttp.tapir.*
import sttp.tapir.codec.refined.TapirCodecRefined
import sttp.tapir.codec.refined.*
import sttp.tapir.generic.auto.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.jsonBody

object DoubloonEndpoints {
  val createDoubloon: Endpoint[
    Secret[String],
    DoubloonUpdate,
    SecurityError | BusinessError,
    Int,
    Any
  ] =
    EndpointSecurity.securedEndpoint.post
      .in("api" / "doubloons")
      .in(jsonBody[DoubloonUpdate].example(EndpointsExamples.doubloonUpdate))
      .description(
        "Insert a new doubloon into the database - returns doubloon id. Provided ID will be ignored."
      )
      .tag("Doubloons")
      .out(jsonBody[Int])
      .errorOut(
        SecurityError.variants.and(oneOfDefaultVariant(jsonBody[BusinessError]))
      )

  val updateDoubloon: Endpoint[
    Secret[String],
    DoubloonUpdate,
    SecurityError | BusinessError,
    Unit,
    Any
  ] =
    EndpointSecurity.securedEndpoint.put
      .in("api" / "doubloons")
      .in(jsonBody[DoubloonUpdate].example(EndpointsExamples.doubloonUpdate))
      .description("Update doubloon amount")
      .tag("Doubloons")
      .errorOut(
        SecurityError.variants.and(oneOfDefaultVariant(jsonBody[BusinessError]))
      )

  val deleteDoubloon: Endpoint[
    Secret[String],
    DoubloonUpdate,
    SecurityError | BusinessError,
    Unit,
    Any
  ] =
    EndpointSecurity.securedEndpoint.delete
      .in("api" / "doubloons")
      .in(jsonBody[DoubloonUpdate].example(EndpointsExamples.doubloonUpdate))
      .description("Delete doubloon")
      .tag("Doubloons")
      .errorOut(
        SecurityError.variants.and(oneOfDefaultVariant(jsonBody[BusinessError]))
      )

  val getAmountToSpend: Endpoint[
    Secret[String],
    Unit,
    SecurityError | BusinessError,
    Int,
    Any
  ] =
    EndpointSecurity.securedEndpoint.get
      .in("api" / "doubloons" / "maxAmountToSpend")
      .description(
        "Get max amount of points to spend by a single user in a time span"
      )
      .tag("Doubloons")
      .out(jsonBody[Int])
      .errorOut(
        SecurityError.variants.and(oneOfDefaultVariant(jsonBody[BusinessError]))
      )

  val getDoubloonsSpentByOthers: Endpoint[Secret[
    String
  ], String, SecurityError | BusinessError, List[
    SpentByOthers
  ], Any] =
    EndpointSecurity.securedEndpoint.get
      .in(
        "api" / "doubloons" / "spentByOthers" / path[String]("monthAndYear")
          .example(
            EndpointsExamples.currentDateFormatted
          )
      )
      .description(
        "Get total amount of points spent by each user (except the provided one) in a time span"
      )
      .tag("Doubloons")
      .out(jsonBody[List[SpentByOthers]])
      .errorOut(
        SecurityError.variants.and(oneOfDefaultVariant(jsonBody[BusinessError]))
      )

  val getDoubloonsSummary: Endpoint[Secret[
    String
  ], (String, String), SecurityError | BusinessError, List[
    DoubloonSummary
  ], Any] =
    EndpointSecurity.securedEndpoint.get
      .in(
        "api" / "doubloons" / "summary" / path[String]("email").example(
          EndpointsExamples.email
        )
          / path[String]("monthAndYear").example(
            EndpointsExamples.currentDateFormatted
          )
      )
      .description(
        "Get all the doubloons gifted to the provided user in a time span"
      )
      .tag("Doubloons")
      .out(jsonBody[List[DoubloonSummary]])
      .errorOut(
        SecurityError.variants.and(oneOfDefaultVariant(jsonBody[BusinessError]))
      )

  val getCurrentSpentDoubloons
      : Endpoint[Secret[String], Unit, SecurityError | BusinessError, List[
        Doubloon
      ], Any] =
    EndpointSecurity.securedEndpoint.get
      .in("api" / "doubloons" / "current")
      .description(
        "Get list of doubloons spent in current time span by the provided user"
      )
      .tag("Doubloons")
      .out(jsonBody[List[Doubloon]])
      .errorOut(
        SecurityError.variants.and(oneOfDefaultVariant(jsonBody[BusinessError]))
      )

  val getAvailableMonths
      : Endpoint[Secret[String], Unit, SecurityError | BusinessError, List[
        String
      ], Any] =
    EndpointSecurity.securedEndpoint.get
      .in("api" / "doubloons" / "availableMonths")
      .description(
        "Get list of unique dates, during which the user received any doubloons - useful to let user pick the date for summary"
      )
      .tag("Doubloons")
      .out(jsonBody[List[String]])
      .errorOut(
        SecurityError.variants.and(oneOfDefaultVariant(jsonBody[BusinessError]))
      )
}

package com.ocadotechnology.doubloon

import sttp.tapir.*
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.generic.auto.*
import sttp.tapir.codec.refined.TapirCodecRefined
import sttp.tapir.codec.refined.*
import io.circe.generic.auto.*
import io.circe.refined.*
import sttp.tapir.generic.auto.*
import com.ocadotechnology.EndpointsExamples
import com.ocadotechnology.common.DTO.GetSummary
import com.ocadotechnology.doubloon.DTO.{DoubloonSummary, GetSpentByOthers, SpentByOthers}

object DoubloonEndpoints {
  val createDoubloon: PublicEndpoint[Doubloon, String, Int, Any] = endpoint.post
    .in("api" / "doubloons" / "create")
    .in(jsonBody[Doubloon].example(EndpointsExamples.doubloon))
    .description("Insert a new doubloon into the database - requires Doubloon object, returns doubloon id. Provided ID will be ignored.")
    .tag("Doubloons")
    .out(jsonBody[Int])
    .errorOut(jsonBody[String])

  val updateDoubloon: PublicEndpoint[Doubloon, String, Unit, Any] = endpoint.post
    .in("api" / "doubloons" / "update")
    .in(jsonBody[Doubloon].example(EndpointsExamples.doubloon))
    .description("Update doubloon amount - requires Doubloon object")
    .tag("Doubloons")
    .errorOut(jsonBody[String])

  val deleteDoubloon: PublicEndpoint[Doubloon, String, Unit, Any] = endpoint.post
    .in("api" / "doubloons" / "delete")
    .in(jsonBody[Doubloon].example(EndpointsExamples.doubloon))
    .description("Delete doubloon - requires Doubloon object")
    .tag("Doubloons")
    .errorOut(jsonBody[String])

  val getAmountToSpend: PublicEndpoint[String, String, Int, Any] = endpoint.get
    .in("api" / "doubloons" / "getAmountToSpend" / path[String]("teamId").example("1"))
    .description("Get max amount of points to spend by a single user in a time span")
    .tag("Doubloons")
    .out(jsonBody[Int])
    .errorOut(jsonBody[String])

  val getDoubloonsSpentByOthers: PublicEndpoint[GetSpentByOthers, String, List[SpentByOthers], Any] = endpoint.post
    .in("api" / "doubloons" / "getSpentByOthers")
    .description("Get total amount of points spent by each user in a time span")
    .tag("Doubloons")
    .in(jsonBody[GetSpentByOthers].example(EndpointsExamples.spentByOthersDTO))
    .out(jsonBody[List[SpentByOthers]])
    .errorOut(jsonBody[String])

  val getDoubloonResults: PublicEndpoint[GetSummary, String, List[DoubloonSummary], Any] = endpoint.post
    .in("api" / "doubloons" / "results")
    .description("Get all the doubloons gifted to the provided user in a time span")
    .tag("Doubloons")
    .in(jsonBody[GetSummary].example(EndpointsExamples.resultDTO))
    .out(jsonBody[List[DoubloonSummary]])
    .errorOut(jsonBody[String])

  val getCurrentSpentDoubloons: PublicEndpoint[String, String, List[Doubloon], Any] = endpoint.get
    .in("api" / "doubloons" / "current" / path[String]("email").example(EndpointsExamples.email))
    .description("Get list of doubloons spent in current time span by the user - requires email")
    .tag("Doubloons")
    .out(jsonBody[List[Doubloon]])
    .errorOut(jsonBody[String])
}

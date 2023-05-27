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
    .in("api" / "doubloons")
    .in(jsonBody[Doubloon].example(EndpointsExamples.doubloon))
    .description("Insert a new doubloon into the database - returns doubloon id. Provided ID will be ignored.")
    .tag("Doubloons")
    .out(jsonBody[Int])
    .errorOut(jsonBody[String])

  val updateDoubloon: PublicEndpoint[Doubloon, String, Unit, Any] = endpoint.put
    .in("api" / "doubloons")
    .in(jsonBody[Doubloon].example(EndpointsExamples.doubloon))
    .description("Update doubloon amount")
    .tag("Doubloons")
    .errorOut(jsonBody[String])

  val deleteDoubloon: PublicEndpoint[Doubloon, String, Unit, Any] = endpoint.delete
    .in("api" / "doubloons")
    .in(jsonBody[Doubloon].example(EndpointsExamples.doubloon))
    .description("Delete doubloon")
    .tag("Doubloons")
    .errorOut(jsonBody[String])

  val getAmountToSpend: PublicEndpoint[String, String, Int, Any] = endpoint.get
    .in("api" / "doubloons" / "maxAmountToSpend" / path[String]("teamId").example("1"))
    .description("Get max amount of points to spend by a single user in a time span")
    .tag("Doubloons")
    .out(jsonBody[Int])
    .errorOut(jsonBody[String])

  val getDoubloonsSpentByOthers: PublicEndpoint[(String, String), String, List[SpentByOthers], Any] = endpoint.get
    .in("api" / "doubloons" / "spentByOthers" / path[String]("email").example(EndpointsExamples.email)
      / path[String]("monthAndYear").example(EndpointsExamples.currentDateFormatted))
    .description("Get total amount of points spent by each user (except the provided one) in a time span")
    .tag("Doubloons")
    .out(jsonBody[List[SpentByOthers]])
    .errorOut(jsonBody[String])

  val getDoubloonsSummary: PublicEndpoint[(String, String), String, List[DoubloonSummary], Any] = endpoint.get
    .in("api" / "doubloons" / "summary" / path[String] ("email").example(EndpointsExamples.email)
      / path[String]("monthAndYear").example(EndpointsExamples.currentDateFormatted))
    .description("Get all the doubloons gifted to the provided user in a time span")
    .tag("Doubloons")
    .out(jsonBody[List[DoubloonSummary]])
    .errorOut(jsonBody[String])

  val getCurrentSpentDoubloons: PublicEndpoint[String, String, List[Doubloon], Any] = endpoint.get
    .in("api" / "doubloons" / "current" / path[String]("email").example(EndpointsExamples.email))
    .description("Get list of doubloons spent in current time span by the provided user")
    .tag("Doubloons")
    .out(jsonBody[List[Doubloon]])
    .errorOut(jsonBody[String])
}

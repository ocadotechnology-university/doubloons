package com.ocadotechnology.category

import sttp.tapir.*
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.generic.auto.*
import sttp.tapir.codec.refined.TapirCodecRefined
import sttp.tapir.codec.refined.*
import io.circe.generic.auto.*
import io.circe.refined.*

object CategoryEndpoints {
  val getCategories: PublicEndpoint[Unit, String, List[Category], Any] = endpoint.get
    .in("api" / "categories")
    .description("Get the list of category objects")
    .tag("Categories")
    .out(jsonBody[List[Category]])
    .errorOut(jsonBody[String])
}

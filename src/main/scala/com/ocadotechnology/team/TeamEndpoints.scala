package com.ocadotechnology.team

import sttp.tapir.*
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.generic.auto.*
import sttp.tapir.codec.refined.TapirCodecRefined
import sttp.tapir.codec.refined.*
import io.circe.generic.auto.*
import io.circe.refined.*
import sttp.tapir.generic.auto.*

object TeamEndpoints {
  val getTeamInfo: PublicEndpoint[String, String, Team, Any] = endpoint.get
    .in("api" / "teams" / "get" / path[String]("teamId").example("1"))
    .description("Get team information (name, description)")
    .tag("Teams")
    .out(jsonBody[Team])
    .errorOut(jsonBody[String])
}

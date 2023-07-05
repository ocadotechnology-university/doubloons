package com.ocadotechnology.doubloons.team

import sttp.tapir.*
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.generic.auto.*
import sttp.tapir.codec.refined.TapirCodecRefined
import sttp.tapir.codec.refined.*
import io.circe.generic.auto.*
import io.circe.refined.*
import sttp.tapir.generic.auto.*
import com.ocadotechnology.doubloons.security.SecurityError
import com.ocadotechnology.doubloons.security.EndpointSecurity
import com.ocadotechnology.doubloons.BusinessError
import com.ocadotechnology.sttp.oauth2.Secret

object TeamEndpoints {
  val getTeamInfo: Endpoint[
    Secret[String],
    Unit,
    SecurityError | BusinessError,
    Team,
    Any
  ] = EndpointSecurity.securedEndpoint.get
    .in("api" / "teams")
    .description("Get team information (name, description)")
    .tag("Teams")
    .out(jsonBody[Team])
    .errorOut(
      SecurityError.variants.and(oneOfDefaultVariant(jsonBody[BusinessError]))
    )
}

package com.ocadotechnology.doubloons.team

import com.ocadotechnology.doubloons.BusinessError
import com.ocadotechnology.doubloons.comment.DTO.CreateTeam
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

  val createTeam: Endpoint[
    Secret[String],
    CreateTeam,
    SecurityError | BusinessError,
    Team,
    Any
  ] =
    EndpointSecurity.securedEndpoint.post
      .in("api" / "teams")
      .description("Create team by providing name and description")
      .tag("Teams")
      .in(jsonBody[CreateTeam])
      .out(jsonBody[Team])
      .errorOut(
        SecurityError.variants.and(oneOfDefaultVariant(jsonBody[BusinessError]))
      )
}

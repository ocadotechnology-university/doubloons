package com.ocadotechnology.doubloons.comment

import sttp.tapir.*
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.codec.refined.TapirCodecRefined
import sttp.tapir.codec.refined.*
import io.circe.generic.auto.*
import io.circe.refined.*
import sttp.tapir.generic.auto.*
import com.ocadotechnology.doubloons.EndpointsExamples
import com.ocadotechnology.doubloons.comment.DTO.CommentSummary
import com.ocadotechnology.doubloons.comment.DTO.CommentUpdate
import com.ocadotechnology.doubloons.comment.DTO.DeleteCommentDTO
import com.ocadotechnology.doubloons.common.DTO.GetSummary
import com.ocadotechnology.doubloons.security.EndpointSecurity
import com.ocadotechnology.doubloons.security.SecurityError
import com.ocadotechnology.doubloons.BusinessError
import com.ocadotechnology.sttp.oauth2.Secret

object CommentEndpoints {
  val getComments
      : Endpoint[Secret[String], String, SecurityError | BusinessError, List[
        Comment
      ], Any] =
    EndpointSecurity.securedEndpoint.get
      .in(
        "api" / "comments" / path[String]("monthAndYear").example(
          EndpointsExamples.currentDateFormatted
        )
      )
      .description(
        "Get list of comments created in provided time span by provided user"
      )
      .tag("Comments")
      .out(jsonBody[List[Comment]])
      .errorOut(
        SecurityError.variants.and(oneOfDefaultVariant(jsonBody[BusinessError]))
      )

  val upsertComment: Endpoint[
    Secret[String],
    CommentUpdate,
    SecurityError | BusinessError,
    Unit,
    Any
  ] = EndpointSecurity.securedEndpoint.post
    .in("api" / "comments")
    .description(
      "Create or update the comment - depends if the comment is already in database"
    )
    .tag("Comments")
    .in(jsonBody[CommentUpdate].example(EndpointsExamples.commentUpdate))
    .errorOut(
      SecurityError.variants.and(oneOfDefaultVariant(jsonBody[BusinessError]))
    )

  val deleteComment: Endpoint[
    Secret[String],
    DeleteCommentDTO,
    SecurityError | BusinessError,
    Unit,
    Any
  ] =
    EndpointSecurity.securedEndpoint.delete
      .in("api" / "comments")
      .description("Delete the comment")
      .tag("Comments")
      .in(jsonBody[DeleteCommentDTO].example(EndpointsExamples.deleteComment))
      .errorOut(
        SecurityError.variants.and(oneOfDefaultVariant(jsonBody[BusinessError]))
      )

  val getCommentsSummary
      : Endpoint[Secret[String], String, SecurityError | BusinessError, List[
        CommentSummary
      ], Any] =
    EndpointSecurity.securedEndpoint.get
      .in(
        "api" / "comments" / "summary" / path[String]("monthAndYear").example(
          EndpointsExamples.currentDateFormatted
        )
      )
      .description(
        "Get all the comments given to provided user in provided time span"
      )
      .tag("Comments")
      .out(jsonBody[List[CommentSummary]])
      .errorOut(
        SecurityError.variants.and(oneOfDefaultVariant(jsonBody[BusinessError]))
      )
}

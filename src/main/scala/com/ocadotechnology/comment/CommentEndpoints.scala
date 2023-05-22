package com.ocadotechnology.comment

import sttp.tapir.*
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.codec.refined.TapirCodecRefined
import sttp.tapir.codec.refined.*
import io.circe.generic.auto.*
import io.circe.refined.*
import sttp.tapir.generic.auto._
import com.ocadotechnology.EndpointsExamples
import com.ocadotechnology.comment.DTO.CommentSummary
import com.ocadotechnology.common.DTO.GetSummary

object CommentEndpoints {
  val getCurrentCommentsByEmail: PublicEndpoint[String, String, List[Comment], Any] = endpoint.get
    .in("api" / "comments" / "current" / path[String]("email").example(EndpointsExamples.email))
    .description("Get list of comments created in current time span by the user - requires email")
    .tag("Comments")
    .out(jsonBody[List[Comment]])
    .errorOut(jsonBody[String])

  val createComment: PublicEndpoint[Comment, String, Unit, Any] = endpoint.post
    .in("api" / "comments" / "create")
    .description("Create a comment - requires Comment object")
    .tag("Comments")
    .in(jsonBody[Comment].example(EndpointsExamples.comment))
    .errorOut(jsonBody[String])

  val updateComment: PublicEndpoint[Comment, String, Unit, Any] = endpoint.post
    .in("api" / "comments" / "update")
    .description("Update the comment - requires Comment object")
    .tag("Comments")
    .in(jsonBody[Comment].example(EndpointsExamples.comment))
    .errorOut(jsonBody[String])

  val deleteComment: PublicEndpoint[Comment, String, Unit, Any] = endpoint.post
    .in("api" / "comments" / "delete")
    .description("Delete the comment - requires Comment object")
    .tag("Comments")
    .in(jsonBody[Comment].example(EndpointsExamples.comment))
    .errorOut(jsonBody[String])

  val getCommentResults: PublicEndpoint[GetSummary, String, List[CommentSummary], Any] = endpoint.post
    .in("api" / "comments" / "results")
    .description("Get all the comments given to provided user in a time span")
    .tag("Comments")
    .in(jsonBody[GetSummary])
    .out(jsonBody[List[CommentSummary]])
    .errorOut(jsonBody[String])
}

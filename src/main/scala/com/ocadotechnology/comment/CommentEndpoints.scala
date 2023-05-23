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
  val getComments: PublicEndpoint[(String, String), String, List[Comment], Any] = endpoint.get
    .in("api" / "comments" / "current" / path[String]("email").example(EndpointsExamples.email)
      / path[String]("monthAndYear").example(EndpointsExamples.currentDateFormatted))
    .description("Get list of comments created in current time span by the user - requires email")
    .tag("Comments")
    .out(jsonBody[List[Comment]])
    .errorOut(jsonBody[String])

  val upsertComment: PublicEndpoint[Comment, String, Unit, Any] = endpoint.post
    .in("api" / "comments" / "upsert")
    .description("Create or update the comment - depends if the comment is already in database")
    .tag("Comments")
    .in(jsonBody[Comment].example(EndpointsExamples.comment))
    .errorOut(jsonBody[String])

  val deleteComment: PublicEndpoint[Comment, String, Unit, Any] = endpoint.post
    .in("api" / "comments" / "delete")
    .description("Delete the comment - requires Comment object")
    .tag("Comments")
    .in(jsonBody[Comment].example(EndpointsExamples.comment))
    .errorOut(jsonBody[String])

  val getCommentsSummary: PublicEndpoint[(String, String), String, List[CommentSummary], Any] = endpoint.get
    .in("api" / "comments" / "summary" / path[String]("givenTo").example(EndpointsExamples.email)
      / path[String]("monthAndYear").example(EndpointsExamples.currentDateFormatted))
    .description("Get all the comments given to provided user in a time span")
    .tag("Comments")
    .out(jsonBody[List[CommentSummary]])
    .errorOut(jsonBody[String])
}

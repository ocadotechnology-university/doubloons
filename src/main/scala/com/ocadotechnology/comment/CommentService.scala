package com.ocadotechnology.comment

import cats.effect.IO
import com.ocadotechnology.comment.DTO.CommentSummary
import com.ocadotechnology.common.DTO.GetSummary

trait CommentService {
  def getComments(email: String, monthAndYear: String): IO[Either[String, List[Comment]]]

  def upsertComment(comment: Comment): IO[Either[String, Unit]]

  def deleteComment(comment: Comment): IO[Either[String, Unit]]

  def getCommentResults(data: GetSummary): IO[Either[String, List[CommentSummary]]]

}

object CommentService {
  def instance(commentRepository: CommentRepository): CommentService = new CommentService {
    override def getComments(email: String, monthAndYear: String): IO[Either[String, List[Comment]]] = {
      commentRepository.getComments(email, monthAndYear).map {
        case Nil => Left(s"No comments found for current time span for email: $email")
        case comments => Right(comments)
      }
    }

    override def upsertComment(comment: Comment): IO[Either[String, Unit]] = {
      commentRepository.upsertComment(comment).map {
        case Left(CommentRepository.Failure.CommentUpsert(reason)) => Left(s"$reason")
        case Right(_) => Right(())
      }
    }


    override def deleteComment(comment: Comment): IO[Either[String, Unit]] = {
      commentRepository.deleteComment(comment).map {
        case Left(CommentRepository.Failure.CommentDeletion(reason)) => Left(s"$reason")
        case Right(_) => Right(())
      }
    }

    override def getCommentResults(data: GetSummary): IO[Either[String, List[CommentSummary]]] = {
      commentRepository.getCommentResults(data)
        .map {
          case Nil => Left(s"No comments found for user ${data.givenTo} during ${data.monthAndYear}")
          case results => Right(results)
        }
    }
    
  }
}
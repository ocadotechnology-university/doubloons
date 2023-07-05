package com.ocadotechnology.doubloons.comment

import cats.effect.IO
import com.ocadotechnology.doubloons.comment.DTO.{
  CommentSummary,
  DeleteCommentDTO
}
import com.ocadotechnology.doubloons.common.DTO.GetSummary
import com.ocadotechnology.doubloons.BusinessError

trait CommentService {
  def getComments(
      email: String,
      monthAndYear: String
  ): IO[Either[BusinessError, List[Comment]]]

  def upsertComment(comment: Comment): IO[Either[BusinessError, Unit]]

  def deleteComment(comment: DeleteCommentDTO): IO[Either[BusinessError, Unit]]

  def getCommentsSummary(
      givenTo: String,
      monthAndYear: String
  ): IO[Either[BusinessError, List[CommentSummary]]]

}

object CommentService {
  def instance(commentRepository: CommentRepository): CommentService =
    new CommentService {
      override def getComments(
          email: String,
          monthAndYear: String
      ): IO[Either[BusinessError, List[Comment]]] = {
        commentRepository.getComments(email, monthAndYear).map { result =>
          Right(result)
        }

      }

      override def upsertComment(
          comment: Comment
      ): IO[Either[BusinessError, Unit]] = {
        commentRepository.upsertComment(comment).map {
          case Left(reason) => Left(BusinessError(s"$reason"))
          case Right(_)     => Right(())
        }
      }

      override def deleteComment(
          comment: DeleteCommentDTO
      ): IO[Either[BusinessError, Unit]] = {
        commentRepository.deleteComment(comment).map {
          case Left(reason) => Left(BusinessError(s"$reason"))
          case Right(_)     => Right(())
        }
      }

      override def getCommentsSummary(
          givenTo: String,
          monthAndYear: String
      ): IO[Either[BusinessError, List[CommentSummary]]] = {
        commentRepository.getCommentsSummary(givenTo, monthAndYear).map {
          result => Right(result)
        }
      }

    }
}

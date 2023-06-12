package com.ocadotechnology.comment

import cats.effect.IO
import com.ocadotechnology.comment.DTO.{CommentSummary, DeleteCommentDTO}
import com.ocadotechnology.common.DTO.GetSummary

trait CommentService {
  def getComments(email: String, monthAndYear: String): IO[Either[String, List[Comment]]]

  def upsertComment(comment: Comment): IO[Either[String, Unit]]

  def deleteComment(comment: DeleteCommentDTO): IO[Either[String, Unit]]

  def getCommentsSummary(givenTo: String, monthAndYear: String): IO[Either[String, List[CommentSummary]]]

}

object CommentService {
  def instance(commentRepository: CommentRepository): CommentService = new CommentService {
    override def getComments(email: String, monthAndYear: String): IO[Either[String, List[Comment]]] = {
      commentRepository.getComments(email, monthAndYear).map{result => Right(result)}

    }

    override def upsertComment(comment: Comment): IO[Either[String, Unit]] = {
      commentRepository.upsertComment(comment).map {
        case Left(CommentRepository.Failure.CommentUpsert(reason)) => Left(s"$reason")
        case Right(_) => Right(())
      }
    }


    override def deleteComment(comment: DeleteCommentDTO): IO[Either[String, Unit]] = {
      commentRepository.deleteComment(comment).map {
        case Left(CommentRepository.Failure.CommentDeletion(reason)) => Left(s"$reason")
        case Right(_) => Right(())
      }
    }

    override def getCommentsSummary(givenTo: String, monthAndYear: String): IO[Either[String, List[CommentSummary]]] = {
      commentRepository.getCommentsSummary(givenTo, monthAndYear).map{result => Right(result)}
    }
    
  }
}
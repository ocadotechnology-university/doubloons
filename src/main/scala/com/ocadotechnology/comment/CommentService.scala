package com.ocadotechnology.comment

import cats.effect.IO

trait CommentService {
  def getCurrentCommentsByEmail(email: String): IO[Either[String, List[Comment]]]

  def createComment(comment: Comment): IO[Either[String, Unit]]

  def updateComment(comment: Comment): IO[Either[String, Unit]]

  def deleteComment(comment: Comment): IO[Either[String, Unit]]

}

object CommentService {
  def instance(commentRepository: CommentRepository): CommentService = new CommentService {
    override def getCurrentCommentsByEmail(email: String): IO[Either[String, List[Comment]]] = {
      commentRepository.getCurrentCommentsByEmail(email).map {
        case Nil => Left(s"No comments found for current time span for email: $email")
        case comments => Right(comments)
      }
    }

    override def createComment(comment: Comment): IO[Either[String, Unit]] = {
      commentRepository.createComment(comment).map {
        case Left(CommentRepository.Failure.CommentCreation(reason)) => Left(s"$reason")
        case Right(_) => Right(())
      }
    }

    override def updateComment(comment: Comment): IO[Either[String, Unit]] = {
      commentRepository.updateComment(comment).map {
        case Left(CommentRepository.Failure.CommentUpdate(reason)) => Left(s"$reason")
        case Right(_) => Right(())
      }
    }

    override def deleteComment(comment: Comment): IO[Either[String, Unit]] = {
      commentRepository.deleteComment(comment).map {
        case Left(CommentRepository.Failure.CommentDeletion(reason)) => Left(s"$reason")
        case Right(_) => Right(())
      }
    }
  }
}
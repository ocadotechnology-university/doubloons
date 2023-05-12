package com.ocadotechnology.comment

import cats.effect.IO
import com.ocadotechnology.common.GetResultDTO

trait CommentService {
  def getCurrentCommentsByEmail(email: String): IO[Either[String, List[Comment]]]

  def createComment(comment: Comment): IO[Either[String, Unit]]

  def updateComment(comment: Comment): IO[Either[String, Unit]]

  def deleteComment(comment: Comment): IO[Either[String, Unit]]

  def getCommentResults(data: GetResultDTO): IO[Either[String, List[CommentResultDTO]]]

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

    override def getCommentResults(data: GetResultDTO): IO[Either[String, List[CommentResultDTO]]] = {
      commentRepository.getCommentResults(data)
        .map {
          case Nil => Left(s"No comments found for user ${data.givenTo} during ${data.monthAndYear}")
          case results => Right(results)
        }
    }
    
  }
}
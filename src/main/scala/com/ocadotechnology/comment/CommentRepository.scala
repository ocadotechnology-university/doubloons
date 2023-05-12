package com.ocadotechnology.comment

import cats.effect.IO
import cats.implicits.*
import com.ocadotechnology.common.GetResultDTO
import com.ocadotechnology.database.DatabaseConfig.xa
import doobie.*
import doobie.implicits.*
import doobie.refined.implicits.*

import java.time.LocalDate
import java.time.format.DateTimeFormatter

trait CommentRepository {
  def getCurrentCommentsByEmail(email: String): IO[List[Comment]]
  def createComment(comment: Comment): IO[Either[CommentRepository.Failure, Int]]
  def updateComment(comment: Comment): IO[Either[CommentRepository.Failure, Int]]
  def deleteComment(comment: Comment): IO[Either[CommentRepository.Failure, Int]]
  def getCommentResults(data: GetResultDTO): IO[List[CommentResultDTO]]
}

object CommentRepository {
  enum Failure {
    case CommentCreation(reason: String)
    case CommentUpdate(reason: String)
    case CommentDeletion(reason: String)
  }

  def instance: CommentRepository = new CommentRepository {
    override def getCurrentCommentsByEmail(email: String): IO[List[Comment]] = {
      val monthYearFormatter = DateTimeFormatter.ofPattern("MM-yyyy")
      val currentDateFormatted = LocalDate.now().format(monthYearFormatter)
      sql"""SELECT * FROM comments WHERE given_by = $email AND month_and_year = $currentDateFormatted"""
        .query[Comment]
        .to[List]
        .transact(xa)
    }

    override def createComment(comment: Comment): IO[Either[Failure, Int]] = {
      sql"""INSERT INTO comments (month_and_year, given_to, given_by, comment)
            VALUES (${comment.monthAndYear}, ${comment.givenTo}, ${comment.givenBy}, ${comment.comment})
         """
        .update
        .run
        .transact(xa)
        .attemptSql
        .map(_.leftMap(e => Failure.CommentCreation(e.getMessage)))
    }

    override def updateComment(comment: Comment): IO[Either[Failure, Int]] = {
      sql"""UPDATE comments
            SET comment = ${comment.comment}
            WHERE month_and_year = ${comment.monthAndYear}
              AND given_by = ${comment.givenBy}
              AND given_to = ${comment.givenTo}"""
        .update
        .run
        .transact(xa)
        .attemptSql
        .map(_.leftMap(e => Failure.CommentUpdate(e.getMessage)))
    }

    override def deleteComment(comment: Comment): IO[Either[Failure, Int]] = {
      sql"""DELETE FROM comments
            WHERE month_and_year = ${comment.monthAndYear}
                AND given_by = ${comment.givenBy}
                AND given_to = ${comment.givenTo}"""
        .update
        .run
        .transact(xa)
        .attemptSql
        .map(_.leftMap(e => Failure.CommentDeletion(e.getMessage)))
    }

    override def getCommentResults(data: GetResultDTO): IO[List[CommentResultDTO]] = {
      sql"""SELECT given_by, comment FROM comments
           	WHERE given_to = ${data.givenTo}
           	AND month_and_year = ${data.monthAndYear}"""
        .query[CommentResultDTO]
        .to[List]
        .transact(xa)
    }

  }
}

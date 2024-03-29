package com.ocadotechnology.doubloons.comment

import cats.effect.IO
import cats.implicits.*
import com.ocadotechnology.doubloons.comment.DTO.{
  CommentSummary,
  DeleteCommentDTO
}
import com.ocadotechnology.doubloons.common.DTO.GetSummary
import com.ocadotechnology.doubloons.database.DatabaseConfig
import doobie.*
import doobie.implicits.*
import doobie.refined.implicits.*

import java.time.LocalDate
import java.time.format.DateTimeFormatter

trait CommentRepository {
  def getComments(email: String, monthAndYear: String): IO[List[Comment]]
  def upsertComment(
      comment: Comment
  ): IO[Either[CommentRepository.Failure, Int]]
  def deleteComment(
      comment: DeleteCommentDTO
  ): IO[Either[CommentRepository.Failure, Int]]
  def getCommentsSummary(
      givenTo: String,
      monthAndYear: String
  ): IO[List[CommentSummary]]
}

object CommentRepository {
  enum Failure {
    case CommentUpsert(reason: String)
    case CommentDeletion(reason: String)
  }

  def instance(dbConfig: DatabaseConfig): CommentRepository =
    new CommentRepository {
      override def getComments(
          email: String,
          monthAndYear: String
      ): IO[List[Comment]] = {
        sql"""SELECT * FROM comments WHERE given_by = $email AND month_and_year = $monthAndYear"""
          .query[Comment]
          .to[List]
          .transact(dbConfig.xa)
      }

      override def upsertComment(comment: Comment): IO[Either[Failure, Int]] = {
        sql"""INSERT INTO comments (month_and_year, given_to, given_by, comment)
           VALUES (${comment.monthAndYear}, ${comment.givenTo}, ${comment.givenBy}, ${comment.comment})
           ON CONFLICT (month_and_year, given_to, given_by)
           DO UPDATE SET comment = EXCLUDED.comment;""".update.run
          .transact(dbConfig.xa)
          .attemptSql
          .map(_.leftMap(e => Failure.CommentUpsert(e.getMessage)))
      }

      override def deleteComment(
          comment: DeleteCommentDTO
      ): IO[Either[Failure, Int]] = {
        sql"""DELETE FROM comments
            WHERE month_and_year = ${comment.monthAndYear}
                AND given_by = ${comment.givenBy}
                AND given_to = ${comment.givenTo}""".update.run
          .transact(dbConfig.xa)
          .attemptSql
          .map(_.leftMap(e => Failure.CommentDeletion(e.getMessage)))
      }

      override def getCommentsSummary(
          givenTo: String,
          monthAndYear: String
      ): IO[List[CommentSummary]] = {
        sql"""SELECT given_by, comment FROM comments
           	WHERE given_to = $givenTo
           	AND month_and_year = $monthAndYear"""
          .query[CommentSummary]
          .to[List]
          .transact(dbConfig.xa)
      }

    }
}

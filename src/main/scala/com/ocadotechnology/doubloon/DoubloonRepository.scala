package com.ocadotechnology.doubloon

import cats.effect.IO
import cats.implicits.*
import com.ocadotechnology.common.DTO.GetSummary
import com.ocadotechnology.database.DatabaseConfig.xa
import com.ocadotechnology.doubloon.DTO.{DoubloonSummary, GetSpentByOthers, SpentByOthers}
import doobie.*
import doobie.implicits.*
import doobie.refined.implicits.*

import java.time.LocalDate
import java.time.format.DateTimeFormatter
trait DoubloonRepository {
  def getCurrentSpentDoubloonsByEmail(email: String): IO[List[Doubloon]]
  def createDoubloon(doubloon: Doubloon): IO[Either[DoubloonRepository.Failure, Int]]
  def updateDoubloon(doubloon: Doubloon): IO[Either[DoubloonRepository.Failure, Int]]
  def deleteDoubloon(doubloon: Doubloon): IO[Either[DoubloonRepository.Failure, Int]]

  def getAmountOfUsersInTeam(teamId: String): IO[Option[Int]]

  def getDoubloonsSpentByOthers(email: String, monthAndYear: String): IO[List[SpentByOthers]]

  def getDoubloonsSummary(givenTo: String, monthAndYear: String): IO[List[DoubloonSummary]]
  def getAvailableMonths: IO[List[String]]
}

object DoubloonRepository{

  enum Failure {
    case DoubloonCreationFailure(reason: String)
    case DoubloonUpdateFailure(reason: String)
    case DoubloonDeleteFailure(reason: String)
  }

  def instance: DoubloonRepository = new DoubloonRepository {

    override def getCurrentSpentDoubloonsByEmail(email: String): IO[List[Doubloon]] = {
      val monthYearFormatter = DateTimeFormatter.ofPattern("MM-yyyy")
      val currentDateFormatted = LocalDate.now().format(monthYearFormatter)
      sql"""SELECT * FROM doubloons WHERE given_by = $email AND month_and_year = $currentDateFormatted"""
        .query[Doubloon]
        .to[List]
        .transact(xa)
    }

    override def createDoubloon(doubloon: Doubloon): IO[Either[Failure, Int]] = {
      sql"""INSERT INTO doubloons (category_id, given_to, given_by, amount, month_and_year)
           VALUES (${doubloon.categoryId}, ${doubloon.givenTo},
            ${doubloon.givenBy}, ${doubloon.amount}, ${doubloon.monthAndYear}) RETURNING doubloon_id"""
        .update
        .withUniqueGeneratedKeys[Int]("doubloon_id")
        .transact(xa)
        .attemptSql
        .map(_.leftMap(e => Failure.DoubloonCreationFailure(e.getMessage)))
    }

    override def updateDoubloon(doubloon: Doubloon): IO[Either[Failure, Int]] = {
      sql"""UPDATE doubloons
            SET amount = ${doubloon.amount}
            WHERE doubloon_id = ${doubloon.doubloonId}"""
        .update
        .run
        .transact(xa)
        .attemptSql
        .map(_.leftMap(e => Failure.DoubloonUpdateFailure(e.getMessage)))
    }

    override def deleteDoubloon(doubloon: Doubloon): IO[Either[Failure, Int]] = {
      sql"""DELETE FROM doubloons
            WHERE doubloon_id = ${doubloon.doubloonId}"""
        .update
        .run
        .transact(xa)
        .attemptSql
        .map(_.leftMap(e => Failure.DoubloonDeleteFailure(e.getMessage)))
    }

    override def getAmountOfUsersInTeam(teamId: String): IO[Option[Int]] = {
      sql"""SELECT COUNT(*) FROM users WHERE team_id = $teamId"""
        .query[Int]
        .option
        .transact(xa)
    }

    override def getDoubloonsSpentByOthers(email: String, monthAndYear: String): IO[List[SpentByOthers]] = {
      sql"""SELECT given_by, SUM(amount) FROM doubloons
           	WHERE month_and_year = $monthAndYear
           	AND given_by IN (
           		SELECT email FROM users WHERE team_id = (SELECT team_id FROM users WHERE email = $email)
           		    AND email != $email
           	)
           	GROUP BY given_by"""
        .query[SpentByOthers]
        .to[List]
        .transact(xa)
    }

    override def getDoubloonsSummary(givenTo: String, monthAndYear: String): IO[List[DoubloonSummary]] = {
      sql"""SELECT given_by, category_id, amount FROM doubloons
           	WHERE given_to = $givenTo
           	AND month_and_year = $monthAndYear"""
        .query[DoubloonSummary]
        .to[List]
        .transact(xa)
    }

    override def getAvailableMonths: IO[List[String]] = {
      sql"""WITH sorted_cte AS (
             SELECT month_and_year,
                    ROW_NUMBER() OVER (PARTITION BY SUBSTRING(month_and_year, 4, 4), SUBSTRING(month_and_year, 1, 2)
                                       ORDER BY SUBSTRING(month_and_year, 4, 4)::int DESC, SUBSTRING(month_and_year, 1, 2)::int DESC) AS rn
             FROM doubloons
           )
           SELECT month_and_year
           FROM sorted_cte
           WHERE rn = 1
           ORDER BY SUBSTRING(month_and_year, 4, 4)::int DESC, SUBSTRING(month_and_year, 1, 2)::int DESC;"""
        .query[String]
        .to[List]
        .transact(xa)
    }

  }
}

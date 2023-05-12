package com.ocadotechnology.doubloon

import cats.effect.IO
import cats.implicits.*
import com.ocadotechnology.database.DatabaseConfig.xa
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

  def getDoubloonsSpentByOthers(data: GetSpentByOthersDTO): IO[List[SpentByOthersDTO]]
  
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

    override def getDoubloonsSpentByOthers(data: GetSpentByOthersDTO): IO[List[SpentByOthersDTO]] = {
      sql"""SELECT given_by, SUM(amount) FROM doubloons
           	WHERE month_and_year = ${data.monthAndDate}
           	AND given_by IN (
           		SELECT email FROM users WHERE team_id = ${data.teamId} AND email != ${data.email}
           	)
           	GROUP BY given_by"""
        .query[SpentByOthersDTO]
        .to[List]
        .transact(xa)
    }
    
  }
}

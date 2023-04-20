package com.ocadotechnology.doubloon

import cats.effect.IO
import cats.implicits.*
import com.ocadotechnology.database.DatabaseConfig.xa
import doobie.*
import doobie.implicits.*
import doobie.refined.implicits.*
trait DoubloonRepository {

  def createDoubloon(doubloon: Doubloon): IO[Either[DoubloonRepository.Failure, Int]]
  def updateDoubloon(doubloon: Doubloon): IO[Either[DoubloonRepository.Failure, Int]]
  def deleteDoubloon(doubloon: Doubloon): IO[Either[DoubloonRepository.Failure, Int]]

}

object DoubloonRepository{

  enum Failure:
    case DoubloonCreationFailure(reason: String)
    case DoubloonUpdateFailure(reason: String)
    case DoubloonDeleteFailure(reason: String)

  def instance: DoubloonRepository = new DoubloonRepository:

    override def createDoubloon(doubloon: Doubloon): IO[Either[Failure, Int]] =
      sql"""INSERT INTO doubloons (doubloon_id, category_id, given_to, given_by, amount, month_and_year)
           VALUES (${doubloon.doubloonId}, ${doubloon.categoryId}, ${doubloon.givenTo},
            ${doubloon.givenBy}, ${doubloon.amount}, ${doubloon.monthAndYear})"""
        .update
        .run
        .transact(xa)
        .attemptSql
        .map(_.leftMap(e => Failure.DoubloonCreationFailure(e.getMessage)))

    override def updateDoubloon(doubloon: Doubloon): IO[Either[Failure, Int]] =
      sql"""UPDATE doubloons
            SET amount = ${doubloon.amount}
            WHERE doubloon_id = ${doubloon.doubloonId}"""
        .update
        .run
        .transact(xa)
        .attemptSql
        .map(_.leftMap(e => Failure.DoubloonUpdateFailure(e.getMessage)))

    override def deleteDoubloon(doubloon: Doubloon): IO[Either[Failure, Int]] =
      sql"""DELETE FROM doubloons
            WHERE doubloon_id = ${doubloon.doubloonId}"""
      .update
      .run
      .transact(xa)
      .attemptSql
      .map(_.leftMap(e => Failure.DoubloonDeleteFailure(e.getMessage)))


}

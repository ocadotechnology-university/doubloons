package com.ocadotechnology.doubloons.team

import cats.effect.IO
import cats.implicits.*
import com.ocadotechnology.doubloons.database.DatabaseConfig
import doobie.*
import doobie.implicits.*
import doobie.refined.implicits.*
import eu.timepit.refined.api.*
import eu.timepit.refined.types.string.NonEmptyString

trait TeamRepository {
  def getTeamInfo(teamId: String): IO[Option[Team]]
  def createTeam(
      name: NonEmptyString,
      description: NonEmptyString
  ): IO[Either[TeamRepository.Failure, NonEmptyString]]
}

object TeamRepository {

  enum Failure {
    case TeamCreationFailure(reason: String)
  }

  def instance(dbConfig: DatabaseConfig): TeamRepository = new TeamRepository {

    override def getTeamInfo(teamId: String): IO[Option[Team]] =
      sql"""SELECT * FROM teams WHERE team_id = $teamId"""
        .query[Team]
        .option
        .transact(dbConfig.xa)

    override def createTeam(
        name: NonEmptyString,
        description: NonEmptyString
    ): IO[Either[TeamRepository.Failure, NonEmptyString]] =
      for {
        uuid <- IO.randomUUID
        id = uuid.toString()
        result <- sql"""
        INSERT INTO doubloons.teams (team_id, team_name, team_description) VALUES ($id, $name, $description)
        """.update.run.transact(dbConfig.xa).attemptSql
      } yield result
        .map(_ => Refined.unsafeApply(id)) // safe since uuid is never empty
        .leftMap(e => Failure.TeamCreationFailure(e.getMessage))

  }

}

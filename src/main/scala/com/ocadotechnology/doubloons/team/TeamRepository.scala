package com.ocadotechnology.doubloons.team

import cats.effect.IO
import cats.implicits.*
import com.ocadotechnology.doubloons.database.DatabaseConfig.xa
import doobie.*
import doobie.implicits.*
import doobie.refined.implicits.*

trait TeamRepository {
  def getTeamInfo(teamId: String): IO[Option[Team]]
}

object TeamRepository {
  def instance: TeamRepository = new TeamRepository {
    override def getTeamInfo(teamId: String): IO[Option[Team]] = {
      sql"""SELECT * FROM teams WHERE team_id = $teamId"""
        .query[Team]
        .option
        .transact(xa)
    }
  }
}

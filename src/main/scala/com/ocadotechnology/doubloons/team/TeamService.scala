package com.ocadotechnology.doubloons.team

import cats.effect.IO
import com.ocadotechnology.doubloons.BusinessError

trait TeamService {
  def getTeamInfo(teamId: String): IO[Either[BusinessError, Team]]
}

object TeamService {
  def instance(teamRepository: TeamRepository): TeamService = new TeamService {
    override def getTeamInfo(
        teamId: String
    ): IO[Either[BusinessError, Team]] = {
      teamRepository
        .getTeamInfo(teamId)
        .map {
          case None => Left(BusinessError(s"No team found for teamId: $teamId"))
          case Some(team) => Right(team)
        }
    }
  }
}

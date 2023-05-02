package com.ocadotechnology.team

import cats.effect.IO

trait TeamService {
  def getTeamInfo(teamId: String): IO[Either[String, Team]]
}

object TeamService {
  def instance(teamRepository: TeamRepository): TeamService = new TeamService {
    override def getTeamInfo(teamId: String): IO[Either[String, Team]] = {
      teamRepository.getTeamInfo(teamId)
        .map {
          case None => Left(s"No team found for teamId: $teamId")
          case Some(team) => Right(team)
        }
    }
  }
}

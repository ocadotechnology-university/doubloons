package com.ocadotechnology.doubloons.team

import cats.effect.IO
import cats.implicits.*
import com.ocadotechnology.doubloons.BusinessError
import com.ocadotechnology.doubloons.comment.DTO.CreateTeam
import eu.timepit.refined.types.string.NonEmptyString

trait TeamService {
  def getTeamInfo(teamId: String): IO[Either[BusinessError, Team]]
  def createTeam(team: CreateTeam): IO[Either[BusinessError, Team]]
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

    override def createTeam(team: CreateTeam): IO[Either[BusinessError, Team]] =
      teamRepository.createTeam(team.name, team.description).map {
        case Right(id) => Team(id, team.name, team.description.some).asRight
        case Left(error) =>
          Left(BusinessError(s"Failed to create team due to $error"))
      }
  }
}

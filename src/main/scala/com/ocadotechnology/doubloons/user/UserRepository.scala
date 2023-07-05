package com.ocadotechnology.doubloons.user

import cats.effect.IO
import cats.implicits.*
import com.ocadotechnology.doubloons.database.DatabaseConfig.xa
import doobie.*
import doobie.implicits.*
import doobie.refined.implicits.*

/** Implements CRUD operations using User model
  */
trait UserRepository {
  def createUser(user: User): IO[Either[UserRepository.Failure, Int]]

  def getUserByEmail(email: String): IO[Option[UserView]]

  def getUsersByTeamId(teamId: String): IO[List[UserView]]

  def assignUserToTeam(
      email: String,
      teamId: String
  ): IO[Either[UserRepository.Failure, Unit]]
}

object UserRepository {

  enum Failure {
    case UserCreationFailure(reason: String)
    case NoSuchTeam(teamId: String)
  }

  def instance: UserRepository = new UserRepository {

    override def createUser(user: User): IO[Either[Failure, Int]] = {
      sql"""INSERT INTO users (email, team_id, first_name, last_name, password, avatar)
           VALUES (${user.email}, ${user.teamId}, ${user.firstName}, ${user.lastName}, ${user.password}, ${user.avatar})""".update.run
        .transact(xa)
        .attemptSql
        .map(_.leftMap(e => Failure.UserCreationFailure(e.getMessage)))
    }

    override def assignUserToTeam(
        email: String,
        teamId: String
    ): IO[Either[Failure, Unit]] =
      sql"""UPDATE users SET team_id=${teamId} WHERE email=${email}""".update.run
        .transact(xa)
        .attemptSql
        .map(_.leftMap(e => Failure.NoSuchTeam(e.getMessage)).map(_ => ()))

    override def getUserByEmail(email: String): IO[Option[UserView]] = {
      sql"""SELECT email, team_id, first_name, last_name, avatar FROM users WHERE email = $email """
        .query[UserView]
        .option
        .transact(xa)
    }

    override def getUsersByTeamId(teamId: String): IO[List[UserView]] = {
      sql"""SELECT email, team_id, first_name, last_name, avatar FROM users WHERE team_id = $teamId """
        .query[UserView]
        .to[List]
        .transact(xa)
    }

  }
}

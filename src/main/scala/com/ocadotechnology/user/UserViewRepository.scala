package com.ocadotechnology.user

import cats.effect.IO
import com.ocadotechnology.database.DatabaseConfig.xa
import com.ocadotechnology.user.UserViewRepository
import doobie.*
import doobie.implicits.*
import doobie.refined.implicits.*

/**
 * Implements Read operations using UserView model
 */

trait UserViewRepository {
  def getUserByEmail(email: String): IO[Option[UserView]]
  def getUsersByTeamId(teamId: String): IO[List[UserView]]
}
object UserViewRepository {
  
  def instance: UserViewRepository = new UserViewRepository {

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

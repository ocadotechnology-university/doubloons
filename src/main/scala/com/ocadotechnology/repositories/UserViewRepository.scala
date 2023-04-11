package com.ocadotechnology.repositories

import cats.effect.IO
import doobie.*
import doobie.implicits.*
import doobie.refined.implicits.*

import com.ocadotechnology.models.UserView
import com.ocadotechnology.database.DatabaseConfig.xa

/**
 * Implements Read operations using UserView model
 */

trait UserViewRepository {
  def getUserByEmail(email: String): IO[Option[UserView]]
  def getUsersByTeamId(teamId: Int): IO[List[UserView]]
}
object UserViewRepository {
  
  def instance: UserViewRepository = new UserViewRepository:

    override def getUserByEmail(email: String): IO[Option[UserView]] =
      sql"""SELECT email, team_id, first_name, last_name, avatar FROM users WHERE email = $email """
        .query[UserView]
        .option
        .transact(xa)

    override def getUsersByTeamId(teamId: Int): IO[List[UserView]] = 
      sql"""SELECT email, team_id, first_name, last_name, avatar FROM users WHERE team_id = $teamId """
        .query[UserView]
        .to[List]
        .transact(xa)
    
}

package com.ocadotechnology.repositories

import cats.effect.IO
import doobie.*
import doobie.implicits.*
import doobie.refined.implicits.*

import com.ocadotechnology.models.UserView
import com.ocadotechnology.database.DatabaseConfig.xa

/**
 * Implements CRUD operations on data entities for UserView model
 */

trait UserViewRepository {
  def getUserByEmail(email: String): IO[Option[UserView]]
  def getUsersByTeamId(teamId: Int): IO[List[UserView]]
}
object UserViewRepository {
  
  def instance: UserViewRepository = new UserViewRepository:

    override def getUserByEmail(email: String): IO[Option[UserView]] =
      sql"""SELECT email, "teamId", "firstName", "lastName", avatar, "leadingTeam" FROM users WHERE email = $email """
        .query[UserView]
        .option
        .transact(xa)

    override def getUsersByTeamId(teamId: Int): IO[List[UserView]] = 
      sql"""SELECT email, "teamId", "firstName", "lastName", avatar, "leadingTeam" FROM users WHERE "teamId" = $teamId """
        .query[UserView]
        .to[List]
        .transact(xa)
    
}

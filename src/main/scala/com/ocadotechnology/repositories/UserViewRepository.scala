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
  def getUserViewListByTeamId(teamId: Int): IO[List[UserView]]
}
object UserViewRepository {
  
  def instance: UserViewRepository = new UserViewRepository:
    override def getUserViewListByTeamId(teamId: Int): IO[List[UserView]] = 
      sql"""SELECT email, "teamId", "firstName", "lastName", avatar, "leadingTeam" FROM users WHERE "teamId" = $teamId """
        .query[UserView]
        .to[List]
        .transact(xa)
    
}

package com.ocadotechnology.repositories

import cats.effect.IO
import doobie.implicits.toSqlInterpolator
import sttp.tapir.server.http4s.Http4sServerInterpreter
import doobie.*
import doobie.implicits.*
import doobie.util.transactor.Transactor.Aux
import doobie.refined.*
import doobie.refined.implicits.*

import com.ocadotechnology.models.UserView
import com.ocadotechnology.database.DatabaseConfig.xa

// Implements CRUD operations on data entities for UserView model
object UserViewRepository {
  def getUserViewListByTeamId(teamId: Int): IO[List[UserView]] = {
    sql"""SELECT email, "teamId", "firstName", "lastName", avatar, "leadingTeam" FROM users WHERE "teamId" = $teamId """
      .query[UserView]
      .to[List]
      .transact(xa)
  }
}

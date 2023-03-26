package com.ocadotechnology.repositories

import cats.effect.IO
import doobie.implicits.toSqlInterpolator
import sttp.tapir.server.http4s.Http4sServerInterpreter

import doobie.*
import doobie.implicits.*
import doobie.util.transactor.Transactor.Aux
import doobie.refined.*
import doobie.refined.implicits.*

import com.ocadotechnology.models.User
import com.ocadotechnology.database.DatabaseConfig.xa


// Implements CRUD operations on data entities for User model
object UserRepository {

  def getUserByEmail(email: String): IO[Option[User]] = {
    sql"""SELECT * FROM users WHERE email = $email """
      //    sql"""SELECT * FROM "Users" LIMIT 1"""
      .query[User]
      .option
      .transact(xa)
  }

}

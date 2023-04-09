package com.ocadotechnology.repositories

import cats.effect.IO
import doobie.*
import doobie.implicits.*
import doobie.refined.implicits.*

import com.ocadotechnology.models.User
import com.ocadotechnology.database.DatabaseConfig.xa

/**
 * Implements CRUD operations on data entities for User model
 */
trait UserRepository {
  def createUser(user: User): IO[Either[java.sql.SQLException, Int]]
}

object UserRepository {
  def instance: UserRepository = new UserRepository:

    override def createUser(user: User): IO[Either[java.sql.SQLException, Int]] =
      sql"""INSERT INTO users (email, "teamId", "firstName", "lastName", password, avatar)
           VALUES (${user.email}, ${user.teamId}, ${user.firstName}, ${user.lastName}, ${user.password}, ${user.avatar})"""
        .update
        .run
        .transact(xa)
        .attemptSql
}

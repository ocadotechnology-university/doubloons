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
  def getUserByEmail(email: String): IO[Option[User]]

}

object UserRepository {
  def instance: UserRepository = new UserRepository:
    override def getUserByEmail(email: String): IO[Option[User]] =
      sql"""SELECT * FROM users WHERE email = $email """
        .query[User]
        .option
        .transact(xa)
  
}

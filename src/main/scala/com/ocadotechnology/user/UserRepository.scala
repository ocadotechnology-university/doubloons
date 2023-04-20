package com.ocadotechnology.user

import cats.effect.IO
import cats.implicits.*
import com.ocadotechnology.database.DatabaseConfig.xa
import doobie.*
import doobie.implicits.*
import doobie.refined.implicits.*

/**
 * Implements CRUD operations using User model
 */
trait UserRepository {
  def createUser(user: User): IO[Either[UserRepository.Failure, Int]]
}

object UserRepository {

  enum Failure {
    case UserCreationFailure(reason: String)
  }

  def instance: UserRepository = new UserRepository {

    override def createUser(user: User): IO[Either[Failure, Int]] = {
      sql"""INSERT INTO users (email, team_id, first_name, last_name, password, avatar)
           VALUES (${user.email}, ${user.teamId}, ${user.firstName}, ${user.lastName}, ${user.password}, ${user.avatar})"""
        .update
        .run
        .transact(xa)
        .attemptSql
        .map(_.leftMap(e => Failure.UserCreationFailure(e.getMessage)))
    }
    
  }
}

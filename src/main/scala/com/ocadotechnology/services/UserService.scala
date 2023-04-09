package com.ocadotechnology.services

import cats.effect.IO
import com.ocadotechnology.models.User
import com.ocadotechnology.repositories.UserRepository
import io.circe.JsonObject
import eu.timepit.refined.auto.autoUnwrap

/**
 * Business Logic for User model
 */

trait UserService {
  def getUserByEmailLogic(email: String): IO[Either[String, User]]
  def createUser(user: User): IO[Either[String, String]]
}
object UserService {


  def instance(userRepository: UserRepository): UserService = new UserService:
    override def getUserByEmailLogic(email: String): IO[Either[String, User]] =
      userRepository.getUserByEmail(email)
        .map {
          case Some(user) => Right(user)
          case None => Left("User not found")
        }

    override def createUser(user: User): IO[Either[String, String]] =
        userRepository.createUser(user).map {
          case Left(e: java.sql.SQLException) => Left(s"${e.getMessage}")
          case Right(value) => Right(s"Affected rows: $value")
        }

}

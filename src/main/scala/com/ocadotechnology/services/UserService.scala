package com.ocadotechnology.services

import cats.effect.IO
import com.ocadotechnology.models.User
import com.ocadotechnology.repositories.UserRepository

/**
 * Business Logic for User model
 */

trait UserService {
  def createUserLogic(user: User): IO[Either[String, String]]
}
object UserService {
  
  def instance(userRepository: UserRepository): UserService = new UserService:

    override def createUserLogic(user: User): IO[Either[String, String]] =
        userRepository.createUser(user).map {
          case Left(e: java.sql.SQLException) => Left(s"${e.getMessage}")
          case Right(value) => Right(s"Affected rows: $value")
        }
        
}

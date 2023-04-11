package com.ocadotechnology.services

import cats.effect.IO
import com.ocadotechnology.models.User
import com.ocadotechnology.repositories.UserRepository

/**
 * Business Logic for User model
 */

trait UserService {
  def createUser(user: User): IO[Either[String, Unit]]
}
object UserService {
  
  def instance(userRepository: UserRepository): UserService = new UserService:

    override def createUser(user: User): IO[Either[String, Unit]] =
        userRepository.createUser(user).map {
          case Left(e: java.sql.SQLException) => Left(s"${e.getMessage}")
          case Right(_) => Right(())
        }
        
}

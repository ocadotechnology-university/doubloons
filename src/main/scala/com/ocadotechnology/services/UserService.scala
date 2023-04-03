package com.ocadotechnology.services

import cats.effect.IO
import com.ocadotechnology.models.User
import com.ocadotechnology.repositories.UserRepository

/**
 * Business Logic for User model
 */

trait UserService {
  def getUserByEmailLogic(email: String): IO[Either[String, User]]
}
object UserService {


  def instance(userRepository: UserRepository): UserService = new UserService:
    override def getUserByEmailLogic(email: String): IO[Either[String, User]] =
      userRepository.getUserByEmail(email)
        .map {
          case Some(user) => Right(user)
          case None => Left("User not found")
        }

}

 

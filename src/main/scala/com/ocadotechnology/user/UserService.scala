package com.ocadotechnology.user

import cats.effect.IO
import com.ocadotechnology.user.UserService

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
        case Left(UserRepository.Failure.UserCreationFailure(reason)) => Left(s"$reason")
        case Right(_) => Right(())
      }
        
}

package com.ocadotechnology.user

import cats.effect.IO
import com.ocadotechnology.user.UserService

/**
 * Business Logic for User model
 */

trait UserService {
  def createUser(user: User): IO[Either[String, Unit]]

  def getUserByEmail(email: String): IO[Either[String, UserView]]

  def getUsersByTeamId(teamId: String): IO[Either[String, List[UserView]]]
}
object UserService {
  
  def instance(userRepository: UserRepository): UserService = new UserService {

    override def createUser(user: User): IO[Either[String, Unit]] = {
      userRepository.createUser(user).map {
        case Left(UserRepository.Failure.UserCreationFailure(reason)) => Left(s"$reason")
        case Right(_) => Right(())
      }
    }

    override def getUserByEmail(email: String): IO[Either[String, UserView]] = {
      userRepository.getUserByEmail(email)
        .map {
          case Some(user) => Right(user)
          case None => Left("User not found")
        }
    }

    override def getUsersByTeamId(teamId: String): IO[Either[String, List[UserView]]] = {
      userRepository.getUsersByTeamId(teamId)
        .map {
          case Nil => Left(s"No users found with teamId: $teamId")
          case userViews => Right(userViews)
        }
    }
    
  }
}

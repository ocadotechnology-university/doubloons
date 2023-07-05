package com.ocadotechnology.doubloons.user

import cats.effect.IO
import cats.implicits.*
import com.ocadotechnology.doubloons.user.UserService
import com.ocadotechnology.doubloons.BusinessError

/** Business Logic for User model
  */

trait UserService {
  def createUser(user: User): IO[Either[BusinessError, Unit]]

  def getUserByEmail(email: String): IO[Either[BusinessError, UserView]]

  def joinTeam(email: String, teamId: String): IO[Either[BusinessError, Unit]]

  def getUsersByTeamId(
      teamId: String
  ): IO[Either[BusinessError, List[UserView]]]
}
object UserService {

  def instance(userRepository: UserRepository): UserService = new UserService {

    override def joinTeam(
        email: String,
        teamId: String
    ): IO[Either[BusinessError, Unit]] =
      userRepository.assignUserToTeam(email, teamId).map {
        case Left(reason) => Left(BusinessError(s"$reason"))
        case Right(_)     => Right(())
      }

    override def createUser(user: User): IO[Either[BusinessError, Unit]] = {
      userRepository.createUser(user).map {
        case Left(UserRepository.Failure.UserCreationFailure(reason)) =>
          Left(BusinessError(s"$reason"))
        case Right(_) => Right(())
      }
    }

    override def getUserByEmail(
        email: String
    ): IO[Either[BusinessError, UserView]] = {
      userRepository
        .getUserByEmail(email)
        .map {
          case Some(user) => Right(user)
          case None       => Left(BusinessError("User not found"))
        }
    }

    override def getUsersByTeamId(
        teamId: String
    ): IO[Either[BusinessError, List[UserView]]] = {
      userRepository
        .getUsersByTeamId(teamId)
        .map { result => Right(result) }
    }

  }
}

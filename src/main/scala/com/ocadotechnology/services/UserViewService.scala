package com.ocadotechnology.services

import scala.util.Try
import cats.effect.IO
import com.ocadotechnology.models.UserView
import com.ocadotechnology.repositories
import com.ocadotechnology.repositories.UserViewRepository

/**
 * Business Logic for UserView model
 */

trait UserViewService {
  def getUserByEmailLogic(email: String): IO[Either[String, UserView]]
  def getUsersByTeamIdLogic(teamId: String): IO[Either[String, List[UserView]]]
}
object UserViewService {
  
  def instance(userViewRepository: UserViewRepository): UserViewService = new UserViewService:
    
    override def getUserByEmailLogic(email: String): IO[Either[String, UserView]] =
      userViewRepository.getUserByEmail(email)
        .map {
          case Some(user) => Right(user)
          case None => Left("User not found")
        }
    
    override def getUsersByTeamIdLogic(teamId: String): IO[Either[String, List[UserView]]] =
      Try(teamId.toInt).toEither match {
        case Left(_) => IO.pure(Left(s"Invalid teamId: $teamId"))
        case Right(teamIdInt) =>
          userViewRepository.getUsersByTeamId(teamIdInt)
            .map {
              case Nil => Left(s"No users found with teamId: $teamId")
              case userViews => Right(userViews)
            }
      }

}

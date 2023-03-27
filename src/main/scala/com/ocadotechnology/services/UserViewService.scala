package com.ocadotechnology.services

import scala.util.Try
import cats.effect.IO

import com.ocadotechnology.models.UserView
import com.ocadotechnology.repositories.UserViewRepository.*

/**
 * Business Logic for UserView model
 */
object UserViewService {
  def getUserViewListByTeamIdLogic(teamId: String): IO[Either[String, List[UserView]]] = {
    
    Try(teamId.toInt).toEither match {
      case Left(_) => IO.pure(Left(s"Invalid teamId: $teamId"))
      case Right(teamIdInt) =>
        getUserViewListByTeamId(teamIdInt)
          .map {
            case Nil => Left(s"No users found with teamId: $teamId")
            case userViews => Right(userViews)
          }
    }
  }

}

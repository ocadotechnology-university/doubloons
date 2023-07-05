package com.ocadotechnology.doubloons.doubloon

import cats.effect.IO
import com.ocadotechnology.doubloons.common.DTO.GetSummary
import com.ocadotechnology.doubloons.doubloon.DTO.{
  DoubloonSummary,
  GetSpentByOthers,
  SpentByOthers
}
import com.ocadotechnology.doubloons.BusinessError
trait DoubloonService {

  def getCurrentSpentDoubloonsByEmail(
      email: String
  ): IO[Either[BusinessError, List[Doubloon]]]
  def createDoubloon(doubloon: Doubloon): IO[Either[BusinessError, Int]]
  def updateDoubloon(doubloon: Doubloon): IO[Either[BusinessError, Unit]]
  def deleteDoubloon(doubloon: Doubloon): IO[Either[BusinessError, Unit]]
  def getAmountToSpend(teamId: String): IO[Either[BusinessError, Int]]
  def calculateAmountToSpend(usersAmount: Int): Int
  def getDoubloonsSpentByOthers(
      email: String,
      monthAndYear: String
  ): IO[Either[BusinessError, List[SpentByOthers]]]
  def getDoubloonsSummary(
      givenTo: String,
      monthAndYear: String
  ): IO[Either[BusinessError, List[DoubloonSummary]]]
  def getAvailableMonths(email: String): IO[Either[BusinessError, List[String]]]
}

object DoubloonService {

  def instance(doubloonRepository: DoubloonRepository): DoubloonService =
    new DoubloonService {

      override def getCurrentSpentDoubloonsByEmail(
          email: String
      ): IO[Either[BusinessError, List[Doubloon]]] = {
        doubloonRepository.getCurrentSpentDoubloonsByEmail(email).map {
          result => Right(result)
        }
      }

      override def createDoubloon(
          doubloon: Doubloon
      ): IO[Either[BusinessError, Int]] = {
        doubloonRepository.createDoubloon(doubloon).map {
          case Left(reason) =>
            Left(BusinessError(s"Failed to create doubloon due to $reason"))
          case Right(id) => Right(id)
        }
      }

      override def updateDoubloon(
          doubloon: Doubloon
      ): IO[Either[BusinessError, Unit]] = {
        doubloonRepository.updateDoubloon(doubloon).map {
          case Left(reason) =>
            Left(BusinessError(s"Failed to update doubloon due to $reason"))
          case Right(_) => Right(())
        }
      }

      override def deleteDoubloon(
          doubloon: Doubloon
      ): IO[Either[BusinessError, Unit]] = {
        doubloonRepository.deleteDoubloon(doubloon).map {
          case Left(reason) =>
            Left(BusinessError(s"Failed to delete doubloon due to $reason"))
          case Right(_) => Right(())
        }
      }

      override def getAmountToSpend(
          teamId: String
      ): IO[Either[BusinessError, Int]] = {
        doubloonRepository
          .getAmountOfUsersInTeam(teamId)
          .map {
            case Some(numOfUsers) if numOfUsers > 0 =>
              Right(calculateAmountToSpend(numOfUsers))
            case _ =>
              Left(
                BusinessError(
                  s"No users found in team with teamId: $teamId - cannot calculate the amount of doubloons to spend"
                )
              )
          }
      }

      override def calculateAmountToSpend(usersAmount: Int): Int = {
        (usersAmount - 1) * 3
      }

      override def getDoubloonsSpentByOthers(
          email: String,
          monthAndYear: String
      ): IO[Either[BusinessError, List[SpentByOthers]]] = {
        doubloonRepository.getDoubloonsSpentByOthers(email, monthAndYear).map {
          result => Right(result)
        }
      }

      override def getDoubloonsSummary(
          givenTo: String,
          monthAndYear: String
      ): IO[Either[BusinessError, List[DoubloonSummary]]] = {
        doubloonRepository.getDoubloonsSummary(givenTo, monthAndYear).map {
          result => Right(result)
        }
      }

      override def getAvailableMonths(
          email: String
      ): IO[Either[BusinessError, List[String]]] = {
        doubloonRepository.getAvailableMonths(email).map { result =>
          Right(result)
        }
      }

    }
}

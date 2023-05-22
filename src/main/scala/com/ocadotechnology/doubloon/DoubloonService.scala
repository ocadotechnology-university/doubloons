package com.ocadotechnology.doubloon

import cats.effect.IO
import com.ocadotechnology.common.DTO.GetSummary
import com.ocadotechnology.doubloon.DTO.{DoubloonSummary, GetSpentByOthers, SpentByOthers}
trait DoubloonService {
  
  def getCurrentSpentDoubloonsByEmail(email: String): IO[Either[String, List[Doubloon]]]
  def createDoubloon(doubloon: Doubloon): IO[Either[String, Int]]
  def updateDoubloon(doubloon: Doubloon): IO[Either[String, Unit]]
  def deleteDoubloon(doubloon: Doubloon): IO[Either[String, Unit]]
  def getAmountToSpend(teamId: String): IO[Either[String, Int]]
  def calculateAmountToSpend(usersAmount: Int): Int
  def getDoubloonsSpentByOthers(data: GetSpentByOthers): IO[Either[String, List[SpentByOthers]]]
  def getDoubloonResults(data: GetSummary): IO[Either[String, List[DoubloonSummary]]]
}

object  DoubloonService {
  
  def instance(doubloonRepository: DoubloonRepository): DoubloonService = new DoubloonService {

    override def getCurrentSpentDoubloonsByEmail(email: String): IO[Either[String, List[Doubloon]]] = {
      doubloonRepository.getCurrentSpentDoubloonsByEmail(email)
        .map {
          case Nil => Left(s"No spent doubloons found for user: $email this month")
          case doubloons => Right(doubloons)
        }
    }

    override def createDoubloon(doubloon: Doubloon): IO[Either[String, Int]] = {
      doubloonRepository.createDoubloon(doubloon).map {
        case Left(DoubloonRepository.Failure.DoubloonCreationFailure(reason)) => Left(s"$reason")
        case Right(id) => Right(id)
      }
    }

    override def updateDoubloon(doubloon: Doubloon): IO[Either[String, Unit]] = {
      doubloonRepository.updateDoubloon(doubloon).map {
        case Left(DoubloonRepository.Failure.DoubloonUpdateFailure(reason)) => Left(s"$reason")
        case Right(_) => Right(())
      }
    }

    override def deleteDoubloon(doubloon: Doubloon): IO[Either[String, Unit]] = {
      doubloonRepository.deleteDoubloon(doubloon).map {
        case Left(DoubloonRepository.Failure.DoubloonDeleteFailure(reason)) => Left(s"$reason")
        case Right(_) => Right(())
      }
    }

    override def getAmountToSpend(teamId: String): IO[Either[String, Int]] = {
      doubloonRepository.getAmountOfUsersInTeam(teamId)
        .map {
          case Some(numOfUsers) => Right(calculateAmountToSpend(numOfUsers))
          case None => Left(s"No users found in team with teamId: $teamId - cannot calculate the amount of doubloons to spend")
        }
    }

    override def calculateAmountToSpend(usersAmount: Int): Int = {
      (usersAmount - 1) * 3
    }

    override def getDoubloonsSpentByOthers(data: GetSpentByOthers): IO[Either[String, List[SpentByOthers]]] = {
      doubloonRepository.getDoubloonsSpentByOthers(data)
        .map {
          case Nil => Left(s"No spent doubloons found this month for users other than: ${data.email} in team: ${data.teamId}")
          case doubloons => Right(doubloons)
        }
    }

    override def getDoubloonResults(data: GetSummary): IO[Either[String, List[DoubloonSummary]]] = {
      doubloonRepository.getDoubloonResults(data)
        .map {
          case Nil => Left(s"No doubloons given to ${data.givenTo} found during ${data.monthAndYear}")
          case results => Right(results)
        }
    }

  }
}

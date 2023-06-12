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
  def getDoubloonsSpentByOthers(email: String, monthAndYear: String): IO[Either[String, List[SpentByOthers]]]
  def getDoubloonsSummary(givenTo: String, monthAndYear: String): IO[Either[String, List[DoubloonSummary]]]
  def getAvailableMonths: IO[Either[String, List[String]]]
}

object  DoubloonService {

  def instance(doubloonRepository: DoubloonRepository): DoubloonService = new DoubloonService {

    override def getCurrentSpentDoubloonsByEmail(email: String): IO[Either[String, List[Doubloon]]] = {
      doubloonRepository.getCurrentSpentDoubloonsByEmail(email).map{result => Right(result)}
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
          case Some(numOfUsers) if numOfUsers > 0 => Right(calculateAmountToSpend(numOfUsers))
          case _ => Left(s"No users found in team with teamId: $teamId - cannot calculate the amount of doubloons to spend")
        }
    }

    override def calculateAmountToSpend(usersAmount: Int): Int = {
      (usersAmount - 1) * 3
    }

    override def getDoubloonsSpentByOthers(email: String, monthAndYear: String): IO[Either[String, List[SpentByOthers]]] = {
      doubloonRepository.getDoubloonsSpentByOthers(email, monthAndYear).map{result => Right(result)}
    }

    override def getDoubloonsSummary(givenTo: String, monthAndYear: String): IO[Either[String, List[DoubloonSummary]]] = {
      doubloonRepository.getDoubloonsSummary(givenTo, monthAndYear).map{result => Right(result)}
    }

    override def getAvailableMonths: IO[Either[String, List[String]]] = {
      doubloonRepository.getAvailableMonths.map{result => Right(result)}
    }
    
  }
}

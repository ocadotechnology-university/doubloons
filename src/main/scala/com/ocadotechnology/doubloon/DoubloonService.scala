package com.ocadotechnology.doubloon

import cats.effect.IO
trait DoubloonService {
  
  def createDoubloon(doubloon: Doubloon): IO[Either[String, Unit]]
  def updateDoubloon(doubloon: Doubloon): IO[Either[String, Unit]]
  def deleteDoubloon(doubloon: Doubloon): IO[Either[String, Unit]]
  
}

object  DoubloonService{
  
  def instance(doubloonRepository: DoubloonRepository): DoubloonService = new DoubloonService:
    
    override def createDoubloon(doubloon: Doubloon): IO[Either[String, Unit]] =
      doubloonRepository.createDoubloon(doubloon).map {
        case Left(DoubloonRepository.Failure.DoubloonCreationFailure(reason)) => Left(s"$reason")
        case Right(_) => Right(())
      }
      
    override def updateDoubloon(doubloon: Doubloon): IO[Either[String, Unit]] =
      doubloonRepository.updateDoubloon(doubloon).map {
        case Left(DoubloonRepository.Failure.DoubloonUpdateFailure(reason)) => Left(s"$reason")
        case Right(_) => Right(())
        }

    override def deleteDoubloon(doubloon: Doubloon): IO[Either[String, Unit]] =
      doubloonRepository.deleteDoubloon(doubloon).map {
        case Left(DoubloonRepository.Failure.DoubloonDeleteFailure(reason)) => Left(s"$reason")
        case Right(_) => Right(())
      }
}

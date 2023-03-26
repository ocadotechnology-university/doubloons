package com.ocadotechnology.services

import cats.effect.IO
import com.ocadotechnology.models.User
import com.ocadotechnology.repositories.UserRepository._

// Business Logic for User model
object UserService {
  
  def getUserByEmailLogic(email: String): IO[Either[String, User]] = {
    getUserByEmail(email)
      .map {
        case Some(user) => Right(user)
        case None => Left("User not found")
      }
  }
  
}

 

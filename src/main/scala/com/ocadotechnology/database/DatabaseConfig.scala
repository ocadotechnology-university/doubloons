package com.ocadotechnology.database

import cats.effect.IO
import doobie.Transactor
import doobie.util.transactor.Transactor.Aux

object DatabaseConfig {

  val xa: Aux[IO, Unit] = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost:5432/doubloons_db",
    //"jdbc:postgresql://0.0.0.0:5432/Doubloons",
    "postgres",
    "admin"
  )

}

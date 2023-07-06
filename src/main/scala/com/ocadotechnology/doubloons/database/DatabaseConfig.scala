package com.ocadotechnology.doubloons.database

import cats.effect.IO
import doobie.Transactor
import doobie.util.transactor.Transactor.Aux
import com.ocado.ospnow.wms.otpconfig.RdsConfig

trait DatabaseConfig {
  def xa: Aux[IO, Unit]
}

object DatabaseConfig {

  def instance(rdsConfig: RdsConfig): DatabaseConfig = new DatabaseConfig {

    val xa: Aux[IO, Unit] = Transactor.fromDriverManager[IO](
      "org.postgresql.Driver",
      rdsConfig.jdbcUri,
      rdsConfig.user,
      rdsConfig.password.value
    )
  }

}

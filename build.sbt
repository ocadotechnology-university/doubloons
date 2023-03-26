val Scala3 = "3.2.2"

val Versions = new {
  val tapir = "1.2.9"
  val http4s = "0.23.18"
  val logback = "1.4.5"
  val circe = "3.8.13"
  val scalaTest = "3.2.15"
  val timepit = "0.10.2"
  val tapirDoobie = "0.19.0-M7"
  val postgresql = "42.5.4"
  val doobie = "1.0.0-RC1"
}

lazy val rootProject = (project in file(".")).settings(
  Seq(
    name := "doubloons",
    version := "0.1.0-SNAPSHOT",
    organization := "com.ocadotechnology",
    scalaVersion := Scala3,
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % Versions.tapir,
      "org.http4s" %% "http4s-ember-server" % Versions.http4s,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % Versions.tapir,
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % Versions.tapir,
      "ch.qos.logback" % "logback-classic" % Versions.logback,
      "com.softwaremill.sttp.tapir" %% "tapir-sttp-stub-server" % Versions.tapir % Test,
      "org.scalatest" %% "scalatest" % Versions.scalaTest % Test,
      "com.softwaremill.sttp.client3" %% "circe" % Versions.circe % Test,
      "io.circe" %% "circe-refined" % "0.14.5",
      "eu.timepit" %% "refined" % Versions.timepit,
      "org.tpolecat" %% "doobie-core"      % Versions.doobie,
      "org.tpolecat" %% "doobie-refined" % Versions.doobie,
      "org.tpolecat" %% "doobie-postgres" % Versions.doobie, // Postgres driver 42.3.1 + type mappings.
      "org.tpolecat" %% "doobie-scalatest" % Versions.doobie % "test",
      "com.softwaremill.sttp.tapir" %% "tapir-refined" % Versions.tapir,
      "com.softwaremill.sttp.tapir" %% "tapir-cats" % Versions.tapir
    )
  )
)


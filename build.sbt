val Scala3 = "3.2.2"

val Versions = new {
  val tapir = "1.2.9"
  val http4s = "0.23.18"
  val logback = "1.4.5"
  val circe = "3.8.13"
  val scalaTest = "3.2.15"
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
      "com.softwaremill.sttp.client3" %% "circe" % Versions.circe % Test
    )
  )
)


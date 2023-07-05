val Scala3 = "3.3.0"

val Versions = new {
  val tapir = "1.6.0"
  val http4s = "0.23.18"
  val logback = "1.4.5"
  val circe = "0.14.5"
  val sttp = "3.8.13"
  val scalaTest = "3.2.15"
  val refined = "0.10.2"
  val doobie = "1.0.0-RC2"
  val sttpOAuth2 = "0.17.0-RC1"
  val mouse = "1.2.1"
  val ducktape = "0.1.9"
}

lazy val rootProject = (project in file("."))
  .enablePlugins(OtpDeployPlugin)
  .settings(
    Seq(
      name := "doubloons",
      version := "0.1.0-SNAPSHOT",
      organization := "com.ocadotechnology.doubloons",
      scalaVersion := Scala3,
      libraryDependencies ++= Seq(
        "org.scalatest" %% "scalatest" % Versions.scalaTest % Test,
        "eu.timepit" %% "refined" % Versions.refined,
        "org.http4s" %% "http4s-ember-server" % Versions.http4s,
        "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % Versions.tapir,
        "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % Versions.tapir,
        "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % Versions.tapir,
        "com.softwaremill.sttp.tapir" %% "tapir-refined" % Versions.tapir,
        "com.softwaremill.sttp.tapir" %% "tapir-cats" % Versions.tapir,
        "com.softwaremill.sttp.tapir" %% "tapir-sttp-stub-server" % Versions.tapir % Test,
        "com.ocadotechnology" %% "sttp-oauth2" % Versions.sttpOAuth2,
        "com.ocadotechnology" %% "sttp-oauth2-circe" % Versions.sttpOAuth2,
        "ch.qos.logback" % "logback-classic" % Versions.logback,
        "io.circe" %% "circe-refined" % Versions.circe,
        "com.softwaremill.sttp.client3" %% "circe" % Versions.sttp,
        "com.softwaremill.sttp.client3" %% "fs2" % Versions.sttp,
        "org.tpolecat" %% "doobie-core" % Versions.doobie,
        "org.tpolecat" %% "doobie-refined" % Versions.doobie,
        "org.tpolecat" %% "doobie-postgres" % Versions.doobie,
        "org.tpolecat" %% "doobie-scalatest" % Versions.doobie % Test,
        "org.typelevel" %% "mouse" % Versions.mouse,
        "io.github.arainko" %% "ducktape" % Versions.ducktape
      ),
      assembly / assemblyMergeStrategy := SbtAssemblyAopPlugin
        .defaultStrategyWithAop((assembly / assemblyMergeStrategy).value),
      fork := true
    )
  )

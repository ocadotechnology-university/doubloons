package com.ocadotechnology

import cats.effect.{ExitCode, IO, IOApp}
import com.comcast.ip4s.{Host, Port, port}
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router
import sttp.tapir.server.http4s.Http4sServerInterpreter
import doobie.*
import doobie.implicits.*
import doobie.util.transactor.Transactor.Aux



object Main extends IOApp:

  val xa: Aux[IO, Unit] = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost:5432/Doubloons",
    "postgres",
    "admin"
  )
  def getUser(email: String): IO[Either[String, User]] = {
    sql"""SELECT * FROM "Users" WHERE "Email" = $email """
//    sql"""SELECT * FROM "Users" LIMIT 1"""
      .query[User]
      .option
      .transact(xa)
      .map {
        case Some(user) => Right(user)
        case None => Left("User not found")
      }
  }


  override def run(args: List[String]): IO[ExitCode] =


    val routes = Http4sServerInterpreter[IO]().toRoutes(Endpoints.all)

    val port = sys.env
      .get("HTTP_PORT")
      .flatMap(_.toIntOption)
      .flatMap(Port.fromInt)
      .getOrElse(port"8080")

    EmberServerBuilder
      .default[IO]
      .withHost(Host.fromString("0.0.0.0").get)
      .withPort(port)
      .withHttpApp(Router("/" -> routes).orNotFound)
      .build
      .use { server =>
        for {
          _ <- IO.println(s"Go to http://localhost:${server.address.getPort}/docs to open SwaggerUI. Press ENTER key to exit.")
          _ <- IO.readLine
        } yield ()
      }
      .as(ExitCode.Success)

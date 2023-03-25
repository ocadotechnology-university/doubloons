package com.ocadotechnology

import cats.effect.{ExitCode, IO, IOApp}
import com.comcast.ip4s.{Host, Port, port}
import com.ocadotechnology.Router.routes
import com.ocadotechnology.models.User
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router
import sttp.tapir.server.http4s.Http4sServerInterpreter



object Main extends IOApp:
  
  override def run(args: List[String]): IO[ExitCode] =
    
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

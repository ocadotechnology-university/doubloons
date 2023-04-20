package com.ocadotechnology

import cats.effect.{ExitCode, IO, IOApp}
import com.comcast.ip4s.{Host, Port, port}
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router
import sttp.tapir.server.http4s.Http4sServerInterpreter
import com.ocadotechnology.Router as ApplicationRouter
import com.ocadotechnology.doubloon.{DoubloonRepository, DoubloonService}
import com.ocadotechnology.user.{UserRepository, UserService, UserViewRepository, UserViewService}



object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {

    val port = sys.env
      .get("HTTP_PORT")
      .flatMap(_.toIntOption)
      .flatMap(Port.fromInt)
      .getOrElse(port"8080")

    val userRepository = UserRepository.instance
    val userService = UserService.instance(userRepository)

    val userViewRepository = UserViewRepository.instance
    val userViewService = UserViewService.instance(userViewRepository)


    val doubloonRepository = DoubloonRepository.instance
    val doubloonService = DoubloonService.instance(doubloonRepository)

    val router = new ApplicationRouter(userService, userViewService, doubloonService)

    EmberServerBuilder
      .default[IO]
      .withHost(Host.fromString("0.0.0.0").get)
      .withPort(port)
      .withHttpApp(Router("/" -> router.routes).orNotFound)
      .build
      .use { server =>
        for {
          _ <- IO.println(s"Go to http://localhost:${server.address.getPort}/docs to open SwaggerUI. Press ENTER key to exit.")
          _ <- IO.readLine
        } yield ()
      }
      .as(ExitCode.Success)
    
  }
}
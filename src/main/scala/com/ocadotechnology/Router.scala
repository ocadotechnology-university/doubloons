package com.ocadotechnology

import cats.effect.IO
import com.ocadotechnology.Endpoints.*
import com.ocadotechnology.services.{UserService, UserViewService}
import org.http4s.HttpRoutes
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter



object Router {
  val getUserServerEndpoint: ServerEndpoint[Any, IO] = getUserByEmail.serverLogic(email => UserService.getUserByEmailLogic(email))

  val getSafeUsersEndpoint: ServerEndpoint[Any, IO] = getUsersByTeamId.serverLogic(teamId => UserViewService.getUserViewListByTeamIdLogic(teamId))


  val apiEndpoints: List[ServerEndpoint[Any, IO]] = List(getUserServerEndpoint, getSafeUsersEndpoint)

  val docEndpoints: List[ServerEndpoint[Any, IO]] = SwaggerInterpreter()
    .fromServerEndpoints[IO](apiEndpoints, "doubloons", "1.0.0")

  val all: List[ServerEndpoint[Any, IO]] = apiEndpoints ++ docEndpoints

  val routes: HttpRoutes[IO] = Http4sServerInterpreter[IO]().toRoutes(all)
}

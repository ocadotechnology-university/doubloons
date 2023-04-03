package com.ocadotechnology

import cats.effect.IO
import org.http4s.HttpRoutes
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter

import com.ocadotechnology.Endpoints.*
import com.ocadotechnology.services.*


class Router(userService: UserService, userViewService: UserViewService) {
  val getUserServerEndpoint: ServerEndpoint[Any, IO] = getUserByEmail.serverLogic(email => userService.getUserByEmailLogic(email))

  val getSafeUsersEndpoint: ServerEndpoint[Any, IO] = getUsersByTeamId.serverLogic(teamId => userViewService.getUserViewListByTeamIdLogic(teamId))


  val apiEndpoints: List[ServerEndpoint[Any, IO]] = List(getUserServerEndpoint, getSafeUsersEndpoint)

  val docEndpoints: List[ServerEndpoint[Any, IO]] = SwaggerInterpreter()
    .fromServerEndpoints[IO](apiEndpoints, "doubloons", "1.0.0")

  val all: List[ServerEndpoint[Any, IO]] = apiEndpoints ++ docEndpoints

  val routes: HttpRoutes[IO] = Http4sServerInterpreter[IO]().toRoutes(all)
}

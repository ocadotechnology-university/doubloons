package com.ocadotechnology

import cats.effect.IO
import org.http4s.HttpRoutes
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import com.ocadotechnology.Endpoints.*
import com.ocadotechnology.user.{UserService, UserViewService}
import com.ocadotechnology.doubloon.DoubloonService


class Router(userService: UserService, userViewService: UserViewService, doubloonService: DoubloonService) {
  val getUserByEmailServerEndpoint: ServerEndpoint[Any, IO] = getUserByEmail.serverLogic(email => userViewService.getUserByEmail(email))

  val getUsersByTeamIdServerEndpoint: ServerEndpoint[Any, IO] = getUsersByTeamId.serverLogic(teamId => userViewService.getUsersByTeamId(teamId))

  val createUserServerEndpoint: ServerEndpoint[Any, IO] = createUser.serverLogic(user => userService.createUser(user))

  val getCurrentSpentDoubloonsServerEndpoints: ServerEndpoint[Any, IO] = getCurrentSpentDoubloons
    .serverLogic(email => doubloonService.getCurrentSpentDoubloonsByEmail(email))

  val createDoubloonServerEndpoint: ServerEndpoint[Any, IO] = createDoubloon.serverLogic(doubloon => doubloonService.createDoubloon(doubloon))

  val updateDoubloonServerEndpoint: ServerEndpoint[Any, IO] = updateDoubloon.serverLogic(doubloon => doubloonService.updateDoubloon(doubloon))
  
  val deleteDoubloonServerEndpoint: ServerEndpoint[Any, IO] = deleteDoubloon.serverLogic(doubloon => doubloonService.deleteDoubloon(doubloon))

  val apiEndpoints: List[ServerEndpoint[Any, IO]] = List(getUserByEmailServerEndpoint, getUsersByTeamIdServerEndpoint,
    createUserServerEndpoint, getCurrentSpentDoubloonsServerEndpoints, createDoubloonServerEndpoint,
    updateDoubloonServerEndpoint, deleteDoubloonServerEndpoint)

  val docEndpoints: List[ServerEndpoint[Any, IO]] = SwaggerInterpreter()
    .fromServerEndpoints[IO](apiEndpoints, "doubloons", "1.0.0")

  val all: List[ServerEndpoint[Any, IO]] = apiEndpoints ++ docEndpoints

  val routes: HttpRoutes[IO] = Http4sServerInterpreter[IO]().toRoutes(all)
}

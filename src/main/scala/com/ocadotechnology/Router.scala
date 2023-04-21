package com.ocadotechnology

import cats.effect.IO
import org.http4s.HttpRoutes
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import com.ocadotechnology.Endpoints.*
import com.ocadotechnology.comment.CommentService
import com.ocadotechnology.user.{UserService, UserViewService}
import com.ocadotechnology.doubloon.DoubloonService


class Router(userService: UserService, userViewService: UserViewService, doubloonService: DoubloonService, commentService: CommentService) {
  val getUserByEmailServerEndpoint: ServerEndpoint[Any, IO] = getUserByEmail.serverLogic(email => userViewService.getUserByEmail(email))

  val getUsersByTeamIdServerEndpoint: ServerEndpoint[Any, IO] = getUsersByTeamId.serverLogic(teamId => userViewService.getUsersByTeamId(teamId))

  val createUserServerEndpoint: ServerEndpoint[Any, IO] = createUser.serverLogic(user => userService.createUser(user))

  val getCurrentSpentDoubloonsServerEndpoint: ServerEndpoint[Any, IO] = getCurrentSpentDoubloons
    .serverLogic(email => doubloonService.getCurrentSpentDoubloonsByEmail(email))

  val createDoubloonServerEndpoint: ServerEndpoint[Any, IO] = createDoubloon.serverLogic(doubloon => doubloonService.createDoubloon(doubloon))

  val updateDoubloonServerEndpoint: ServerEndpoint[Any, IO] = updateDoubloon.serverLogic(doubloon => doubloonService.updateDoubloon(doubloon))

  val deleteDoubloonServerEndpoint: ServerEndpoint[Any, IO] = deleteDoubloon.serverLogic(doubloon => doubloonService.deleteDoubloon(doubloon))

  val getCurrentCommentsByEmailServerEndpoint: ServerEndpoint[Any, IO] = getCurrentCommentsByEmail
    .serverLogic(email => commentService.getCurrentCommentsByEmail(email))

  val createCommentServerEndpoint: ServerEndpoint[Any, IO] = createComment.serverLogic(comment => commentService.createComment(comment))

  val updateCommentServerEndpoint: ServerEndpoint[Any, IO] = updateComment.serverLogic(comment => commentService.updateComment(comment))

  val deleteCommentServerEndpoint: ServerEndpoint[Any, IO] = deleteComment.serverLogic(comment => commentService.deleteComment(comment))

  val apiEndpoints: List[ServerEndpoint[Any, IO]] = List(getUserByEmailServerEndpoint, getUsersByTeamIdServerEndpoint,
    createUserServerEndpoint, getCurrentSpentDoubloonsServerEndpoint, createDoubloonServerEndpoint,
    updateDoubloonServerEndpoint, deleteDoubloonServerEndpoint, getCurrentCommentsByEmailServerEndpoint,
    createCommentServerEndpoint, updateCommentServerEndpoint, deleteCommentServerEndpoint)

  val docEndpoints: List[ServerEndpoint[Any, IO]] = SwaggerInterpreter()
    .fromServerEndpoints[IO](apiEndpoints, "doubloons", "1.0.0")

  val all: List[ServerEndpoint[Any, IO]] = apiEndpoints ++ docEndpoints

  val routes: HttpRoutes[IO] = Http4sServerInterpreter[IO]().toRoutes(all)
}

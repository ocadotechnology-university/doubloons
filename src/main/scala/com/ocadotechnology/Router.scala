package com.ocadotechnology

import cats.effect.IO
import org.http4s.HttpRoutes
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import com.ocadotechnology.user.UserEndpoints.*
import com.ocadotechnology.doubloon.DoubloonEndpoints.*
import com.ocadotechnology.comment.CommentEndpoints.*
import com.ocadotechnology.category.CategoryEndpoints.*
import com.ocadotechnology.team.TeamEndpoints.*
import com.ocadotechnology.category.CategoryService
import com.ocadotechnology.comment.CommentService
import com.ocadotechnology.user.UserService
import com.ocadotechnology.doubloon.DoubloonService
import com.ocadotechnology.team.TeamService


class Router(userService: UserService, doubloonService: DoubloonService, commentService: CommentService, 
             categoryService: CategoryService, teamService: TeamService) {

  val getUserByEmailServerEndpoint: ServerEndpoint[Any, IO] = getUserByEmail.serverLogic(email => userService.getUserByEmail(email))

  val getUsersByTeamIdServerEndpoint: ServerEndpoint[Any, IO] = getUsersByTeamId.serverLogic(teamId => userService.getUsersByTeamId(teamId))

  val createUserServerEndpoint: ServerEndpoint[Any, IO] = createUser.serverLogic(user => userService.createUser(user))

  val getCurrentSpentDoubloonsServerEndpoint: ServerEndpoint[Any, IO] = getCurrentSpentDoubloons
    .serverLogic(email => doubloonService.getCurrentSpentDoubloonsByEmail(email))

  val createDoubloonServerEndpoint: ServerEndpoint[Any, IO] = createDoubloon.serverLogic(doubloon => doubloonService.createDoubloon(doubloon))

  val updateDoubloonServerEndpoint: ServerEndpoint[Any, IO] = updateDoubloon.serverLogic(doubloon => doubloonService.updateDoubloon(doubloon))

  val deleteDoubloonServerEndpoint: ServerEndpoint[Any, IO] = deleteDoubloon.serverLogic(doubloon => doubloonService.deleteDoubloon(doubloon))

  val getAmountToSpendServerEndpoint: ServerEndpoint[Any, IO] = getAmountToSpend.serverLogic(teamId => doubloonService.getAmountToSpend(teamId))

  val getDoubloonsSpentByOthersServerEndpoint: ServerEndpoint[Any, IO] = getDoubloonsSpentByOthers
    .serverLogic(data => doubloonService.getDoubloonsSpentByOthers(data))

  val getDoubloonResultsServerEndpoint: ServerEndpoint[Any, IO] = getDoubloonResults.serverLogic(data => doubloonService.getDoubloonResults(data))

  val getCurrentCommentsByEmailServerEndpoint: ServerEndpoint[Any, IO] = getCurrentCommentsByEmail
    .serverLogic(email => commentService.getCurrentCommentsByEmail(email))

  val createCommentServerEndpoint: ServerEndpoint[Any, IO] = createComment.serverLogic(comment => commentService.createComment(comment))

  val updateCommentServerEndpoint: ServerEndpoint[Any, IO] = updateComment.serverLogic(comment => commentService.updateComment(comment))

  val deleteCommentServerEndpoint: ServerEndpoint[Any, IO] = deleteComment.serverLogic(comment => commentService.deleteComment(comment))

  val getCommentResultsServerEndpoint: ServerEndpoint[Any, IO] = getCommentResults.serverLogic(data => commentService.getCommentResults(data))

  val getCategoriesServerEndpoint: ServerEndpoint[Any, IO] = getCategories.serverLogic(_ => categoryService.getCategories)

  val getTeamInfoServerEndpoint: ServerEndpoint[Any, IO] = getTeamInfo.serverLogic(teamId => teamService.getTeamInfo(teamId))

  val apiEndpoints: List[ServerEndpoint[Any, IO]] = List(
    getUserByEmailServerEndpoint, 
    getUsersByTeamIdServerEndpoint,
    createUserServerEndpoint, 
    getCurrentSpentDoubloonsServerEndpoint, 
    createDoubloonServerEndpoint,
    updateDoubloonServerEndpoint, 
    deleteDoubloonServerEndpoint, 
    getAmountToSpendServerEndpoint,
    getDoubloonsSpentByOthersServerEndpoint, 
    getDoubloonResultsServerEndpoint, 
    getCurrentCommentsByEmailServerEndpoint,
    createCommentServerEndpoint, 
    updateCommentServerEndpoint, 
    deleteCommentServerEndpoint,
    getCommentResultsServerEndpoint,
    getCategoriesServerEndpoint,
    getTeamInfoServerEndpoint)

  val docEndpoints: List[ServerEndpoint[Any, IO]] = SwaggerInterpreter()
    .fromServerEndpoints[IO](apiEndpoints, "doubloons", "1.0.0")

  val all: List[ServerEndpoint[Any, IO]] = apiEndpoints ++ docEndpoints

  val routes: HttpRoutes[IO] = Http4sServerInterpreter[IO]().toRoutes(all)
}

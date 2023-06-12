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
    .serverLogic((email, monthAndYear) => doubloonService.getDoubloonsSpentByOthers(email, monthAndYear))

  val getDoubloonsSummaryServerEndpoint: ServerEndpoint[Any, IO] = getDoubloonsSummary.serverLogic((givenTo, monthAndYear) => doubloonService.getDoubloonsSummary(givenTo, monthAndYear))

  val getAvailableMonthsServerEndpoint: ServerEndpoint[Any, IO] = getAvailableMonths.serverLogic(email => doubloonService.getAvailableMonths(email))

  val getCommentsServerEndpoint: ServerEndpoint[Any, IO] = getComments
    .serverLogic((email, monthAndYear) => commentService.getComments(email, monthAndYear))

  val upsertCommentServerEndpoint: ServerEndpoint[Any, IO] = upsertComment.serverLogic(comment => commentService.upsertComment(comment))

  val deleteCommentServerEndpoint: ServerEndpoint[Any, IO] = deleteComment.serverLogic(comment => commentService.deleteComment(comment))

  val getCommentsSummaryServerEndpoint: ServerEndpoint[Any, IO] = getCommentsSummary.serverLogic((givenTo, monthAndYear) => commentService.getCommentsSummary(givenTo, monthAndYear))

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
    getDoubloonsSummaryServerEndpoint,
    getAvailableMonthsServerEndpoint,
    getCommentsServerEndpoint,
    upsertCommentServerEndpoint,
    deleteCommentServerEndpoint,
    getCommentsSummaryServerEndpoint,
    getCategoriesServerEndpoint,
    getTeamInfoServerEndpoint)

  val docEndpoints: List[ServerEndpoint[Any, IO]] = SwaggerInterpreter()
    .fromServerEndpoints[IO](apiEndpoints, "doubloons", "1.0.0")

  val all: List[ServerEndpoint[Any, IO]] = apiEndpoints ++ docEndpoints

  val routes: HttpRoutes[IO] = Http4sServerInterpreter[IO]().toRoutes(all)
}

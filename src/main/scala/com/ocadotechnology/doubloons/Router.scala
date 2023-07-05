package com.ocadotechnology.doubloons

import cats.effect.IO
import org.http4s.HttpRoutes
import sttp.tapir.*
import sttp.tapir.generic.auto.*
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import com.ocadotechnology.doubloons.user.UserEndpoints.*
import com.ocadotechnology.doubloons.doubloon.DoubloonEndpoints.*
import com.ocadotechnology.doubloons.comment.Comment
import com.ocadotechnology.doubloons.comment.CommentEndpoints.*
import com.ocadotechnology.doubloons.category.CategoryEndpoints.*
import com.ocadotechnology.doubloons.team.TeamEndpoints.*
import com.ocadotechnology.doubloons.category.CategoryService
import com.ocadotechnology.doubloons.comment.CommentService
import com.ocadotechnology.doubloons.user.UserService
import com.ocadotechnology.doubloons.doubloon.DoubloonService
import com.ocadotechnology.doubloons.team.TeamService
import com.ocadotechnology.doubloons.security.Security
import com.ocadotechnology.doubloons.security.AuthEndpoints
import com.ocadotechnology.doubloons.security.UserSession
import cats.implicits.*
import mouse.all.*
import io.github.arainko.ducktape.*
import com.ocadotechnology.doubloons.doubloon.Doubloon

class Router(
    userService: UserService,
    doubloonService: DoubloonService,
    commentService: CommentService,
    categoryService: CategoryService,
    teamService: TeamService,
    security: Security[IO],
    statics: Statics
) {

  val userInfoServerEndpoint: ServerEndpoint[Any, IO] =
    userInfo
      .serverSecurityLogic(security.decodeAndIntrospectToken)
      .serverLogic(session =>
        _ => userService.getUserByEmail(session.email.value)
      )

  val getUserByEmailServerEndpoint: ServerEndpoint[Any, IO] =
    getUserByEmail
      .serverSecurityLogic(security.decodeAndIntrospectToken)
      .serverLogic(_ => email => userService.getUserByEmail(email))

  val joinTeamEndpoint: ServerEndpoint[Any, IO] =
    joinTeam
      .serverSecurityLogic(security.decodeAndIntrospectToken)
      .serverLogic(session =>
        teamId => userService.joinTeam(session.email.value, teamId)
      )

  val getUsersByTeamIdServerEndpoint: ServerEndpoint[Any, IO] =
    getUsersByTeamId
      .serverSecurityLogic(security.decodeAndIntrospectToken)
      .serverLogic { session => _ =>
        {
          for {
            user <- userService
              .getUserByEmail(session.email.value)
              .liftEitherT
            teamId <- user.teamId
              .map(_.value)
              .toRight(BusinessError(s"Failed to find team for ${user.email}"))
              .toEitherT
            users <- userService.getUsersByTeamId(teamId).liftEitherT
            _ <- IO
              .println(s"Found members $users for team $teamId")
              .liftEitherT
          } yield users
        }.value

      }

  val getCurrentSpentDoubloonsServerEndpoint: ServerEndpoint[Any, IO] =
    getCurrentSpentDoubloons
      .serverSecurityLogic(security.decodeAndIntrospectToken)
      .serverLogic(session =>
        _ =>
          doubloonService.getCurrentSpentDoubloonsByEmail(
            session.email.value
          )
      )

  val createDoubloonServerEndpoint: ServerEndpoint[Any, IO] =
    createDoubloon
      .serverSecurityLogic(security.decodeAndIntrospectToken)
      .serverLogic(session =>
        doubloonUpdate =>
          doubloonService.createDoubloon(
            doubloonUpdate
              .into[Doubloon]
              .transform(Field.const(_.givenBy, session.email))
          )
      )

  val updateDoubloonServerEndpoint: ServerEndpoint[Any, IO] =
    updateDoubloon
      .serverSecurityLogic(security.decodeAndIntrospectToken)
      .serverLogic { session => doubloonUpdate =>
        doubloonService.updateDoubloon(
          doubloonUpdate
            .into[Doubloon]
            .transform(Field.const(_.givenBy, session.email))
        )
      }

  val deleteDoubloonServerEndpoint: ServerEndpoint[Any, IO] =
    deleteDoubloon
      .serverSecurityLogic(security.decodeAndIntrospectToken)
      .serverLogic(session =>
        doubloonUpdate =>
          doubloonService.deleteDoubloon(
            doubloonUpdate
              .into[Doubloon]
              .transform(Field.const(_.givenBy, session.email))
          )
      )

  val getAmountToSpendServerEndpoint: ServerEndpoint[Any, IO] =
    getAmountToSpend
      .serverSecurityLogic(security.decodeAndIntrospectToken)
      .serverLogic { session => _ =>
        {
          for {
            user <- userService
              .getUserByEmail(session.email.value)
              .liftEitherT
            teamId <- user.teamId
              .map(_.value)
              .toRight(BusinessError(s"Failed to find team for ${user.email}"))
              .toEitherT
            users <- doubloonService.getAmountToSpend(teamId).liftEitherT
          } yield users
        }.value

      }

  val getDoubloonsSpentByOthersServerEndpoint: ServerEndpoint[Any, IO] =
    getDoubloonsSpentByOthers
      .serverSecurityLogic(security.decodeAndIntrospectToken)
      .serverLogic(session =>
        monthAndYear =>
          doubloonService
            .getDoubloonsSpentByOthers(session.email.value, monthAndYear)
      )

  val getDoubloonsSummaryServerEndpoint: ServerEndpoint[Any, IO] =
    getDoubloonsSummary
      .serverSecurityLogic(security.decodeAndIntrospectToken)
      .serverLogic(_ =>
        (givenTo, monthAndYear) =>
          doubloonService.getDoubloonsSummary(givenTo, monthAndYear)
      )

  val getAvailableMonthsServerEndpoint: ServerEndpoint[Any, IO] =
    getAvailableMonths
      .serverSecurityLogic(security.decodeAndIntrospectToken)
      .serverLogic(session =>
        _ => doubloonService.getAvailableMonths(session.email.value)
      )

  val getCommentsServerEndpoint: ServerEndpoint[Any, IO] = getComments
    .serverSecurityLogic(security.decodeAndIntrospectToken)
    .serverLogic(session =>
      monthAndYear =>
        commentService
          .getComments(session.email.value, monthAndYear)
    )

  val upsertCommentServerEndpoint: ServerEndpoint[Any, IO] =
    upsertComment
      .serverSecurityLogic(security.decodeAndIntrospectToken)
      .serverLogic { session => commentUpdate =>
        commentService.upsertComment(
          commentUpdate
            .into[Comment]
            .transform(Field.const(_.givenBy, session.email))
        )
      }

  val deleteCommentServerEndpoint: ServerEndpoint[Any, IO] =
    deleteComment
      .serverSecurityLogic(security.decodeAndIntrospectToken)
      .serverLogic(_ => comment => commentService.deleteComment(comment))

  val getCommentsSummaryServerEndpoint: ServerEndpoint[Any, IO] =
    getCommentsSummary
      .serverSecurityLogic(security.decodeAndIntrospectToken)
      .serverLogic(session =>
        monthAndYear =>
          commentService.getCommentsSummary(session.email.value, monthAndYear)
      )

  val getCategoriesServerEndpoint: ServerEndpoint[Any, IO] =
    getCategories
      .serverSecurityLogic(security.decodeAndIntrospectToken)
      .serverLogic(_ => _ => categoryService.getCategories)

  val getTeamInfoServerEndpoint: ServerEndpoint[Any, IO] =
    getTeamInfo
      .serverSecurityLogic(security.decodeAndIntrospectToken)
      .serverLogic(session =>
        _ =>
          {
            for {
              user <- userService
                .getUserByEmail(session.email.value)
                .liftEitherT
              teamId <- user.teamId
                .map(_.value)
                .toRight(
                  BusinessError(s"Failed to find team for ${user.email}")
                )
                .toEitherT
              team <- teamService.getTeamInfo(teamId).liftEitherT
            } yield team
          }.value
      )

  val loginEndpoint =
    AuthEndpoints.login.serverLogicSuccess(_ => security.ssoRedirect())

  val authorizeEndpoint =
    AuthEndpoints.authorize.serverLogic {
      security.issueCookieAndRedirect(_)
    }

  val apiEndpoints: List[ServerEndpoint[Any, IO]] = statics.endpoints ++ List(
    userInfoServerEndpoint,
    joinTeamEndpoint,
    getUsersByTeamIdServerEndpoint,
    getUserByEmailServerEndpoint,
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
    getTeamInfoServerEndpoint,
    loginEndpoint,
    authorizeEndpoint
  )

  val docEndpoints: List[ServerEndpoint[Any, IO]] = SwaggerInterpreter()
    .fromServerEndpoints[IO](apiEndpoints, "doubloons", "1.0.0")

  val all: List[ServerEndpoint[Any, IO]] = apiEndpoints ++ docEndpoints

  val routes: HttpRoutes[IO] = Http4sServerInterpreter[IO]().toRoutes(all)
}

package com.ocadotechnology.doubloons

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import com.comcast.ip4s.Host
import com.comcast.ip4s.Port
import com.comcast.ip4s.port
import com.ocadotechnology.doubloons.category.CategoryRepository
import com.ocadotechnology.doubloons.category.CategoryService
import com.ocadotechnology.doubloons.comment.CommentRepository
import com.ocadotechnology.doubloons.comment.CommentService
import com.ocadotechnology.doubloons.doubloon.DoubloonRepository
import com.ocadotechnology.doubloons.doubloon.DoubloonService
import com.ocadotechnology.doubloons.security.Security
import com.ocadotechnology.doubloons.team.TeamRepository
import com.ocadotechnology.doubloons.team.TeamService
import com.ocadotechnology.doubloons.user.UserRepository
import com.ocadotechnology.doubloons.user.UserService
import com.ocadotechnology.doubloons.Router as ApplicationRouter
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router
import sttp.tapir.server.http4s.Http4sServerInterpreter
import com.ocadotechnology.doubloons.security.Cryptography
import com.ocadotechnology.sttp.oauth2.TokenIntrospection
import com.ocadotechnology.sttp.oauth2.Secret
import com.ocadotechnology.sttp.oauth2.Introspection.TokenIntrospectionResponse
import com.ocadotechnology.doubloons.security.OIDC
import sttp.client3.*
import sttp.model.Uri
import sttp.client3.httpclient.fs2.HttpClientFs2Backend
import com.ocadotechnology.sttp.oauth2.AuthorizationCodeProvider
import com.ocadotechnology.sttp.oauth2.AuthorizationCodeProvider.{
  Config => OAuthConfig
}

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {

    for {
      sttpBackend <- HttpClientFs2Backend.resource[IO]()
      port = sys.env
        .get("HTTP_PORT")
        .flatMap(_.toIntOption)
        .flatMap(Port.fromInt)
        .getOrElse(port"8080")

      crypto = Cryptography.instance[IO]

      oidc = OIDC.ioInstance(
        uri"https://login.microsoftonline.com/0cb0fbf6-09ad-4c56-81fe-44c8576c323b",
        uri"http://localhost:8080/auth/authorize",
        "eec81012-d926-45e4-ae61-871f5d5b35f3",
        Secret("SECRET"),
        "openid profile email user.read api://eec81012-d926-45e4-ae61-871f5d5b35f3/users.write.all"
      )(sttpBackend)

      userRepository = UserRepository.instance
      userService = UserService.instance(userRepository)

      security = Security.ioInstance(
        crypto,
        Secret("verysecretencryptionkey"),
        oidc,
        userService
      )

      staticEndpoints = new Statics(security)

      doubloonRepository = DoubloonRepository.instance
      doubloonService = DoubloonService.instance(doubloonRepository)

      commentRepository = CommentRepository.instance
      commentService = CommentService.instance(commentRepository)

      categoryRepository = CategoryRepository.instance
      categoryService = CategoryService.instance(categoryRepository)

      teamRepository = TeamRepository.instance
      teamService = TeamService.instance(teamRepository)

      router = new ApplicationRouter(
        userService,
        doubloonService,
        commentService,
        categoryService,
        teamService,
        security,
        staticEndpoints
      )

      server <- EmberServerBuilder
        .default[IO]
        .withHost(Host.fromString("0.0.0.0").get)
        .withPort(port)
        .withHttpApp(Router("/" -> router.routes).orNotFound)
        .build

    } yield server

  }.use { _ => IO.never }
    .as(ExitCode.Success)
}

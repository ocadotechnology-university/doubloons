package com.ocadotechnology.doubloons

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.kernel.Resource
import com.comcast.ip4s.Host
import com.comcast.ip4s.Port
import com.comcast.ip4s.port
import com.ocado.ospnow.wms.otpconfig.ConfigSource
import com.ocado.ospnow.wms.otpconfig.S3Reader
import com.ocado.ospnow.wms.otpconfig.decoders._
import com.ocado.ospnow.wms.otpconfig.envs.OtpEnvironment
import com.ocado.ospnow.wms.otpconfig.envs.implicits._
import com.ocado.ospnow.wms.otpconfig.s3reader.awssdk.AwsSdkS3Reader
import com.ocado.ospnow.wms.otpconfig.typesafe.implicits._
import com.ocadotechnology.doubloons.category.CategoryRepository
import com.ocadotechnology.doubloons.category.CategoryService
import com.ocadotechnology.doubloons.comment.CommentRepository
import com.ocadotechnology.doubloons.comment.CommentService
import com.ocadotechnology.doubloons.database.DatabaseConfig
import com.ocadotechnology.doubloons.doubloon.DoubloonRepository
import com.ocadotechnology.doubloons.doubloon.DoubloonService
import com.ocadotechnology.doubloons.security.Cryptography
import com.ocadotechnology.doubloons.security.OIDC
import com.ocadotechnology.doubloons.security.Security
import com.ocadotechnology.doubloons.team.TeamRepository
import com.ocadotechnology.doubloons.team.TeamService
import com.ocadotechnology.doubloons.user.UserRepository
import com.ocadotechnology.doubloons.user.UserService
import com.ocadotechnology.doubloons.Router as ApplicationRouter
import com.ocadotechnology.sttp.oauth2.AuthorizationCodeProvider
import com.ocadotechnology.sttp.oauth2.AuthorizationCodeProvider.{
  Config => OAuthConfig
}
import com.ocadotechnology.sttp.oauth2.Introspection.TokenIntrospectionResponse
import com.ocadotechnology.sttp.oauth2.Secret
import com.ocadotechnology.sttp.oauth2.TokenIntrospection
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router
import pureconfig.*
import pureconfig.generic.derivation.default.*
import sttp.client3.*
import sttp.client3.httpclient.fs2.HttpClientFs2Backend
import sttp.model.Uri
import sttp.tapir.server.http4s.Http4sServerInterpreter

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {

    for {
      sttpBackend <- HttpClientFs2Backend.resource[IO]()
      given OtpEnvironment[IO] = OtpEnvironment.instance[IO]
      given S3Reader[IO] <- AwsSdkS3Reader.instanceFromEnv[IO]
      configSource <- Resource.eval(ConfigSource.fromEnvs[IO])

      rdsConfig <- Resource.eval(configSource.getRdsConfig)
      typesafeConfig <- Resource.eval(configSource.getTypesafeConfig)

      dbConfig = DatabaseConfig.instance(rdsConfig)

      appConfig = pureconfig.ConfigSource
        .fromConfig(typesafeConfig)
        .loadOrThrow[AppConfig]

      port = sys.env
        .get("HTTP_PORT")
        .flatMap(_.toIntOption)
        .flatMap(Port.fromInt)
        .getOrElse(port"8080")

      crypto = Cryptography.instance[IO]

      oidc = OIDC.ioInstance(
        uri"${appConfig.oidc.baseUrl}",
        uri"${appConfig.oidc.redirectUri}",
        s"${appConfig.oidc.clientId}",
        Secret(s"${appConfig.oidc.clientSecret}"),
        s"${appConfig.oidc.scope}"
      )(sttpBackend)

      userRepository = UserRepository.instance(dbConfig)
      userService = UserService.instance(userRepository)

      security = Security.ioInstance(
        crypto,
        Secret(appConfig.oidc.clientSecret),
        oidc,
        userService
      )

      staticEndpoints = new Statics(security)

      doubloonRepository = DoubloonRepository.instance(dbConfig)
      doubloonService = DoubloonService.instance(doubloonRepository)

      commentRepository = CommentRepository.instance(dbConfig)
      commentService = CommentService.instance(commentRepository)

      categoryRepository = CategoryRepository.instance(dbConfig)
      categoryService = CategoryService.instance(categoryRepository)

      teamRepository = TeamRepository.instance(dbConfig)
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

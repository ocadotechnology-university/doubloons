import scala.sys.process._

lazy val buildFrontend = taskKey[Unit]("Builds frontend")
buildFrontend := {
  val log = streams.value.log
  val frontDir = baseDirectory.value / "frontend"
  val npmInstall = Process(Seq("npm", "install"), frontDir)
  val npmBuild = Process(Seq("npm", "run", "build"), frontDir)
  val retCode = npmInstall #&& npmBuild ! log

  val frontendBuildDir = baseDirectory.value / "frontend" / "build"
  val resourceDir = resourceManaged.value / "main" / "web"
  for {
    (from, to) <- frontendBuildDir ** "*" pair Path.rebase(
      frontendBuildDir,
      resourceDir
    )
  } yield {
    Sync.copy(from, to)
    to
  }
  if (retCode != 0)
    throw new IllegalStateException("Frontend build failed")
}

Compile / resourceGenerators += Def.task {
  val frontendBuildDir = baseDirectory.value / "frontend" / "build"
  val resourceDir = resourceManaged.value / "main" / "web"
  for {
    (from, to) <- frontendBuildDir ** "*" pair Path.rebase(
      frontendBuildDir,
      resourceDir
    )
  } yield {
    Sync.copy(from, to)
    to
  }

}

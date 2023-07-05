import sbt._
import sbt.Keys._
import sbtassembly.AssemblyKeys._
import sbtassembly.MergeStrategy
import sbtassembly.PathList
import com.ocado.ospnow.wms.sbt.assembly.aop.SbtAssemblyAopPlugin

object AssemblyConfigurationPlugin extends AutoPlugin {

  override val trigger = allRequirements

  override val projectSettings = Seq(
    assembly / assemblyMergeStrategy := SbtAssemblyAopPlugin
      .defaultStrategyWithAop {
        case PathList(ps @ _*) if ps.last.endsWith("module-info.class") =>
          MergeStrategy.discard
        case PathList(ps @ _*) if ps.last endsWith ".properties" =>
          MergeStrategy.concat
        case PathList(ps @ _*) if ps.last endsWith "deriving.conf" =>
          MergeStrategy.concat
        case x =>
          val oldStrategy = (assembly / assemblyMergeStrategy).value
          oldStrategy(x)
      }
  )

}

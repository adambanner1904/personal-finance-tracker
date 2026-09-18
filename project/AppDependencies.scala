import sbt.*
import play.sbt.PlayImport.{guice, jdbc, filters}

object AppDependencies {
  private val web = Seq(
    guice,
    filters, 
    "org.mindrot" % "jbcrypt" % "0.4",
  )

  private lazy val doobieVersion = "1.0.0-RC13"
  
  private val persistence = Seq(
    "org.typelevel" %% "cats-effect" % "3.7.0",
    "org.typelevel" %% "doobie-core" % doobieVersion,
    "org.typelevel" %% "doobie-postgres" % doobieVersion,
    "org.typelevel" %% "doobie-hikari" % doobieVersion,
    "org.postgresql" % "postgresql" % "42.7.13",
  )
  
  private val test = Seq(
    jdbc % Test,
    "org.typelevel" %% "doobie-scalatest" % doobieVersion % Test,
    "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.2" % Test
  )

  val all = web ++ persistence ++ test 
}
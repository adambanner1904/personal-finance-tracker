name := """personal-finance-tracker"""
organization := "com.github.adambanner1904"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
lazy val scalaversioin = "3.3.8"

scalaVersion := scalaversioin
lazy val doobieVersion = "1.0.0-RC13"

libraryDependencies ++= Seq(
  guice,
  "org.postgresql" % "postgresql" % "42.7.13",
  "org.flywaydb" %% "flyway-play" % "9.1.0",
  "org.typelevel" %% "cats-effect" % "3.5.4",
  "org.typelevel" %% "doobie-core" % doobieVersion,
  "org.typelevel" %% "doobie-postgres" % doobieVersion,
  "org.typelevel" %% "doobie-hikari" % doobieVersion,
  jdbc % Test,
  "org.typelevel" %% "doobie-scalatest" % doobieVersion % Test,
  "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.2" % Test
)

scalacOptions ++= Seq(
  "-Wunused:all",
  "-Wconf:src=src_managed/.*:s,src=routes:s"
)

Test / fork := true
Test / javaOptions += "-Dconfig.file=conf/application-test.conf"

inThisBuild(List(
  scalaVersion := scalaversioin,
  semanticdbEnabled := true,
  semanticdbVersion := scalafixSemanticdb.revision
))

ThisBuild / scalafixConfig := Some(file(".scalafix.conf"))


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.adambanner.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.adambanner.binders._"

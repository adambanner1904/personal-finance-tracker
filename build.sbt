name := """personal-finance-tracker"""
organization := "com.github.adambanner1904"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
lazy val scalaversioin = "3.5.2"

scalaVersion := scalaversioin
lazy val doobieVersion = "1.0.0-RC13"

libraryDependencies ++= Seq(
  guice,
  "org.mindrot" % "jbcrypt" % "0.4",
  "org.postgresql" % "postgresql" % "42.7.13",
  "org.typelevel" %% "cats-effect" % "3.7.0",
  "org.typelevel" %% "doobie-core" % doobieVersion,
  "org.typelevel" %% "doobie-postgres" % doobieVersion,
  "org.typelevel" %% "doobie-hikari" % doobieVersion,
  jdbc % Test,
  "org.typelevel" %% "doobie-scalatest" % doobieVersion % Test,
  "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.2" % Test
)

scalacOptions ++= Seq(
  "-Wunused:imports",
  "-Wconf:src=src_managed/.*:s,src=routes:s"
)

scalafixOnCompile := true

Test / fork := true
Test / javaOptions += "-Dconfig.file=conf/application-test.conf"

inThisBuild(List(
  scalaVersion := scalaversioin,
  semanticdbEnabled := true,
  semanticdbVersion := scalafixSemanticdb.revision
))

ThisBuild / scalafixConfig := Some(file(".scalafix.conf"))


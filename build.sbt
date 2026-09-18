lazy val scala3Version = "3.5.2"

ThisBuild / scalaVersion := scala3Version
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision
ThisBuild / scalafixConfig := Some(file(".scalafix.conf"))

ThisBuild / scalacOptions ++= Seq(
  "-Wunused:imports"
)


ThisBuild / coverageExcludedFiles :=
  ".*Routes.*;.*ReverseRoutes.*"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := "personal-finance-tracker",
    organization := "com.github.adambanner1904",
    version := "1.0-SNAPSHOT",
    libraryDependencies ++= AppDependencies.all
  )

Test / fork := true
Test / javaOptions += "-Dconfig.resource=application-test.conf"

TwirlKeys.templateImports += "forms._"

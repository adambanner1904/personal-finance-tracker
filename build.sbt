name := """personal-finance-tracker"""
organization := "com.github.adambanner1904"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "3.3.8"
lazy val doobieVersion = "1.0.0-RC13"

libraryDependencies ++= Seq(
  guice,
  "org.postgresql" % "postgresql" % "42.7.13",
  "org.flywaydb" %% "flyway-play" % "9.1.0",
  "org.typelevel" %% "cats-effect" % "3.5.4",
  "org.typelevel" %% "doobie-core" % doobieVersion,
  "org.typelevel" %% "doobie-postgres" % doobieVersion,
  "org.typelevel" %% "doobie-hikari" % doobieVersion,
  "org.typelevel" %% "doobie-specs2" % doobieVersion % Test,
  "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.2" % Test
)


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.adambanner.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.adambanner.binders._"

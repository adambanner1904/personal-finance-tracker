package config


import play.api.Configuration
import javax.inject.{Singleton, Inject}

@Singleton
class AppConfig @Inject() (config: Configuration) {

  lazy val dbDriver = config.get[String]("db.default.driver")
  lazy val dbUrl = config.get[String]("db.default.url")
  lazy val dbUser = config.get[String]("db.default.user")
  lazy val dbPassword = config.get[String]("db.default.password")
}
  
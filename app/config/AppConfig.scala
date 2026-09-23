package config

import javax.inject.{Inject, Singleton}

import scala.concurrent.duration.*

@Singleton
class AppConfig @Inject() ():
  lazy val sessionTimeToLive: FiniteDuration = 30.minutes

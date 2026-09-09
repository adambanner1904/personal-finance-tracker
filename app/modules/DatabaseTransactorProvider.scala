package modules

import play.api.Configuration
import play.api.inject.ApplicationLifecycle

import javax.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, Future}

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import com.zaxxer.hikari.HikariConfig
import jakarta.inject.Provider
import org.typelevel.doobie.hikari.HikariTransactor

@Singleton
class DatabaseTransactorProvider @Inject() (
  config: Configuration,
  lifecycle: ApplicationLifecycle,
)(using ExecutionContext)
    extends Provider[HikariTransactor[IO]]:

  private val dbConfig = config.get[Configuration]("db.default")
  private val driver   = dbConfig.get[String]("driver")
  private val url      = dbConfig.get[String]("url")
  private val user     = dbConfig.get[String]("username")
  private val password = dbConfig.get[String]("password")
  private val poolSize = dbConfig.get[Int]("poolSize")

  // Create standard Hikari configuration object
  private val hikariConfig = new HikariConfig()
  hikariConfig.setDriverClassName(driver)
  hikariConfig.setJdbcUrl(url)
  hikariConfig.setUsername(user)
  hikariConfig.setPassword(password)
  hikariConfig.setMaximumPoolSize(poolSize)

  // Construct the Doobie HikariTransactor.
  // Using the .unsafeRunSync() here because Guice providers must return synchronously on startup.
  private val (transactor: HikariTransactor[IO], shutdownAction) =
    HikariTransactor.fromHikariConfig[IO](hikariConfig).allocated.unsafeRunSync()

  // Use Play's lifecycle to ensure that the db connection is closed down with the application
  lifecycle.addStopHook: () =>
    Future {
      shutdownAction.unsafeToFuture()
    }

  override def get(): HikariTransactor[IO] = transactor

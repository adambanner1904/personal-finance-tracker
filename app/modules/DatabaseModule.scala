package modules

import javax.inject.Singleton

import cats.effect.IO
import com.google.inject.{AbstractModule, Provides}
import org.typelevel.doobie.hikari.HikariTransactor

class DatabaseModule extends AbstractModule:

  @Provides
  @Singleton
  def provideHikariTransactor(provider: DatabaseTransactorProvider): HikariTransactor[IO] =
    provider.get()

package modules

import cats.effect.IO
import com.google.inject.AbstractModule
import com.google.inject.Provides
import org.typelevel.doobie.hikari.HikariTransactor

import javax.inject.Singleton

class DatabaseModule extends AbstractModule:

  @Provides
  @Singleton
  def provideHikariTransactor(provider: DatabaseTransactorProvider): HikariTransactor[IO] =
    provider.get()

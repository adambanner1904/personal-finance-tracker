package modules

import com.google.inject.{AbstractModule, Provides, Singleton}
import play.api.inject.ApplicationLifecycle
import cats.effect.IO

class DatabaseModule extends AbstractModule {

  // @Provides
  // @Singleton
  // def provideTransactor(lifecycle: ApplicationLifecycle): HikariTransactor[IO] = 
  //   val transactor: HikariTransactor[IO] = HikariTransactor
}
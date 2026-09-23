package models.db

import cats.effect.IO
import org.typelevel.doobie.hikari.HikariTransactor

type Transactor = HikariTransactor[IO]

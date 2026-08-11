package models.db

import org.typelevel.doobie.hikari.HikariTransactor
import cats.effect.IO

type Transactor = HikariTransactor[IO]
package implicits

import models.db.Transactor

import cats.effect.IO
import cats.effect.unsafe.IORuntime
import org.typelevel.doobie.ConnectionIO
import org.typelevel.doobie.implicits.*

object Repository:

  given IORuntime = cats.effect.unsafe.implicits.global

  extension [A](conn: ConnectionIO[A])(using xa: Transactor)
    def execute: A = conn.transact(xa).unsafeRunSync()

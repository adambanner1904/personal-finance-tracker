package persistence

import javax.inject.Inject
import models.db.Transactor
import org.typelevel.doobie.implicits.*
import cats.effect.unsafe.implicits.global

class HealthRepository @Inject() (xa: Transactor):
  def select1: Int = 
    sql"select 1;"
      .query[Int]
      .unique
      .transact(xa)
      .unsafeRunSync()
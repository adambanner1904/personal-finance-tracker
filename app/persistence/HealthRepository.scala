package persistence

import models.db.Transactor

import javax.inject.Inject

import cats.effect.unsafe.implicits.global
import org.typelevel.doobie.implicits.*

class HealthRepository @Inject() (xa: Transactor):
  def select1: Int = 
    sql"select 1;"
      .query[Int]
      .unique
      .transact(xa)
      .unsafeRunSync()
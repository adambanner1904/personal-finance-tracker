package basespecs

import models.db.Transactor

import cats.effect.unsafe.IORuntime
import cats.effect.unsafe.implicits.global
import org.scalatest.BeforeAndAfterEach
import org.typelevel.doobie.implicits.*

class DbSpec extends UnitSpec with BeforeAndAfterEach:

  given IORuntime = global

  lazy val xa: Transactor = inject[Transactor]

  override def beforeEach(): Unit = truncateAll()

  private def truncateAll(): Unit =
    sql"TRUNCATE TABLE users, snapshots, accounts RESTART IDENTITY CASCADE".update.run
      .transact(xa)
      .unsafeRunSync()

  def createTestUser(email: String, passwordHash: String): Long =
    sql"INSERT INTO users (email, password_hash) VALUES ($email, $passwordHash)".update
      .withUniqueGeneratedKeys[Long]("id")
      .transact(xa)
      .unsafeRunSync()

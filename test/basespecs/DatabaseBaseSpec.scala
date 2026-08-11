package basespecs

import cats.effect.unsafe.implicits.global
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import org.typelevel.doobie.implicits.*
import models.db.Transactor
import cats.effect.unsafe.IORuntime

class DatabaseBaseSpec extends PlaySpec with GuiceOneAppPerSuite with BeforeAndAfterEach:

  given IORuntime = global

  lazy val xa: Transactor = app.injector.instanceOf[Transactor]

  override def beforeEach(): Unit = truncateAll()

  private def truncateAll(): Unit =
    sql"TRUNCATE TABLE users, snapshots, accounts RESTART IDENTITY CASCADE"
      .update
      .run
      .transact(xa)
      .unsafeRunSync()
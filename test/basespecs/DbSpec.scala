package basespecs

import implicits.Repository.*
import models.db.Transactor
import models.Session

import testdata.FakeSessions
import org.scalatest.BeforeAndAfterEach
import org.typelevel.doobie.implicits.toSqlInterpolator // sql"..."

class DbSpec extends UnitSpec with BeforeAndAfterEach with FakeSessions:

  lazy val xa: Transactor            = inject[Transactor]
  given Transactor                   = xa

  override def beforeEach(): Unit = truncateAll()

  private def truncateAll(): Unit =
    sql"""
        TRUNCATE TABLE users, snapshots, accounts RESTART IDENTITY CASCADE
    """.update.run.execute

  def insertTestUser(
    email: String = "test@example.com",
    passwordHash: String = "passwordHash",
  ): Long =
    sql"""
        INSERT INTO users (email, password_hash) 
        VALUES ($email, $passwordHash)
        RETURNING id
    """.query[Long].unique.execute

  def createUserSession(): Session =
    val userId = insertTestUser()
    createSession(validSessionId, userId)

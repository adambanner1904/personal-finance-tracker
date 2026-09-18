package persistence

import models.*

import java.util.UUID

import scala.concurrent.duration.DurationInt

import org.typelevel.doobie.ConnectionIO
import org.typelevel.doobie.implicits.*
import org.typelevel.doobie.postgres.implicits.*

@javax.inject.Singleton
class SessionRepository:
  def createSession(userId: Long): ConnectionIO[UUID] =
    sql"insert into user_sessions (user_id, expires_at) values ($userId, ${Time.now + 30.minutes})".update
      .withUniqueGeneratedKeys[UUID]("session_id")

  def getSession(sessionId: UUID): ConnectionIO[Option[Session]] =
    sql"select * from user_sessions where session_id = $sessionId"
      .query[Session]
      .option

  def updateSession(sessionId: UUID)(newExpiryTime: Time): ConnectionIO[Unit] =
    sql"update user_sessions set expires_at = $newExpiryTime where session_id = $sessionId".update.run
      .map(_ => ())

  def deleteSession(sessionId: UUID): ConnectionIO[Unit] =
    sql"delete from user_sessions where session_id = $sessionId".update.run
      .map(_ => ())

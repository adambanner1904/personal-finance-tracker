package persistence

import org.typelevel.doobie.ConnectionIO
import java.util.UUID
import models.*


@javax.inject.Singleton
class SessionRepository:
  def createSession(userId: Long, expiresAt: Time): ConnectionIO[UUID] = ???
  def getSession(sessionId: UUID): ConnectionIO[Session] = ???
  
  def updateSession(sessionId: UUID)(newExpiryTime: Time): ConnectionIO[Session] = ???
  def deleteSession(sessionId: UUID): ConnectionIO[Unit] = ???
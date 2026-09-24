package data

import models.{Session, Time}

import java.util.UUID

import scala.concurrent.duration.*

trait FakeSessions:
  val validSessionId   = UUID.fromString("123e4567-e89b-12d3-a456-426614174000")
  val expiredSessionId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001")
  val validUserId      = 1L
  val expiredUserId    = 2L

  val validSession = Session(
    sessionId = validSessionId,
    userId = validUserId,
    createdAt = Time.now - 10.minutes,
    expiresAt = Time.now + 50.minutes,
  )

  val expiredSession = models.Session(
    sessionId = expiredSessionId,
    userId = expiredUserId,
    createdAt = Time.now - 1.hours,
    expiresAt = Time.now - 30.minutes,
  )

  def createSession(
    sessionId: UUID,
    userId: Long,
    createdAt: Time = Time.now - 10.minutes,
    expiresAt: Time = Time.now + 50.minutes,
  ): Session =
    Session(
      sessionId = sessionId,
      userId = userId,
      createdAt = createdAt,
      expiresAt = expiresAt,
    )

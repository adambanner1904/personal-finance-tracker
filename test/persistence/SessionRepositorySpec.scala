package persistence

import implicits.Repository.*
import models.*

import java.time.Instant
import java.util.UUID

import basespecs.DbSpec

class SessionRepositorySpec extends DbSpec:

  lazy val sessionRepo: SessionRepository = inject[SessionRepository]

  private def makeSession(id: Long): UUID =
    sessionRepo.insert(id).execute

  "SessionRepository" should:
    "create a session" in {
      val userId    = insertTestUser()
      val sessionId = makeSession(userId)
      sessionId shouldBe a[UUID]
    }

    "get a session" in {
      val userId    = insertTestUser()
      val sessionId = makeSession(userId)
      val session   = sessionRepo.findById(sessionId).execute.value
      session.userId shouldBe userId
    }

    "update a session" in {
      val userId    = insertTestUser()
      val sessionId = makeSession(userId)
      val newTime   = Time.unsafeFrom(Instant.parse("1970-01-01T00:02:02Z"))

      sessionRepo.updateExpiryTime(sessionId)(newTime).execute

      val updatedSession: Session = sessionRepo.findById(sessionId).execute.value
      updatedSession.expiresAt shouldBe newTime
    }

    "delete a session" in {
      val userId    = insertTestUser()
      val sessionId = makeSession(userId)
      sessionRepo.deleteById(sessionId).execute
      val deletedSession = sessionRepo.findById(sessionId).execute

      deletedSession shouldBe None
    }

package persistence

import models.*

import java.time.Instant
import java.util.UUID

import basespecs.DbSpec
import org.typelevel.doobie.implicits.*

class SessionRepositorySpec extends DbSpec:

  
  lazy val userRepo: UserRepository = inject[UserRepository]
  lazy val testRepo: SessionRepository = inject[SessionRepository]
  
  private def createUser(): Long = 
    userRepo
      .insertUser(EmailAddress.unsafeFrom("test2@gmail.com"), "123")
      .transact(xa).unsafeRunSync().value
  
  private def makeSession(id: Long): UUID = 
    testRepo.createSession(id).transact(xa).unsafeRunSync()
    
  "SessionRepository" should: 
    "create a session" in: 
      val userId = createUser()
      val sessionId = makeSession(userId)
      sessionId shouldBe a [UUID]

    "get a session" in: 
      val userId = createUser()
      val sessionId = makeSession(userId)
      val session = testRepo.getSession(sessionId).transact(xa).unsafeRunSync().value
      session.userId shouldBe userId

    "update a session" in:
      val userId = createUser()
      val sessionId = makeSession(userId)
      val newTime = Time.unsafeFrom(Instant.parse("1970-01-01T00:02:02Z"))
      testRepo.updateSession(sessionId)(newTime).transact(xa).unsafeRunSync()
      val updatedSession: Session = testRepo.getSession(sessionId).transact(xa).unsafeRunSync().value
      updatedSession.expiresAt shouldBe newTime
    
    "delete a session" in {
      val userId = createUser()
      val sessionId = makeSession(userId)
      testRepo.deleteSession(sessionId).transact(xa).unsafeRunSync()
      val deletedSession = testRepo.getSession(sessionId).transact(xa).unsafeRunSync()

      deletedSession shouldBe None
    }
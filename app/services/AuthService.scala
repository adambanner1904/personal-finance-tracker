package services

import config.AppConfig
import models.*
import models.db.Transactor
import models.errors.*
import persistence.{SessionRepository, UserRepository}

import java.util.UUID
import javax.inject.{Inject, Singleton}

import scala.concurrent.duration.*

import cats.effect.unsafe.implicits.global
import org.mindrot.jbcrypt.BCrypt
import org.typelevel.doobie.implicits.*

@Singleton
class AuthService @Inject() (
  appConfig: AppConfig,
  userRepo: UserRepository,
  sessionRepo: SessionRepository,
  xa: Transactor,
):
  import AuthService.*

  def createUser(email: String, password: String): Either[SignUpError, UUID] =
    for
      validEmail <- EmailAddress.from(email).left.map(SignUpError.InvalidEmail(_))
      userId     <- userRepo
        .insertUser(validEmail, hash(password))
        .transact(xa)
        .unsafeRunSync()
      sessionId = sessionRepo
        .createSession(userId)
        .transact(xa)
        .unsafeRunSync()
    yield sessionId

  // Can be None if not present in table
  def getSession(sessionId: UUID): Option[Session] =
    sessionRepo.getSession(sessionId).transact(xa).unsafeRunSync()

  def deleteSession(sessionId: UUID): Unit =
    sessionRepo.deleteSession(sessionId).transact(xa).unsafeRunSync()

  def loginUser(email: String, password: String): Either[LoginError, UUID] =
    for
      validEmail <- EmailAddress.from(email).left.map(LoginError.InvalidEmail(_))
      user       <- userRepo
        .loadUser(validEmail)
        .transact(xa)
        .unsafeRunSync()
        .toRight(LoginError.InvalidCredentials)
      _ <- Either.cond(
        passwordsMatch(password, user.passwordHash),
        (),
        LoginError.InvalidCredentials,
      )
      sessionId = sessionRepo
        .createSession(user.id)
        .transact(xa)
        .unsafeRunSync()
    yield sessionId

  def keepAlive(session: Session): Unit =
    if session.expiresAt - Time.now <= 5.minutes
    then sessionRepo.updateSession(session.sessionId)(Time.now + appConfig.sessionTimeToLive)

object AuthService:

  def hash(password: String) =
    BCrypt.hashpw(password, BCrypt.gensalt())

  def passwordsMatch(givenPassword: String, userPasswordHash: String) =
    BCrypt.checkpw(givenPassword, userPasswordHash)

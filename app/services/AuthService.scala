package services

import javax.inject.{Inject, Singleton}
import persistence.{UserRepository, SessionRepository}
import models.db.*
import models.*
import org.mindrot.jbcrypt.BCrypt
import org.typelevel.doobie.implicits.*
import cats.effect.unsafe.implicits.global
import java.util.UUID
import config.AppConfig

import scala.concurrent.duration.*
import action.UserRequest


@Singleton
class AuthService @Inject() (
  appConfig: AppConfig, 
  userRepo: UserRepository, 
  sessionRepo: SessionRepository, 
  xa: Transactor
):
  
  def createUser(email: String, password: String): Either[EmailAddress.Error | DbError, User] = 
    val passwordHash = BCrypt.hashpw(password, BCrypt.gensalt())

    for 
      validEmail <- EmailAddress.from(email) 
      user <- userRepo.insertUser(validEmail, passwordHash).transact(xa).unsafeRunSync()
    yield user

  def getSession(maybeSessionId: UUID): Either[DbError, Session] = ??? 

  def deleteSession(sessionId: UUID): Unit = ??? 

  def createSession(userId: Long): Session = ??? 

  def updateSession(userId: Long)(newExpiryTime: Time): Unit = ???

  def keepAlive(using request: UserRequest[?]): Unit = 
    val session = request.userSession
    if session.expiresAt - Time.now <= 5.minutes
      then sessionRepo.updateSession(session.sessionId)(Time.now + appConfig.sessionTimeToLive)
      
    

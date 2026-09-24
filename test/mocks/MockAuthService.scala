package mocks

import models.errors.{LoginError, SignUpError}
import models.{Session, Time}
import services.AuthService

import java.util.UUID

import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar

trait MockAuthService extends MockitoSugar:

  val mockAuthService = mock[AuthService]

  def mockGetSession(sessionId: UUID, userId: Long, expiresAt: Time): Unit =
    val session = Session(sessionId, userId, createdAt = Time.now, expiresAt = expiresAt)
    when(mockAuthService.getSession(sessionId)).thenReturn(Some(session))

  def mockCreateUser(email: String, password: String = "password")(
    toReturn: Either[SignUpError, UUID],
  ): Unit =
    when(mockAuthService.createUser(email, password)).thenReturn(toReturn)

  def mockLoginUser(email: String, password: String = "password")(
    toReturn: Either[LoginError, UUID],
  ): Unit =
    when(mockAuthService.loginUser(email, password)).thenReturn(toReturn)

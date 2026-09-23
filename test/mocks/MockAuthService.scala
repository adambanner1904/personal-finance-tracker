package mocks


import services.AuthService
import models.{Session, Time}
import java.util.UUID
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import models.errors.SignUpError

trait MockAuthService extends MockitoSugar:

  val mockAuthService = mock[AuthService]

  def mockGetSession(sessionId: UUID, userId: Long, expiresAt: Time): Unit =
    val session = Session(sessionId, userId, createdAt = Time.now, expiresAt = expiresAt)
    when(mockAuthService.getSession(sessionId)).thenReturn(Some(session))

  def mockCreateUser(email: String, password: String = "password")(toReturn: Either[SignUpError, UUID]): Unit =
    when(mockAuthService.createUser(email, password)).thenReturn(toReturn)
package basespecs

import play.api.test.Injecting
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.{
  EitherValues,
  OptionValues
}

import play.api.mvc.{BodyParsers, ControllerComponents}
import services.AuthService
import action.AuthenticatedAction
import scala.concurrent.ExecutionContext
import org.scalatestplus.mockito.MockitoSugar

import java.util.UUID
import models.{Session, Time}
import org.mockito.Mockito.when

class UnitSpec 
  extends AnyWordSpec 
  with Matchers 
  with GuiceOneAppPerSuite 
  with OptionValues
  with EitherValues
  with Injecting
  with MockitoSugar:

  
  private val bodyParser = inject[BodyParsers.Default]
  val mockAuthService = mock[AuthService]

  given ExecutionContext = inject[ExecutionContext]

  val controllerComponents = inject[ControllerComponents] 
  val authenticatedAction = new AuthenticatedAction(mockAuthService, bodyParser) // both needed to create mocked controllers

  
  def mockSession(sessionId: UUID, userId: Long, expiresAt: Time): Unit =
    val session = Session(sessionId, userId, createdAt = Time.now, expiresAt = expiresAt)
    when(mockAuthService.getSession(sessionId)).thenReturn(Some(session))
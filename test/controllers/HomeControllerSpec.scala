package controllers

import basespecs.UnitSpec
import models.Time
import play.api.test.FakeRequest
import play.api.test.Helpers._

import java.util.UUID
import scala.concurrent.duration._

import helpers.AuthHelpers.requestWithSession
import org.mockito.Mockito.when
import play.api.mvc.{ControllerComponents}
import action.AuthenticatedAction
import scala.concurrent.ExecutionContext
import mocks.MockAuthService
import play.api.mvc.BodyParsers

class HomeControllerSpec extends UnitSpec with MockAuthService:

  val cc = inject[ControllerComponents]
  val bp = inject[BodyParsers.Default]
  given ExecutionContext = cc.executionContext
  
  val mockAuthenticatedAction = new AuthenticatedAction(mockAuthService, bp)

  val controller           = new HomeController(cc, mockAuthenticatedAction)

  val indexRequest = FakeRequest(GET, "/")
  val index        = controller.index().apply(indexRequest)

  "HomeController GET /index" should:
    "render the index page from a new instance of controller" in:
      status(index) shouldBe OK
      contentType(index) shouldBe Some("text/html")
      contentAsString(index) should include("Welcome to Play")

      
  "HomeController GET /home" should:
    "fail and redirect to log in page if user has no session-id in Cookies" in:
      val homePage = controller.home().apply(requestWithSession(None))
      status(homePage) shouldBe SEE_OTHER
      redirectLocation(homePage) shouldBe Some("/auth/log-in")

    "fail and redirect to log in page if user has a malformed session-id in Cookies" in:
      val homePage = controller.home().apply(requestWithSession(Some("malformed-uuid")))
      status(homePage) shouldBe SEE_OTHER
      redirectLocation(homePage) shouldBe Some("/auth/log-in")

    "fail and redirect to log in page if user has a session-id in Cookies that cannot be found" in:
      val sessionId = UUID.randomUUID()

      when(mockAuthService.getSession(sessionId)).thenReturn(None)

      val homePage = controller.home().apply(requestWithSession(Some(sessionId.toString)))
      status(homePage) shouldBe SEE_OTHER
      redirectLocation(homePage) shouldBe Some("/auth/log-in")

    "fail and redirect to log in page if user has a session-id in Cookies that has expired" in:
      val sessionId = UUID.randomUUID()
      mockGetSession(sessionId, userId = 1L, expiresAt = Time.now - 30.minutes)

      val homePage = controller.home().apply(requestWithSession(Some(sessionId.toString)))
      status(homePage) shouldBe SEE_OTHER
      redirectLocation(homePage) shouldBe Some("/auth/log-in")

    "render the home page when the session-id in Cookies is valid" in:
      val sessionId = UUID.randomUUID()
      mockGetSession(sessionId, userId = 1L, expiresAt = Time.now + 30.minutes)
      
      val homePage = controller.home().apply(requestWithSession(Some(sessionId.toString)))
      status(homePage) shouldBe OK
      contentType(homePage) shouldBe Some("text/html")
      contentAsString(homePage) should include("You are logged in")

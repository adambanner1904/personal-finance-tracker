package controllers

import play.api.test.FakeRequest
import play.api.test.Helpers.*

import action.AuthenticatedAction
import models.Time

import java.util.UUID

import scala.concurrent.duration.*

import basespecs.ControllerSpec
import helpers.AuthHelpers.requestWithSession
import mocks.MockAuthService
import org.mockito.Mockito.when

class HomeControllerSpec extends ControllerSpec with MockAuthService:

  val authenticatedAction = new AuthenticatedAction(mockAuthService, messagesApi, bp)

  val controller = new HomeController(mcc, authenticatedAction)

  val indexRequest = FakeRequest(GET, "/")
  val index        = controller.index().apply(indexRequest)

  "HomeController GET /index" should:
    "render the index page from a new instance of controller" in:
      status(index) shouldBe OK
      contentType(index) shouldBe Some("text/html")
      contentAsString(index) should include("Welcome to Play")

  "HomeController GET /home" should:
    def getHomePage(sessionId: Option[String]) =
      controller.home().apply(requestWithSession(GET, "/home", sessionId))
      
    "fail and redirect to log in page if user has no session-id in Cookies" in:
      val homePage = getHomePage(None)
      status(homePage) shouldBe SEE_OTHER
      redirectLocation(homePage) shouldBe Some("/auth/log-in")

    "fail and redirect to log in page if user has a malformed session-id in Cookies" in:
      val homePage = getHomePage(Some("malformed-id"))
      status(homePage) shouldBe SEE_OTHER
      redirectLocation(homePage) shouldBe Some("/auth/log-in")

    "fail and redirect to log in page if user has a session-id in Cookies that cannot be found" in:
      val sessionId = UUID.randomUUID()

      when(mockAuthService.getSession(sessionId)).thenReturn(None)

      val homePage = getHomePage(Some(sessionId.toString))
      status(homePage) shouldBe SEE_OTHER
      redirectLocation(homePage) shouldBe Some("/auth/log-in")

    "fail and redirect to log in page if user has a session-id in Cookies that has expired" in:
      val sessionId = UUID.randomUUID()
      mockGetSession(sessionId, userId = 1L, expiresAt = Time.now - 30.minutes)

      val homePage = getHomePage(Some(sessionId.toString))
      status(homePage) shouldBe SEE_OTHER
      redirectLocation(homePage) shouldBe Some("/auth/log-in")

    "render the home page when the session-id in Cookies is valid" in:
      val sessionId = UUID.randomUUID()
      mockGetSession(sessionId, userId = 1L, expiresAt = Time.now + 30.minutes)

      val homePage = getHomePage(Some(sessionId.toString))
      status(homePage) shouldBe OK
      contentType(homePage) shouldBe Some("text/html")
      contentAsString(homePage) should include("You are logged in")

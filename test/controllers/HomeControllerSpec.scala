package controllers

import basespecs.UnitSpec
import models.Time
import play.api.test.FakeRequest
import play.api.test.Helpers._

import java.util.UUID
import scala.concurrent.duration._

import helpers.AuthHelpers.requestWithSession
import org.mockito.Mockito.when

/** Add your spec here. You can mock out a whole application including requests, plugins etc.
  *
  * For more information, see
  * https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
  */
class HomeControllerSpec extends UnitSpec:

  val controller           = new HomeController(controllerComponents, authenticatedAction)

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

      when(mockAuthService.getSession(sessionId)).thenReturn(None) // Mockito's default null breaks Option handling, so stub explicitly

      val homePage = controller.home().apply(requestWithSession(Some(sessionId.toString)))
      status(homePage) shouldBe SEE_OTHER
      redirectLocation(homePage) shouldBe Some("/auth/log-in")

    "fail and redirect to log in page if user has a session-id in Cookies that has expired" in:
      val sessionId = UUID.randomUUID()
      mockSession(sessionId, userId = 1L, expiresAt = Time.now - 30.minutes)

      val homePage = controller.home().apply(requestWithSession(Some(sessionId.toString)))
      status(homePage) shouldBe SEE_OTHER
      redirectLocation(homePage) shouldBe Some("/auth/log-in")

    "render the home page when the session-id in Cookies is valid" in:
      val sessionId = UUID.randomUUID()
      mockSession(sessionId, userId = 1L, expiresAt = Time.now + 30.minutes)
      
      val homePage = controller.home().apply(requestWithSession(Some(sessionId.toString)))
      status(homePage) shouldBe OK
      contentType(homePage) shouldBe Some("text/html")
      contentAsString(homePage) should include("You are logged in")

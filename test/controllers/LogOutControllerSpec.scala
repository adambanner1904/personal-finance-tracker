package controllers

import play.api.test.Helpers.*

import basespecs.ControllerSpec
import testdata.FakeSessions
import helpers.AuthHelpers.requestWithSession
import mocks.{FakeAuthenticatedAction, MockAuthService}

class LogOutControllerSpec extends ControllerSpec with MockAuthService with FakeSessions:

  val fakeAuthenticatedAction = new FakeAuthenticatedAction(Some(validSession))

  val controller  = new LogOutController(mcc, fakeAuthenticatedAction, mockAuthService)
  val postRequest =
    requestWithSession("POST", routes.LogOutController.post().url, Some(validSessionId.toString))

  "LogOutController POST /auth/log-out" should:
    "delete the session and redirect to home page" in:

      val result = controller.post().apply(postRequest)
      status(result) shouldBe SEE_OTHER
      redirectLocation(result) shouldBe Some(routes.HomeController.index().url)
      val discardedCookie = cookies(result).get("session-id")
      discardedCookie.map(_.value) shouldBe Some("")
      discardedCookie.flatMap(_.maxAge) shouldBe Some(0)

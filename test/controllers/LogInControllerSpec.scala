package controllers

import play.api.test.*
import play.api.test.Helpers.*
import play.api.test.CSRFTokenHelper.*
import play.api.mvc.MessagesControllerComponents

import basespecs.UnitSpec
import mocks.MockAuthService

import models.errors.{LoginError, ParseEmailError}

class LogInControllerSpec extends UnitSpec with MockAuthService:

  val mcc = inject[MessagesControllerComponents]
  val controller = new LogInController(mockAuthService, mcc)
  val getRequest = FakeRequest(routes.LogInController.get()).withCSRFToken
  
  def postRequest(email: String, pw: String = "password") = 
    FakeRequest(routes.LogInController.submit())
      .withFormUrlEncodedBody("Email" -> email, "Password" -> pw)
      .withCSRFToken
  
  "LogInController" should:
    "return 200 OK for GET /login" in {
      val result = controller.get()(getRequest)
      status(result) shouldBe OK
    }

    "fail and refresh with flash if email is empty" in {
      val result = controller.submit()(postRequest(""))
      status(result) shouldBe 400
      contentType(result) shouldBe Some("text/html")
      contentAsString(result) should include("This field is required")
    }

    "fail and refresh with flash if email is not valid" in {
      val invalidEmail = "invalid-email"
      mockLoginUser(invalidEmail, "password")(Left(LoginError.InvalidEmail(ParseEmailError.InvalidEmailFormat)))
      
      val result = controller.submit()(postRequest(invalidEmail))
      status(result) shouldBe SEE_OTHER
      redirectLocation(result) shouldBe Some(routes.LogInController.get().url)
      flash(result).get("error") shouldBe Some("Email format is invalid")
    }

    "fail and refresh with flash if credentials are invalid" in {
      val testEmail = "test@gmail.com"
      mockLoginUser(testEmail, "wrongpassword")(Left(LoginError.InvalidCredentials))

      val result = controller.submit()(postRequest(testEmail, "wrongpassword"))
      status(result) shouldBe SEE_OTHER
      redirectLocation(result) shouldBe Some(routes.LogInController.get().url)
      flash(result).get("error") shouldBe Some("Username or password not found")
    }

    "redirect to home page if login is successful" in {
      val validEmail = "test@gmail.com"
      val sessionId = java.util.UUID.randomUUID()
      mockLoginUser(validEmail, "password")(Right(sessionId))

      val result = controller.submit()(postRequest(validEmail, "password"))
      status(result) shouldBe SEE_OTHER
      redirectLocation(result) shouldBe Some(routes.HomeController.home().url)
      cookies(result).get("session-id").map(_.value) shouldBe Some(sessionId.toString)
    }
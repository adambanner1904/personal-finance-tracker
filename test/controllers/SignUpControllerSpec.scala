package controllers

import play.api.test.CSRFTokenHelper.*
import play.api.test.FakeRequest
import play.api.test.Helpers.*

import models.errors.{ParseEmailError, SignUpError}

import basespecs.ControllerSpec
import mocks.MockAuthService

class SignUpControllerSpec extends ControllerSpec with MockAuthService:

  "SignUpController" should:
    val controller = new SignUpController(mockAuthService, mcc)
    val getRequest = FakeRequest(GET, "/auth/sign-up").withCSRFToken
    def postRequest(email: String, pw: String = "password", cpw: String = "password") =
      FakeRequest(POST, "/auth/sign-up")
        .withFormUrlEncodedBody("Email" -> email, "Password" -> pw, "Confirm Password" -> cpw)
        .withCSRFToken

    "render the sign up page" in {
      val result = controller.get().apply(getRequest)
      status(result) shouldBe 200
      contentType(result) shouldBe Some("text/html")
    }

    "fail and refresh with flash if email is empty" in {
      val result = controller.submit().apply(postRequest(""))
      status(result) shouldBe 400
      contentType(result) shouldBe Some("text/html")
      contentAsString(result) should include("This field is required")
    }

    "fail and refresh with flash if email is not valid" in {
      val invalidEmail = "invalid-email"
      mockCreateUser(invalidEmail)(
        Left(SignUpError.InvalidEmail(ParseEmailError.InvalidEmailFormat)),
      )
      val result = controller.submit().apply(postRequest(invalidEmail))
      status(result) shouldBe SEE_OTHER
      redirectLocation(result) shouldBe Some(routes.SignUpController.get().url)
      flash(result).get("error") shouldBe Some("Email format is invalid")
    }

    "fail and redirect to log in page if email is already used" in {
      val testEmail = "test@gmail.com"
      mockCreateUser(testEmail)(Left(SignUpError.EmailAlreadyUsed))

      val result = controller.submit().apply(postRequest(testEmail))
      status(result) shouldBe 303
      redirectLocation(result) shouldBe Some(routes.LogInController.get().url)
      flash(result).get("error") shouldBe Some(
        "That email already has an account, please log in instead",
      )
    }

    "reredirect to home page if sign up is successful" in {
      val validEmail = "test@gmail.com"
      val sessionId  = java.util.UUID.randomUUID()
      mockCreateUser(validEmail)(Right(sessionId))

      val result = controller.submit().apply(postRequest(validEmail))
      status(result) shouldBe 303
      redirectLocation(result) shouldBe Some(routes.HomeController.home().url)
      flash(result).get("success") shouldBe Some("User account has been created.")
      cookies(result).get("session-id").map(_.value) shouldBe Some(sessionId.toString)
    }

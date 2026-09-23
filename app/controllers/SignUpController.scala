package controllers

import play.api.*
import play.api.mvc.*

import forms.*
import models.errors.*
import services.AuthService

import javax.inject.*

class SignUpController @Inject() (authService: AuthService, val mcc: MessagesControllerComponents)
    extends MessagesAbstractController(mcc)
    with Logging:

  val thisPage = routes.SignUpController.get()

  def get() = Action { implicit request: MessagesRequest[?] =>
    Ok(views.html.auth.signUp(userSignUpForm))
  }

  def submit() = Action { implicit request: MessagesRequest[?] =>
    userSignUpForm
      .bindFromRequest()
      .fold(
        formWithErrors =>
          logger.warn(s"Sign up form errored with ${formWithErrors.errors}")
          BadRequest(views.html.auth.signUp(formWithErrors))
        ,
        signUpData =>
          authService.createUser(signUpData.email, signUpData.password) match
            case Left(SignUpError.InvalidEmail(err)) =>
              Redirect(thisPage)
                .flashing("error" -> err.message)
            case Left(SignUpError.EmailAlreadyUsed) =>
              Redirect(routes.LogInController.get())
                .flashing("error" -> "That email already has an account, please log in instead")
            case Right(sessionId) =>
              Redirect(routes.HomeController.home())
                .withCookies(Cookie("session-id", sessionId.toString))
                .flashing("success" -> s"User account has been created."),
      )
  }

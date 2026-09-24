package controllers

import play.api.Logging
import play.api.mvc.*

import forms.auth.*
import models.errors.LoginError.*
import services.AuthService

import javax.inject.{Inject, Singleton}

@Singleton
class LogInController @Inject() (authService: AuthService, val mcc: MessagesControllerComponents)
    extends MessagesAbstractController(mcc)
    with Logging:
  def get() = Action { implicit request: MessagesRequest[?] =>
    Ok(views.html.auth.logIn(userLogInForm))
  }

  def submit() = Action { implicit request: MessagesRequest[?] =>
    def thisPage = routes.LogInController.get()
    userLogInForm
      .bindFromRequest()
      .fold(
        formWithErrors =>
          logger.warn(s"Log in form errored with ${formWithErrors.errors}")
          BadRequest(views.html.auth.logIn(formWithErrors))
        ,
        logInData =>
          authService.loginUser(logInData.email, logInData.password) match
            case Left(InvalidEmail(err)) =>
              Redirect(thisPage)
                .flashing("error" -> err.message)
            case Left(InvalidCredentials) =>
              Redirect(thisPage)
                .flashing("error" -> "Username or password not found")
            case Right(sessionId) =>
              Redirect(routes.HomeController.home())
                .withCookies(Cookie("session-id", sessionId.toString)),
      )

  }

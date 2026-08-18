package controllers


import play.api.*
import play.api.mvc.*

import javax.inject.*
import forms.*
import services.AuthService
import models.EmailAddress.Error.*
import models.db.DbError.*


class SignUpController @Inject() (authService: AuthService, val mcc: MessagesControllerComponents)
    extends MessagesAbstractController(mcc) with Logging:

  val thisPage = routes.SignUpController.get()

  def get() = Action { implicit request: MessagesRequest[?] => 
    Ok(views.html.auth.signUp(userForm))
  }

  def submit() = Action { implicit request: MessagesRequest[?] => 
    userForm
      .bindFromRequest()
      .fold(
        formWithErrors => 
          logger.warn(s"Sign up form errored with ${formWithErrors.errors}")
          BadRequest(views.html.auth.signUp(formWithErrors)),
        userData => 
          logger.info("hit here")
          authService.createUser(userData.email, userData.password) match 
            case Left(Empty) => Redirect(thisPage).flashing("error" -> "Email field cannot be left blank") // This will never be hit
            case Left(TooLong) => Redirect(thisPage).flashing("error" -> "Email field must be less than 320 characters") // This is covered by html attribute 
            case Left(InvalidFormat) => Redirect(thisPage).flashing("error" -> "Not a valid email!") 
            case Left(UniqueViolation) => Redirect(thisPage).flashing("error" -> "That email already has an account, please log in instead")
            case Right(user) => Redirect(routes.HomeController.index()).flashing("success" -> s"User account has been created for email: ${user.email}")
      )
  }
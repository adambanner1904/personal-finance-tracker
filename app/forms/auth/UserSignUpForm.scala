package forms.auth

import play.api.data.*
import play.api.data.Forms.*

case class UserSignUpData(email: String, password: String, confirmPassword: String)

object UserSignUpData:
  def unapply(u: UserSignUpData): Option[(String, String, String)] = Some(
    (u.email, u.password, u.confirmPassword),
  )

val userSignUpForm = Form(
  mapping(
    "Email"            -> nonEmptyText,
    "Password"         -> nonEmptyText,
    "Confirm Password" -> nonEmptyText,
  )(UserSignUpData.apply)(UserSignUpData.unapply)
    .verifying("Passwords must match", user => user.password == user.confirmPassword),
)

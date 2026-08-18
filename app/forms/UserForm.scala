package forms

import play.api.data.*
import play.api.data.Forms.*
// import play.api.data.validation.Constraints.*

case class UserData(email: String, password: String, confirmPassword: String)

object UserData {
  def unapply(u: UserData): Option[(String, String, String)] = Some((u.email, u.password, u.confirmPassword))
}

val userForm = Form(
  mapping(
    "Email" -> nonEmptyText,
    "Password" -> nonEmptyText,
    "Confirm Password" -> nonEmptyText
  )(UserData.apply)(UserData.unapply).verifying("Passwords must match", user => user.password == user.confirmPassword)
)
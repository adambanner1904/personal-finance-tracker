package forms

import play.api.data.*
import play.api.data.Forms.*

case class UserLogInData(email: String, password: String)

object UserLogInData {
  def unapply(u: UserLogInData): Option[(String, String)] = Some((u.email, u.password))
}

val userLogInForm = Form(
  mapping(
    "Email" -> nonEmptyText,
    "Password" -> nonEmptyText
  )(UserLogInData.apply)(UserLogInData.unapply)
)
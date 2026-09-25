package data


import play.api.mvc.*
import play.api.test.FakeRequest
import play.api.test.CSRFTokenHelper.*

import action.*
import models.Session
import play.api.i18n.MessagesApi

trait FakeRequests:

  def fakeUserRequest(userSession: Session, path: String = "/", form: Option[Map[String, String]] = None)(using messagesApi: MessagesApi): UserRequest[AnyContent] =
    val request = form match
      case Some(data) => FakeRequest("POST", path).withFormUrlEncodedBody(data.toSeq*)
      case None       => FakeRequest("GET", path)
    UserRequest(userSession, MessagesRequest(request.withCSRFToken, messagesApi))

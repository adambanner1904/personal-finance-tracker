package data


import play.api.mvc.*
import play.api.test.FakeRequest

import action.*
import models.Session
import play.api.i18n.MessagesApi

trait FakeRequests:

  def fakeUserRequest(userSession: Session)(using messagesApi: MessagesApi): UserRequest[?] =
    UserRequest(userSession, MessagesRequest(FakeRequest("GET", "/"), messagesApi))

package action

import play.api.mvc.*
import play.api.i18n.Messages

import models.Session

case class UserRequest[A](userSession: Session, request: MessagesRequest[A])
    extends WrappedRequest[A](request)
    with MessagesRequestHeader:
  def messages: Messages = request.messages

given sessionFromUserRequest(using request: UserRequest[?]): Session = request.userSession

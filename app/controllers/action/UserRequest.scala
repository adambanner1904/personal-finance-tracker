package action

import play.api.mvc.*
import play.api.i18n.Messages

import models.Session

case class UserRequest[A](userSession: Session, request: MessagesRequest[A])
    extends WrappedRequest[A](request)
    with MessagesRequestHeader:
  def messages: Messages = request.messages

// Lets any code needing only a `Session` (e.g. repositories/services) resolve it automatically
// wherever an (implicit) UserRequest is in scope, without depending on the web-request type itself.
// Defined at package level (not nested in an object) so `import action.*` picks it up.
given sessionFromUserRequest(using request: UserRequest[?]): Session = request.userSession

package action

import play.api.mvc.{Request, WrappedRequest}

import models.Session

case class UserRequest[A](userSession: Session, request: Request[A])
    extends WrappedRequest[A](request)

package mocks

import play.api.mvc.*
import play.api.i18n.MessagesApi

import action.{UserAction, UserRequest}
import models.Session

import scala.concurrent.{ExecutionContext, Future}

class FakeAuthenticatedAction(session: Option[Session])(using
  ec: ExecutionContext,
  messagesApi: MessagesApi,
) extends UserAction:

  override val parser: BodyParser[AnyContent]               = play.api.test.Helpers.stubBodyParser()
  override protected def executionContext: ExecutionContext = ec

  override protected def refine[A](request: Request[A]): Future[Either[Result, UserRequest[A]]] =
    Future.successful {
      session match
        case Some(s) => Right(UserRequest(s, MessagesRequest(request, messagesApi)))
        case None    => Left(Results.SeeOther(controllers.routes.LogInController.get().url))
    }

package helpers

import action.{UserAction, UserRequest}
import models.Session
import play.api.i18n.MessagesApi
import play.api.mvc.*

import scala.concurrent.{ExecutionContext, Future}

class FakeUserAction(
  session: Session,
  messagesApi: MessagesApi,
  val parser: BodyParsers.Default
)(using ec: ExecutionContext) extends UserAction:

  protected def executionContext: ExecutionContext = ec

  override protected def refine[A](
    request: Request[A]
  ): Future[Either[Result, UserRequest[A]]] =
    Future.successful {
      Right(UserRequest(session, MessagesRequest(request, messagesApi)))
    }

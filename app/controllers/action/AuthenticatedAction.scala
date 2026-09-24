package action

import play.api.mvc.*
import play.api.mvc.Results.*
import play.api.i18n.MessagesApi

import models.Time
import services.AuthService
import controllers.routes.LogInController

import java.util.UUID
import javax.inject.Inject

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

trait UserAction
    extends ActionBuilder[UserRequest, AnyContent]
    with ActionRefiner[Request, UserRequest]

class AuthenticatedAction @Inject() (
  authService: AuthService,
  messagesApi: MessagesApi,
  val parser: BodyParsers.Default,
)(using
  ExecutionContext,
) extends UserAction:

  protected def executionContext: ExecutionContext = summon[ExecutionContext]
  protected def refine[A](request: Request[A]): Future[Either[Result, UserRequest[A]]] =
    Future.successful {

      val messagesRequest = MessagesRequest(request, messagesApi)
      val redirectToLogin = SeeOther(LogInController.get().url)

      for
        cookie <- request.cookies
          .get("session-id")
          .toRight(redirectToLogin) // fails if no cookie
        sessionId <- Try(UUID.fromString(cookie.value)).toOption
          .toRight(redirectToLogin) // fails if cookie is malformed
        session <- authService
          .getSession(sessionId)
          .toRight(redirectToLogin) // can fail if session not found
        _ <- Either.cond(
          Time.now < session.expiresAt,   // fails if session has expired
          authService.keepAlive(session), // if session has not expired then keep alive
          redirectToLogin,
        )
      yield UserRequest(session, messagesRequest)

    }

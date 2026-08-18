package action

import scala.concurrent.ExecutionContext
import play.api.mvc.*
import play.api.mvc.Results.* 
import scala.concurrent.Future
import javax.inject.Inject
import services.AuthService
import java.util.UUID
import java.time.{Instant, Duration}
import models.Time
import scala.concurrent.duration.DurationInt

class AuthenticatedAction @Inject() (authService: AuthService)(using ExecutionContext) 
  extends ActionRefiner[Request, UserRequest]: 
    def executionContext: ExecutionContext = summon[ExecutionContext]
    def refine[A](request: Request[A]): Future[Either[Result, UserRequest[A]]] = Future.successful { 
      for 
        sessionId <- request.cookies.get("sessionId")
          .map(cookie => UUID.fromString(cookie.value))
          .toRight(Unauthorized)
        session <- authService.getSession(sessionId).left.map(_ => Unauthorized) // may need to be more specific here
      yield 
        given userRequest: UserRequest[A] = UserRequest(session, request)
        if session.expiresAt - Time.now <= 5.minutes
          then authService.keepAlive

        userRequest
      
    }

object AuthenticatedAction:
  extension (time1: Instant)
    def -(time2: Instant) = Math.abs(Duration.between(time1, time2).toMinutes())
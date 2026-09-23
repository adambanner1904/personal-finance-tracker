package controllers

import play.api.mvc.*

import action.{UserRequest, UserAction}
import services.AuthService

import javax.inject.{Inject, Singleton}

@Singleton
class LogOutController @Inject() (
  val mcc: MessagesControllerComponents,
  authenticatedAction: UserAction,
  authService: AuthService,
) extends MessagesAbstractController(mcc):

  def post() = authenticatedAction { implicit userRequest: UserRequest[?] =>
    val sessionId = userRequest.userSession.sessionId
    authService.deleteSession(sessionId)
    Redirect(routes.HomeController.index()).discardingCookies(DiscardingCookie("session-id"))
  }

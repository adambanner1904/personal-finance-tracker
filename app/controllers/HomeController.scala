package controllers

import play.api.*
import play.api.mvc.*

import action.AuthenticatedAction

import javax.inject.*

@Singleton
class HomeController @Inject() (
  val controllerComponents: ControllerComponents,
  val authenticatedAction: AuthenticatedAction,
) extends BaseController:
  def index() = Action { implicit request: Request[?] =>
    Ok(views.html.index())
  }

  def home() = authenticatedAction { implicit request: Request[?] =>
    Ok(views.html.home())
  }

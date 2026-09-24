package controllers

import play.api.*
import play.api.mvc.*

import action.*

import javax.inject.{Inject, Singleton}

@Singleton
class ListInstitutionsController @Inject() (
  authenticatedAction: UserAction, 
  val controllerComponents: ControllerComponents
) extends BaseController:
    
  def get() = authenticatedAction { implicit request: UserRequest[?] =>
    val institutions = List()
    Ok(views.html.institutions.list(institutions))
  }
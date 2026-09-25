package controllers

import play.api.*
import play.api.mvc.*

import action.*

import javax.inject.{Inject, Singleton}
import services.InstitutionService


@Singleton
class ListInstitutionsController @Inject() (
  authenticatedAction: UserAction, 
  institutionService: InstitutionService,
  val mcc: MessagesControllerComponents
) extends MessagesAbstractController(mcc):
    
  def get() = authenticatedAction { implicit request: UserRequest[?] =>
    val institutions = institutionService.listInstitutions(request.userId)
    Ok(views.html.institutions.list(institutions))
  }
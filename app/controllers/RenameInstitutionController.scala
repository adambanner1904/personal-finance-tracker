package controllers

import play.api.*
import play.api.mvc.*

import javax.inject.{Inject, Singleton}

import action.*

import forms.institutions.RenameInstitutionForm.renameInstitutionForm
import services.InstitutionService

@Singleton
class RenameInstitutionController @Inject() (
  authenticatedAction: UserAction,
  institutionService: InstitutionService,
  val mcc: MessagesControllerComponents
) extends MessagesAbstractController(mcc):

  def get(institutionId: Long) = authenticatedAction { implicit request: UserRequest[?] =>
    val institution = institutionService.findById(institutionId, request.userId).getOrElse(throw new NoSuchElementException(s"Institution with ID $institutionId not found for user ${request.userId}"))
    Ok(views.html.institutions.rename(institution, renameInstitutionForm))
  }

  // def post(institutionId: Long) = ???
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

  def post(institutionId: Long) = authenticatedAction { implicit request: UserRequest[?] =>
    renameInstitutionForm
      .bindFromRequest()
      .fold(
        formWithErrors =>
          BadRequest(views.html.institutions.rename(institutionService.findById(institutionId, request.userId).getOrElse(throw new NoSuchElementException(s"Institution with ID $institutionId not found for user ${request.userId}")), formWithErrors)),
        institutionData =>
          institutionService.findById(institutionId, request.userId) match
            case Some(institution) =>
              institutionService.renameInstitution(institutionId, request.userId)(institutionData.name)
              Redirect(routes.ListInstitutionsController.get())
                .flashing("success" -> s"Institution '${institution.name}' renamed to '${institutionData.name}'.")
            case None =>
              Redirect(routes.ListInstitutionsController.get())
                .flashing("error" -> s"Institution with ID $institutionId not found.")
      )
  }
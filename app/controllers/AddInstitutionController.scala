package controllers

import play.api.*
import play.api.mvc.*

import action.*
import action.given

import forms.institutions.AddInstitutionForm.createInstituteForm
import services.InstitutionService

import javax.inject.{Inject, Singleton}

@Singleton
class AddInstitutionController @Inject() (
  authenticatedAction: UserAction,
  institutionService: InstitutionService,
  mcc: MessagesControllerComponents
) extends MessagesAbstractController(mcc):

  def get() = authenticatedAction { implicit request: UserRequest[?] =>
    Ok(views.html.institutions.create(createInstituteForm))
  }

  def post() = authenticatedAction { implicit request: UserRequest[?] =>
    createInstituteForm
      .bindFromRequest() 
      .fold(
        formWithErrors =>
          BadRequest(views.html.institutions.create(formWithErrors)),
        institutionData =>
          institutionService.addInstitution(institutionData.name) match
            case Left(error) =>
              Redirect(routes.AddInstitutionController.get())
                .flashing("error" -> error.message)
            case Right(_) =>
              Redirect(routes.ListInstitutionsController.get())
                .flashing("success" -> s"Institution '${institutionData.name}' created.")
      )
  }
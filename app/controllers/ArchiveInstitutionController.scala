package controllers


import javax.inject.{Inject, Singleton}
import play.api.mvc.*

import action.* 
import services.InstitutionService

@Singleton
class ArchiveInstitutionController @Inject() (
  authenticatedAction: UserAction, 
  institutionService: InstitutionService,
  val mcc: MessagesControllerComponents
) extends MessagesAbstractController(mcc):

  def archive(institutionId: Long) = authenticatedAction { implicit request: UserRequest[?] =>
    val archivedInstitution = institutionService.archiveInstitution(institutionId, request.userId)
    Redirect(routes.ListInstitutionsController.get())
      .flashing("success" -> s"Institution '$archivedInstitution' archived.")
  }
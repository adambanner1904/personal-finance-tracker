package forms.institutions

import play.api.data.*
import play.api.data.Forms.*

case class RenameInstitutionData(name: String)

object RenameInstitutionForm:
  def unapply(i: RenameInstitutionData): Option[String] = Some(i.name)

  val renameInstitutionForm: Form[RenameInstitutionData] = Form(
    mapping(
      "name" -> nonEmptyText
    )(RenameInstitutionData.apply)(RenameInstitutionForm.unapply)
  )
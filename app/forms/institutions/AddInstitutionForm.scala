package forms.institutions

import play.api.data.*
import play.api.data.Forms.*

case class AddInstitutionData(name: String)

object AddInstitutionForm:
  def unapply(i: AddInstitutionData): Option[String] = Some(i.name)

  val createInstituteForm: Form[AddInstitutionData] = Form(
    mapping(
      "name" -> nonEmptyText
    )(AddInstitutionData.apply)(AddInstitutionForm.unapply)
  )
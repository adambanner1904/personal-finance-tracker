package controllers

import play.api.test.Helpers._
import basespecs.ControllerSpec
import mocks.MockInstitutionService
import testdata.*

class ArchiveInstitutionControllerSpec 
  extends ControllerSpec 
  with MockInstitutionService
  with FakeInstitutions:

  val controller = new ArchiveInstitutionController(
    fakeUserAction,
    mockInstitutionService,
    mcc
  )

  "ArchiveInstitutionController" should {
    "archive an institution and redirect to the list" in {
      mockArchiveInstitution(halifax.id, validUserId)(halifax.name)

      val result = controller.archive(halifax.id).apply(userRequest)

      // Verify the result
      status(result) shouldBe SEE_OTHER
      redirectLocation(result).value shouldBe routes.ListInstitutionsController.get().url
      flash(result).get("success").value shouldBe s"Institution '${halifax.name}' archived."
    }
  }
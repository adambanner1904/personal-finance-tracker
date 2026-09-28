package controllers

import basespecs.ControllerSpec
import mocks.MockInstitutionService
import testdata.FakeInstitutions

import play.api.test.Helpers._

class RenameInstitutionControllerSpec 
  extends ControllerSpec 
  with MockInstitutionService
  with FakeInstitutions:

  val controller = new RenameInstitutionController(
    fakeUserAction,
    mockInstitutionService,
    mcc
  )

  "RenameInstitutionController" should {
    "return the rename institution page" in {
      mockFindById(halifax.id, validUserId)(Some(halifax))
      
      val result = controller.get(halifax.id).apply(userRequest)

      // Verify the result
      status(result) shouldBe OK
      contentAsString(result) should include("Rename Institution")
    }
  }
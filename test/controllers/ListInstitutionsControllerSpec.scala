package controllers

import play.api.*

import play.api.test.Helpers.*

import basespecs.ControllerSpec
import mocks.MockInstitutionService
import testdata.FakeInstitutions

class ListInstitutionsControllerSpec 
  extends ControllerSpec 
  with MockInstitutionService
  with FakeInstitutions:

  val controller = new ListInstitutionsController(
    fakeUserAction,
    mockInstitutionService,
    mcc
  )
  
  "ListInstitutionsController" should {
    "return a list of institutions" in {
      mockListInstitutions(validUserId, halifax, starling)

      // Call the controller action
      val result = controller.get().apply(userRequest)

      // Verify the result
      status(result) shouldBe OK
      contentAsString(result) should include(halifax.name)
      contentAsString(result) should include(starling.name)
    }
  }
package controllers

import play.api.*

import play.api.test.Helpers.*

import basespecs.ControllerSpec
import mocks.MockInstitutionService

class ListInstitutionsControllerSpec extends ControllerSpec with MockInstitutionService:

  val controller = new ListInstitutionsController(
    fakeUserAction,
    mockInstitutionService,
    mcc
  )
  
  "ListInstitutionsController" should {
    "return a list of institutions" in {
      mockListInstitutions(validUserId, "Bank A", "Bank B")

      // Call the controller action
      val result = controller.get().apply(userRequest)

      // Verify the result
      status(result) shouldBe OK
      contentAsString(result) should include("Bank A")
      contentAsString(result) should include("Bank B")
    }
  }
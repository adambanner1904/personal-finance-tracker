package controllers

import basespecs.ControllerSpec
import mocks.MockInstitutionService
import testdata.{FakeInstitutions, FakeRequests}

import play.api.test.Helpers._

class RenameInstitutionControllerSpec 
  extends ControllerSpec 
  with MockInstitutionService
  with FakeInstitutions
  with FakeRequests:

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

    def postUrl(name: String) = 
      fakeUserRequest(validSession, routes.RenameInstitutionController.post(halifax.id).url, Some(Map("name" -> name)))

    "handle form submission with valid data" in {
      mockRenameInstitution(halifax.id, validUserId)("Renamed Bank")
      mockFindById(halifax.id, validUserId)(Some(halifax))

      val request = postUrl("Renamed Bank")

      val result = controller.post(halifax.id).apply(request)

      // Verify the result
      status(result) shouldBe SEE_OTHER
      redirectLocation(result) shouldBe Some(routes.ListInstitutionsController.get().url)
      flash(result).get("success") shouldBe Some("Institution 'Halifax' renamed to 'Renamed Bank'.")
    }

    "handle form submission with invalid data" in {
      mockFindById(halifax.id, validUserId)(Some(halifax))

      val request = postUrl("")

      val result = controller.post(halifax.id).apply(request)

      // Verify the result
      status(result) shouldBe BAD_REQUEST
      contentAsString(result) should include("This field is required")
    }
  }
package controllers

import play.api.test.Helpers._
import basespecs.ControllerSpec
import mocks.MockInstitutionService
import models.errors.AddInstitutionError

class AddInstitutionControllerSpec extends ControllerSpec with MockInstitutionService:

  val controller = new AddInstitutionController(
    fakeUserAction,
    mockInstitutionService,
    mcc
  )

  "AddInstitutionController" should {
    "return the add institution form" in {
      // Call the controller action
      val result = controller.get().apply(userRequest)

      // Verify the result
      status(result) shouldBe OK
      contentAsString(result) should include("Add Institution")
    }

    def postUrl(name: String) = 
      fakeUserRequest(validSession, routes.AddInstitutionController.post().url, Some(Map("name" -> name)))

    "handle form submission with valid data" in {
      val request = postUrl("New Bank")

      mockAddInstitution(validUserId, "New Bank")(Right(()))

      // Call the controller action
      val result = controller.post().apply(request)

      // Verify the result
      status(result) shouldBe SEE_OTHER
      redirectLocation(result) shouldBe Some(routes.ListInstitutionsController.get().url)
      flash(result).get("success") shouldBe Some("Institution 'New Bank' created.")
    }

    "handle form submission with invalid data" in {
      // Call the controller action
      val request = postUrl("")
      val result = controller.post().apply(request)

      // Verify the result
      status(result) shouldBe BAD_REQUEST
      contentAsString(result) should include("This field is required")
    }

    "handle form submission when institution already exists" in {
      mockAddInstitution(validUserId, "Existing Bank")(Left(AddInstitutionError.InstitutionAlreadyExists("Existing Bank")))

      // Call the controller action
      val request = postUrl("Existing Bank")
      val result = controller.post().apply(request)

      // Verify the result
      status(result) shouldBe SEE_OTHER
      redirectLocation(result) shouldBe Some(routes.AddInstitutionController.get().url)
      flash(result).get("error") shouldBe Some("An institution with the name 'Existing Bank' already exists.")
    }
  }


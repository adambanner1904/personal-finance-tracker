package services

import basespecs.ServiceSpec
import models.errors.AddInstitutionError

import mocks.MockInstitutionRepository
import models.Session
import data.FakeInstitutions

class InstitutionServiceSpec 
  extends ServiceSpec 
  with MockInstitutionRepository
  with FakeInstitutions:
  
  lazy val institutionService = new InstitutionService(using xa, mockInstitutionRepo)

  given Session = validSession

  "InstitutionService" should:
    "return an error if the institution already exists" in {
      mockFindByName(halifax.name)(Some(halifax))
      
      val result = institutionService.addInstitution(halifax.name)
      result shouldBe Left(AddInstitutionError.InstitutionAlreadyExists(halifax.name))
    }

    "successfully add a new institution if it does not exist" in {
      val name = "New Institution"
      mockFindByName(name)(None)
      mockInsert(name)
      
      val result = institutionService.addInstitution(name)
      result shouldBe Right(())
    }
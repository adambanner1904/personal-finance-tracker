package services

import basespecs.ServiceSpec
import models.errors.AddInstitutionError

import mocks.MockInstitutionRepository
import testdata.FakeInstitutions

class InstitutionServiceSpec 
  extends ServiceSpec 
  with MockInstitutionRepository
  with FakeInstitutions:
  
  lazy val institutionService = new InstitutionService(using xa, mockInstitutionRepo)

  val userId = 1L

  "InstitutionService" should:
    "return an error if the institution already exists" in {
      mockFindByName(halifax.name, userId)(Some(halifax))
      
      val result = institutionService.addInstitution(halifax.name, userId)
      result shouldBe Left(AddInstitutionError.InstitutionAlreadyExists(halifax.name))
    }

    "successfully add a new institution if it does not exist" in {
      val name = "New Institution"
      mockFindByName(name, userId)(None)
      mockInsert(name, userId)
      
      val result = institutionService.addInstitution(name, userId)
      result shouldBe Right(())
    }

    "return a list of names of all institutions" in {
      mockList(userId, halifax, starling)

      val result = institutionService.listInstitutions(userId)
      result shouldBe List(halifax, starling)
    }

    "rename an existing institution" in {
      val newName = "Renamed Institution"
      mockRename(halifax.id, userId)(newName)

      val result = institutionService.renameInstitution(halifax.id, userId)(newName)
      result shouldBe ()
    }
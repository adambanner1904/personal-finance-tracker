package persistence

import action.UserRequest
import implicits.Repository.*
import models.db.Transactor
import models.{Institution, Time}

import basespecs.DbSpec

class InstitutionRepositorySpec extends DbSpec:
  import InstitutionRepositorySpec.*

  given institutionRepo: InstitutionRepository = inject[InstitutionRepository]

  "Selecting an institution by id" should {
    "return the institution if it exists" in {
      val request = createUserRequest()

      val institutionId = insertInstitution("Test Institution")(using request)
      val result        = institutionRepo
        .findById(institutionId)(using request)
        .execute
        .value
      result shouldBe a[Institution]
      result.name shouldBe "Test Institution"
    }

    "return None if the institution does not exist" in {
      val request = createUserRequest()

      val result =
        institutionRepo.findById(999)(using request).execute
      result shouldBe None
    }
  }

  "Finding all institutions for a user" should {
    "return a list of institutions" in {
      val request = createUserRequest()

      val program = for
        _            <- institutionRepo.insert("Institution 1")(using request)
        _            <- institutionRepo.insert("Institution 2")(using request)
        institutions <- institutionRepo.list()(using request)
      yield institutions

      val result = program.execute
      result.length shouldBe 2
      result.map(_.name) should contain allOf ("Institution 1", "Institution 2")
    }
  }

  "Archiving an institution" should {
    "set the archived_at field" in {
      val request = createUserRequest()

      val institutionId = insertInstitution("To Archive")(using request)
      val archivedAt    = Time.now
      val updatedId     = institutionRepo
        .archive(institutionId, archivedAt)(using request)
        .execute

      updatedId shouldBe institutionId

      val result = institutionRepo
        .findById(institutionId)(using request)
        .execute
        .value
      result.archivedAt shouldBe Some(archivedAt)
    }
  }

  "Unarchiving an institution" should {
    "set the archived_at field to null/None" in {
      val request = createUserRequest()

      val institutionId = insertInstitution("To Dearchive")(using request)
      val archivedAt    = Time.now
      val archivedId    = institutionRepo
        .archive(institutionId, archivedAt)(using request)
        .execute

      val archived = institutionRepo
        .findById(institutionId)(using request)
        .execute
        .value
      archived.archivedAt shouldBe Some(archivedAt)

      val unarchivedId = institutionRepo
        .unarchive(institutionId)(using request)
        .execute

      unarchivedId shouldBe institutionId

      val result = institutionRepo
        .findById(unarchivedId)(using request)
        .execute
        .value
      result.archivedAt shouldBe None
    }
  }

object InstitutionRepositorySpec:
  private def insertInstitution(
    name: String,
  )(using request: UserRequest[?], institutionRepo: InstitutionRepository, xa: Transactor): Long =
    institutionRepo.insert(name)(using request).execute

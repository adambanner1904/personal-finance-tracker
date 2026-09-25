package persistence

import implicits.Repository.*
import models.db.Transactor
import models.{Institution, Time}

import basespecs.DbSpec

class InstitutionRepositorySpec extends DbSpec:
  import InstitutionRepositorySpec.*

  given institutionRepo: InstitutionRepository = inject[InstitutionRepository]

  "Selecting an institution by id" should {
    "return the institution if it exists" in {
      val session = createUserSession()

      val institutionId = insertInstitution("Test Institution", session.userId)
      val result        = institutionRepo
        .findById(institutionId, session.userId)
        .execute
        .value
      result shouldBe a[Institution]
      result.name shouldBe "Test Institution"
    }

    "return None if the institution does not exist" in {
      val session = createUserSession()

      val result =
        institutionRepo.findById(999, session.userId).execute
      result shouldBe None
    }
  }

  "Finding all institutions for a user" should {
    "return a list of institutions" in {
      val session = createUserSession()

      val program = for
        _            <- institutionRepo.insert("Institution 1", session.userId)
        _            <- institutionRepo.insert("Institution 2", session.userId)
        institutions <- institutionRepo.list(session.userId)
      yield institutions

      val result = program.execute
      result.length shouldBe 2
      result.map(_.name) should contain allOf ("Institution 1", "Institution 2")
    }

    "return a list sorted by name" in {
      val session = createUserSession()

      val program = for
        _            <- institutionRepo.insert("Z Institution", session.userId)
        _            <- institutionRepo.insert("A Institution", session.userId)
        _            <- institutionRepo.insert("M Institution", session.userId)
        institutions <- institutionRepo.list(session.userId)
      yield institutions

      val result = program.execute
      result.map(_.name) shouldBe List("A Institution", "M Institution", "Z Institution")
    }
  }

  "Archiving an institution" should {
    "set the archived_at field" in {
      val session = createUserSession()

      val institutionId = insertInstitution("To Archive", session.userId)
      val archivedAt     = institutionRepo
        .archive(institutionId, session.userId)
        .execute

      val result = institutionRepo
        .findById(institutionId, session.userId)
        .execute
        .value
      result.archivedAt shouldBe Some(archivedAt)
    }
  }

  "Unarchiving an institution" should {
    "set the archived_at field to null/None" in {
      val session = createUserSession()

      val institutionId = insertInstitution("To Dearchive", session.userId)
      val archivedAt    = institutionRepo
        .archive(institutionId, session.userId)
        .execute

      val archived = institutionRepo
        .findById(institutionId, session.userId)
        .execute
        .value
      archived.archivedAt shouldBe Some(archivedAt)

      val unarchivedId = institutionRepo
        .unarchive(institutionId, session.userId)
        .execute

      unarchivedId shouldBe institutionId

      val result = institutionRepo
        .findById(unarchivedId, session.userId)
        .execute
        .value
      result.archivedAt shouldBe None
    }
  }

  "Renaming an institution" should {
    "update the name field" in {
      val session = createUserSession()

      val institutionId = insertInstitution("Old Name", session.userId)
      val renamedId      = institutionRepo
        .rename(institutionId, session.userId)("New Name")
        .execute

      renamedId shouldBe institutionId

      val result = institutionRepo
        .findById(renamedId, session.userId)
        .execute
        .value
      result.name shouldBe "New Name"
    }
  }

object InstitutionRepositorySpec:
  private def insertInstitution(
    name: String,
    userId: Long = 1L
  )(using institutionRepo: InstitutionRepository, xa: Transactor): Long =
    institutionRepo.insert(name, userId).execute

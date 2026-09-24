package persistence

import models.Session
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

      val institutionId = insertInstitution("Test Institution")(using session)
      val result        = institutionRepo
        .findById(institutionId)(using session)
        .execute
        .value
      result shouldBe a[Institution]
      result.name shouldBe "Test Institution"
    }

    "return None if the institution does not exist" in {
      val session = createUserSession()

      val result =
        institutionRepo.findById(999)(using session).execute
      result shouldBe None
    }
  }

  "Finding all institutions for a user" should {
    "return a list of institutions" in {
      val session = createUserSession()

      val program = for
        _            <- institutionRepo.insert("Institution 1")(using session)
        _            <- institutionRepo.insert("Institution 2")(using session)
        institutions <- institutionRepo.list()(using session)
      yield institutions

      val result = program.execute
      result.length shouldBe 2
      result.map(_.name) should contain allOf ("Institution 1", "Institution 2")
    }

    "return a list sorted by name" in {
      val session = createUserSession()

      val program = for
        _            <- institutionRepo.insert("Z Institution")(using session)
        _            <- institutionRepo.insert("A Institution")(using session)
        _            <- institutionRepo.insert("M Institution")(using session)
        institutions <- institutionRepo.list()(using session)
      yield institutions

      val result = program.execute
      result.map(_.name) shouldBe List("A Institution", "M Institution", "Z Institution")
    }
  }

  "Archiving an institution" should {
    "set the archived_at field" in {
      val session = createUserSession()

      val institutionId = insertInstitution("To Archive")(using session)
      val archivedAt     = institutionRepo
        .archive(institutionId)(using session)
        .execute

      val result = institutionRepo
        .findById(institutionId)(using session)
        .execute
        .value
      result.archivedAt shouldBe Some(archivedAt)
    }
  }

  "Unarchiving an institution" should {
    "set the archived_at field to null/None" in {
      val session = createUserSession()

      val institutionId = insertInstitution("To Dearchive")(using session)
      val archivedAt    = institutionRepo
        .archive(institutionId)(using session)
        .execute

      val archived = institutionRepo
        .findById(institutionId)(using session)
        .execute
        .value
      archived.archivedAt shouldBe Some(archivedAt)

      val unarchivedId = institutionRepo
        .unarchive(institutionId)(using session)
        .execute

      unarchivedId shouldBe institutionId

      val result = institutionRepo
        .findById(unarchivedId)(using session)
        .execute
        .value
      result.archivedAt shouldBe None
    }
  }

  "Renaming an institution" should {
    "update the name field" in {
      val session = createUserSession()

      val institutionId = insertInstitution("Old Name")(using session)
      val renamedId      = institutionRepo
        .rename(institutionId, "New Name")(using session)
        .execute

      renamedId shouldBe institutionId

      val result = institutionRepo
        .findById(renamedId)(using session)
        .execute
        .value
      result.name shouldBe "New Name"
    }
  }

object InstitutionRepositorySpec:
  private def insertInstitution(
    name: String,
  )(using session: Session, institutionRepo: InstitutionRepository, xa: Transactor): Long =
    institutionRepo.insert(name)(using session).execute

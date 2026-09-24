package persistence

import play.api.test.FakeRequest

import action.UserRequest
import models.{Institution, Time}

import basespecs.DbSpec
import data.FakeSessions
import org.typelevel.doobie.implicits.*

class InstitutionRepositorySpec extends DbSpec with FakeSessions:

  val institutionRepo: InstitutionRepository = inject[InstitutionRepository]

  private def setupTestData(): UserRequest[?] =
    val userId = createTestUser("test@example.com", "passwordHash")
    UserRequest(
      userSession = createSession(validSessionId, userId),
      request = FakeRequest("GET", "/"),
    )

  "Selecting an institution by id" should {
    "return the institution if it exists" in {
      val request = setupTestData()

      val institutionId = insertInstitution("Test Institution")(using request)
      val result        = institutionRepo
        .findById(institutionId)(using request)
        .transact(xa)
        .unsafeRunSync()
        .value
      result shouldBe a[Institution]
      result.name shouldBe "Test Institution"
    }

    "return None if the institution does not exist" in {
      val request = setupTestData()

      val result =
        institutionRepo.findById(999)(using request).transact(xa).unsafeRunSync()
      result shouldBe None
    }
  }

  "Finding all institutions for a user" should {
    "return a list of institutions" in {
      val request = setupTestData()

      val program = for
        _            <- institutionRepo.insertInstitution("Institution 1")(using request)
        _            <- institutionRepo.insertInstitution("Institution 2")(using request)
        institutions <- institutionRepo.getAllInstitutions()(using request)
      yield institutions

      val result = program.transact(xa).unsafeRunSync()
      result.length shouldBe 2
      result.map(_.name) should contain allOf ("Institution 1", "Institution 2")
    }
  }

  "Archiving an institution" should {
    "set the archived_at field" in {
      val request = setupTestData()

      val institutionId = insertInstitution("To Archive")(using request)
      val archivedAt    = Time.now
      val updatedId     = institutionRepo
        .archiveInstitution(institutionId, archivedAt)(using request)
        .transact(xa)
        .unsafeRunSync()

      updatedId shouldBe institutionId

      val result = institutionRepo
        .findById(institutionId)(using request)
        .transact(xa)
        .unsafeRunSync()
        .value
      result.archivedAt shouldBe Some(archivedAt)
    }
  }

  "Unarchiving an institution" should {
    "set the archived_at field to null/None" in {
      val request = setupTestData()

      val institutionId = insertInstitution("To Dearchive")(using request)
      val archivedAt    = Time.now
      val archivedId    = institutionRepo
        .archiveInstitution(institutionId, archivedAt)(using request)
        .transact(xa)
        .unsafeRunSync()

      val archived = institutionRepo
        .findById(institutionId)(using request)
        .transact(xa)
        .unsafeRunSync()
        .value
      archived.archivedAt shouldBe Some(archivedAt)

      val unarchivedId = institutionRepo
        .unarchiveInstitution(institutionId)(using request)
        .transact(xa)
        .unsafeRunSync()

      unarchivedId shouldBe institutionId

      val result = institutionRepo
        .findById(unarchivedId)(using request)
        .transact(xa)
        .unsafeRunSync()
        .value
      result.archivedAt shouldBe None
    }
  }

  private def insertInstitution(name: String)(using request: UserRequest[?]): Long =
    institutionRepo.insertInstitution(name)(using request).transact(xa).unsafeRunSync()

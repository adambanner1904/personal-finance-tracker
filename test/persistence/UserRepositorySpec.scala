package persistence

import models.EmailAddress
import models.errors.SignUpError

import basespecs.DbSpec
import org.typelevel.doobie.implicits.*

class UserRepositorySpec extends DbSpec:

  lazy val testRepository: UserRepository = inject[UserRepository]

  val testEmail              = EmailAddress.unsafeFrom("test@gmail.com")
  val testPasswordHash       = "1234"
  val aDifferentPasswordHash = "4321"

  private def insertTestUser(email: EmailAddress, pw: String) =
    testRepository
      .insert(testEmail, testPasswordHash)
      .transact(xa)
      .unsafeRunSync()

  "Creating a user" should:
    "successfully return a Right[Long] value as that user's id" in:
      val id = insertTestUser(testEmail, testPasswordHash).value
      id shouldBe a[Long]

    "fail if that email already exists" in:
      val _     = insertTestUser(testEmail, testPasswordHash)
      val error = insertTestUser(testEmail, aDifferentPasswordHash).left.value
      error shouldBe a[SignUpError] // Email already used

  "Loading a user from a given email" should:
    "return the user if an entry exists for that given email" in:
      val id = insertTestUser(testEmail, testPasswordHash).value

      val loadedUser = testRepository
        .loadUser(testEmail)
        .transact(xa)
        .unsafeRunSync()
        .value

      loadedUser.passwordHash shouldBe testPasswordHash
      loadedUser.id shouldBe id

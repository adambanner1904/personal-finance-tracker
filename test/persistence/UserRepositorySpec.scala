package persistence

import implicits.Repository.*
import models.EmailAddress
import models.errors.SignUpError

import basespecs.DbSpec

class UserRepositorySpec extends DbSpec:

  lazy val userRepo: UserRepository = inject[UserRepository]

  val testEmail              = EmailAddress.unsafeFrom("test@gmail.com")
  val testPasswordHash       = "1234"
  val aDifferentPasswordHash = "4321"

  private def insertUser(email: EmailAddress, pw: String) =
    userRepo
      .insert(email, pw)
      .execute

  "Creating a user" should:
    "successfully return a Right[Long] value as that user's id" in:
      val id = insertUser(testEmail, testPasswordHash).value
      id shouldBe a[Long]

    "fail if that email already exists" in:
      val _     = insertUser(testEmail, testPasswordHash)
      val error = insertUser(testEmail, aDifferentPasswordHash).left.value
      error shouldBe a[SignUpError] // Email already used

  "Loading a user from a given email" should:
    "return the user if an entry exists for that given email" in:
      val id = insertUser(testEmail, testPasswordHash).value

      val loadedUser = userRepo
        .findByEmail(testEmail)
        .execute
        .value

      loadedUser.passwordHash shouldBe testPasswordHash
      loadedUser.id shouldBe id

package services

import basespecs.UnitSpec
import java.util.UUID
import models.errors.LoginError

class AuthServiceSpec extends UnitSpec:
  val authService = inject[AuthService]
  
  "AuthService" should:
    "fail to log in a user with invalid credentials" in:
      val result = authService.loginUser("authService@gmail.com", "password123").left.value
      result shouldBe a[LoginError]
    
    "create a user and return a session ID" in:
      val sessionId = authService.createUser("authservice@gmail.com", "password123").value
      sessionId shouldBe a[UUID]

    "login a user and return a session ID" in:
      val sessionId = authService.loginUser("authService@gmail.com", "password123").value
      sessionId shouldBe a[UUID]
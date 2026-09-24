package models

import models.errors.ParseEmailError

import basespecs.UnitSpec

class EmailAddressSpec extends UnitSpec:
  "Parsing an email" should:
    "fail if the email is blank" in:
      val result = EmailAddress.from("").left.value
      result shouldBe a[ParseEmailError]

    "fail if the email is too long" in:
      val email  = "a" * 320 + "@gmail.com"
      val result = EmailAddress.from(email).left.value
      result shouldBe a[ParseEmailError]

    "fail if the email is not in a valid email format" in:
      val email  = "thisisnotanemail.com"
      val result = EmailAddress.from(email).left.value
      result shouldBe a[ParseEmailError]

    "parse a valid email" in:
      val email  = "  tHisis@anemail.COM  "
      val result = EmailAddress.from(email).value

      result shouldBe a[EmailAddress]
      result.value shouldBe "thisis@anemail.com"

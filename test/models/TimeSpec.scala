package models

import java.time.Instant

import scala.concurrent.duration.*

import basespecs.UnitSpec

class TimeSpec extends UnitSpec:
  "Time operations" should:
    "work" in:
      val time1 = Time.unsafeFrom(Instant.parse("1970-01-01T00:01:01Z"))
      val time2 = Time.unsafeFrom(Instant.parse("1970-01-01T00:02:02Z"))
      time1 + 1.minute + 1.second shouldBe time2
      time2 - time1 shouldBe 1.minute
      time1 < time2 shouldBe true
      time2 > time1 shouldBe true
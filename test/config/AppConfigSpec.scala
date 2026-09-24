package config

import scala.concurrent.duration.*

class AppConfigSpec extends basespecs.UnitSpec:
  "AppConfig" should:
    "load the configuration" in:
      val config = inject[AppConfig]
      config should not be null
      config.sessionTimeToLive shouldBe a[scala.concurrent.duration.FiniteDuration]
      config.sessionTimeToLive shouldBe 30.minutes

package models

import java.time.Instant
import org.typelevel.doobie.util.meta.Meta
import java.time.Duration as JavaDuration
import scala.concurrent.duration.*

opaque type Time = Instant

object Time:
  def unsafeFrom(value: Instant): Time = value
  def now: Time = unsafeFrom(Instant.now())
  
  extension (time1: Time)
    def value: Instant = time1
    infix def -(time2: Time): FiniteDuration = FiniteDuration(JavaDuration.between(time1, time2).toMinutes(), "m")
    infix def +(duration: FiniteDuration): Time = 
          time1.plus(JavaDuration.ofMillis(duration.toMillis))

  import org.typelevel.doobie.implicits.javatimedrivernative.JavaInstantMeta
  given Meta[Time] = Meta[Instant](using JavaInstantMeta).imap(unsafeFrom)(_.value)
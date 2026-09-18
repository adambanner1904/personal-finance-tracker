package models

import java.time.{
  Duration as JavaDuration,
  Instant
}

import scala.concurrent.duration.*

import org.typelevel.doobie.util.meta.Meta

opaque type Time = Instant

object Time:
  def unsafeFrom(value: Instant): Time = value
  def now: Time = unsafeFrom(Instant.now())
  
  extension (time1: Time)
    def value: Instant = time1
    infix def -(time2: Time): FiniteDuration = FiniteDuration(JavaDuration.between(time2, time1).toMinutes(), "m")
    infix def +(duration: FiniteDuration): Time = 
          time1.plus(JavaDuration.ofMillis(duration.toMillis))

    infix def >(time2: Time): Boolean = JavaDuration.between(time2, time1).toSeconds > 0
    infix def <(time2: Time): Boolean = JavaDuration.between(time2, time1).toSeconds < 0

  import org.typelevel.doobie.implicits.javatimedrivernative.JavaInstantMeta
  given Meta[Time] = Meta[Instant](using JavaInstantMeta).imap(unsafeFrom)(_.value)
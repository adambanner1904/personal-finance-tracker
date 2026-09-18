package basespecs

import play.api.test.Injecting
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.{
  EitherValues,
  OptionValues
}

class UnitSpec 
  extends AnyWordSpec 
  with Matchers 
  with GuiceOneAppPerSuite 
  with OptionValues
  with EitherValues
  with Injecting
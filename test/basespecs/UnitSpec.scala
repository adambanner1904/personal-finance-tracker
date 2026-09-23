package basespecs

import play.api.test.Injecting

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.{
  EitherValues,
  OptionValues
}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite

class UnitSpec 
  extends AnyWordSpec 
  with Matchers 
  with GuiceOneAppPerSuite 
  with OptionValues
  with EitherValues
  with Injecting

  
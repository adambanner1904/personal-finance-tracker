package basespecs

import play.api.test.Injecting

import scala.concurrent.ExecutionContext

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.{EitherValues, OptionValues}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import org.scalatestplus.mockito.MockitoSugar
import data.{FakeRequests, FakeSessions}
import play.api.i18n.MessagesApi
import play.api.mvc.Results


class UnitSpec
    extends AnyWordSpec
    with Matchers
    with GuiceOneAppPerSuite
    with OptionValues
    with EitherValues
    with Injecting
    with Results
    with MockitoSugar
    with FakeRequests
    with FakeSessions:

  given ExecutionContext = inject[ExecutionContext]
  lazy val messagesApi: MessagesApi  = inject[MessagesApi]

  val userRequest = fakeUserRequest(validSession)(using messagesApi)

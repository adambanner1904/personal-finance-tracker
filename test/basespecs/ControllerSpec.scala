package basespecs

import play.api.mvc.*
import play.api.test.Helpers.*
import play.api.i18n.MessagesApi

import org.apache.pekko.stream.Materializer
import helpers.FakeUserAction

class ControllerSpec extends UnitSpec:
  val mcc         = inject[MessagesControllerComponents]
  val bp         = inject[BodyParsers.Default]
  given MessagesApi           = stubMessagesApi()
  given Materializer = app.materializer

  val fakeUserAction = FakeUserAction(validSession, messagesApi, bp)
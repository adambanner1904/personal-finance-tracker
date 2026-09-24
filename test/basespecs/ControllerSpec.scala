package basespecs

import play.api.mvc.*
import play.api.test.Helpers.*
import play.api.i18n.MessagesApi

class ControllerSpec extends UnitSpec:
  val mcc         = inject[MessagesControllerComponents]
  val bp         = inject[BodyParsers.Default]
  given MessagesApi           = stubMessagesApi()
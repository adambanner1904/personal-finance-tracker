package helpers

object AuthHelpers:
  def requestWithSession(sessionId: Option[String]) =
    val request = play.api.test.FakeRequest()
    sessionId match
      case Some(id) => request.withCookies(play.api.mvc.Cookie("session-id", id))
      case None     => request
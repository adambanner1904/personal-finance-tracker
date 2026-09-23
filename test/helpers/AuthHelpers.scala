package helpers

object AuthHelpers:
  def requestWithSession(method: String, url: String, sessionId: Option[String]) =
    val request = play.api.test.FakeRequest(method, url)
    sessionId match
      case Some(id) => request.withCookies(play.api.mvc.Cookie("session-id", id))
      case None     => request
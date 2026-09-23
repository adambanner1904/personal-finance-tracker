package modules

import com.google.inject.AbstractModule
import action.{AuthenticatedAction, UserAction}

class AuthModule extends AbstractModule:
  override def configure(): Unit =
    bind(classOf[UserAction]).to(classOf[AuthenticatedAction])

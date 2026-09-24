package modules

import action.{AuthenticatedAction, UserAction}

import com.google.inject.AbstractModule

class AuthModule extends AbstractModule:
  override def configure(): Unit =
    bind(classOf[UserAction]).to(classOf[AuthenticatedAction])

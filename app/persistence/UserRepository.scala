package persistence

import models.errors.SignUpError
import models.{EmailAddress, User}

import org.typelevel.doobie.ConnectionIO
import org.typelevel.doobie.implicits.*
import org.typelevel.doobie.postgres.*

class UserRepository:

  def insert(
    email: EmailAddress,
    passwordHash: String,
  ): ConnectionIO[Either[SignUpError, Long]] =
    sql"""
        insert into users (email, password_hash) 
        values (${email.value}, $passwordHash)
        returning id
    """
      .query[Long]
      .unique
      .attemptSomeSqlState:
        case sqlstate.class23.UNIQUE_VIOLATION => SignUpError.EmailAlreadyUsed

  def findByEmail(email: EmailAddress): ConnectionIO[Option[User]] =
    sql"""
        select * from users 
        where email = ${email.value}
    """
      .query[User]
      .option

package persistence

import org.typelevel.doobie.postgres.*
import org.typelevel.doobie.implicits.*
import org.typelevel.doobie.postgres.implicits.*
import models.EmailAddress
import org.typelevel.doobie.ConnectionIO
import models.User
import models.db.DbError


class UserRepository:
  
  def insertUser(email: EmailAddress, passwordHash: String): ConnectionIO[Either[DbError, User]] = 
    sql"insert into users (email, password_hash) values (${email.value}, $passwordHash)"
      .update
      .withUniqueGeneratedKeys[User]("id", "email", "password_hash", "created_at", "updated_at")
      .attemptSomeSqlState: 
        case sqlstate.class23.UNIQUE_VIOLATION => DbError.UniqueViolation


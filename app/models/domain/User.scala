package models.domain

import java.time.Instant

case class User(
  id: Long, 
  email: EmailAddress, 
  passwordHash: String, 
  createdAt: Instant,
  updatedAt: Instant, 
)

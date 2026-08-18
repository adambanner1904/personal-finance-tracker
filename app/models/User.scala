package models

import models.*

case class User(
  id: Long, 
  email: EmailAddress, 
  passwordHash: String, 
  createdAt: Time,
  updatedAt: Time, 
)

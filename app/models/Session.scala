package models

import java.util.UUID

case class Session(
  sessionId: UUID,
  userId: Long,
  createdAt: Time,
  expiresAt: Time,
)

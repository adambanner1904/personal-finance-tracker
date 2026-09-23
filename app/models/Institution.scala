package models

case class Institution(
  id: Long,
  userId: Long,
  name: String,
  archivedAt: Option[Time],
  createdAt: Time,
  updatedAt: Time,
)
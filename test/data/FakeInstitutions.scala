package data

import models.{Institution, Time}

trait FakeInstitutions:
  val halifax = Institution(
    id = 1L,
    userId = 1L,
    name = "Halifax",
    archivedAt = None,
    createdAt = Time.now,
    updatedAt = Time.now,
  )
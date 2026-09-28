package testdata

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

  val starling = Institution(
    id = 2L,
    userId = 1L,
    name = "Starling",
    archivedAt = None,
    createdAt = Time.now,
    updatedAt = Time.now,
  )
package basespecs

import models.db.Transactor

class ServiceSpec extends UnitSpec:
  val xa = inject[Transactor]
  given Transactor = xa
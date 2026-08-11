package repositories

import basespecs.DatabaseBaseSpec
import org.typelevel.doobie.implicits.*

class SimpleRepositorySpec extends DatabaseBaseSpec:
  "A simple select 1 query" should: 
    "return an integer 1" in:
      val one = sql"select 1;"
        .query[Int].unique
        .transact(xa)
        .unsafeRunSync()

      one mustBe 1
  

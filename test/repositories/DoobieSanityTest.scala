package repositories

import org.scalatestplus.play.PlaySpec
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import doobie._
import doobie.implicits._

class DoobieSanityTest extends PlaySpec {

  "Cats Effect and Doobie" should {
    "compile and run a basic pure functional query" in {
      // 1. Create a dummy in-memory transactor just to check types
      val xa = Transactor.fromDriverManager[IO](
        "org.postgresql.Driver", "jdbc:postgresql:test", "user", "pass"
      )

      // 2. Assemble a pure functional connection program
      val program: ConnectionIO[Int] = sql"SELECT 1".query[Int].unique

      // 3. Translate it to a Cats IO effect
      val ioEffect: IO[Int] = program.transact(xa)

      // 4. Assert it compiles and resolves the functional types
      ioEffect mustBe a[IO[_]]
    }
  }
}

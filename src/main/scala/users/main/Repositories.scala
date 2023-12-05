package users.main
import cats.data._
import users.config._
import users.persistence.repositories._

object Repositories {
  val reader: Reader[Unit, Repositories] = Reader((_: Unit) ⇒ Repositories())
  val fromApplicationConfig: Reader[ApplicationConfig, Repositories] = reader.local[ApplicationConfig](_ ⇒ ())
}

final case class Repositories() {
  val userRepositories: UserRepository = UserRepository.inMemory()
}

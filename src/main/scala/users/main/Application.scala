package users.main
import cats.data._
import users.config._

case class Application(services: Services)

object Application {
  private val reader: Reader[Services, Application] =
    Reader(Application.apply)

  val fromApplicationConfig: Reader[ApplicationConfig, Application] =
    Services.fromApplicationConfig andThen reader
}

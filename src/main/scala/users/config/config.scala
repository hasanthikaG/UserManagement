package users.config
import cats.data._

case class ApplicationConfig(executors: ExecutorsConfig, services: ServicesConfig)
case class ExecutorsConfig(services: ExecutorsConfig.ServicesConfig)
case class ServicesConfig(users: ServicesConfig.UsersConfig)

object ExecutorsConfig {
  val fromApplicationConfig: Reader[ApplicationConfig, ExecutorsConfig] = Reader(_.executors)
  case class ServicesConfig(parallelism: Int)
}

object ServicesConfig {
  val fromApplicationConfig: Reader[ApplicationConfig, ServicesConfig] = Reader(_.services)
  case class UsersConfig(failureProbability: Double, timeoutProbability: Double)
}

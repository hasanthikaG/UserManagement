package users
import users.config._
import users.main._

object Main extends App {
  val config = ApplicationConfig(
    executors = ExecutorsConfig(
      services = ExecutorsConfig.ServicesConfig(parallelism = 4)
    ),
    services = ServicesConfig(
      users = ServicesConfig.UsersConfig(
        failureProbability = 0.1,
        timeoutProbability = 0.1
      )
    )
  )

  val application = Application.fromApplicationConfig.run(config)
}

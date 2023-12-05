package users.persistence.repositories.users
import cats.implicits.catsSyntaxEq
import users.domain._
import users.persistence.repositories.UserRepository
import scala.collection.concurrent.TrieMap
import scala.concurrent.Future

private[users] object InMemoryRepository {
  private final val UserMap: TrieMap[User.Id, User] = TrieMap.empty
}

private[users] class InMemoryRepository extends UserRepository {
  import InMemoryRepository._

  /* Insert an user */
  def insert(user: User): Future[Done] = Future.successful {
    UserMap + (user.id → user)
    Done
  }

  /* Get a single user by id */
  def get(id: User.Id): Future[Option[User]] = Future.successful(UserMap.get(id))

  /* Get a single user by username */
  def getByUsername(username: Username): Future[Option[User]] = Future.successful {
    UserMap.collectFirst {
      case (_, user) if user.username === username ⇒ user
    }
  }

  /* Get all users list */
  def all(): Future[List[User]] =
    Future.successful(UserMap.values.toList)
}

package users.persistence
import users.persistence.repositories.users.Repository

package object repositories {
  type UserRepository = users.Repository
  val UserRepository: Repository.type = Repository
}

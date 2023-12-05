package users.domain
import cats.implicits.catsSyntaxEq
import cats.kernel.Eq
import java.time.OffsetDateTime
import com.softwaremill.quicklens._

final case class User(
    id: User.Id,
    username: Username,
    email: EmailAddress,
    password: Password,
    metadata: User.Metadata
) {
  private def status: User.Status = User.status(this)

  def isActive: Boolean = status === User.Status.Active
  def isBlocked: Boolean = status === User.Status.Blocked
  def isDeleted: Boolean = status === User.Status.Deleted

  def updateEmailAddress(email: EmailAddress, at: OffsetDateTime): User = User.updateEmail(this, email, at)
  def updatePassword(password: Password, at: OffsetDateTime): User = User.updatePassword(this, password, at)
  def resetPassword(at: OffsetDateTime): User = User.resetPassword(this, at)
  def block(at: OffsetDateTime): User = User.block(this, at)
  def unblock(at: OffsetDateTime): User = User.unblock(this, at)
  def delete(at: OffsetDateTime): User = User.delete(this, at)

}

object User {
  final case class Id(value: String) extends AnyVal
  final case class Metadata(
      version: Int,
      createdAt: OffsetDateTime,
      updatedAt: OffsetDateTime,
      blockedAt: Option[OffsetDateTime],
      deletedAt: Option[OffsetDateTime]
  )

  sealed trait Status
  private object Status {
    final case object Active extends Status
    final case object Blocked extends Status
    final case object Deleted extends Status

    implicit val eq: Eq[Status] = Eq.fromUniversalEquals
  }

  /* Define user status */
  private final def status(user: User): Status =
    if (user.metadata.deletedAt.isDefined) Status.Deleted
    else if (user.metadata.blockedAt.isDefined) Status.Blocked
    else Status.Active

  /* Assign user values */
  private def apply(
      id: User.Id,
      username: Username,
      email: EmailAddress,
      password: Password,
      at: OffsetDateTime
  ): User = User(id, username, email, password, Metadata(1, at, at, None, None))

  /* Update user email */
  private def updateEmail(user: User, email: EmailAddress, at: OffsetDateTime): User =
    user
      .modify(_.email)
      .setTo(email)
      .modify(_.metadata.updatedAt)
      .setTo(at)
      .modify(_.metadata.version)
      .setTo(_ + 1)

  /* Update user password */
  private def updatePassword(user: User, password: Password, at: OffsetDateTime): User =
    user
      .modify(_.password)
      .setTo(password)
      .modify(_.metadata.updatedAt)
      .setTo(at)
      .modify(_.metadata.version)
      .setTo(_ + 1)

  /* User reset password */
  private def resetPassword(user: User, at: OffsetDateTime): User =
    user
      .modify(_.password)
      .setTo(None)
      .modify(_.metadata.updatedAt)
      .setTo(at)
      .modify(_.metadata.version)
      .setTo(_ + 1)

  /* Block user */
  private def block(user: User, at: OffsetDateTime): User =
    user
      .modify(_.metadata.blockedAt)
      .setTo(Some(at))
      .modify(_.metadata.updatedAt)
      .setTo(at)
      .modify(_.metadata.version)
      .using(_ + 1)

  /* Un block user */
  def unblock(user: User, at: OffsetDateTime): User =
    user
      .modify(_.metadata.blockedAt)
      .setTo(None)
      .modify(_.metadata.updatedAt)
      .setTo(at)
      .modify(_.metadata.version)
      .using(_ + 1)

  /* Delete user */
  def delete(user: User, at: OffsetDateTime): User =
    user
      .modify(_.metadata.deletedAt)
      .setTo(Some(at))
      .modify(_.metadata.updatedAt)
      .setTo(at)
      .modify(_.metadata.version)
      .using(_ + 1)

}

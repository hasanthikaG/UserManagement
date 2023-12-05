package users.domain
import cats.kernel.Eq

final case class Username(value: String) extends AnyVal

object Username {
  implicit val eq: Eq[Username] = Eq.fromUniversalEquals
}

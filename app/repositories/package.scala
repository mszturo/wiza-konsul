import models.RodzajDecyzji
import models.RodzajDecyzji.RodzajDecyzji
import slick.jdbc.PostgresProfile.api._

package object repositories {
  implicit val rodzajDecyzjiMapper = MappedColumnType.base[RodzajDecyzji, String](
    e => e.toString,
    s => RodzajDecyzji.withName(s)
  )
}

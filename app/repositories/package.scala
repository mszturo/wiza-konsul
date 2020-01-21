import models.RodzajDecyzji
import models.RodzajDecyzji.RodzajDecyzji
import play.api.db.slick.{DbName, DefaultSlickApi}
import slick.jdbc.PostgresProfile.api._

package object repositories {
  implicit val rodzajDecyzjiMapper = MappedColumnType.base[RodzajDecyzji, String](
    e => e.toString,
    s => RodzajDecyzji.withName(s)
  )

  lazy val dbName = DbName("default")
}

package models.db

import models.RodzajDecyzji
import play.api.libs.json.{Reads, Writes}

object RodzajDecyzjiDb {
  implicit val readsMyEnum: Reads[models.RodzajDecyzji.Value] =
    Reads.enumNameReads(RodzajDecyzji)
  implicit val writesMyEnum: Writes[models.RodzajDecyzji.Value] =
    Writes.enumNameWrites
}

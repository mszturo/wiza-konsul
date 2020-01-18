package models

import models.db.TypDokumentuRow

class TypDokumentu(
  var id: Long,
  var nazwa: String
) {
  def toRow: TypDokumentuRow = TypDokumentuRow(id, nazwa)
}
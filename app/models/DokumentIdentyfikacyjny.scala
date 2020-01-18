package models

import models.db.DokumentIdentyfikacyjnyRow

class DokumentIdentyfikacyjny(
  var id: Long,
  var nrDokumentu: String,
  var typDokumentu: TypDokumentu
){
  def toRow: DokumentIdentyfikacyjnyRow = DokumentIdentyfikacyjnyRow(
    id,
    nrDokumentu,
    typDokumentu.id
  )
}
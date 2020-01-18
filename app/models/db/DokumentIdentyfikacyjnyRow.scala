package models.db

import models.{DokumentIdentyfikacyjny, TypDokumentu}

case class DokumentIdentyfikacyjnyRow(
  id: Long,
  nrDokumentu: String,
  typDokumentu: Long
){
  def toEntity(typDokumentu: TypDokumentu) = new DokumentIdentyfikacyjny(
    id,
    nrDokumentu,
    typDokumentu
  )
}



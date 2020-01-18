package models.db

import models.TypDokumentu

case class TypDokumentuRow(
  id: Long,
  nazwa: String
){
  def toEntity = new TypDokumentu(id, nazwa)
}


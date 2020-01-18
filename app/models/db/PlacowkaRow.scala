package models.db

import models.Placowka

case class PlacowkaRow(
  id: Long,
  nazwa: String,
  maksSprawZagregowanych: Int
){
  def toEntity: Placowka = new Placowka(
    id,
    nazwa,
    maksSprawZagregowanych
  )
}
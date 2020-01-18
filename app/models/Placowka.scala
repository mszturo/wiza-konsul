package models

import models.db.PlacowkaRow

class Placowka(
  var id: Long,
  var nazwa: String,
  var maksSprawZagregowanych: Int
){
  def toRow: PlacowkaRow = PlacowkaRow(
    id,
    nazwa,
    maksSprawZagregowanych
  )
}
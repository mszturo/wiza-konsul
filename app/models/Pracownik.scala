package models

import models.db.PracownikRow

class Pracownik(
  var id: Long,
  var login: String,
  var haslo: String
){
  def toRow: PracownikRow = PracownikRow(
    id,
    login,
    haslo,
    czyKierownik = false
  )
}
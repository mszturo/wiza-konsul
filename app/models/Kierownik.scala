package models

import models.db.PracownikRow

class Kierownik(
  id: Long,
  login: String,
  haslo: String)
  extends Pracownik(id, login, haslo){
  override def toRow: PracownikRow = PracownikRow(
    id,
    login,
    haslo,
    czyKierownik = true
  )
}
package models

import java.sql.Date

import models.RodzajDecyzji.RodzajDecyzji
import models.db.DecyzjaRow

class Decyzja(
  var id: Long,
  var dataDecyzji : Date,
  var uzasadnienie : String,
  var wydajacy : Kierownik,
  var rodzajDecyzji : RodzajDecyzji
){
  def toRow: DecyzjaRow = DecyzjaRow(
    id,
    dataDecyzji,
    uzasadnienie,
    wydajacy.id,
    rodzajDecyzji
  )
}
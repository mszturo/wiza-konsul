package models

import java.sql.Timestamp

import models.db.SprawaRow

class Sprawa(
  var id: Long,
  var zdjecie: String,
  var trescSprawy: String,
  var identyfikator: String,
  var dataUtworzenia: Timestamp,
  var czyWyslana: Boolean,
  var daneOsobowe: DaneOsobowe,
  var aktualnaDecyzja: Decyzja,
  var czyZakonczona: Boolean
){
  def toRow: SprawaRow = SprawaRow(
    id,
    zdjecie,
    trescSprawy,
    identyfikator,
    dataUtworzenia,
    czyWyslana,
    daneOsobowe.id,
    aktualnaDecyzja.id,
    czyZakonczona,
    None)
}
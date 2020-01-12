package models

import java.sql.Timestamp

class Sprawa(
  var zdjecie: String,
  var trescSprawy: String,
  var identyfikator: String,
  var dataUtworzenia: Timestamp,
  var czyWyslana: Boolean,
  var daneOsobowe: DaneOsobowe,
  var aktualnaDecyzja: Decyzja,
  var czyZakonczona: Boolean
)
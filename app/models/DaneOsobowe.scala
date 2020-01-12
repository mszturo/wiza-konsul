package models

import java.sql.Date

class DaneOsobowe(
  var imie: String,
  var drugieImie: Option[String],
  var nazwisko: String,
  var dataUrodzenia: Date,
  var miejsceUrodzenia: String,
  var pesel: String,
  var dokumentIdentyfikacyjny: DokumentIdentyfikacyjny
)

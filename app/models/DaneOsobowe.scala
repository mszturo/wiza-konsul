package models

import java.sql.Date

import models.db.DaneOsoboweRow

class DaneOsobowe(
  var id: Long,
  var imie: String,
  var drugieImie: Option[String],
  var nazwisko: String,
  var dataUrodzenia: Date,
  var miejsceUrodzenia: String,
  var pesel: String,
  var dokumentIdentyfikacyjny: DokumentIdentyfikacyjny
){
  def toRow: DaneOsoboweRow = DaneOsoboweRow(
    id,
    imie,
    drugieImie,
    nazwisko,
    dataUrodzenia,
    miejsceUrodzenia,
    pesel,
    dokumentIdentyfikacyjny.id
  )
}
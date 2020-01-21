package models.db

import java.sql.Date

import models.{DaneOsobowe, DokumentIdentyfikacyjny}

case class DaneOsoboweRow(
  id: Long,
  imie: String,
  drugieImie: Option[String],
  nazwisko: String,
  dataUrodzenia: Date,
  miejsceUrodzenia: String,
  pesel: String,
  dokumentIdentyfikacyjny: Long
){
  def toEntity(dokumentIdentyfikacyjny: DokumentIdentyfikacyjny) = new DaneOsobowe(
    id,
    imie,
    drugieImie,
    nazwisko,
    dataUrodzenia,
    miejsceUrodzenia,
    pesel,
    dokumentIdentyfikacyjny
  )
}

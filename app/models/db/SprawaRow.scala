package models.db

import java.sql.Timestamp

import models.{DaneOsobowe, Decyzja, Sprawa}

case class SprawaRow(
  id: Long,
  zdjecie: String,
  trescSprawy: String,
  identyfikator: String,
  dataUtworzenia: Timestamp,
  czyWyslana: Boolean,
  daneOsobowe: Long,
  aktualnaDecyzja: Option[Long],
  czyZakonczona: Boolean,
  przesylka: Option[Long]
){
  def toEntity(daneOsobowe: DaneOsobowe, aktualnaDecyzja: Option[Decyzja]) = new Sprawa(
    id,
    zdjecie,
    trescSprawy,
    identyfikator,
    dataUtworzenia,
    czyWyslana,
    daneOsobowe,
    aktualnaDecyzja,
    czyZakonczona
  )
}
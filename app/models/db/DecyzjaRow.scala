package models.db

import java.sql.Date

import models.RodzajDecyzji.RodzajDecyzji
import models.{Decyzja, Kierownik}

case class DecyzjaRow(
  id: Long,
  dataDecyzji : Date,
  uzasadnienie : String,
  wydajacy : Long,
  rodzajDecyzji : RodzajDecyzji
){
  def toEntity(wydajacy: Kierownik): Decyzja = new Decyzja(
    id,
    dataDecyzji,
    uzasadnienie,
    wydajacy,
    rodzajDecyzji
  )
}
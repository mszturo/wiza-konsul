package models

import java.sql.Timestamp

import models.db.PrzesylkaRow

class Przesylka(
  var id: Long,
  var dataPrzeslania: Timestamp,
  var sprawy: java.util.List[Sprawa],
  var placowka: Placowka
){
  def toRow: PrzesylkaRow = PrzesylkaRow(
    id,
    dataPrzeslania,
    placowka.id
  )
}
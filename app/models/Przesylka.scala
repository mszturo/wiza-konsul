package models

import java.sql.Timestamp

class Przesylka(
  var dataPrzeslania: Timestamp,
  var sprawy: java.util.List[Sprawa],
  var placowka: Placowka
)
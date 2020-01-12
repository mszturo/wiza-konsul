package models

import java.sql.Date

import models.RodzajDecyzji.RodzajDecyzji

class Decyzja(
  var dataDecyzji : Date,
  var uzasadnienie : String,
  var wydajacy : Kierownik,
  var sprawa : Sprawa,
  var rodzajDecyzji : RodzajDecyzji
)
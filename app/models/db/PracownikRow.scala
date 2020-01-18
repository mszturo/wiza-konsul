package models.db

import models.{Kierownik, Pracownik}

case class PracownikRow(
  id: Long,
  login: String,
  haslo: String,
  czyKierownik: Boolean
){
  def toEntity: Pracownik = {
    if(czyKierownik)
      new Kierownik(id, login, haslo)
  else
      new Pracownik(id, login, haslo)
  }
}
package models.db

import java.sql.Timestamp

import models.{Placowka, Przesylka, Sprawa}

import scala.jdk.CollectionConverters._

case class PrzesylkaRow(
  id: Long,
  dataPrzeslania: Timestamp,
  placowka: Long
){
  def toEntity(sprawy: List[Sprawa], placowka: Placowka) = new Przesylka(
    id,
    dataPrzeslania,
    sprawy.asJava,
    placowka
  )
}
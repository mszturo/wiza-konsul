package repositories

import java.sql.Timestamp
import java.util

import javax.inject.{Inject, Singleton}
import models.db.PrzesylkaRow
import models.{Kierownik, Przesylka}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}
import scala.language.implicitConversions

@Singleton
class PrzesylkaRepo @Inject()
(val sRepo: SprawaRepo,
  val pRepo: PlacowkaRepo,
  val doRepo: DaneOsoboweRepo)
  (dbConfigProvider: DatabaseConfigProvider)
  (implicit ec: ExecutionContext)
  extends Repository[Przesylka] {
  private[repositories] lazy val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private[repositories] class PrzesylkaTable(tag: Tag) extends Table[PrzesylkaRow](tag, "przesylki") {

    def id = column[Long]("id")

    def dataPrzeslania = column[Timestamp]("data_przeslania")

    def placowkaId = column[Long]("placowka")

    def placowka =
      foreignKey("P_ID", placowkaId, placowki)(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)

    def * = (id, dataPrzeslania, placowkaId) <> (PrzesylkaRow.tupled, PrzesylkaRow.unapply)
  }

  private[repositories] lazy val daneOsobowe = doRepo.daneOsobowe
  private[repositories] lazy val decyzje = sRepo.decyzje
  private[repositories] lazy val dokumentyIdentyfikacyjne = doRepo.dokumentyIdentyfikacyjne
  private[repositories] lazy val placowki = pRepo.placowki
  private[repositories] lazy val pracownicy = sRepo.pracownicy
  private[repositories] lazy val przesylki = TableQuery[PrzesylkaTable]
  private[repositories] lazy val sprawy = sRepo.sprawy
  private[repositories] lazy val typyDokumentu = doRepo.typyDokumentu

  def upsert(entity: Przesylka): Future[Boolean] = db.run {
    przesylki
      .insertOrUpdate(entity.toRow)
      .checkSingleRow
  }

  def delete(entity: Przesylka): Future[Boolean] = db.run {
    przesylki
      .filter(_.id === entity.id)
      .delete
      .checkSingleRow
  }

  def get(id: Long): Future[Option[Przesylka]] = db.run {
    przesylki.filter(_.id === id)
      .join(placowki)
      .on(_.placowkaId === _.id)
      .join(sprawy)
      .on(_._1.id === _.przesylkaId)
      .join(daneOsobowe)
      .on(_._2.daneOsoboweId === _.id)
      .join(dokumentyIdentyfikacyjne)
      .on(_._2.dokumentIdentyfikacyjnyId === _.id)
      .join(typyDokumentu)
      .on(_._2.typDokumentuId === _.id)
      .join(decyzje)
      .on(_._1._1._1._2.aktualnaDecyzjaId === _.id)
      .join(pracownicy)
      .on(_._2.wydajacyId === _.id)
      .result
      .map(_.headOption.groupBy(_._1)
        .map {
          case (((((((pr, pl), _), _), _), _), _), list) =>
            val sprawy = list
              .flatMap {
                case ((((((_, s), dOs), di), td), d), prac) =>
                  prac.toEntity match {
                    case k: Kierownik =>
                      Some(s.toEntity(dOs.toEntity(di.toEntity(td.toEntity)), d.toEntity(k)))
                    case _ => None
                  }
              }.toList
            pr.toEntity(sprawy, pl.toEntity)
        }.headOption)
  }

  def list(): Future[util.List[Przesylka]] = db.run {
    przesylki
      .join(placowki)
      .on(_.placowkaId === _.id)
      .join(sprawy)
      .on(_._1.id === _.przesylkaId)
      .join(daneOsobowe)
      .on(_._2.daneOsoboweId === _.id)
      .join(dokumentyIdentyfikacyjne)
      .on(_._2.dokumentIdentyfikacyjnyId === _.id)
      .join(typyDokumentu)
      .on(_._2.typDokumentuId === _.id)
      .join(decyzje)
      .on(_._1._1._1._2.aktualnaDecyzjaId === _.id)
      .join(pracownicy)
      .on(_._2.wydajacyId === _.id)
      .result
      .map(_.groupBy(_._1)
        .map {
          case (((((((pr, pl), _), _), _), _), _), list) =>
            val sprawy = list
              .flatMap {
                case ((((((_, s), dOs), di), td), d), prac) =>
                  prac.toEntity match {
                    case k: Kierownik =>
                      Some(s.toEntity(dOs.toEntity(di.toEntity(td.toEntity)), d.toEntity(k)))
                    case _ => None
                  }
              }.toList
            pr.toEntity(sprawy, pl.toEntity)
        })
  }
}

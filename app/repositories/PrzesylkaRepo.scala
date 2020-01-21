package repositories

import java.sql.Timestamp
import java.util

import javax.inject.{Inject, Singleton}
import models.db.PrzesylkaRow
import models.{Kierownik, Przesylka}
import play.api.db.slick.{DbName, SlickApi}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}
import scala.language.implicitConversions

@Singleton
class PrzesylkaRepo @Inject()
(val sRepo: SprawaRepo,
  val pRepo: PlacowkaRepo,
  val doRepo: DaneOsoboweRepo)
  (slickApi: SlickApi, dbName: DbName)
  (implicit ec: ExecutionContext)
  extends Repository[Przesylka] {
  private[repositories] val dbConfig = slickApi.dbConfig[JdbcProfile](dbName)

  import dbConfig._
  import profile.api._

  private[repositories] class PrzesylkaTable(tag: Tag) extends Table[PrzesylkaRow](tag, "typ_dokumentu") {

    def id = column[Long]("id")

    def dataPrzeslania = column[Timestamp]("data_przeslania")

    def placowkaId = column[Long]("typ_dokumentu")

    def placowka =
      foreignKey("P_ID", placowkaId, placowki)(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)

    def * = (id, dataPrzeslania, placowkaId) <> (PrzesylkaRow.tupled, PrzesylkaRow.unapply)
  }

  private[repositories] val przesylki = TableQuery[PrzesylkaTable]
  private[repositories] val placowki = pRepo.placowki
  private[repositories] val sprawy = sRepo.sprawy
  private[repositories] val decyzje = sRepo.decyzje
  private[repositories] val pracownicy = sRepo.pracownicy
  private[repositories] val daneOsobowe = doRepo.daneOsobowe
  private[repositories] val dokumentyIdentyfikacyjne = doRepo.dokumentyIdentfikacyjne
  private[repositories] val typyDokumentu = doRepo.typyDokumentu

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

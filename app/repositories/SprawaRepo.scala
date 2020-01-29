package repositories

import java.sql.Timestamp
import java.util

import javax.inject.{Inject, Singleton}
import models.db.SprawaRow
import models.{Kierownik, Sprawa}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SprawaRepo @Inject()(
  val dRepo: DecyzjaRepo,
  val doRepo: DaneOsoboweRepo)
  (dbConfigProvider: DatabaseConfigProvider)
  (implicit ec: ExecutionContext)
  extends Repository[Sprawa] {
  private[repositories] lazy val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig._
  import profile.api._

  private[repositories] class SprawaTable(tag: Tag) extends Table[SprawaRow](tag, "sprawy") {

    def id = column[Long]("id", O.PrimaryKey)

    def zdjecie = column[String]("zdjecie")

    def trescSprawy = column[String]("tresc_sprawy")

    def identyfikator = column[String]("identyfikator")

    def dataUtworzenia = column[Timestamp]("data_utworzenia")

    def czyWyslana = column[Boolean]("czy_wyslana")

    def daneOsoboweId = column[Long]("dane_osobowe")

    def daneOsoboweFK =
      foreignKey("DO_ID", daneOsoboweId, sprawy)(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)

    def aktualnaDecyzjaId = column[Option[Long]]("aktualna_decyzja")

    def aktualnaDecyzja =
      foreignKey("AD_ID", aktualnaDecyzjaId, decyzje)(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)

    def czyZakonczona = column[Boolean]("czy_zakonczona")

    def przesylkaId = column[Option[Long]]("przesylka")

    def przesylkaFk =
      foreignKey("P_ID", przesylkaId, sprawy)(_.id.?, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)

    def * = (id, zdjecie, trescSprawy, identyfikator, dataUtworzenia, czyWyslana, daneOsoboweId, aktualnaDecyzjaId, czyZakonczona, przesylkaId) <> (SprawaRow.tupled, SprawaRow.unapply)
  }

  private[repositories] lazy val sprawy = TableQuery[SprawaTable]
  private[repositories] lazy val dokumentyIdentyfikacyjne = doRepo.dokumentyIdentyfikacyjne
  private[repositories] lazy val daneOsobowe = doRepo.daneOsobowe
  private[repositories] lazy val pracownicy = dRepo.pracownicy
  private[repositories] lazy val typyDokumentu = doRepo.typyDokumentu
  private[repositories] lazy val decyzje = dRepo.decyzje

  def upsert(entity: Sprawa): Future[Boolean] = db.run {
    sprawy
      .insertOrUpdate(entity.toRow)
      .checkSingleRow
  }

  def delete(entity: Sprawa): Future[Boolean] = db.run {
    sprawy
      .filter(_.id === entity.id)
      .delete
      .checkSingleRow
  }

  def get(id: Long): Future[Option[Sprawa]] = db.run {
    sprawy.filter(_.id === id)
      .join(daneOsobowe)
      .on(_.daneOsoboweId === _.id)
      .join(dokumentyIdentyfikacyjne)
      .on(_._2.dokumentIdentyfikacyjnyId === _.id)
      .join(typyDokumentu)
      .on(_._2.typDokumentuId === _.id)
      .joinLeft(decyzje)
      .on(_._1._1._1.aktualnaDecyzjaId === _.id)
      .joinLeft(pracownicy)
      .on(_._2.map(_.wydajacyId) === _.id)
      .result
      .map(_.headOption.flatMap {
        case (((((s, dO), di), td), d), p) =>
          p.fold(
            Some(s.toEntity(dO.toEntity(di.toEntity(td.toEntity)), None))
          )(prac => prac.toEntity match {
            case k: Kierownik =>
              Some(s.toEntity(dO.toEntity(di.toEntity(td.toEntity)), d.map(_.toEntity(k))))
            case _ => None[Sprawa]
          })
      })
  }

  def list(): Future[util.List[Sprawa]] = {
    db.run {
      sprawy
        .join(daneOsobowe)
        .on(_.daneOsoboweId === _.id)
        .join(dokumentyIdentyfikacyjne)
        .on(_._2.dokumentIdentyfikacyjnyId === _.id)
        .join(typyDokumentu)
        .on(_._2.typDokumentuId === _.id)
        .joinLeft(decyzje)
        .on(_._1._1._1.aktualnaDecyzjaId === _.id)
        .joinLeft(pracownicy)
        .on(_._2.map(_.wydajacyId) === _.id)
        .result
        .map(_.flatMap {
          case (((((s, dO), di), td), d), p) =>
            p.fold(
              Some(s.toEntity(dO.toEntity(di.toEntity(td.toEntity)), None))
            )(prac => prac.toEntity match {
              case k: Kierownik =>
                Some(s.toEntity(dO.toEntity(di.toEntity(td.toEntity)), d.map(_.toEntity(k))))
              case _ => None[Sprawa]
            })
        })
    }
  }
}

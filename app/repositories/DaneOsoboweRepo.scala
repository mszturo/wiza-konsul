package repositories

import java.sql.Date
import java.util

import javax.inject.{Inject, Singleton}
import models.DaneOsobowe
import models.db.DaneOsoboweRow
import play.api.db.slick.{DbName, SlickApi}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DaneOsoboweRepo @Inject()(
  val diRepo: DokumentIdentyfikacyjnyRepo)
  (slickApi: SlickApi, dbName: DbName)
  (implicit ec: ExecutionContext)
  extends Repository[DaneOsobowe] {
  private[repositories] val dbConfig = slickApi.dbConfig[JdbcProfile](dbName)

  import dbConfig._
  import profile.api._

  private[repositories] class DaneOsoboweTable(tag: Tag) extends Table[DaneOsoboweRow](tag, "dane_osobowe") {

    def id = column[Long]("id")

    def imie = column[String]("imie")

    def drugieImie = column[Option[String]]("drugie_imie")

    def nazwisko = column[String]("nazwisko")

    def dataUrodzenia = column[Date]("data_urodzenia")

    def miejsceUrodzenia = column[String]("miejsce_urodzenia")

    def pesel = column[String]("pesel")

    def dokumentIdentyfikacyjnyId = column[Long]("dokument_identyfikacyjny")

    def dokumentIdentyfikacyjny =
      foreignKey("DI_ID", dokumentIdentyfikacyjnyId, dokumentyIdentfikacyjne)(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)

    def * = (id, imie, drugieImie, nazwisko, dataUrodzenia, miejsceUrodzenia, pesel, dokumentIdentyfikacyjnyId) <> (DaneOsoboweRow.tupled, DaneOsoboweRow.unapply)
  }

  private[repositories] val daneOsobowe = TableQuery[DaneOsoboweTable]
  private[repositories] val typyDokumentu = diRepo.typyDokumentu
  private[repositories] val dokumentyIdentfikacyjne = diRepo.dokumentyIdentfikacyjne

  def upsert(entity: DaneOsobowe): Future[Boolean] = db.run {
    daneOsobowe
      .insertOrUpdate(entity.toRow)
      .checkSingleRow
  }

  def delete(entity: DaneOsobowe): Future[Boolean] = db.run {
    daneOsobowe
      .filter(_.id === entity.id)
      .delete
      .checkSingleRow
  }

  def get(id: Long): Future[Option[DaneOsobowe]] = db.run {
    daneOsobowe.filter(_.id === id)
      .join(dokumentyIdentfikacyjne)
      .on(_.dokumentIdentyfikacyjnyId === _.id)
      .join(typyDokumentu)
      .on(_._2.typDokumentuId === _.id)
      .result
      .map(_.headOption.map { case ((dO, di), td) => dO.toEntity(di.toEntity(td.toEntity))})
  }

  def list(): Future[util.List[DaneOsobowe]] = db.run {
    daneOsobowe
      .join(dokumentyIdentfikacyjne)
      .on(_.dokumentIdentyfikacyjnyId === _.id)
      .join(typyDokumentu)
      .on(_._2.typDokumentuId === _.id)
      .result
      .map(_.map { case ((dO, di), td) => dO.toEntity(di.toEntity(td.toEntity))})
  }
}

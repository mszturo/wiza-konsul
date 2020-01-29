package repositories

import java.sql.Date
import java.util

import javax.inject.{Inject, Singleton}
import models.DaneOsobowe
import models.db.DaneOsoboweRow
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DaneOsoboweRepo @Inject()(
  val diRepo: DokumentIdentyfikacyjnyRepo)
  (dbConfigProvider: DatabaseConfigProvider)
  (implicit ec: ExecutionContext)
  extends Repository[DaneOsobowe] {
  private[repositories] lazy val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private[repositories] class DaneOsoboweTable(tag: Tag) extends Table[DaneOsoboweRow](tag, "dane_osobowe") {

    def id = column[Long]("id", O.PrimaryKey)

    def imie = column[String]("imie")

    def drugieImie = column[Option[String]]("drugie_imie")

    def nazwisko = column[String]("nazwisko")

    def dataUrodzenia = column[Date]("data_urodzenia")

    def miejsceUrodzenia = column[String]("miejsce_urodzenia")

    def pesel = column[String]("pesel")

    def dokumentIdentyfikacyjnyId = column[Long]("dokument_identyfikacyjny")

    def dokumentIdentyfikacyjny =
      foreignKey("DI_ID", dokumentIdentyfikacyjnyId, dokumentyIdentyfikacyjne)(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)

    def * = (id, imie, drugieImie, nazwisko, dataUrodzenia, miejsceUrodzenia, pesel, dokumentIdentyfikacyjnyId) <> (DaneOsoboweRow.tupled, DaneOsoboweRow.unapply)
  }

  private[repositories] lazy val daneOsobowe = TableQuery[DaneOsoboweTable]
  private[repositories] lazy val typyDokumentu = diRepo.typyDokumentu
  private[repositories] lazy val dokumentyIdentyfikacyjne = diRepo.dokumentyIdentfikacyjne

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
      .join(dokumentyIdentyfikacyjne)
      .on(_.dokumentIdentyfikacyjnyId === _.id)
      .join(typyDokumentu)
      .on(_._2.typDokumentuId === _.id)
      .result
      .map(_.headOption.map { case ((dO, di), td) => dO.toEntity(di.toEntity(td.toEntity))})
  }

  def list(): Future[util.List[DaneOsobowe]] = db.run {
    daneOsobowe
      .join(dokumentyIdentyfikacyjne)
      .on(_.dokumentIdentyfikacyjnyId === _.id)
      .join(typyDokumentu)
      .on(_._2.typDokumentuId === _.id)
      .result
      .map(_.map { case ((dO, di), td) => dO.toEntity(di.toEntity(td.toEntity))})
  }
}

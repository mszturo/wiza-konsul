package repositories

import java.util

import javax.inject.{Inject, Singleton}
import models.DokumentIdentyfikacyjny
import models.db.DokumentIdentyfikacyjnyRow
import play.api.db.slick.{DbName, SlickApi}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DokumentIdentyfikacyjnyRepo @Inject()
  (val tdRepo: TypDokumentuRepo)
  (slickApi: SlickApi, dbName: DbName)
  (implicit ec: ExecutionContext)
  extends Repository [DokumentIdentyfikacyjny] {
  private[repositories] val dbConfig = slickApi.dbConfig[JdbcProfile](dbName)

  import dbConfig._
  import profile.api._

  private[repositories] class DokumentIdentyfikacyjnyTable(tag: Tag) extends Table[DokumentIdentyfikacyjnyRow](tag, "typ_dokumentu") {

    def id = column[Long]("id")

    def nrDokumentu = column[String]("nr_dokumentu")

    def typDokumentuId = column[Long]("typ_dokumentu")
    def typDokumentu =
      foreignKey("TD_ID", typDokumentuId, typyDokumentu)(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)

    def * = (id, nrDokumentu, typDokumentuId) <> (DokumentIdentyfikacyjnyRow.tupled, DokumentIdentyfikacyjnyRow.unapply)
  }

  private[repositories] val dokumentyIdentfikacyjne = TableQuery[DokumentIdentyfikacyjnyTable]
  private[repositories] val typyDokumentu = tdRepo.typyDokumentu

  def upsert(entity: DokumentIdentyfikacyjny): Future[Boolean] = db.run {
    dokumentyIdentfikacyjne
      .insertOrUpdate(entity.toRow)
      .checkSingleRow
  }

  def delete(entity: DokumentIdentyfikacyjny): Future[Boolean] = db.run {
    dokumentyIdentfikacyjne
      .filter(_.id === entity.id)
      .delete
      .checkSingleRow
  }

  def get(id: Long): Future[Option[DokumentIdentyfikacyjny]] = db.run {
    dokumentyIdentfikacyjne.filter(_.id === id)
      .join(typyDokumentu)
      .on(_.typDokumentuId === _.id)
      .result
      .map(_.headOption.map{case (di, td) => di.toEntity(td.toEntity)})
  }

  def list(): Future[util.List[DokumentIdentyfikacyjny]] = db.run {
    dokumentyIdentfikacyjne
      .join(typyDokumentu)
      .on(_.typDokumentuId === _.id)
      .result
      .map(_.map{case (di, td) => di.toEntity(td.toEntity)})
  }
}

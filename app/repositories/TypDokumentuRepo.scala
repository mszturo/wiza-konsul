package repositories

import java.util

import javax.inject.{Inject, Singleton}
import models.TypDokumentu
import models.db.TypDokumentuRow
import play.api.db.slick.{DbName, SlickApi}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TypDokumentuRepo @Inject()
(slickApi: SlickApi, dbName: DbName)
  (implicit ec: ExecutionContext)
  extends Repository[TypDokumentu] {
  private[repositories] val dbConfig = slickApi.dbConfig[JdbcProfile](dbName)

  import dbConfig._
  import profile.api._

  private[repositories] class TypDokumentuTable(tag: Tag) extends Table[TypDokumentuRow](tag, "typ_dokumentu") {

    def id = column[Long]("id")

    def nazwa = column[String]("name")

    def * = (id, nazwa) <> (TypDokumentuRow.tupled, TypDokumentuRow.unapply)
  }


  def upsert(entity: TypDokumentu): Future[Boolean] = db.run {
    typyDokumentu
      .insertOrUpdate(entity.toRow)
      .checkSingleRow
  }

  def delete(entity: TypDokumentu): Future[Boolean] = db.run {
    typyDokumentu
      .filter(_.id === entity.id)
      .delete
      .checkSingleRow
  }

  def get(id: Long): Future[Option[TypDokumentu]] = db.run {
    typyDokumentu.filter(_.id === id)
      .result
      .map(_.headOption.map(_.toEntity))
  }

  def list(): Future[util.List[TypDokumentu]] = db.run {
    typyDokumentu
      .result
      .map(_.map(_.toEntity))
  }

  private[repositories] val typyDokumentu = TableQuery[TypDokumentuTable]

}
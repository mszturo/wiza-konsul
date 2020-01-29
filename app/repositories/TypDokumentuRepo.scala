package repositories

import java.util

import javax.inject.{Inject, Singleton}
import models.TypDokumentu
import models.db.TypDokumentuRow
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TypDokumentuRepo @Inject()
(dbConfigProvider: DatabaseConfigProvider)
  (implicit ec: ExecutionContext)
  extends Repository[TypDokumentu] {
  private[repositories] lazy val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private[repositories] class TypDokumentuTable(tag: Tag) extends Table[TypDokumentuRow](tag, "typy_dokumentu") {
    def id = column[Long]("id", O.PrimaryKey)

    def nazwa = column[String]("name")

    def * = (id, nazwa) <> (TypDokumentuRow.tupled, TypDokumentuRow.unapply)
  }


  def upsert(entity: TypDokumentu): Future[Option[Long]] = db.run {
    (typyDokumentu returning typyDokumentu.map(_.id))
      .insertOrUpdate(entity.toRow)
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

  private[repositories] lazy val typyDokumentu = TableQuery[TypDokumentuTable]
}
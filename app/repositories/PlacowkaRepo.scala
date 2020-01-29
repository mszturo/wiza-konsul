package repositories

import java.util

import javax.inject.{Inject, Singleton}
import models.Placowka
import models.db.PlacowkaRow
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PlacowkaRepo @Inject()
(dbConfigProvider: DatabaseConfigProvider)
  (implicit ec: ExecutionContext)
  extends Repository[Placowka] {
  private[repositories] lazy val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig._
  import profile.api._

  private[repositories] class PlacowkaTable(tag: Tag) extends Table[PlacowkaRow](tag, "placowki") {

    def id = column[Long]("id", O.PrimaryKey)

    def nazwa = column[String]("nazwa")

    def maksSprawZagregowanych = column[Int]("maks_spraw_zagregowanych")

    def * = (id, nazwa, maksSprawZagregowanych) <> (PlacowkaRow.tupled, PlacowkaRow.unapply)
  }


  def upsert(entity: Placowka): Future[Option[Long]] = db.run {
    (placowki returning placowki.map(_.id))
      .insertOrUpdate(entity.toRow)
  }

  def delete(entity: Placowka): Future[Boolean] = db.run {
    placowki
      .filter(_.id === entity.id)
      .delete
      .checkSingleRow
  }

  def get(id: Long): Future[Option[Placowka]] = db.run {
    placowki.filter(_.id === id)
      .result
      .map(_.headOption.map(_.toEntity))
  }

  def list(): Future[util.List[Placowka]] = db.run {
    placowki
      .result
      .map(_.map(_.toEntity))
  }

  private[repositories] lazy val placowki = TableQuery[PlacowkaTable]
}
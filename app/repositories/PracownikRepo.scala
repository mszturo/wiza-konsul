package repositories

import java.util

import javax.inject.{Inject, Singleton}
import models.Pracownik
import models.db.PracownikRow
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PracownikRepo @Inject()
(dbConfigProvider: DatabaseConfigProvider)
  (implicit ec: ExecutionContext)
  extends Repository[Pracownik] {
  private[repositories] lazy val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig._
  import profile.api._

  private[repositories] class PracownikTable(tag: Tag) extends Table[PracownikRow](tag, "pracownicy") {

    def id = column[Long]("id", O.PrimaryKey)

    def login = column[String]("login")

    def haslo = column[String]("haslo")

    def czyKierownik = column[Boolean]("czy_kierownik")

    def * = (id, login, haslo, czyKierownik) <> (PracownikRow.tupled, PracownikRow.unapply)
  }

  private[repositories] lazy val pracownicy = TableQuery[PracownikTable]

  def upsert(entity: Pracownik): Future[Boolean] = db.run {
    pracownicy
      .insertOrUpdate(entity.toRow)
      .checkSingleRow
  }

  def delete(entity: Pracownik): Future[Boolean] = db.run {
    pracownicy
      .filter(_.id === entity.id)
      .delete
      .checkSingleRow
  }

  def get(id: Long): Future[Option[Pracownik]] = db.run {
    pracownicy.filter(_.id === id)
      .result
      .map(_.headOption.map(_.toEntity))
  }

  def list(): Future[util.List[Pracownik]] = db.run {
    pracownicy
      .result
      .map(_.map(_.toEntity))
  }
}

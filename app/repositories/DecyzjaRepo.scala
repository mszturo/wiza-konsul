package repositories

import java.sql.Date
import java.util

import javax.inject.{Inject, Singleton}
import models.RodzajDecyzji.RodzajDecyzji
import models.db.DecyzjaRow
import models.{Decyzja, Kierownik}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DecyzjaRepo @Inject()
(val pRepo: PracownikRepo)
  (dbConfigProvider: DatabaseConfigProvider)
  (implicit ec: ExecutionContext)
  extends Repository[Decyzja] {
  private[repositories] lazy val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private[repositories] class DecyzjaTable(tag: Tag) extends Table[DecyzjaRow](tag, "decyzje") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def dataDecyzji = column[Date]("data_decyzji")

    def uzasadnienie = column[String]("uzasadnienie")

    def wydajacyId = column[Long]("wydajacy")

    def wydajacy =
      foreignKey("W_ID", wydajacyId, pracownicy)(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)

    def rodzajDecyzji = column[RodzajDecyzji]("rodzaj_decyzji")

    def * = (id, dataDecyzji, uzasadnienie, wydajacyId, rodzajDecyzji) <> (DecyzjaRow.tupled, DecyzjaRow.unapply)
  }

  private[repositories] lazy val decyzje = TableQuery[DecyzjaTable]
  private[repositories] lazy val pracownicy = pRepo.pracownicy

  def upsert(entity: Decyzja): Future[Option[Long]] = db.run {
    (decyzje returning decyzje.map(_.id)).insertOrUpdate(entity.toRow)
  }

  def delete(entity: Decyzja): Future[Boolean] = db.run {
    decyzje
      .filter(_.id === entity.id)
      .delete
      .checkSingleRow
  }

  def get(id: Long): Future[Option[Decyzja]] = db.run {
    decyzje.filter(_.id === id)
      .join(pracownicy)
      .on(_.wydajacyId === _.id)
      .result
      .map(_.headOption.flatMap {
        case (d, p) => p.toEntity match {
          case k: Kierownik =>
            Some(d.toEntity(k))
          case _ => None
        }
      })
  }

  def list(): Future[util.List[Decyzja]] = db.run {
    decyzje
      .join(pracownicy)
      .on(_.wydajacyId === _.id)
      .result
      .map(_.flatMap {
        case (d, p) => p.toEntity match {
          case k: Kierownik =>
            Some(d.toEntity(k))
          case _ => None
        }
      })
  }
}

package repositories

import java.util

import slick.dbio.{DBIOAction, NoStream}

import scala.concurrent.{ExecutionContext, Future}
import scala.jdk.CollectionConverters._
import scala.language.implicitConversions

trait Repository[T] {
  def upsert(entity: T): Future[Boolean]

  def delete(entity: T): Future[Boolean]

  def get(id: Long): Future[Option[T]]

  def list(): Future[util.List[T]]

  final implicit def seqToJava(seq: Seq[T]): util.List[T] = {
    seq.asJava
  }

  final implicit def iterableToJava(it: Iterable[T]): util.List[T] = {
    it.toList.asJava
  }

  final implicit class IntBoolMapper[+R,+S <: slick.dbio.NoStream,-E <: slick.dbio.Effect](dbio: DBIOAction[R, S, E]) {
    def checkSingleRow(implicit ec: ExecutionContext): DBIOAction[Boolean, NoStream, E] = dbio
      .map {
        case 1 => true
        case _ => false
      }
  }

}

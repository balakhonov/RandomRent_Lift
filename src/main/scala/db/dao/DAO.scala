package db.dao

import java.lang.reflect.ParameterizedType

import scala.collection.JavaConversions.asScalaBuffer

import org.hibernate.Session
import org.hibernate.Transaction

import db.HibernateUtil

class DAO[T] {

  /**
   *
   */
  var tp: Class[T] = getClass().getGenericSuperclass().asInstanceOf[ParameterizedType].getActualTypeArguments()(0).asInstanceOf[Class[T]]

  /**
   *
   */
  def execute[T](fun: (Session) => T): T = {
    var tx: Transaction = null;
    var session = HibernateUtil.openSession();

    try {
      tx = session.beginTransaction()

      var res: T = fun(session)

      tx.commit()

      return res
    } catch {
      case e: Exception => {
        if (tx != null) {
          tx.rollback()
        }
        throw new Exception(e)
      }
    } finally {
      if (session != null) {
        session.close()
      }
    }
  }

  /**
   *
   */
  def get(id: Int): T = {
    if (id < 0) {
      throw new IllegalArgumentException("ID(" + id + ") should not be < 0");
    }

    execute(s => s.get(tp, id).asInstanceOf[T])
  }

  /**
   *
   */
  def save(entry: T): T = {
    if (entry == null) {
      throw new IllegalArgumentException(tp.getSimpleName() + " should not be null");
    }

    execute(s => s.save(entry).asInstanceOf[T])
  }

  /**
   *
   */
  def update(entry: T): T = {
    if (entry == null) {
      throw new IllegalArgumentException(tp.getSimpleName() + " should not be null");
    }

    execute(s => s.update(entry).asInstanceOf[T])
  }

  /**
   *
   */
  def getAll(): List[T] = {

    def load(session: Session): List[T] = {
      var query = session.createQuery("from " + tp.getSimpleName())
      query.list().asInstanceOf[java.util.List[T]].toList
    }

    execute(s => load(s))
  }
}
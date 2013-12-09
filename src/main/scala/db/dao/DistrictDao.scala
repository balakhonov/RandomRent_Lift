package db.dao

import org.hibernate.Session
import db.mapping.Country
import db.mapping.City
import db.mapping.District

object DistrictDao extends DAO[District] {

  /**
   *
   */
  def getList(parentId: Int): List[District] = {
    if (parentId < 1) {
      throw new IllegalArgumentException("Parent ID(" + parentId + ") should not be < 1")
    }

    var list = List[District]()

    def process(session: Session): List[District] = {
      var query = session.createQuery("FROM District WHERE cityId = :parentId")
      query.setParameter("parentId", parentId)

      var it = query.list().iterator()
      while (it.hasNext()) {
        var el = it.next().asInstanceOf[District]
        list ::= el
      }

      return list
    }

    execute(s => process(s))
  }

  def count(parentId: Int): Long = {
    if (parentId < 1) {
      throw new IllegalArgumentException("Parent ID(" + parentId + ") should not be < 1")
    }

    var list = List[District]()

    def process(session: Session): Long = {
      var query = session.createQuery("SELECT count(*) FROM District WHERE cityId = :parentId")
      query.setParameter("parentId", parentId)

      var count = query.uniqueResult().asInstanceOf[Long];
      return count;
    }

    execute(s => process(s))
  }
}
package db.dao

import db.mapping.Province
import org.hibernate.Session

object ProvinceDao extends DAO[Province] {

  /**
   *
   */
  def getList(parentId: Int): List[Province] = {
    if (parentId < 1) {
      throw new IllegalArgumentException("Parent ID(" + parentId + ") should not be < 1")
    }

    var list = List[Province]()

    def process(session: Session): List[Province] = {
      var query = session.createQuery("FROM Province WHERE countryId = :parentId")
      query.setParameter("parentId", parentId)

      var it = query.list().iterator()
      while (it.hasNext()) {
        var el = it.next().asInstanceOf[Province]
        list ::= el
      }

      return list
    }

    execute(s => process(s))
  }
}
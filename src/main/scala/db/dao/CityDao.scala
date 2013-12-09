package db.dao

import org.hibernate.Session
import db.mapping.Country
import db.mapping.City

object CityDao extends DAO[City] {

  /**
   *
   */
  def getList(parentId: Int): List[City] = {
    if (parentId < 1) {
      throw new IllegalArgumentException("Parent ID(" + parentId + ") should not be < 1")
    }

    var list = List[City]()

    def process(session: Session): List[City] = {
      var query = session.createQuery("FROM City WHERE provinceId = :parentId")
      query.setParameter("parentId", parentId)

      var it = query.list().iterator()
      while (it.hasNext()) {
        var el = it.next().asInstanceOf[City]
        list ::= el
      }

      return list
    }

    execute(s => process(s))
  }

  /**
   *
   */
  def getByCountry(parentId: Int): List[City] = {
    if (parentId < 1) {
      throw new IllegalArgumentException("Parent ID(" + parentId + ") should not be < 1")
    }

    var list = List[City]()

    def process(session: Session): List[City] = {
      var query = session.createQuery("FROM City WHERE countryId = :parentId")
      query.setParameter("parentId", parentId)

      var it = query.list().iterator()
      while (it.hasNext()) {
        var el = it.next().asInstanceOf[City]
        list ::= el
      }

      return list
    }

    execute(s => process(s))
  }

  /**
   *
   */
  def getCity(countryId: Int, name: String): City = {
    if (countryId < 1) {
      throw new IllegalArgumentException("Country ID(" + countryId + ") should not be < 1")
    }
    if (name == null || name.isEmpty) {
      throw new IllegalArgumentException("City name should not be null or empty")
    }

    def process(session: Session): City = {
      var query = session.createQuery("FROM City WHERE countryId = :countryId AND name = :name")
      query.setParameter("countryId", countryId)
      query.setParameter("name", name)

      query.uniqueResult().asInstanceOf[City];
    }

    execute(s => process(s))
  }
}
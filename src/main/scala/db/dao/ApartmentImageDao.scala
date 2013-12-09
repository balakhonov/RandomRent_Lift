package db.dao

import org.hibernate.Session
import db.mapping.Country
import db.mapping.City
import db.mapping.ApartmentImage

object ApartmentImageDao extends DAO[ApartmentImage] {

  def setReferences(idList: List[Int], apartmentId: Int): Int = {
    if (apartmentId < 1) {
      throw new IllegalArgumentException("Apartment ID(%d) should not be < 1".format(apartmentId))
    }

    if (idList.length < 1) return 0

    var count = 0;

    var list = idList.map { i => new Integer(i).asInstanceOf[Object] }.toArray

    def process(session: Session) = {
      var query = session.createQuery("UPDATE ApartmentImage SET apartmentId = :apartmentId WHERE id IN (:ids)")
      query.setParameterList("ids", list)
      query.setParameter("apartmentId", apartmentId)

      count = query.executeUpdate();
    }

    execute(s => process(s))

    count
  }

  /**
   *
   */
  def getList(parentId: Int): List[ApartmentImage] = {
    if (parentId < 0) {
      throw new IllegalArgumentException("Parent ID(" + parentId + ") should not be < 0")
    }

    var list = List[ApartmentImage]()

    def process(session: Session): List[ApartmentImage] = {
      var query = session.createQuery("FROM ApartmentImage WHERE apartmentId = :parentId")
      query.setParameter("parentId", parentId)

      var it = query.list().iterator()
      while (it.hasNext()) {
        var el = it.next().asInstanceOf[ApartmentImage]
        list ::= el
      }

      return list
    }

    execute(s => process(s))
  }
}
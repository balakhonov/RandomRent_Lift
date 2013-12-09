package db.dao

import org.hibernate.Session

import db.mapping.Apartment

object ApartmentDao extends DAO[Apartment] {

  /**
   *
   */
  def getApartments(districtId: Int, limit: Int, offset: Int): List[Apartment] = {
    if (districtId < 1) {
      throw new IllegalArgumentException("District ID(" + districtId + ") should not be < 1");
    }
    if (limit < 0) {
      throw new IllegalArgumentException("Limit(" + limit + ") should not be < 0");
    }
    if (offset < 0) {
      throw new IllegalArgumentException("Offset(" + offset + ") should not be < 0");
    }

    var list = List[Apartment]()
    var sb = new StringBuilder()
    sb.append("FROM Apartment m ")
    sb.append("WHERE m.districtId = :districtId ")
    sb.append("ORDER BY m.id DESC ")

    def process(session: Session): List[Apartment] = {
      var query = session.createQuery(sb.toString())
      query.setParameter("districtId", districtId)
      query.setFirstResult(offset)
      query.setMaxResults(limit)

      var it = query.list().iterator()
      while (it.hasNext()) {
        var m = it.next().asInstanceOf[Apartment];
        list ::= m;
      }

      return list
    }

    execute(s => process(s))
  }

  /**
   *
   */
  def getApartments(limit: Int, offset: Int): List[Apartment] = {
    if (limit < 0) {
      throw new IllegalArgumentException("Limit(" + limit + ") should not be < 0");
    }

    var list = List[Apartment]()
    var sb = new StringBuilder();
    sb.append("FROM Apartment m ");
    sb.append("ORDER BY m.id DESC ");

    def process(session: Session): List[Apartment] = {
      var query = session.createQuery(sb.toString());
      query.setMaxResults(limit);
      query.setFirstResult(offset);

      var it = query.list().iterator();
      while (it.hasNext()) {
        var m = it.next().asInstanceOf[Apartment];
        list ::= m;
      }

      return list;
    }
    execute(s => process(s))
  }

  /**
   *
   * @param limit
   * @param offset
   * @param adTypeId
   * @param periodId
   * @param roomId
   * @param furnished
   * @param airConditioning
   * @param fridge
   * @param cableTelevision
   * @param washer
   * @param fireplace
   * @param internet
   * @param boiler
   * @param nearbyParking
   * @return
   */
  def getApartments(limit: Int, offset: Int, cityId: Int, districtId: Int,
    adTypeId: Int, periodId: Int, priceFrom: Int, priceTo: Int, roomIds: java.util.List[Integer],
    furnished: Boolean, airConditioning: Boolean, fridge: Boolean, cableTelevision: Boolean,
    washer: Boolean, fireplace: Boolean, internet: Boolean, boiler: Boolean,
    nearbyParking: Boolean): List[Apartment] = {
    if (limit < 0) {
      throw new IllegalArgumentException("Limit(" + limit + ") should not be < 0");
    }

    var list = List[Apartment]();
    var sb = new StringBuilder();
    sb.append("FROM Apartment m ");
    sb.append("WHERE m.cityId = " + cityId);
    sb.append(" AND m.price >= " + priceFrom);
    sb.append(" AND m.price <= " + priceTo);
    if (districtId != 0)
      sb.append(" AND m.districtId =" + districtId);
    if (adTypeId != 0)
      sb.append(" AND m.adType =" + adTypeId);
    if ((adTypeId != 3 || adTypeId != 4) && periodId != 0)
      sb.append(" AND m.period =" + periodId);
    if (roomIds.size() > 0)
      sb.append(" AND m.roomTypeId in (:ids)");
    if (furnished)
      sb.append(" AND m.furnished =" + furnished);
    if (airConditioning)
      sb.append(" AND m.airConditioning =" + airConditioning);
    if (fridge)
      sb.append(" AND m.fridge =" + fridge);
    if (cableTelevision)
      sb.append(" AND m.cableTelevision =" + cableTelevision);
    if (washer)
      sb.append(" AND m.washer =" + washer);
    if (fireplace)
      sb.append(" AND m.fireplace =" + fireplace);
    if (internet)
      sb.append(" AND m.internet =" + internet);
    if (boiler)
      sb.append(" AND m.boiler =" + boiler);
    if (nearbyParking)
      sb.append(" AND m.nearbyParking =" + nearbyParking);

    sb.append(" ORDER BY m.id DESC ");

    def process(session: Session): List[Apartment] = {
      var query = session.createQuery(sb.toString());
      query.setMaxResults(limit);
      query.setFirstResult(offset);
      if (roomIds.size() > 0)
        query.setParameterList("ids", roomIds);

      var it = query.list().iterator();
      while (it.hasNext()) {
        var m = it.next().asInstanceOf[Apartment];
        list ::= m;
      }
      return list.reverse;
    }
    execute(s => process(s))
  }

  /**
   *
   */
  def getApartmentsCount(cityId: Int, districtId: Int, adTypeId: Int, periodId: Int,
    priceFrom: Int, priceTo: Int, roomIds: java.util.List[Integer], furnished: Boolean,
    airConditioning: Boolean, fridge: Boolean, cableTelevision: Boolean, washer: Boolean,
    fireplace: Boolean, internet: Boolean, boiler: Boolean, nearbyParking: Boolean): Long = {

    var sb = new StringBuilder();
    sb.append("SELECT count(*) FROM Apartment m ");
    sb.append("WHERE m.cityId = " + cityId);
    sb.append(" AND m.price >= " + priceFrom);
    sb.append(" AND m.price <= " + priceTo);
    if (districtId != 0)
      sb.append(" AND m.districtId =" + districtId);
    if (adTypeId != 0)
      sb.append(" AND m.adType =" + adTypeId);
    if ((adTypeId != 3 || adTypeId != 4) && periodId != 0)
      sb.append(" AND m.period =" + periodId);
    if (roomIds.size() > 0)
      sb.append(" AND m.roomTypeId in (:ids)");
    if (furnished)
      sb.append(" AND m.furnished =" + furnished);
    if (airConditioning)
      sb.append(" AND m.airConditioning =" + airConditioning);
    if (fridge)
      sb.append(" AND m.fridge =" + fridge);
    if (cableTelevision)
      sb.append(" AND m.cableTelevision =" + cableTelevision);
    if (washer)
      sb.append(" AND m.washer =" + washer);
    if (fireplace)
      sb.append(" AND m.fireplace =" + fireplace);
    if (internet)
      sb.append(" AND m.internet =" + internet);
    if (boiler)
      sb.append(" AND m.boiler =" + boiler);
    if (nearbyParking)
      sb.append(" AND m.nearbyParking =" + nearbyParking);

    sb.append(" ORDER BY m.id DESC ");

    def process(session: Session): Long = {
      var query = session.createQuery(sb.toString());
      if (roomIds.size() > 0)
        query.setParameterList("ids", roomIds);

      var count = query.uniqueResult().asInstanceOf[Long];

      return count;
    }

    execute(s => process(s))
  }
}
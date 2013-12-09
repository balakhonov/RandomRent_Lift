package db.dao

import org.junit.Test
import scala.collection.mutable.ArrayBuffer
import scala.xml.Elem
import scala.xml.Node
import db.mapping.City
import java.lang.Double
import db.mapping.Province
import db.mapping.Country

class ApartmentImageDaoTest {

  @Test
  def setReferencesTest() = {
    var idList = List(1, 2, 3, 4)
    var count = ApartmentImageDao.setReferences(idList, 1)
    println("Count: " + count)
  }

  @Test
  def setReferencesTest2(): Unit = {

    var country = new Country()
    country.setId(1)

    val xml: Node = null

    if (xml == null)
      return

    var regions = xml.child.asInstanceOf[ArrayBuffer[Node]]

    for (region <- regions) {
      var nameSeq = (region.attribute("name") toList)

      if (!nameSeq.isEmpty) {
        var name = nameSeq.head.toString
        var province = new Province()
        province.setName(name)
        province.setCountry(country)

        //        ProvinceDao.save(province)

        println("=====" + name)

        var cities = region.child.asInstanceOf[ArrayBuffer[Node]]
        for (el <- cities) {
          var latSeq = (el.attribute("lat") toList)
          var lonSeq = (el.attribute("lon") toList)
          var nameSeq = (el.attribute("name") toList)

          if (!nameSeq.isEmpty) {
            var lat = Double.parseDouble(latSeq.head.toString())
            var lon = Double.parseDouble(lonSeq.head.toString())
            var name = nameSeq.head.toString
            println(lat + " " + lon + " " + name)

            var city = new City()
            city.setName(name);
            city.setLat(lat);
            city.setLon(lon);
            city.setCountry(country)
            city.setProvince(province)
            //            CityDao.save(city)
          }
        }
      }
    }
  }
}
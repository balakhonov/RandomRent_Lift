package code.snippet.site.main

import db.mapping.City
import db.dao.CityDao
import scala.xml.Elem
import scala.xml.Null
import scala.xml.NodeSeq
import scala.xml.Text

object MainPage {

  def citiesGroup = {

    def li(c: City) = {
      <li><a href={ "/ua/" + c.getName }>{ c.getName }</a></li>
    }

    def ul(provinceId: Int, list: List[Elem]) = {
      <ul id={ "more-cities-region-" + provinceId } class="more-cities-pop-up">
        <li class="title">Другие города:</li>
        { list }
      </ul>
    }

    CityDao.getByCountry(1).groupBy(_.getProvinceId).map(e => ul(e._1, e._2.sortBy(_.getName).map(li)))
  }
}
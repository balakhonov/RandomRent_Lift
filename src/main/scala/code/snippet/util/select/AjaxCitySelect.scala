package code.snippet.util.select

import net.liftweb.http.js.JsCmd
import db.mapping.Country
import net.liftweb.http.S
import net.liftweb.common.Empty
import net.liftweb.common.Full
import net.liftweb.util.Helpers.strToCssBindPromoter
import net.liftweb.http.SHtml.ajaxSelectObj
import db.dao.CountryDao
import balakhonov.util.RequestParametersUtil.P
import db.mapping.City
import db.dao.CityDao
import scala.xml.Elem
import net.liftweb.common.Box

object AjaxCitySelect {

  private def getCitiesSelectDataByProvice(withFirstEmpty: Boolean, proinceId: Int) = {
    var list: List[(City, String)] = CityDao.getList(proinceId).sortBy(_.getName).map(a => (a, a.getName))
    if (withFirstEmpty)
      list ::= (new City(), "")

    var selected = if (list.isEmpty) Empty else {
      var selectedElems = list.filter(_._1.getId == P.ciid)
      var elem = if (selectedElems.isEmpty) null else selectedElems(0)._1
      Full(elem)
    }
    (selected, list)
  }

  private def getCitiesSelectData(withFirstEmpty: Boolean) = {
    getCitiesSelectDataByProvice(withFirstEmpty, P.pid)
  }

  def select(withFirstEmpty: Boolean, func: (City) => JsCmd) = {
    var listSelect = getCitiesSelectData(withFirstEmpty)

    "#city-list" #> ajaxSelectObj[City](listSelect._2, listSelect._1, (el: City) => func(el))
  }

  def selectHtml(withFirstEmpty: Boolean, func: (City) => JsCmd): Elem = {
    var listSelect = getCitiesSelectData(withFirstEmpty)

    ajaxSelectObj[City](listSelect._2, listSelect._1, (el: City) => func(el))
  }

  def selectHtml(proinceId: Int, withFirstEmpty: Boolean, func: (City) => JsCmd): Elem = {
    var listSelect = getCitiesSelectDataByProvice(withFirstEmpty, proinceId)

    ajaxSelectObj[City](listSelect._2, listSelect._1, (el: City) => func(el), "id" -> "city-list")
  }
}
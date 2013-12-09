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
import db.mapping.District
import db.dao.DistrictDao
import scala.xml.Elem
import net.liftweb.common.Box

object AjaxDistrictSelect {

  private def getSelectDataByCity(withFirstEmpty: Boolean, cityId: Int) = {
    var list: List[(District, String)] = DistrictDao.getList(cityId).sortBy(_.getName).map(a => (a, a.getName))
    if (withFirstEmpty)
      list ::= (new District(), "")

    var selected = if (list.isEmpty) Empty else {
      var selectedElems = list.filter(_._1.getId == P.dis)
      var elem = if (selectedElems.isEmpty) null else selectedElems(0)._1
      Full(elem)
    }
    (selected, list)
  }

  private def getSelectData(withFirstEmpty: Boolean) = {
    getSelectDataByCity(withFirstEmpty, P.ciid)
  }

  def select(withFirstEmpty: Boolean, func: (District) => JsCmd) = {
    var listSelect = getSelectData(withFirstEmpty)

    "#district-list" #> ajaxSelectObj[District](listSelect._2, listSelect._1, (el: District) => func(el))
  }

  def selectHtml(withFirstEmpty: Boolean, func: (District) => JsCmd): Elem = {
    var listSelect = getSelectData(withFirstEmpty)

    ajaxSelectObj[District](listSelect._2, listSelect._1, (el: District) => func(el))
  }

  def selectHtml(cityId: Int, withFirstEmpty: Boolean, func: (District) => JsCmd): Elem = {
    var listSelect = getSelectDataByCity(withFirstEmpty, cityId)

    ajaxSelectObj[District](listSelect._2, listSelect._1, (el: District) => func(el), "id" -> "district-list")
  }
}
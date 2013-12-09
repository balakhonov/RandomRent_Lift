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
import db.mapping.Province
import db.dao.ProvinceDao
import scala.xml.Elem

object AjaxProvinceSelect {

  private def getProvinceSelectDataByCountry(withFirstEmpty: Boolean, func2: (Int) => Any) = {
    getProvinceSelectData(withFirstEmpty, P.cid, func2)
  }

  private def getProvinceSelectData(withFirstEmpty: Boolean, countryId: Int, func2: (Int) => Any) = {
    var list: List[(Province, String)] = ProvinceDao.getList(countryId).sortBy(_.getName).map(a => (a, a.getName))
    if (withFirstEmpty)
      list ::= (new Province(), "")

    var selectedId = 0;
    var selected = if (list.isEmpty) {
      Empty
    } else {
      var selectedElems = list.filter(_._1.getId == P.pid)
      var elem = if (selectedElems.isEmpty) {
        selectedId = list(0)._1.getId
        null
      } else {
        selectedId = selectedElems(0)._1.getId
        selectedElems(0)._1
      }

      Full(elem)
    }

    if (func2 != null) {
      func2(selectedId)
    }

    (selected, list)
  }

  def select(withFirstEmpty: Boolean, func: (Province) => JsCmd, func2: (Int) => Any) = {
    var listSelect = getProvinceSelectDataByCountry(withFirstEmpty, func2)

    "#province-list" #> ajaxSelectObj[Province](listSelect._2, listSelect._1, (el: Province) => func(el))
  }

  def selectHtml(withFirstEmpty: Boolean, func: (Province) => JsCmd, func2: (Int) => Any): Elem = {
    var listSelect = getProvinceSelectDataByCountry(withFirstEmpty, func2)

    ajaxSelectObj[Province](listSelect._2, listSelect._1, (el: Province) => func(el))
  }
}
package code.snippet.admin.district

import scala.xml.NodeSeq
import scala.xml.Text
import scala.xml.Unparsed
import balakhonov.util.RequestParametersUtil.P
import db.dao.CountryDao
import db.dao.ProvinceDao
import db.dao.CityDao
import db.dao.DistrictDao
import db.mapping.Country
import db.mapping.Province
import db.mapping.City
import db.mapping.District
import net.liftweb.common.Empty
import net.liftweb.common.Full
import net.liftweb.http.RequestVar
import net.liftweb.http.S
import net.liftweb.http.SHtml.ajaxSelectObj
import net.liftweb.http.SHtml.hidden
import net.liftweb.http.SHtml.span
import net.liftweb.http.SHtml.text
import net.liftweb.http.js.JsCmd
import net.liftweb.util.Helpers.AttrBindParam
import net.liftweb.util.Helpers.BindParam
import net.liftweb.util.Helpers.bind
import net.liftweb.util.Helpers.strToCssBindPromoter
import net.liftweb.util.Helpers.strToSuperArrowAssoc
import code.snippet.util.select.AjaxCountrySelect
import code.snippet.util.select.AjaxProvinceSelect
import code.snippet.util.select.AjaxCitySelect
import code.snippet.util.select.AjaxCitySelect

object PageDistrict {
  private val districtsPageUrl = "/admin/districts";

  def isCountryFound(html: NodeSeq) =
    if (P.country != null) html else NodeSeq.Empty

  def isProvinceFound(html: NodeSeq) =
    if (P.province != null) html else NodeSeq.Empty

  def isCityFound(html: NodeSeq) =
    if (P.city != null) html else NodeSeq.Empty

  def isDistrictFound(html: NodeSeq) =
    if (P.district != null) html else NodeSeq.Empty

  /**
   * Countries select field
   */
  def countries = {
    AjaxCountrySelect.select(c => S.redirectTo(districtsPageUrl + P.genUrlParam(c.getId, 0, 0, 0)))
  }

  /**
   * Provinces select field
   */
  def provinces = {
    AjaxProvinceSelect.select(true, p => S.redirectTo(districtsPageUrl + P.genUrlParam(P.cid, p.getId, 0, 0)), null)
  }

  /**
   * Provinces select field
   */
  def cities = {
    AjaxCitySelect.select(true, c => S.redirectTo(districtsPageUrl + P.genUrlParam(P.cid, P.pid, c.getId, 0)))
  }

  /**
   *
   */
  def districts(in: NodeSeq): NodeSeq = {

    def getTypeSeq(id: Int, name: String): List[BindParam] = {
      var clazz = if (id == P.dis) "active"; else "non-active"

      var bindParamList: List[BindParam] = List[BindParam]();
      bindParamList ::= ("name" -> name)
      bindParamList ::= AttrBindParam("href", Text(districtsPageUrl + P.genUrlParam(P.cid, P.pid, P.ciid, id)), "href")
      bindParamList ::= AttrBindParam("class", Text(clazz), "class")

      bindParamList
    }

    DistrictDao.getList(P.ciid).flatMap { el => bind("foo", in, getTypeSeq(el.getId(), el.getName()): _*) } toSeq
  }

  def addDistrictButton(in: NodeSeq): NodeSeq = {
    bind("foo", in, AttrBindParam("href", Text("/admin/addDistrict" + P.genUrlParam(0, 0, P.ciid, 0)), "href"))
  }

  /**
   *
   */
  def addForm = {

    if (P.city == null) {
      S.redirectTo("404")
    }

    var name = ""
    var city = new City(P.city);
    var province = ProvinceDao.get(city.getProvinceId())
    var country = CountryDao.get(province.getCountryId)

    def process(): NodeSeq = {
      var district = new District()
      district.setName(name)
      district.setCity(city)

      DistrictDao.save(district)

      var param = P.genUrlParam(country.getId, province.getId, city.getId, district.getId)
      S.redirectTo(districtsPageUrl + param)
    }

    "#country-name" #> span(Unparsed(country.getName), {}) &
      "#province-name" #> span(Unparsed(province.getName), {}) &
      "#city-name" #> span(Unparsed(city.getName), {}) &
      "name=district-name" #> (text(name, s => (name = s)) ++ hidden(process))
  }

  /**
   *
   */
  def updateForm = {

    if (P.district == null) {
      S.redirectTo("404")
    }

    var district = new District(P.district)
    var city = new City(P.city)
    var country = new Country(P.country)
    var name = district.getName

    def process(): NodeSeq = {
      district.setName(name)
      district.setCity(city)

      DistrictDao.update(district)

      var param = P.genUrlParam(country.getId, city.getProvinceId, city.getId, district.getId)
      S.redirectTo(districtsPageUrl + param)
    }

    "name=district-name" #> (text(name, s => (name = s)) ++ hidden(process))

  }
}
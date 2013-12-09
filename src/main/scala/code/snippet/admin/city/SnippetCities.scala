package code.snippet.admin.city

import scala.xml.NodeSeq
import scala.xml.Text
import scala.xml.Unparsed
import balakhonov.util.RequestParametersUtil.P
import db.dao.CountryDao
import db.dao.ProvinceDao
import db.mapping.Country
import db.mapping.Province
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
import db.dao.CityDao
import db.mapping.City
import code.snippet.util.select.AjaxCountrySelect
import code.snippet.util.select.AjaxProvinceSelect

object SnippetCities {

  def isCountryFound(html: NodeSeq) =
    if (P.country != null) html else NodeSeq.Empty

  def isProvinceFound(html: NodeSeq) =
    if (P.province != null) html else NodeSeq.Empty

  def isCityFound(html: NodeSeq) =
    if (P.city != null) html else NodeSeq.Empty

  /**
   * Countries select field
   */
  def countries = {
    AjaxCountrySelect.select(c => S.redirectTo("/admin/cities?cid=" + c.getId()))
  }

  /**
   * Provinces select field
   */
  def provinces = {
    AjaxProvinceSelect.select(true, p => S.redirectTo("/admin/cities?cid=" + P.cid + "&pid=" + p.getId()), null)
  }

  /**
   *
   */
  def cities(in: NodeSeq): NodeSeq = {

    def getTypeSeq(id: Int, name: String): List[BindParam] = {
      var clazz = if (id == P.ciid) "active"; else "non-active"

      var bindParamList: List[BindParam] = List[BindParam]();
      bindParamList ::= ("name" -> name)
      bindParamList ::= AttrBindParam("href", Text("/admin/cities?cid=" + P.cid + "&pid=" + P.pid + "&ciid=" + id), "href")
      bindParamList ::= AttrBindParam("class", Text(clazz), "class")

      bindParamList
    }

    CityDao.getList(P.pid).sortBy(_.getName).flatMap { el => bind("foo", in, getTypeSeq(el.getId(), el.getName()): _*) } toSeq
  }

  def addCityButton(in: NodeSeq): NodeSeq = {
    bind("foo", in, AttrBindParam("href", Text("/admin/addCity?pid=" + P.pid), "href"))
  }

  /**
   *
   */
  def addForm = {
    var cityName = ""
    var province = new Province(P.province)
    var country = CountryDao.get(province.getCountryId)

    if (country == null) {
      S.redirectTo("404")
    }

    def process(): NodeSeq = {
      var city = new City()
      city.setName(cityName)
      city.setCountry(country)
      city.setProvince(province)

      CityDao.save(city)

      S.redirectTo("/admin/cities?cid=" + country.getId + "&pid=" + province.getId)
    }

    "#country-name" #> span(Unparsed(country.getName), {}) &
      "#province-name" #> span(Unparsed(province.getName), {}) &
      "name=city-name" #> (text(cityName, s => (cityName = s)) ++ hidden(process))
  }

  /**
   *
   */
  def updateForm = {
    var country = new Country(P.country)
    var city = new City(P.city)
    var name = city.getName

    def process(): NodeSeq = {
      city.setName(name)
      city.setCountry(country)

      CityDao.update(city)

      S.redirectTo("/admin/cities?cid=" + country.getId + "&pid=" + city.getProvinceId + "&ciid=" + city.getId)
    }

    "name=city-name" #> (text(name, s => (name = s)) ++ hidden(process))
  }
}
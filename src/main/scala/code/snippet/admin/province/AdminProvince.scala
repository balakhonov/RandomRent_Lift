package code.snippet.admin.province

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
import code.snippet.util.select.AjaxCountrySelect

object AdminProvince {

  private object currentCountry extends RequestVar[Country](if (P.cid > 0) CountryDao.get(P.cid) else null)
  private object currentProvince extends RequestVar[Province](if (P.pid > 0) ProvinceDao.get(P.pid) else null)

  def isCountryFound(html: NodeSeq) =
    if (currentCountry.is != null) html else NodeSeq.Empty

  def isProvinceFound(html: NodeSeq) =
    if (currentProvince.is != null) html else NodeSeq.Empty

  /**
   *
   */
  def countries = {
    AjaxCountrySelect.select(c => S.redirectTo("/admin/provinces?cid=" + c.getId()))
  }

  /**
   *
   */
  def provinces(in: NodeSeq): NodeSeq = {

    def getTypeSeq(id: Int, name: String): List[BindParam] = {
      var clazz = if (id == P.pid) "active"; else "non-active"

      var bindParamList: List[BindParam] = List[BindParam]();
      bindParamList ::= ("name" -> name)
      bindParamList ::= AttrBindParam("href", Text("/admin/provinces?cid=" + P.cid + "&pid=" + id), "href")
      bindParamList ::= AttrBindParam("class", Text(clazz), "class")

      bindParamList
    }

    ProvinceDao.getList(P.cid).sortBy(_.getName).flatMap { el => bind("foo", in, getTypeSeq(el.getId(), el.getName()): _*) } toSeq
  }

  def addProvinceButton(in: NodeSeq): NodeSeq = {
    bind("foo", in, AttrBindParam("href", Text("/admin/addProvince?cid=" + P.cid), "href"))
  }

  /**
   *
   */
  def addForm = {
    var provinceName = ""
    var country = new Country(currentCountry)
    var countryName = country.getName

    def process(): NodeSeq = {
      var province = new Province()
      province.setName(provinceName)
      province.setCountry(country)

      ProvinceDao.save(province)

      S.redirectTo("/admin/provinces?cid=" + country.getId)
    }

    "#country-name" #> span(Unparsed(countryName), {}) &
      "name=province-name" #> (text(provinceName, s => (provinceName = s)) ++ hidden(process))
  }

  /**
   *
   */
  def updateForm = {
    var province = new Province(currentProvince)
    var name = province.getName

    def process(): NodeSeq = {
      province.setName(name)

      ProvinceDao.update(province)

      S.redirectTo("/admin/provinces?cid=" + province.getCountryId + "&pid=" + province.getId)
    }

    "name=province-name" #> (text(name, s => (name = s)) ++ hidden(process))
  }
}
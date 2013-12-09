package code.snippet.admin.country

import scala.xml.NodeSeq
import scala.xml.Text

import balakhonov.util.RequestParametersUtil.P
import db.dao.CountryDao
import db.mapping.Country
import net.liftweb.http.RequestVar
import net.liftweb.http.S
import net.liftweb.http.SHtml.hidden
import net.liftweb.http.SHtml.text
import net.liftweb.util.Helpers.AttrBindParam
import net.liftweb.util.Helpers.BindParam
import net.liftweb.util.Helpers.bind
import net.liftweb.util.Helpers.strToCssBindPromoter
import net.liftweb.util.Helpers.strToSuperArrowAssoc

object AdminCountry {

  /**
   *
   */
  private object currentCountry extends RequestVar[Country](if (P.cid > 0) CountryDao.get(P.cid) else null)

  /**
   *
   */
  def isCountryFound(html: NodeSeq): NodeSeq =
    if (currentCountry.is != null) html else NodeSeq.Empty

  /**
   *
   */
  def countries(in: NodeSeq): NodeSeq = {
    println("countries");

    def getTypeSeq(id: Int, name: String): List[BindParam] = {
      var clazz = if (id == P.cid) "active" else " non-active"

      var bindParamList: List[BindParam] = List[BindParam]();
      bindParamList ::= ("name" -> name)
      bindParamList ::= AttrBindParam("href", Text("/admin/countries?cid=" + id), "href")
      bindParamList ::= AttrBindParam("class", Text(clazz), "class")

      bindParamList
    }

    CountryDao.getAll.sortBy(_.getName).flatMap { e => bind("foo", in, getTypeSeq(e.getId(), e.getName()): _*) } toSeq
  }

  /**
   *
   */
  def addForm = {
    var name = ""

    def process(): NodeSeq = {
      var country = new Country()
      country.setName(name)

      CountryDao.save(country);

      S.redirectTo("/admin/countries")
    }

    "name=country-name" #> (text(name, s => (name = s)) ++ hidden(process))
  }

  /**
   *
   */
  def updateForm(in: NodeSeq): NodeSeq = {
    println(currentCountry)
    var country = new Country(currentCountry);
    var name = country.getName

    def process() = {
      country.setName(name)

      CountryDao.update(country)

      S.redirectTo("/admin/countries?cid=" + country.getId)
    }

    bind("foo", in, ("country-name" -> (text(name, s => { name = s }) ++ hidden(process))))
  }
}
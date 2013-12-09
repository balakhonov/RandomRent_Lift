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

object AjaxCountrySelect {

  def select(func: (Country) => JsCmd) = {
    var countries: List[(Country, String)] = CountryDao.getAll.map(a => (a, a.getName))
    countries ::= (new Country(), "")
    println(countries)

    var selectedCountries = countries.filter(el => { el._1.getId == P.cid })
    var selectedCountry = if (selectedCountries.isEmpty) {
      Empty
    } else {
      Full(selectedCountries(0)._1)
    }

    "#country-list" #> ajaxSelectObj[Country](countries, selectedCountry, (el: Country) => func(el))
  }
}
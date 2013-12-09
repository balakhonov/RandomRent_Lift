package bootstrap.liftweb

import db.dao.CityDao
import net.liftweb._
import util._
import Helpers._
import common._
import http._
import code.controller._
import net.liftmodules.JQueryModule
import ch.qos.logback.classic.LoggerContext
import net.liftmodules.widgets.autocomplete.AutoComplete
import net.liftweb.sitemap.{ Menu, SiteMap }
import code.rest.AjaxFileUpload

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {
    // where to search snippet
    LiftRules.addToPackages("code")

    //    LiftRules.jsArtifacts = net.liftweb.http.js.jquery.JQueryArtifacts

    // Build SiteMap
    def sitemap(): SiteMap = SiteMap(
      Menu.i("Home") / "index",
      Menu.i("title") / "title")

    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    //Add controllers
    LiftRules.dispatch.append(RootController)
    LiftRules.dispatch.append(AjaxFileUpload)

    // Use HTML5 for rendering
    LiftRules.htmlProperties.default.set((r: Req) =>
      new Html5Properties(r.userAgent))

    //404 page not found
    LiftRules.uriNotFound.prepend(NamedPF("404handler") {
      case (req, failure) =>
        NotFoundAsTemplate(ParsePath(List("404"), "html", true, false))
    });

    //rewrite mode
    LiftRules.statelessRewrite.prepend(NamedPF("ApartmentsRewrite") {
      case RewriteRequest(ParsePath("ua" :: cityName :: Nil, _, _, _), _, _) => {
        var city = CityDao.getCity(1, cityName)
        var ciid = if (city == null) "0" else city.getId.toString

        RewriteResponse("apartments" :: Nil, Map("ciid" -> ciid))
      }
    })

    // auto fade for notice
    LiftRules.noticesAutoFadeOut.default.set((noticeType: NoticeType.Value) => Full((1 seconds, 2 seconds)))

    // enable the autocomplete widget
    AutoComplete.init();

    //Init the jQuery module, see http://liftweb.net/jquery for more information.
    JQueryModule.InitParam.JQuery = JQueryModule.JQuery172
    JQueryModule.init()
  }
}

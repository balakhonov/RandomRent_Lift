package code
package controller

import net.liftweb.http._
import net.liftweb.util._
import java.util.Date
import code.lib._
import Helpers._
import db.HibernateUtil
import db.dao.ApartmentDao
import balakhonov.util.RequestParametersUtil.P

/**
 * A simple MVC controller.  This controller will intercept
 * the given URL and will transform the reterm page based on
 * some computed value
 */
object RootController extends MVCHelper {
  val FORMAT = new java.text.SimpleDateFormat("dd-MM-yyyy")

  // Update the time on the index (home) page
  //  serve {
  //    case "index" :: Nil => {
  // replace the contents of the element with id "time" with the date
  //      "#time *" #> DependencyFactory.inject[Date].map(_.toString) &
  //        "@address [content]" #> "words, we, really, want" &
  //        "title *" #> "I am different"
  //    }
  //  }

//  serve {
//    case "apartments" :: city :: Nil => {
//      println("city is: " + city)
//      "title *" #> ("Аренда жилья, офисов и участков в городе " + P.city.getName) &
//        "#page-title *" #> ("Аренда жилья, офисов и участков в городе " + P.city.getName) &
//        "#page-desc" #> ("Посуточная и длительная аренда квартир, домов в городе " + P.city.getName) &
//        "@keywords [content]" #> "ololo keys 3"
//    }

    //    case "apartments" :: city :: Nil => {
    //      println("apartments is: " + city)
    //    }
    //    case "ua" :: city :: Nil => {
    //      println("city is: " + city)
    //
    //      "#page-title" #> ("Аренда жилья, офисов и участков в городе " + P.city.getName) &
    //        "#page-desc" #> ("Посуточная и длительная аренда квартир, домов в городе " + P.city.getName) &
    //        "@keywords [content]" #> "ololo keys 2"
    //    }
//  }

  // serve a page if and only if the second URL param
  // is an Int
  //  serve {
  //    case "show_int" :: AsInt(param) :: AsInt(param2) :: Nil => {
  //      "@keywords [content]" #> "words, we, really, want" &
  //        "#int_value" #> param &
  //        "#string_value" #> param2 &
  //        "#string_value" #> param2 &
  //        "#string_value" #> param2 &
  //        "#string_value" #> param2
  //    }
  //  }
  //
  //  serve {
  //    case "show_int2" :: AsInt(param) :: Nil =>
  //      "#int_value" #> 5
  //  }
}

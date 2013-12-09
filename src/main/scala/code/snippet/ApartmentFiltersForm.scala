package code.snippet

import net.liftweb.http._
import net.liftweb.http.SHtml.{ ajaxSubmit, hidden, onEvent, ajaxCall }
import net.liftweb.util._
import scala.xml.NodeSeq
import code.lib._
import net.liftweb.util.Helpers._
import xml.Text
import db.mapping.Apartment
import balakhonov.util.Util._
import scala.xml.Unparsed
import net.liftweb.http.js.JsCmd.unitToJsCmd
import net.liftweb.util.Helpers._
import scala.Array.canBuildFrom
import scala.xml.NodeSeq.seqToNodeSeq
import net.liftweb.http.js.JsCmd
import net.liftweb.common.Empty
import net.liftweb.http.js.JE
import net.liftweb.http.js.JsCmds
import balakhonov.util.RequestParametersUtil.parseInt
import balakhonov.util.RequestParametersUtil.P
import db.dao.CityDao
import db.dao.DistrictDao

object ApartmentFiltersForm {

  object pageNumber extends RequestVar[Int](parseInt(S.param("page") openOr "1"))
  object adTypeRV extends RequestVar[Int](parseInt(S.param("adtype") openOr "0"))
  object periodRV extends RequestVar[Int](parseInt(S.param("period") openOr "0"))
  object roomsRV extends RequestVar(S.param("rooms") openOr "")
  object priceFromRV extends RequestVar[Int](parseInt(S.param("prf") openOr "1500"))
  object priceToRV extends RequestVar[Int](parseInt(S.param("prt") openOr "2500"))
  object districtRV extends RequestVar[Int](parseInt(S.param("dist") openOr "0"))

  //addition options
  object furnishedRV extends RequestVar(!Empty.equals(S.param("fu")))
  object airConditioningRV extends RequestVar(!Empty.equals(S.param("ai")))
  object fridgeRV extends RequestVar(!Empty.equals(S.param("fr")))
  object cableTelevisionRV extends RequestVar(!Empty.equals(S.param("ca")))
  object washerRV extends RequestVar(!Empty.equals(S.param("wa")))
  object fireplaceRV extends RequestVar(!Empty.equals(S.param("fi")))
  object internetRV extends RequestVar(!Empty.equals(S.param("in")))
  object boilerRV extends RequestVar(!Empty.equals(S.param("bo")))
  object nearbyParkingRV extends RequestVar(!Empty.equals(S.param("ne")))

  object reqParameters extends RequestVar {
    var fPage = getPageNumber
    var fAdType = getAdType
    var fPeriod = getPeriod
    var fRooms = getRooms
    var fPriceFrom = getPriceFrom
    var fPriceTo = getPriceTo
    var fDistrict = getDistrict
    var fCiid = P.ciid

    var fFurnished = isFurnished
    var fAirConditioning = isAirConditioning
    var fFridge = isFridge
    var fCableTelevision = isCableTelevision
    var fWasher = isWasher
    var fFireplace = isFireplace
    var fInternet = isInternet
    var fBoiler = isBoiler
    var fNearbyParking = isNearbyParking

    def getPageNumber(): Int = pageNumber
    def getPriceFrom(): Int = priceFromRV
    def getPriceTo(): Int = priceToRV
    def getAdType(): Int = adTypeRV
    def getPeriod(): Int = periodRV
    def getRooms(): Array[Int] = if (!roomsRV.get.isEmpty) roomsRV.get split (",") map (_ toInt) else Array[Int]()
    def getDistrict(): Int = districtRV

    def isFurnished(): Boolean = furnishedRV.get
    def isAirConditioning(): Boolean = airConditioningRV.get
    def isFridge(): Boolean = fridgeRV.get
    def isCableTelevision(): Boolean = cableTelevisionRV.get
    def isWasher(): Boolean = washerRV.get
    def isFireplace(): Boolean = fireplaceRV.get
    def isInternet(): Boolean = internetRV.get
    def isBoiler(): Boolean = boilerRV.get
    def isNearbyParking(): Boolean = nearbyParkingRV.get

    def resetParams() {
      fPage = 1
      fAdType = getAdType
      fPeriod = getPeriod
      fRooms = getRooms
      fPriceFrom = getPriceFrom
      fPriceTo = getPriceTo
      fDistrict = getDistrict
      fCiid = P.ciid

      fFurnished = isFurnished
      fAirConditioning = isAirConditioning
      fFridge = isFridge
      fCableTelevision = isCableTelevision
      fWasher = isWasher
      fFireplace = isFireplace
      fInternet = isInternet
      fBoiler = isBoiler
      fNearbyParking = isNearbyParking
    }
  }

  def generateUrl(): String = {
    import ApartmentFiltersForm.reqParameters._

    //    println("S.uri : " + S.uri)
    var rootPath = if (P.city == null) "/apartments" else "/ua/" + P.city.getName
    var url = rootPath + "?page=" + fPage
    if (fAdType != 0) url += "&adtype=" + fAdType
    if (fPeriod != 0 && fAdType != 3 && fAdType != 4) url += "&period=" + fPeriod
    if (fRooms.length > 0) url += "&rooms=" + fRooms.mkString(",")
    url += "&prf=" + fPriceFrom
    url += "&prt=" + fPriceTo
    if (fDistrict != 0) url += "&dist=" + fDistrict

    //additions
    if (fFurnished) url += "&fu=" + fFurnished
    if (fAirConditioning) url += "&ai=" + fAirConditioning
    if (fFridge) url += "&fr=" + fFridge
    if (fCableTelevision) url += "&ca=" + fCableTelevision
    if (fWasher) url += "&wa=" + fWasher
    if (fFireplace) url += "&fi=" + fFireplace
    if (fInternet) url += "&in=" + fInternet
    if (fBoiler) url += "&bo=" + fBoiler
    if (fNearbyParking) url += "&ne=" + fNearbyParking
    return url
  }

  private def getDestricsList(in: NodeSeq, apartment: Apartment): NodeSeq = {
    var list = List[String]();
    if (apartment.isFurnished()) list ::= "Мебель"
    if (apartment.isWasher()) list ::= "Стиральная машина"
    if (apartment.isBoiler()) list ::= "Бойлер"
    if (apartment.isFridge()) list ::= "Холодильник"
    if (apartment.isAirConditioning()) list ::= "Кондиционер"

    list.map { el => Unparsed("<li>" + el + "</li>") } toSeq
  }

  /**
   *
   */
  def hasPeriod(html: NodeSeq) = {
    reqParameters.getAdType match {
      case 0 => html
      case 1 => html
      case 2 => html
      case _ => NodeSeq.Empty
    }
  }

  /**
   *
   */
  def FilterAdTypes(in: NodeSeq): NodeSeq = {

    def getTypeSeq(id: Int, typeName: String): List[BindParam] = {
      reqParameters.resetParams
      reqParameters.fAdType = id
      var clazz = "";
      if (id == reqParameters.getAdType) {
        clazz += " active";
      } else {
        clazz += " non-active";
      }
      if (id == 0) {
        clazz += " first"
      }

      var bindParamList: List[BindParam] = List[BindParam]();
      bindParamList ::= ("type" -> typeName)
      bindParamList ::= AttrBindParam("href", Text(generateUrl()), "href")
      bindParamList ::= AttrBindParam("class", Text(clazz), "class")

      bindParamList
    }

    adTypes.flatMap { el => Helpers.bind("foo", in, getTypeSeq(el._1, el._2): _*) } toSeq
  }

  /**
   *
   */
  def FilterPeriods(in: NodeSeq): NodeSeq = {

    def getPeriodSeq(id: Int, name: String): List[BindParam] = {
      reqParameters.resetParams
      reqParameters.fPeriod = id

      var clazz = "";
      if (id == reqParameters.getPeriod) {
        clazz += " active";
      } else {
        clazz += " non-active";
      }
      if (id == 0) {
        clazz += " first"
      }

      var bindParamList: List[BindParam] = List[BindParam]();
      bindParamList ::= ("period" -> name)
      bindParamList ::= AttrBindParam("href", Text(generateUrl()), "href")
      bindParamList ::= AttrBindParam("class", Text(clazz), "class")

      bindParamList
    }

    periods.flatMap { el => Helpers.bind("foo", in, getPeriodSeq(el._1, el._2): _*) } toSeq
  }

  def FilterPrice = {
    var from = reqParameters.getPriceFrom
    var to = reqParameters.getPriceTo

    def process(): JsCmd = {
      reqParameters.resetParams()

      reqParameters.fPriceFrom = from
      reqParameters.fPriceTo = to

      println("redirectTo : " + generateUrl)
      S.redirectTo(generateUrl)
    }

    "#price-from" #> hidden(s => from = asInt(s).get, from.toString) &
      "#price-to" #> hidden(s => to = asInt(s).get, to.toString) &
      "button" #> (SHtml.submit("OK", null) ++ hidden(process))
  }

  /**
   *
   */
  def addForm(in: NodeSeq): NodeSeq = {

    var bindParamList: List[BindParam] = List[BindParam]();
    bindParamList ::= ("destrict1" -> "destrict1")

    bind("foo", in, bindParamList: _*)
  }

  /**
   *
   */
  def FilterRooms = {
    import ApartmentFiltersForm.reqParameters._

    def onClickCallback(id: Int)(s: String): JsCmd = {
      resetParams()

      if (fRooms contains id) {
        fRooms = fRooms diff List(id)
      } else {
        fRooms :+= id
      }

      S.redirectTo(generateUrl)
    }

    "ul" #> roomTypes.map { el =>
      var key = el._1
      var value = el._2
      var checkBoxId = "room-cb-" + key

      var selectors =
        "input [id]" #> Text(checkBoxId) &
          ".lb [for]" #> Text(checkBoxId) &
          ".lb span" #> Text(value) &
          "input [onClick]" #> onEvent(onClickCallback(key)_)

      if (getRooms contains key)
        selectors &= "input [checked]" #> Text("checked")

      selectors
    }
  }

  /**
   *
   */
  def FilterAdditions = {
    import ApartmentFiltersForm.reqParameters._

    def onClickCallback(key: Int)(s: String): JsCmd = {
      resetParams()

      key match {
        case 1 => fFurnished = !fFurnished
        case 2 => fAirConditioning = !fAirConditioning
        case 3 => fFridge = !fFridge
        case 4 => fCableTelevision = !fCableTelevision
        case 5 => fWasher = !fWasher
        case 6 => fFireplace = !fFireplace
        case 7 => fInternet = !fInternet
        case 8 => fBoiler = !fBoiler
        case 9 => fNearbyParking = !fNearbyParking
      }

      S.redirectTo(generateUrl)

      //      var c = ""
      //      key match {
      //        case 1 =>
      //          c = "urlParams['fu'] = !urlParams['fu'];"
      //        case 2 =>
      //          c = "urlParams['ai'] = !urlParams['ai'];"
      //        case 3 =>
      //          c = "urlParams['fr'] = !urlParams['fr'];"
      //        case 4 =>
      //          c = "urlParams['ca'] = !urlParams['ca'];"
      //        case 5 =>
      //          c = "urlParams['wa'] = !urlParams['wa'];"
      //        case 6 =>
      //          c = "urlParams['fi'] = !urlParams['fi'];"
      //        case 7 =>
      //          c = "urlParams['in'] = !urlParams['in'];"
      //        case 8 =>
      //          c = "urlParams['bo'] = !urlParams['bo'];"
      //        case 9 =>
      //          c = "urlParams['ne'] = !urlParams['ne'];"
      //      }
      //
      //      return JsCmds.Run(c + "console.log(urlParams);console.log('" + generateUrl + "');newUrl = '" + generateUrl + "'")
    }

    var selectors =
      "#furn-cb [onClick]" #> onEvent(onClickCallback(1)_) &
        "#air-condit-cb [onClick]" #> onEvent(onClickCallback(2)_) &
        "#fridge-cb [onClick]" #> onEvent(onClickCallback(3)_) &
        "#cab-tv-cb [onClick]" #> onEvent(onClickCallback(4)_) &
        "#laundry-fac-cb [onClick]" #> onEvent(onClickCallback(5)_) &
        "#fireplace-cb [onClick]" #> onEvent(onClickCallback(6)_) &
        "#internet-cb [onClick]" #> onEvent(onClickCallback(7)_) &
        "#boiler-cb [onClick]" #> onEvent(onClickCallback(8)_) &
        "#parking-cb [onClick]" #> onEvent(onClickCallback(9)_)

    if (fFurnished) selectors &= "#furn-cb [checked]" #> Text("checked")
    if (fAirConditioning) selectors &= "#air-condit-cb [checked]" #> Text("checked")
    if (fFridge) selectors &= "#fridge-cb [checked]" #> Text("checked")
    if (fCableTelevision) selectors &= "#cab-tv-cb [checked]" #> Text("checked")
    if (fWasher) selectors &= "#laundry-fac-cb [checked]" #> Text("checked")
    if (fFireplace) selectors &= "#fireplace-cb [checked]" #> Text("checked")
    if (fInternet) selectors &= "#internet-cb [checked]" #> Text("checked")
    if (fBoiler) selectors &= "#boiler-cb [checked]" #> Text("checked")
    if (fNearbyParking) selectors &= "#parking-cb [checked]" #> Text("checked")

    selectors
  }

  /**
   *
   */
  def FilterLocation(in: NodeSeq): NodeSeq = {

    def getTypeSeq(id: Int, name: String) = {
      reqParameters.resetParams
      reqParameters.fDistrict = id

      var clazz = if (id == reqParameters.getDistrict) " active"; else " non-active";
      if (id == 0) {
        clazz += " first"
      }

      (name, generateUrl(), clazz)
    }

    var firstLi = {
      var dd = getTypeSeq(0, "Все")
      <li> <a href={ dd._2 } class={ dd._3 }>{ dd._1 }</a></li>
    }

    var districs = DistrictDao.getList(P.ciid)
    var districtsList = districs.flatMap {
      el =>
        {
          var dd = getTypeSeq(el.getId, el.getName)
          <li> <a href={ dd._2 } class={ dd._3 }>{ dd._1 }</a></li>
        }
    }

    <fieldset id="location-filter-fs">
      <h4 class="side-title">Район</h4>
      <ul>
        { firstLi }
        { districtsList }
      </ul>
    </fieldset>
  }
}
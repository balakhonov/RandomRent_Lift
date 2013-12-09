package code.snippet

import net.liftweb.http._
import net.liftweb.http.SHtml.{ ajaxTextarea, text, number, select, hidden, checkbox_id, checkbox }
import net.liftweb.util._
import code.lib._
import net.liftweb.util.Helpers._
import net.liftweb.http.js.JsCmd
import xml.Text
import net.liftweb.common.Empty
import db.HibernateUtil
import balakhonov.util.Util
import db.mapping.Apartment
import java.util.Date
import db.dao.ApartmentDao
import balakhonov.util.Util._
import net.liftweb.common.Full
import net.liftweb.http.SHtml.ElemAttr.pairToBasic
import net.liftweb.http.SHtml.ElemAttr.pairToBasic
import net.liftweb.http.js.JsCmd.unitToJsCmd
import code.snippet.util.select.AjaxProvinceSelect
import code.snippet.util.select.AjaxCountrySelect
import balakhonov.util.RequestParametersUtil.P
import net.liftweb.http.js.JsCmds.SetHtml
import code.snippet.util.select.AjaxCitySelect
import db.mapping.Province
import scala.xml.NodeSeq
import code.snippet.util.select.AjaxDistrictSelect
import net.liftweb.http.js.JsCmds
import net.liftweb.http.js.JE
import db.mapping.District
import balakhonov.util.RequestParametersUtil.parseInt
import db.dao.DistrictDao
import db.dao.ApartmentImageDao

object AddApartmentForm {
  /**
   * Type ID. Сниму, Сдам
   */
  private object rentType extends RequestVar[Int](parseInt(S.param("type") openOr "0"))

  /**
   * Perod ID. Длительно, посуточно
   */
  private object rentPeriod extends RequestVar[Int](parseInt(S.param("period") openOr "0"))

  /**
   * City ID.
   */
  private object sityId extends RequestVar[Int](parseInt(S.param("ciid") openOr "0"))

  /**
   */
  object uploadedImagesIds extends SessionVar[List[Int]](List[Int]())

  private val bedroomSeq = Util.roomTypes.map { case (k, v) => (k.toString, v) }.toSeq
  private val emptyText = Text("")

  private val topics = Map(1 -> "Длительная аренда квартир", 2 -> "Посуточная аренда квартир", 3 -> "Купля/продажа кваритр")
  private val adTypes = Map(1 -> "Сниму", 2 -> "Сдам", 3 -> "Куплю", 4 -> "Продам")

  private val TITLE_ERROR_ID = "add-title-err"
  private val DISTRICT_ERROR_ID = "add-district-err"
  private val DESCRIPTION_ERROR_ID = "add-description-err"

  /**
   *
   */
  private def getTopic(adType: Int, period: Int): String = {
    (adType, period) match {
      case (1, 1) => { topics.get(1).get }
      case (1, 2) => { topics.get(2).get }
      case (2, 1) => { topics.get(1).get }
      case (2, 2) => { topics.get(2).get }
      case _ => { topics.get(3).get }
    }
  }

  /**
   *
   */
  private def getAdType(adType: Int): String = {
    adTypes.get(adType).get
  }

  def render(in: NodeSeq): NodeSeq = {
    uploadedImagesIds.set(List[Int]())

    if (rentType < 1 || rentType > 2) {
      println("Rent Type(" + rentType + ") should be in [1,2]")
      S.redirectTo("/404")
    }

    if (rentPeriod < 1 || rentPeriod > 2) {
      println("Rent Period(" + rentPeriod + ") should be in [1,2]")
      S.redirectTo("/404")
    }

    if (P.country == null) {
      println("County should not be null")
      S.redirectTo("/404")
    }

    in
  }

  /**
   *
   */
  def addForm = {
    var selectedProvinceId = 0;

    var adType = rentType;
    var period = rentPeriod;
    var districtId = 0;
    var street = "Юмашева 25";
    var bedroomNumber = 0;
    var price: Int = 0;
    var description = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
    var mobileNumber = "(099) 500-38-23";
    var title = "В центре города 2х комнатная с кондиционером"

    var furnished = true;
    var airConditioning = false;
    var fridge = true;
    var cableTelevision = false;
    var washer = true;
    var fireplace = false;
    var internet = false;
    var boiler = false;
    var nearbyParking = false;

    def onProvinceSelect(provinceId: Int) = {
      SetHtml("city-list-wrapper", citySelectHtml(provinceId)) &
        SetHtml("district-list-wrapper", NodeSeq.Empty)
    }

    def onCitySelect(cityId: Int) = {
      sityId.set(cityId)

      if (DistrictDao.count(cityId) == 0) {
        SetHtml("district-block-wrapper", NodeSeq.Empty) &
          SetHtml("district-list-wrapper", NodeSeq.Empty) &
          JE.JsRaw("$('#add-ap-descr-info').show('blind',400);").cmd
      } else {
        SetHtml("district-list-wrapper", districtSelectHtml(cityId))
      }
    }

    def onDistrictSelect(d: District): JsCmd = {
      districtId = d.getId
      JE.JsRaw("$('#add-ap-descr-info').show('blind',400);").cmd
    }

    /**
     * Provinces select field
     */
    def provinceSelect() = {
      AjaxProvinceSelect.selectHtml(true, p => onProvinceSelect(p.getId), id => selectedProvinceId = id)
    }

    def citySelectHtml(provinceId: Int) = {
      if (provinceId > 0)
        <td>Город:</td>
        <td>{ AjaxCitySelect.selectHtml(provinceId, true, c => onCitySelect(c.getId)) }</td>
      else
        NodeSeq.Empty
    }

    def districtSelectHtml(cityId: Int) = {
      if (cityId > 0)
        <td>Район:</td>
        <td>
          { AjaxDistrictSelect.selectHtml(cityId, true, c => { onDistrictSelect(c) }) }
          <span id={ DISTRICT_ERROR_ID } class="error"/>
        </td>
      else
        NodeSeq.Empty
    }

    def validation(): Boolean = {
      var res = true;

      if (districtId < 0) { res = false; S.error(DISTRICT_ERROR_ID, "Выберите район!") } else S.error(DISTRICT_ERROR_ID, "")
      if (title.isEmpty) { res = false; S.error(TITLE_ERROR_ID, "Введите заголовок!") } else S.error(TITLE_ERROR_ID, "")
      if (description.isEmpty) { res = false; S.error(DESCRIPTION_ERROR_ID, "Введите описание!") } else S.error(DESCRIPTION_ERROR_ID, "")

      res
    }

    def processAdd() = {
      var validResult = validation();

      if (validResult) {
        var district = DistrictDao.get(districtId)

        var apartment = new Apartment
        apartment.setCityId(sityId)
        apartment.setDistrict(district)
        apartment.setStreet(street)
        apartment.setDescription(description)
        apartment.setAdType(adType)
        apartment.setPeriod(period)
        apartment.setPostedDate(new Date().getTime)
        apartment.setPrice(price)
        apartment.setRoomTypeId(bedroomNumber)
        apartment.setTitle(title)
        apartment.setMobileNumber(mobileNumber)
        apartment.sethKeyWords("")
        apartment.sethDescription("")
        apartment.sethTitle(title)
        /* addition options */
        apartment.setFurnished(furnished)
        apartment.setAirConditioning(airConditioning)
        apartment.setFridge(fridge)
        apartment.setCableTelevision(cableTelevision)
        apartment.setWasher(washer)
        apartment.setFireplace(fireplace)
        apartment.setInternet(internet)
        apartment.setBoiler(boiler)
        apartment.setNearbyParking(nearbyParking)
        ApartmentDao.save(apartment)

        //update images references
        ApartmentImageDao.setReferences(uploadedImagesIds.get, apartment.getId())

        S.redirectTo("/apartments?page=1&ciid=" + sityId)
      }
    }

    "#topic" #> getTopic(adType, period) &
      "#ad-type" #> getAdType(adType) &
      "#province-list" #> provinceSelect() &
      "name=title-input" #> (text(title, title = _, "placeholder" -> "Пример: В центре города 2х комнатная с кондиционером") ++ <br/><span id={ TITLE_ERROR_ID } class="error"/>) &
      "name=description-input" #> (ajaxTextarea(description, description = _, "id" -> "description-input") ++ <br/><span id={ DESCRIPTION_ERROR_ID } class="error"/>) &
      "name=street-input" #> text(street, street = _, "placeholder" -> "Пример: Юмашева 21") &
      "name=bedroom-num-select" #> select(bedroomSeq, Empty, s => (bedroomNumber = s.toInt), "class" -> "bedroom-select") &
      "name=price-input" #> number(1500, price = _, 1, 40000) &
      "name=mobile-input" #> text(mobileNumber, mobileNumber = _) &
      /* addition options */
      "#furn-cb" #> checkbox(furnished, b => (furnished = b), "id" -> "furn-cb") &
      "#air-condit-cb" #> checkbox(airConditioning, b => (airConditioning = b)) &
      "#fridge-cb" #> checkbox(fridge, b => (fridge = b), "id" -> "fridge-cb") &
      "#cab-tv-cb" #> checkbox(cableTelevision, b => (cableTelevision = b), "id" -> "cab-tv-cb") &
      "name=laundry-fac-cb" #> checkbox(washer, b => (washer = b), "id" -> "laundry-fac-cb") &
      "name=fireplace-cb" #> checkbox(fireplace, b => (fireplace = b), "id" -> "fireplace-cb") &
      "name=internet-cb" #> checkbox(internet, b => (internet = b), "id" -> "internet-cb") &
      "name=boiler-cb" #> checkbox(boiler, b => (boiler = b), "id" -> "boiler-cb") &
      "name=parking-cb" #> checkbox(nearbyParking, b => (nearbyParking = b), "id" -> "parking-cb") &
      "type=submit" #> (SHtml.submit("Опубликовать", null, "class" -> "button") ++ hidden(processAdd))
  }
}
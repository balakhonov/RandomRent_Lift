package code.snippet

import java.util.Date

import scala.collection.JavaConversions.seqAsJavaList
import scala.xml.NodeSeq
import scala.xml.NodeSeq.seqToNodeSeq
import scala.xml.Text
import scala.xml.Unparsed

import db.dao.ApartmentDao
import db.dao.ApartmentImageDao
import db.dao.DistrictDao
import db.mapping.Apartment
import db.mapping.ApartmentImage
import net.liftweb.common.Box.box2Option
import net.liftweb.common.Empty
import net.liftweb.http.S
import net.liftweb.sitemap.LocPath.stringToLocPath
import net.liftweb.sitemap.Menu
import net.liftweb.util.Helpers._
import balakhonov.util.Util._

object ApartmentSnippet {
  val FORMAT = new java.text.SimpleDateFormat("dd-MM-yyyy")
  Menu.i("Home") / "index"
  Menu.i("title") / "title"

  /**
   *
   */
  def showAdditionOptions(in: NodeSeq, apartment: Apartment): NodeSeq = {
    var list = List[String]();
    if (apartment.isFurnished()) list ::= "Мебель"
    if (apartment.isWasher()) list ::= "Стиральная машина"
    if (apartment.isBoiler()) list ::= "Бойлер"
    if (apartment.isFridge()) list ::= "Холодильник"
    if (apartment.isAirConditioning()) list ::= "Кондиционер"

    list.map { el => <li>{ el }</li> }
  }

  /**
   *
   */
  def showAdditionOptions2(in: NodeSeq, apartment: Apartment): NodeSeq = {
    var list = List[String]();
    if (apartment.isInternet()) list ::= "Иннтернет"
    if (apartment.isCableTelevision()) list ::= "Кабелное телевидение"
    if (apartment.isFireplace()) list ::= "Камин"
    if (apartment.isNearbyParking()) list ::= "Рядом парковка"

    list.map { el => <li>{ el }</li> }
  }

  /**
   *
   */
  private def getApartmantMapInfo(in: NodeSeq, ap: Apartment): NodeSeq = {
    var id = ap.getId
    var title = ap.getTitle

    var district = if (ap.getDistrictId() == null)
      "Не указан"
    else
      DistrictDao.get(ap.getDistrictId()).getName

    var images = ApartmentImageDao.getList(id)
    var mainImage = if (images.isEmpty) null else images.get(0)

    var bindParamList: List[BindParam] = List[BindParam]();
    bindParamList ::= ("title" -> title)
    bindParamList ::= ("street" -> ap.getStreet)
    bindParamList ::= ("district" -> district)
    bindParamList ::= ("description" -> Unparsed(ap.getDescription.replace("\n", "<br/>")))
    bindParamList ::= ("mobile" -> ap.getMobileNumber())
    bindParamList ::= ("id" -> id)
    bindParamList ::= ("price" -> ap.getPrice())
    bindParamList ::= ("period" -> periods(ap.getPeriod))
    bindParamList ::= ("rooms" -> ap.getRoomTypeId)
    bindParamList ::= ("posted" -> FORMAT.format(new Date(ap.getPostedDate)))
    bindParamList ::= ("additions" -> showAdditionOptions(in, ap))
    bindParamList ::= ("additions2" -> showAdditionOptions2(in, ap))
    bindParamList ::= (AttrBindParam("map-attr-href", Text("/apartment?id=" + id), "href"))
    bindParamList ::= (AttrBindParam("main-image-src", Text(getMainImageLink(mainImage)), "src"))
    bindParamList ::= ("images" -> images.map { ai => getImageLi(ai) })

    bind("foo", in, bindParamList: _*)
  }

  private def getMainImageLink(ai: ApartmentImage): String = {
    if (ai != null) "/ap-images/" + ai.getName + "-thumb-200x200" else "/images/noimage.png"
  }

  private def getImageLi(ai: ApartmentImage) = {
    <li>
      <a class="imgThumb" href={ "/ap-images/" + ai.getName + "-thumb-200x200" }></a>
      <a class="imgFull" href={ "/ap-images/" + ai.getName + "-max" }></a>
      <div class="imgDesc">Description 1</div>
    </li>
  }

  /**
   *
   */
  def render(in: NodeSeq): NodeSeq = {
    var apartment: Apartment = null;

    var idParam = S.param("id")
    var id = 0;

    if (Empty.equals(idParam)) {
      println("Parameter 'type' not found")
      S.redirectTo("/404")
    } else {
      id = idParam.get.toInt

      if (id < 1) {
        println("id(" + id + ") should be > 0")
        S.redirectTo("/404")
      } else {
        apartment = ApartmentDao.get(id);

        if (apartment == null) {
          S.redirectTo("/404")
        } else {
          getApartmantMapInfo(in, apartment)
        }
      }
    }
  }
} 
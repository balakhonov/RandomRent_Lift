package code.snippet

import java.util.Date
import scala.Array.fallbackCanBuildFrom
import scala.collection.JavaConversions.seqAsJavaList
import scala.collection.mutable.ListBuffer
import scala.xml.Elem
import scala.xml.NodeSeq
import scala.xml.NodeSeq.seqToNodeSeq
import scala.xml.Text
import scala.xml.Unparsed
import ApartmentFiltersForm.generateUrl
import ApartmentFiltersForm.reqParameters._
import ApartmentSnippet.showAdditionOptions
import ApartmentSnippet.showAdditionOptions2
import balakhonov.util.RequestParametersUtil.P
import db.dao.ApartmentDao
import db.dao.ApartmentImageDao
import db.dao.DistrictDao
import db.mapping.Apartment
import db.mapping.ApartmentImage
import net.liftweb.common.Box.box2Option
import net.liftweb.common.Empty
import net.liftweb.http.RequestVar
import net.liftweb.http.S
import net.liftweb.util.AnyVar.whatVarIs
import net.liftweb.util.Helpers._
import net.liftweb.util.CssSel

class ApartmentsSnippet {
  val FORMAT = new java.text.SimpleDateFormat("dd MMMM yyyy 'в' HH:mm")

  /**
   * Count of apartment items per page
   */
  private val ITEMS_PER_PAGE = 9;

  /**
   * Count of visible buttons on pagination
   */
  private val VISIBLE_BUTTONS_COUNT = 3

  private object apartmentsList extends RequestVar[List[Apartment]](List[Apartment]())
  private object apartmentsCountOnPage extends RequestVar[Int](0)
  private object apartmentsCountTotal extends RequestVar[Int](0)

  /**
   *
   */
  private def getApartmantMapInfo(in: NodeSeq, ap: Apartment): NodeSeq = {
    var id = ap.getId
    var title = ap.getTitle
    var district = ""
    if (ap.getDistrictId() != null)
      district = DistrictDao.get(ap.getDistrictId()).getName +" район, "

    var adType = ap.getAdType match {
      case 1 => "Сниму"
      case 2 => "Сдам"
      case 3 => "Куплю"
      case 4 => "Продам"
    }

    //TODO get first image only
    var images = ApartmentImageDao.getList(id)
    var mainImage = if (images.isEmpty) null else images.get(0)

    var bindParamList: List[BindParam] = List[BindParam]();
    bindParamList ::= ("title" -> title)
    bindParamList ::= ("ad-type" -> adType)
    bindParamList ::= ("district" -> district)
    bindParamList ::= ("street" -> ap.getStreet)
    bindParamList ::= ("rooms" -> ap.getRoomTypeId)
    bindParamList ::= ("description" -> Unparsed(ap.getDescription.replace("\n", "<br/>")))
    bindParamList ::= ("id" -> id)
    bindParamList ::= ("price" -> ap.getPrice())
    bindParamList ::= ("posted" -> FORMAT.format(new Date(ap.getPostedDate)))
    bindParamList ::= (AttrBindParam("attr-alt", Text(title), "alt"))
    bindParamList ::= (AttrBindParam("attr-title", Text(title), "title"))
    bindParamList ::= (AttrBindParam("attr-href", Text("/apartment?id=" + id), "href"))
    bindParamList ::= (AttrBindParam("main-image-src", Text(getMainImageLink(mainImage)), "src"))
    bindParamList ::= ("additions" -> showAdditionOptions(in, ap))
    bindParamList ::= ("additions2" -> showAdditionOptions2(in, ap))

    bind("foo", in, bindParamList: _*)
  }

  /**
   *
   */
  private def getMainImageLink(ai: ApartmentImage): String = {
    if (ai != null) "/ap-images/" + ai.getName + "-thumb-200x200" else "/images/noimage.png"
  }

  /**
   *
   */
  private def getApartmants(offset: Int): (List[Apartment], Int) = {
    var totalApartments = ApartmentDao.getApartmentsCount(P.ciid, fDistrict, fAdType, fPeriod, fPriceFrom, fPriceTo, seqAsJavaList(fRooms.map(i => i: java.lang.Integer)),
      fFurnished, fAirConditioning, fFridge, fCableTelevision, fWasher, fFireplace, fInternet, fBoiler, fNearbyParking)

    var list = ApartmentDao.getApartments(ITEMS_PER_PAGE, offset, P.ciid, fDistrict, fAdType, fPeriod, fPriceFrom, fPriceTo, seqAsJavaList(fRooms.map(i => i: java.lang.Integer)),
      fFurnished, fAirConditioning, fFridge, fCableTelevision, fWasher, fFireplace, fInternet, fBoiler, fNearbyParking)

    (list, totalApartments.toInt)
  }

  private def getCurrentPageNumber(): Int = {
    var page = S.param("page")
    var pageNumber = 1

    if (!Empty.equals(page)) {
      pageNumber = augmentString(page.get).toInt
    }

    pageNumber
  }

  private def getPrevPaginationButton(pageNumber: Int): NodeSeq = {
    resetParams()
    fPage = pageNumber - 1

    var content = <span class='r-b'><span class='arrow'>←</span>назад</span>

    if (pageNumber != 1)
      content = <a href={ generateUrl() }>{ content }</a>

    content
  }

  private def getNextPaginationButton(pageNumber: Int): Elem = {
    resetParams()
    fPage = pageNumber + 1

    <a class='r-b' href={ generateUrl() }>вперед<span class='arrow'>→</span></a>
  }

  private def getPaginationButton(pageNumber: Int, currentPageNumber: Int): Elem = {
    resetParams()
    fPage = pageNumber

    var d = Math.abs(currentPageNumber - pageNumber)

    if (d > VISIBLE_BUTTONS_COUNT) {
      if (d == VISIBLE_BUTTONS_COUNT + 1) {
        return <a class='n-b' href={ generateUrl() }>...</a>
      } else {
        return null;
      }
    } else {
      if (currentPageNumber == pageNumber) {
        return <span class='n-b current'>{ pageNumber }</span>
      } else {
        return <a class='n-b' href={ generateUrl() }>{ pageNumber }</a>
      }
    }
  }

  /**
   * Apartments counts block
   */
  def showApartmentCounts(in: NodeSeq): NodeSeq = {
    bind("foo", in, "onpage" -> apartmentsCountOnPage.get, "total" -> apartmentsCountTotal.get)
  }

  /**
   * Apartments blocks short info
   */
  def showApartments(in: NodeSeq): NodeSeq = {
    apartmentsList.flatMap { el => getApartmantMapInfo(in, el) } toSeq
  }

  /**
   * Pagination block
   */
  def showPagination(in: NodeSeq): NodeSeq = {
    // get current page number
    var currentPageNumber = getCurrentPageNumber
    var totalPages = Math.ceil(apartmentsCountTotal.toFloat / ITEMS_PER_PAGE).toInt;

    var links = ListBuffer[NodeSeq]()

    if (currentPageNumber != 1)
      links += getPrevPaginationButton(currentPageNumber);

    for (a <- 1 to totalPages) {
      var node = getPaginationButton(a, currentPageNumber)
      if (node != null)
        links += node
    }
    if (currentPageNumber != totalPages)
      links += getNextPaginationButton(currentPageNumber);

    links.flatMap { s => <li>{ s }</li> } toSeq
  }

  /**
   * Page render
   */
  def render = {
    if (P.ciid < 1) {
      S.redirectTo("/404")
    }
    resetParams()

    var pageNumber = getCurrentPageNumber
    var list = List[db.mapping.Apartment]()

    var result = getApartmants(ITEMS_PER_PAGE * (pageNumber - 1))
    list = result._1

    apartmentsCountTotal.set(result._2)
    apartmentsCountOnPage.set(list.length)

    apartmentsList.set(list)

    "title *" #> ("Аренда жилья, офисов и участков в городе " + P.city.getName) &
      "#page-title *" #> ("Аренда жилья, офисов и участков в городе " + P.city.getName) &
      "#page-desc *" #> ("Посуточная и длительная аренда квартир, домов в городе " + P.city.getName) &
      "@keywords [content]" #> "ololo keys 2"
  }
} 
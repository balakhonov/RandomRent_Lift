package balakhonov.util

import net.liftweb.util.Helpers._
import net.liftweb.http._
import db.mapping.Country
import db.mapping.Province
import db.mapping.City
import db.mapping.District
import db.dao.CountryDao
import db.dao.ProvinceDao
import db.dao.CityDao
import db.dao.DistrictDao

object RequestParametersUtil {
  val cidParamName = "cid";
  val pidParamName = "pid";
  val ciidParamName = "ciid";
  val disParamName = "dis";
  val emptyParamValue = "0";

  /**
   * country ID
   */
  private object cidRV extends RequestVar[Int](parseInt(S.param(cidParamName) openOr emptyParamValue))

  /**
   * Province ID
   */
  private object pidRV extends RequestVar[Int](parseInt(S.param(pidParamName) openOr emptyParamValue))

  /**
   * City ID
   */
  private object ciidRV extends RequestVar[Int](parseInt(S.param(ciidParamName) openOr emptyParamValue))

  /**
   * District ID
   */
  private object disRV extends RequestVar[Int](parseInt(S.param(disParamName) openOr emptyParamValue))

  class Params(aCid: Int, aPid: Int, aCiid: Int, aDis: Int) {
    /**
     * Country ID
     */
    val cid = aCid

    /**
     * Province ID
     */
    val pid = aPid

    /**
     * City ID
     */
    val ciid = aCiid

    /**
     * District ID
     */
    val dis = aDis

    lazy val country = if (cid > 0) CountryDao.get(cid) else null
    lazy val province = if (pid > 0) ProvinceDao.get(pid) else null
    lazy val city = if (ciid > 0) CityDao.get(ciid) else null
    lazy val district = if (dis > 0) DistrictDao.get(dis) else null

    def genUrlParam(cid: Int, pid: Int, ciid: Int, dis: Int): String = {
      var sb = new StringBuilder()
      sb.append("?")
      if (cid > 0) sb.append("%s=%d&".format(cidParamName, cid))
      if (pid > 0) sb.append("%s=%d&".format(pidParamName, pid))
      if (ciid > 0) sb.append("%s=%d&".format(ciidParamName, ciid))
      if (dis > 0) sb.append("%s=%d&".format(disParamName, dis))

      sb.toString
    }
  }

  def parseInt(s: String): Int = {
    try s.toInt catch {
      case e: Exception => {
        e.printStackTrace()
        0
      }
    }
  }

  object P extends RequestVar[Params](new Params(cidRV, pidRV, ciidRV, disRV)) {
    override def toString(): String = {
      var sb = new StringBuilder()
      sb.append("%s: %d\n".format(cidParamName, this.cid))
      sb.append("%s: %d\n".format(pidParamName, this.pid))
      sb.append("%s: %d\n".format(ciidParamName, this.ciid))
      sb.append("%s: %d\n".format(disParamName, this.dis))

      sb.toString
    }
  }
}
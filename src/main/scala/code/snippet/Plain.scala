package code.snippet

import net.liftweb.common.Full
import net.liftweb.http.S
import net.liftweb.util.PassThru

object Plain {
  def render = S.param("name") match {
    case Full(name) =>
      println("load Plain ")
      S.notice("Hello " + name)
      S.redirectTo("/index")
    case _ =>
      PassThru
  }
}
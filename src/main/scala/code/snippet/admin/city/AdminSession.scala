package code.snippet.admin

import net.liftweb.http._
import net.liftweb.util._
import scala.xml.NodeSeq
import balakhonov.util.Validator
import code.lib._
import Helpers._
import net.liftweb.http.js.JsCmd
import net.liftweb.common.Empty

object AdminSession {
  private object password extends RequestVar[String]("")
  private object authorised extends SessionVar[Boolean](false)

  def loggedIn(html: NodeSeq) = if (authorised.get) html else NodeSeq.Empty

  def loggedOut(html: NodeSeq) = if (!authorised.get) html else NodeSeq.Empty

  def logIn = {
    def processLogIn(): JsCmd = {

      if (password.equalsIgnoreCase("zseqsczseqsc12")) {
        authorised.set(true)

        //refresh page
        S.redirectTo("/admin")
      } else {
        S.error("sign-in-pass-err", "Invalid password!")
      }
    }

    "name=sign-in-pass-input" #> (SHtml.passwordElem(password) ++ SHtml.hidden(processLogIn))
  }
}
package code.snippet

import net.liftweb.http._
import net.liftweb.util._
import scala.xml.NodeSeq
import balakhonov.util.Validator
import code.lib._
import Helpers._
import net.liftweb.http.js.JsCmd
import net.liftweb.common.Empty

object LogInForm {
  private object name extends SessionVar("")
  private object password extends RequestVar("")
  private object referer extends RequestVar(S.referer openOr "/")

  private object authorised extends SessionVar(false)

  def loggedIn(html: NodeSeq) =
    if (authorised.get) html else NodeSeq.Empty

  def loggedOut(html: NodeSeq) =
    if (!authorised.get) html else NodeSeq.Empty

  def logIn = {
    def processLogIn(): JsCmd = {

      Validator.isValidEmail(name) match {
        case true => {
          Validator.isValidLogin(name, password) match {
            case true => {
              authorised.set(true)

              //refresh page
              refreshPage()
            }

            case _ => S.error("sign-in-pass-err", "Invalid username/password!")
          }
        }
        case _ => S.error("sign-in-mail-err", "Invalid username format!")
      }
    }

    val r = referer.is
    "name=sign-in-mail-input" #> SHtml.textElem(name) &
      "name=sign-in-pass-input" #> (
        SHtml.passwordElem(password) ++
        SHtml.hidden(() => referer.set(r)) ++ SHtml.hidden(processLogIn))
  }

  def logOut = {
    def processLogOut(): JsCmd = {
      authorised.set(false)
      name.set("")

      //refresh page
      refreshPage()
    }

    "#sign-out-button [onClick]" #> SHtml.ajaxInvoke(processLogOut)
  }

  def refreshPage() {
    S.redirectTo(referer.toString)
  }

  def getName = "*" #> name.is
}
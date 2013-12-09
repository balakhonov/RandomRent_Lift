package code.snippet

import net.liftweb.http._
import net.liftweb.http.SHtml.{ text, ajaxSubmit, password }
import net.liftweb.util._
import scala.xml.NodeSeq
import balakhonov.util.Validator
import code.lib._
import Helpers._
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds
import net.liftweb.http.js.JsCmds.SetHtml
import xml.Text
import net.liftweb.common.Empty
import db.HibernateUtil
import db.dao.UserDao
import db.mapping.User

object RegistrationForm {
  private object referer extends RequestVar(S.referer openOr "/")
  val emptyText = Text("")

  println("RegistrationForm loaded")

  private def registrate(email: String, password: String) {
    var user = new User()
    user.setEmail(email)
    user.setPassword(password)

    UserDao.save(user)
  }

  def regForm = {
    var email = ""
    var pass = ""
    var rePass = ""

    def clearErrors(): JsCmd = {
      println("clearErrors()")

      SetHtml("sign-up-mail-err", Text(email)) &
        SetHtml("sign-up-pass-err", Text(email)) &
        SetHtml("sign-up-repass-err", Text(email))
    }

    def processRegistrate(): JsCmd = {
      println("processRegistrate()")

      Validator.isValidEmail(email) match {
        case true => {
          pass.length() >= 5 match {
            case true => {
              pass.equals(rePass) match {
                case true => {
                  registrate(email, pass);
                  print("Пользователь '" + email + "' зарегистрирован.")

                  return JsCmds.Run("$('#sign-up-dialog').dialog('close');")
                }

                case _ => {
                  S.error("sign-up-repass-err", "Пароли должны совпадать!")
                  return {
                    SetHtml("sign-up-mail-err", emptyText) &
                      SetHtml("sign-up-pass-err", emptyText)
                  }
                }
              }
            }

            case _ => {
              S.error("sign-up-pass-err", "Длина пароля минимум 5 символов!")
              return {
                SetHtml("sign-up-mail-err", emptyText) &
                  SetHtml("sign-up-repass-err", emptyText)
              }
            }
          }
        }

        case _ => {
          S.error("sign-up-mail-err", "Неправильный E-Mail формат!")
          return {
            SetHtml("sign-up-pass-err", emptyText) &
              SetHtml("sign-up-repass-err", emptyText)
          }
        }
      }

      println("processRegistrate() end")
    }

    val r = referer.is
    "name=sign-up-mail-input" #> text(email, s => (email = s)) &
      "name=sign-up-pass-input" #> password(pass, s => (pass = s)) &
      "name=sign-up-re-pass-input" #> (password(rePass, s => (rePass = s)) ++ SHtml.hidden(processRegistrate))
  }
}
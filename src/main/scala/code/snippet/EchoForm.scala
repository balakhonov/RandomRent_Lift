package code.snippet

import net.liftweb.util.Helpers._
import net.liftweb.http.SHtml.{ text, ajaxSubmit }
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds.SetHtml
import xml.Text
import net.liftweb.http.SHtml.hidden

object EchoForm extends {

  def render = {

    var name = ""

    def process(): JsCmd = SetHtml("result", Text(name))

    "@name" #> text(name, s => (name = s)) &
      //    "type=submit" #> ajaxSubmit("Click Me", process)
      "button *+" #> hidden(process)
  }
}
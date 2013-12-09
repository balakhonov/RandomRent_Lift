package code.snippet

import net.liftweb.http.SHtml
import net.liftweb.util._
import Helpers._

object SnippetButton {

  println("load SnippetButton ")

  def button = {
    var textValue: String = ""
    var selectValue: String = ""

    println("load snipped " + textValue)

    def process() {
      println(textValue)
      println(selectValue)
    }
    // SHtml.onSubmit - привязывает к определнному элементу формы функцию, которая будет вызываться на сервере при сабмите формы. Функция принимает значение поля и делает с ним какую-либо логику.
    "#textField" #> SHtml.onSubmit(value => textValue = value) &
      "#selectField" #> SHtml.onSubmit(value => selectValue = value) &
      "#hidden" #> SHtml.onSubmitUnit(process) &
      "#textField [value]" #> textValue

    //    "button [onclick]" #> SHtml.ajaxInvoke(process) &
    //      "button [class]" #> "test-class"

  }
}
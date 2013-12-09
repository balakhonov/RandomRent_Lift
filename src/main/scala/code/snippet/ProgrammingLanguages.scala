package code.snippet

import net.liftweb.util.Helpers._
import net.liftweb.common.Loggable

import net.liftmodules.widgets.autocomplete.AutoComplete

class ProgrammingLanguages extends Loggable {

  val languages = List(
    "C", "C++", "Clojure", "CoffeeScript",
    "Java", "JavaScript",
    "POP-11", "Prolog", "Python", "Processing",
    "Scala", "Scheme", "Smalltalk", "SuperCollider")

  def render = {

    val default = ""

    def suggest(value: String, limit: Int) =
      languages.filter(_.toLowerCase.startsWith(value))

    def submit(value: String): Unit =
      logger.info("Value submitted: " + value)

    "#autocomplete" #> AutoComplete(default, suggest, submit)
  }

}
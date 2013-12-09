package code.snippet

import scala.collection.JavaConversions._
import scala.xml.NodeSeq
import net.liftweb.http._
import net.liftweb.util._
import java.util.Date
import Helpers._
import xml.Text

class UserSnippet {

  //  class User {
  //
  //  }

  val languages = List(
    "C", "C++", "Clojure", "CoffeeScript",
    "Java", "JavaScript",
    "POP-11", "Prolog", "Python", "Processing",
    "Scala", "Scheme", "Smalltalk", "SuperCollider")

  def showAll(in: NodeSeq): NodeSeq = {

    languages.flatMap { el => Helpers.bind("foo", in, "user-name" -> Text(el), "age" -> Text("1")) }

  }
  //
  //  def example(xhtml: NodeSeq) = {
  //    bind("peer", xhtml, "status" -> "excited")
  //  }

  def count(xhtml: NodeSeq) = {
    val pageNumber = S.param("page");
    println("pageNumber" + pageNumber);
    
    val consumption = ("Tuborg Beer", 2) :: ("Harboe pilsner", 2) :: ("Red wine", 1) :: ("Pan Galactic Gargle Blaster", 1) :: Nil
    def bindConsumption(template: NodeSeq): NodeSeq = {
    println("bindConsumption" + template);
      consumption.flatMap { case (bev, count) => bind("beverage", template, "name" -> bev, "count" -> count) }
    }
    println("count" + xhtml);
    Helpers.bind("person", xhtml, "name" -> "Mads", "consumption" -> bindConsumption _)
  }
} 
package me.rickychang.beerdata.crawler

import akka.actor.Actor
import org.jsoup.Jsoup
import org.jsoup.nodes._
import scala.collection.mutable.ListBuffer
import scala.collection.JavaConverters._

class ReviewParser extends Actor {

  def receive = {
    case ReviewPageBody(body) => sender ! new ParsedReviews(extractUserReviews(body))
  }

  private def extractReviewRows(d: Document) = d.select("tr[nowrap]")

  private def extractBeerAndRating(elmt: Element): Option[(Int, Float)] = {
    try {
      val fields = elmt.select("td").asScala
      val beerIdPat = """/(\d+)/\d+/$""".r
      val beerId = beerIdPat.findFirstIn(fields(1).select("a").first().attr("href")) match {
        case Some(beerIdPat(id)) => id.toInt
      }
      val rating = fields(5).text().toFloat
      Some((beerId, rating))
    } catch {
      case e: Exception => println(e); None
    }
  }

  private def extractUserReviews(body: String): List[(Int, Float)] = {
    val doc = Jsoup.parse(body)
    val reviewRows = extractReviewRows(doc).asScala
    val numRatings = reviewRows.length
    if (numRatings == 0) {
      Nil
    } else {
      reviewRows.flatMap(e => extractBeerAndRating(e)).toList
    }
  }

}

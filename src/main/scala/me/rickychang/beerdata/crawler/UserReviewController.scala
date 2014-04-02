package me.rickychang.beerdata.crawler

import akka.actor.Actor
import scala.concurrent.Future
import scala.concurrent.duration._
import akka.actor.ActorSystem
import akka.io.IO
import akka.pattern._
import akka.util.Timeout
import spray.http._
import spray.can.Http
import akka.actor._
import HttpMethods._
import akka.pattern.{ ask, pipe }




class UserReviewController(parser: ActorRef) extends Actor {

  implicit val system = context.system

  val userRatingsPage = "http://www.ratebeer.com/user/%d/ratings/%d/"

  def fetchPages(userId: Int, numPages: Int) = {
    for (i <- 1 to numPages) {
      IO(Http) ! HttpRequest(GET, Uri(userRatingsPage.format(userId, i)))
    }
  }

  def receive = {
    case HttpResponse(status, entity, _, _) => parser ! new ReviewPageBody(entity.asString)
    case ParsedReviews(reviews) => println("Received %d reviews.".format(reviews.length))
    case StartCrawling(userId, pages) => {
      println("Starting")
      fetchPages(userId, pages)
    }
  }



}

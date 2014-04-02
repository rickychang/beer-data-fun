package me.rickychang.beerdata.crawler

import akka.actor._

object UserReviewCrawler extends App {

  val system = ActorSystem("UserReviewCrawler")
  val parser = system.actorOf(Props(new ReviewParser))
  val userController = system.actorOf(Props(new UserReviewController(parser)))
  userController ! StartCrawling(5328, 10)



}

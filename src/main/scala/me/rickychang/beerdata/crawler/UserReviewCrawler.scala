package me.rickychang.beerdata.crawler

import akka.actor._

object UserReviewCrawler extends App {

  implicit val system = ActorSystem("UserReviewCrawler")
  val userController = system.actorOf(Props(new UserReviewController))
  userController ! Start(5328, 10)



}

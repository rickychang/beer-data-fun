package me.rickychang.beerdata

package object crawler {

  case class ParsedReviews(reviews: Seq[(Int, Float)])
  case class ReviewPageBody(body: String)
  case class StartCrawling(userId: Int, numReviewPages: Int)

}
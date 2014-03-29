
import java.io._
import org.jsoup.Jsoup
import org.jsoup.nodes._
import scala.collection.mutable.ListBuffer
import scala.collection.JavaConverters._
val userRatingsPage = "http://www.ratebeer.com/user/5328/ratings/%d/"
val reviews = new ListBuffer[(Int, Float)]()
def extractReviewRows(d: Document) = d.select("tr[nowrap]")
def extractBeerAndRating(elmt: Element): Option[(Int, Float)] = {
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
def extractUserReviews(acc: ListBuffer[(Int, Float)], page: Int): List[(Int, Float)] = {
  val doc = Jsoup.connect(userRatingsPage.format(page)).get()
  val reviewRows = extractReviewRows(doc).asScala
  val numRatings = reviewRows.length
  println(numRatings)
  if (numRatings == 0 || page > 10) {
    acc.toList
  } else {
    val newReviews = reviewRows.flatMap(e => extractBeerAndRating(e))
    acc.appendAll(newReviews)
    extractUserReviews(acc, page + 1)
  }
}
def printToFile(f: java.io.File)(op: java.io.PrintWriter => Unit) {
  val p = new java.io.PrintWriter(f)
  try { op(p) } finally { p.close() }
}

val userReviews = extractUserReviews(reviews, 1)

println(userReviews.length)


printToFile(new File("/Users/rchang/sampleReviews.tsv"))(p => {
  userReviews.foreach(e => p.println(5328 + "\t" + e._1 + "\t" + e._2))
})

import org.jsoup.Jsoup
import scala.io.Source
import java.io._
import org.jsoup.Jsoup
import scala.collection.JavaConverters._

object DumpTopStrongAleUsers extends App {

  def printToFile(f: java.io.File)(op: java.io.PrintWriter => Unit) {
    val p = new java.io.PrintWriter(f)
    try { op(p) } finally { p.close() }
  }

  val topAleReviewers = "http://www.ratebeer.com/Users/CompanyStrong.asp"
  val doc = Jsoup.connect(topAleReviewers).get()
  val reviewerLinks = doc.select("table[width=400] a").asScala.map(_.attr("href"))
  val userIdPattern = """/(\d+)/""".r
  val userIds = reviewerLinks.flatMap { l =>
    userIdPattern.findFirstIn(l) match {
      case Some(userIdPattern(id)) => Some(id.toInt)
      case None => None
    }
  }

  printToFile(new File(args(0)))(p => {
    userIds.foreach(p.println)
  })


}

package crawler

import com.typesafe.scalalogging.Logger
import org.jsoup.Jsoup

import scala.util.Using
import scala.util.control.NonFatal
import scala.concurrent.duration._
import scala.language.postfixOps

object Client {
  def loadPartData(url: String)(implicit logger: Logger): Option[String] = {
    Using.Manager { use ⇒
      val timeout = (5 seconds).toMillis.toInt
      val response = use(Jsoup.connect(s"http://$url").timeout(timeout).execute().bodyStream())
      val content = use(io.Source.fromInputStream(response)).take(40960 / 8)
      Option(content.mkString)
    }.recover {
      case NonFatal(e) ⇒
        logger.error(e.getMessage)
        None
    }.toOption.flatten
  }
}

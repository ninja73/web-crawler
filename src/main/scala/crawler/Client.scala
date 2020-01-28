package crawler

import com.typesafe.scalalogging.Logger
import org.jsoup.Jsoup

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.Using
import scala.util.control.NonFatal

object Client {
  def loadPartData(url: String)(implicit logger: Logger): Option[String] = {
    Using.Manager { use =>
      val timeout = (400 millisecond).toMillis.toInt
      val response = use(Jsoup.connect(s"http://$url").timeout(timeout).execute().bodyStream())
      val content = use(io.Source.fromInputStream(response)).take(5120)
      Option(content.mkString)
    }.recover {
      case NonFatal(e) =>
        logger.error(e.getMessage)
        None
    }.toOption.flatten
  }
}

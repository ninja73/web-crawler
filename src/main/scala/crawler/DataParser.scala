package crawler

import org.jsoup.Jsoup
import scala.jdk.CollectionConverters._

trait DataParser[I] {
  def parse(in: I): Option[Data]
}

final class DataParserOps[I](in: I)(implicit val transfer: DataParser[I]) {
  def parse: Option[Data] = transfer.parse(in)
}

object DataParser {
  implicit def toParserOps[I](in: I)(implicit p: DataParser[I]): DataParserOps[I] = new DataParserOps(in)(p)
}

final case class Html(data: String)

trait HtmlParser extends DataParser[Html] {

  def parse(in: Html): Option[Data] = {
    val doc = Jsoup.parse(in.data)
    val title = doc.title()
    val data = doc.head().getElementsByTag("meta")
      .asScala
      .foldLeft(MetaData(title = title)) {
        case (acc, el) if el.attr("name").toLowerCase == "description" =>
          acc.copy(description = Option(el.attr("content")))
        case (acc, el) if el.attr("name").toLowerCase == "keywords" =>
          acc.copy(keywords = Option(el.attr("content")))
        case (acc, _) => acc
      }
    Option(data)
  }
}

object HtmlParser {
  implicit val instance: DataParser[Html] = new HtmlParser {}
}
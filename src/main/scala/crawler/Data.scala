package crawler

sealed trait Data {
  def toTSV: String
}

final case class MetaData(title: String, keywords: Option[String] = None, description: Option[String] = None) extends Data {
  def toTSV: String = s"$title\t${keywords.getOrElse("")}\t${description.getOrElse("")}"
}
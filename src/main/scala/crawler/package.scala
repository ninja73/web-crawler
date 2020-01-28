import java.io._

import scopt.OptionParser

package object crawler {

  val cores: Int = Runtime.getRuntime.availableProcessors()

  case class Config(inFile: String = "", outFile: String = "out.tsv")

  val parser: OptionParser[Config] = new scopt.OptionParser[Config]("web-crawler") {
    opt[String]('i', "in").action((in, c) => c.copy(inFile = in)).required().text("input file")
    opt[String]('o', "out").action((o, c) => c.copy(outFile = o)).text("output file")
  }

  val urlsReader: String => BufferedReader = (file: String) => new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))
  val resultWriter: String => BufferedWriter = (file: String) => new BufferedWriter(new FileWriter(file))
}

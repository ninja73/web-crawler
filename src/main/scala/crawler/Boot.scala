package crawler

import com.typesafe.scalalogging.Logger
import crawler.Client._
import crawler.DataParser._
import crawler.HtmlParser._
import monix.eval._
import monix.execution.Scheduler.Implicits.global
import monix.reactive._

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.Using

object Boot extends App {
  implicit val logger: Logger = Logger("web-crawler")

  parser.parse(args, Config()) match {
    case Some(Config(inFile, outFile)) =>
      Using(resultWriter(outFile)) { writer =>
        val consumer = ConsumerOps.consumer(writer)

        val observable = Observable.fromLinesReader(Task(urlsReader(inFile)))
          .mapParallelUnordered(cores)(url => Task(loadPartData(url)))
          .collect { case Some(partData) => Html(partData).parse }

        val task = observable.consumeWith(consumer)
        Await.ready(task.runToFuture, Duration.Inf)
      }
    case None =>
      logger.info("Parse args failed")
  }
}

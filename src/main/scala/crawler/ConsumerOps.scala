package crawler

import java.io.Writer

import com.typesafe.scalalogging.LazyLogging
import monix.execution.Ack
import monix.execution.Ack.Continue
import monix.reactive.{Consumer, Observer}

import scala.collection.mutable.ArrayBuffer
import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

object ConsumerOps extends LazyLogging {

  val consumer: Writer => Consumer[Option[Data], Unit] = (w: Writer) => Consumer.create[Option[Data], Unit]((_, _, callback) =>
    new Observer.Sync[Option[Data]] {

      val batchSize = 1000
      val buffer = new ArrayBuffer[String](batchSize)

      def onNext(data: Option[Data]): Ack = {
        data.foreach { d =>
          buffer += d.toTSV
          if (buffer.length >= batchSize) {
            Try(w.write(buffer.mkString("\n"))) match {
              case Success(_) => buffer.clear()
              case Failure(e) => logger.error(e.getMessage)
            }
          }
        }
        Continue
      }

      def onComplete(): Unit = {
        callback.onSuccess {
          Try {
            w.write(buffer.mkString("\n"))
            w.flush()
          }.recover { case NonFatal(e) => logger.error(e.getMessage) }
        }
      }

      def onError(ex: Throwable): Unit = callback.onError(ex)
    })
}

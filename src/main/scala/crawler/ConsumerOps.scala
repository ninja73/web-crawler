package crawler

import java.io.Writer

import monix.execution.Ack
import monix.execution.Ack.Continue
import monix.reactive.{Consumer, Observer}

import scala.collection.mutable.ArrayBuffer

object ConsumerOps {

  val consumer: Writer ⇒ Consumer[Option[Data], Unit] = (w: Writer) ⇒ Consumer.create[Option[Data], Unit]((_, _, callback) ⇒
    new Observer.Sync[Option[Data]] {

      val batchSize = 1000
      val buffer = new ArrayBuffer[String](batchSize)

      def onNext(data: Option[Data]): Ack = {
        data.foreach { d ⇒
          buffer += d.toTSV
          if (buffer.length == batchSize) {
            w.write(buffer.mkString("\n"))
            buffer.clear()
          }
        }
        Continue
      }

      def onComplete(): Unit = {
        callback.onSuccess {
          w.write(buffer.mkString("\n"))
          w.flush()
        }
      }

      def onError(ex: Throwable): Unit = callback.onError(ex)
    })
}

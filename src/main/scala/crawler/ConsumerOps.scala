package crawler

import java.io.Writer

import monix.execution.Ack
import monix.execution.Ack.Continue
import monix.reactive.{Consumer, Observer}

object ConsumerOps {

  val consumer: Writer ⇒ Consumer[Option[Data], Unit] = (w: Writer) ⇒ Consumer.create[Option[Data], Unit]((_, _, callback) ⇒
    new Observer.Sync[Option[Data]] {
      def onNext(data: Option[Data]): Ack = {
        data.foreach(d ⇒ w.write(d.toTSV))
        Continue
      }

      def onComplete(): Unit = callback.onSuccess(w.flush())

      def onError(ex: Throwable): Unit = callback.onError(ex)
    })
}

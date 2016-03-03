import Messages.FastMessage
import rx.lang.scala.Observable

class GenericRepository {

  val messageClient = new MessageClient()

  def GetMessageStream[T](topic: String): Observable[T] = {
    Observable.defer[T](messageClient.getMessageStreamForTopic[T](topic))
      .repeat
      .publish
      .refCount
  }
}
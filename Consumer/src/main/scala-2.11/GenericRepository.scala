import rx.lang.scala.Observable

object GenericRepository {

  private lazy val messageClient = new MessageClient()

  def GetMessageStream[T](topic: String): Observable[T] = {
    Observable.defer[T](messageClient.getMessageStreamForTopic[T](topic))
      .repeat
      .publish
      .refCount
  }
}

//class GenericRepository {
//
//  private val messageClient = new MessageClient()
//
//  def GetMessageStream[T](topic: String): Observable[T] = {
//    Observable.defer[T](messageClient.getMessageStreamForTopic[T](topic))
//      .repeat
//      .publish
//      .refCount
//  }

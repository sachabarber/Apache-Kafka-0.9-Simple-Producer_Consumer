import rx.lang.scala.subscriptions.CompositeSubscription
import rx.lang.scala.{Subscription, Observable}

class MessageClient() {
  val consumerMap = setupMap()

  def setupMap() : Map[String, (() => GenericKafkaConsumer[AnyRef])] = {
    val map = Map[String, () => GenericKafkaConsumer[AnyRef]]()
    //TODO : you would need to add other consumers to the map here
    //TODO : you would need to add other consumers to the map here
    //TODO : you would need to add other consumers to the map here
    //TODO : you would need to add other consumers to the map here
    val updatedMap = map + (Consumers.fastMessageTopic, () =>
    {
      new FastMessageKafkaConsumer().asInstanceOf[GenericKafkaConsumer[AnyRef]]}
    )
    updatedMap
  }

  def getMessageStreamForTopic[T](topic : String) : Observable[T] = {
    Observable.create[T](observer => {
      consumerMap.get(topic) match {
        case Some(messageFactory) => {
          try {
            val streamSource = messageFactory().asInstanceOf[GenericKafkaConsumer[T]]
            streamSource.startConsuming()
            val sub = streamSource.getMessageStream().subscribe(observer)
            CompositeSubscription(sub, Subscription(streamSource.close()))
          }
          catch {
            case throwable : Throwable =>
              val st = throwable.getStackTrace()
              println(s"Got exception : $st")
              Subscription()
          }
        }
        case _ => {
            println("OH NO THATS BAD")
            observer.onCompleted()
            Subscription()
        }
      }
    }).publish.refCount
  }
}
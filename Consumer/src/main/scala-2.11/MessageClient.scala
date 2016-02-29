import rx.lang.scala.subscriptions.CompositeSubscription
import rx.lang.scala.{Subscription, Observable}

/**
  * Created by sacha on 2/25/2016.
  */
class MessageClient() {
  val consumerMap = setupMap()

  def setupMap() : Map[String, (() => GenericKafkaConsumer[AnyRef])] = {
    val map = Map[String, () => GenericKafkaConsumer[AnyRef]]()
    val updatedMap = map + (Consumers.fastMessageTopic, () => { new FastMessageKafkaConsumer().asInstanceOf[GenericKafkaConsumer[AnyRef]]})
    updatedMap
  }

  def getMessageStreamForTopic[T](topic : String) : Observable[T] = {
    Observable.create[T](observer => {
      consumerMap.get(topic) match {
        case Some(messageFactory) => {
          try {
            val streamSource = messageFactory().asInstanceOf[GenericKafkaConsumer[T]]
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
import rx.lang.scala.subscriptions.CompositeSubscription
import rx.lang.scala.{Subscription, Observable}

/**
  * Created by sacha on 2/25/2016.
  */
class MessageClient(val topic : String) {
  val consumerMap = setupMap()

  def setupMap() : Map[String, (() => RxConsumable[AnyRef])] = {
    val map = Map[String, () => RxConsumable[AnyRef]]()
    val updatedMap = map + (Consumers.fastMessageTopic, () => { new FastMessageKafkaConsumer().asInstanceOf[RxConsumable[AnyRef]]})
    updatedMap
  }

  def GetMessageStreamForTopic[T](topic : String) : Observable[T] = {
    Observable.create[T](observer => {
      consumerMap.get(topic) match {
        case Some(messageFactory) => {
          val streamSource = messageFactory().asInstanceOf[RxConsumable[T]]
          val sub = streamSource.getMessageStream().subscribe(observer)
          CompositeSubscription(sub, Subscription(streamSource.close()))
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
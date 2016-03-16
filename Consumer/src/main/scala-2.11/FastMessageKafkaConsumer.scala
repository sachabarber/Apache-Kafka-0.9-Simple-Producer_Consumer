import Messages.FastMessage
import Messages.FastMessageJsonImplicits._
import org.apache.kafka.clients.consumer.ConsumerRecord


class FastMessageKafkaConsumer
  extends GenericKafkaConsumer[FastMessage](Consumers.fastMessageTopic) {

  def pushOneOut(m : FastMessage) : Unit = {
    topicSubject.onNext(m)
  }

  override def readTopicJson(
          record : ConsumerRecord[String,String],
          topic : String) : Option[FastMessage] = {
    readJsonResponse[FastMessage](record,topic)
  }
}


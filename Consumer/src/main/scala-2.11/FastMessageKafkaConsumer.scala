import Messages.FastMessage
import Messages.FastMessageJsonImplicits._
import org.apache.kafka.clients.consumer.ConsumerRecord


class FastMessageKafkaConsumer extends GenericKafkaConsumer[FastMessage](Consumers.fastMessageTopic) {
  override def readTopicJson(record : ConsumerRecord[String,String], topic : String) : Unit = {
    readJsonResponse[FastMessage](record,topic)
  }
}


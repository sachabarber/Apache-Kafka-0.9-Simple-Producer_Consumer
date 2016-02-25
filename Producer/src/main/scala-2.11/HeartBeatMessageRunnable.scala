import java.util.Date
import play.api.libs.json.Json
import Messages.OrderPlacedMessage
import Messages.OrderPlacedMessageJsonImplicits._

class OrderPlacedMessageRunnable(val topic : String, val closeableKafkaProducer : CloseableKafkaProducer) extends MessageRunnable {
  override def produceMessage(): String = {
    Json.toJson(OrderPlacedMessage(new Date())).toString()
  }
}

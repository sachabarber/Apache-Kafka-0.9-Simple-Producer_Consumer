import java.util.Date
import play.api.libs.json.Json
import Messages.HeartBeatMessage
import Messages.HeartBeatMessageJsonImplicits._

class HeartBeatMessageRunnable(val topic : String, val closeableKafkaProducer : CloseableKafkaProducer) extends MessageRunnable {
  override def produceMessage(): String = {
    Json.toJson(HeartBeatMessage(new Date())).toString()
  }
}

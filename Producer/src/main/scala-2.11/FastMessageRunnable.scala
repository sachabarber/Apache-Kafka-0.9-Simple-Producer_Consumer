import java.util.Calendar
import play.api.libs.json.Json
import Messages.{FastMessage}
import Messages.FastMessageJsonImplicits._

class FastMessageRunnable(val topic : String, val closeableKafkaProducer : CloseableKafkaProducer) extends MessageRunnable {
  override def produceMessage(): String = {
    Json.toJson(FastMessage("FastMessage_" + Calendar.getInstance().getTime().toString(),1)).toString()
  }
}

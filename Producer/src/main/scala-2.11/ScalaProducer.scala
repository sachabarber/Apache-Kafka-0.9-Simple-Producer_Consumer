import org.apache.kafka.clients.producer.{KafkaProducer,ProducerRecord}
import java.util.Properties
import com.google.common.io.Resources
import play.api.libs.json.{JsValue, Json}
import Messages.{FastMessage,SummaryMarkerMessage}
import Messages.FastMessageJsonImplicits._
import Messages.SummaryMarkerMessageJsonImplicits._

object ScalaProducer {
  def main(args: Array[String]): Unit = {
    val scalaProducer = new ScalaProducer()
    scalaProducer.run(args)
  }
}


/**
  * This producer will send a bunch of messages to topic "fast-messages". Every so often,
  * it will send a message to "slow-messages". This shows how messages can be sent to
  * multiple topics. On the receiving end, we will see both kinds of messages but will
  * also see how the two topics aren't really synchronized.
  */
class ScalaProducer {

  def run(args: Array[String]) : Unit = {

    println("Press enter to start producer")
    scala.io.StdIn.readLine()

    var producer : KafkaProducer[String, String] = null
    try {
      val props = Resources.getResource("producer.props").openStream()
      val properties = new Properties()
      properties.load(props)
      producer = new KafkaProducer[String,String](properties)
      var jsonText : String = ""
      for( i <- 0 to 1000000) {
        // send lots of messages
        jsonText = Json.toJson(FastMessage(s"FastMessage_$i", i)).toString()
        producer.send(new ProducerRecord[String, String]("fast-messages", jsonText))

        // every so often send to a different topic (i.e 'summary-markers')
        if (i % 1000 == 0) {
          jsonText = Json.toJson(FastMessage(s"FastMessage_$i", i)).toString()
          producer.send(new ProducerRecord[String, String]("fast-messages", jsonText))

          jsonText = Json.toJson(SummaryMarkerMessage(s"SummaryMarkerMessage_$i", i)).toString()
          producer.send(new ProducerRecord[String, String]("summary-markers", jsonText))
        }
        producer.flush()
        println("\"Sent msg number : %s", i)
      }
    }
    catch {
        case throwable : Throwable =>
          val st = throwable.getStackTrace()
          println(s"Got exception : $st")
    }
    finally {
      producer.close()
    }
  }
}

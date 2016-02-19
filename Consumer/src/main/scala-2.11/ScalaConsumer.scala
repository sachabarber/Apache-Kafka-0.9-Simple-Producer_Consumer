import com.google.common.io.Resources
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.util.Arrays
import java.util.Properties
import java.util.Random
import play.api.libs.json.{Reads, Json}
import Messages.{FastMessage, SummaryMarkerMessage}
//import Messages.{TestMessage, FastMessage, SummaryMarkerMessage}
import Messages.FastMessageJsonImplicits._
import Messages.SummaryMarkerMessageJsonImplicits._
//import Messages.TestMessageJsonImplicits._



object ScalaConsumer {
  def main(args: Array[String]): Unit = {
    val scalaConsumer = new ScalaConsumer()
    scalaConsumer.run(args)
  }
}

///**
//  * This program reads messages from two topics.
//  * Whenever a message is received on "slow-messages", the stats are dumped.
//  */
class ScalaConsumer {

  def run(args: Array[String]): Unit = {

    // and the consumer
    var consumer : KafkaConsumer[String, String] = null
    try {

      val props = Resources.getResource("consumer.props").openStream()
      val properties = new Properties()
      properties.load(props)
      if (properties.getProperty("group.id") == null) {
        properties.setProperty("group.id", "group-" + new Random().nextInt(100000))
      }
      consumer = new KafkaConsumer[String, String](properties)
      consumer.subscribe(Arrays.asList("fast-messages", "summary-markers"))
      var timeouts = 0

      while (true) {

        println("consumer loop running, wait for messages")
        // read records with a short timeout. If we time out, we don't really care.
        val records : ConsumerRecords[String, String] = consumer.poll(200)
        val recordCount = records.count()
        if (recordCount == 0) {
          timeouts = timeouts + 1
        } else {

          println(s"Got $recordCount records after $timeouts timeouts\n")
          timeouts = 0
        }

        val iter   = records.iterator()
        while(iter.hasNext()) {
          val record : ConsumerRecord[String,String] = iter.next()
          val topic = record.topic()
          topic match {
            case "fast-messages" => {
              readJsonResponse[FastMessage](record,"FastMessage")
            }
            case "summary-markers" => {
              readJsonResponse[SummaryMarkerMessage](record,"SummaryMarkerMessage")
            }
            case _ => {
              println("Unknown message seen.....crazy stuff")
            }
          }
        }
      }
    }
    catch {
      case throwable : Throwable =>
        val st = throwable.getStackTrace()
        println(s"Got exception : $st")
    }
    finally {
      consumer.close()
    }
  }

  def readJsonResponse[T](record: ConsumerRecord[String,String], topicDescription : String)(implicit reader: Reads[T]) : Unit = {
    try {
      println(s"$topicDescription >")
      Json.parse(record.value()).asOpt[T].map(rm => println(rm))
    }
    catch {
      case throwable: Throwable =>
        val st = throwable.getStackTrace()
        println(s"readJsonResponse() Got exception : $st")
    }
  }


//  def processFastMessage(record: ConsumerRecord[String,String], topicDescription : String): Unit = {
//    try {
//      println(s"$topicDescription >")
//      Json.parse(record.value()).asOpt[Messages.FastMessage].map(rm => println(rm))
//    }
//    catch {
//      case throwable: Throwable =>
//        val st = throwable.getStackTrace()
//        println(s"processMessage() Got exception : $st")
//    }
//  }

//  def processSummaryMarkerMessage(record: ConsumerRecord[String,String], topicDescription : String): Unit = {
//    try {
//      println(s"$topicDescription >")
//      Json.parse(record.value()).asOpt[Messages.SummaryMarkerMessage].map(rm => println(rm))
//    }
//    catch {
//      case throwable: Throwable =>
//        val st = throwable.getStackTrace()
//        println(s"processMessage() Got exception : $st")
//    }
//  }
}
import java.io.Closeable
import Messages.FastMessage
import com.google.common.io.Resources
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.util.Arrays
import java.util.Properties
import java.util.Random
import play.api.libs.json.{Reads, Json}
import rx.lang.scala.Observable
import rx.lang.scala.subjects.PublishSubject

///**
//  * This program reads messages from two topics.
//  * Whenever a message is received on "slow-messages", the stats are dumped.
//  */
abstract class GenericKafkaConsumer[T](topic : String) extends Closeable {
  val topicSubject = PublishSubject.apply[T]()
  var consumer : KafkaConsumer[String, String] = null
  var closeableKafkaConsumer : CloseableKafkaConsumer = null


  def run(): Unit = {




    // and the consumer
    try {

      val props = Resources.getResource("consumer.props").openStream()
      val properties = new Properties()
      properties.load(props)
      if (properties.getProperty("group.id") == null) {
        properties.setProperty("group.id", "group-" + new Random().nextInt(100000))
      }
      consumer = new KafkaConsumer[String, String](properties)
      closeableKafkaConsumer  = new CloseableKafkaConsumer(consumer)
      closeableKafkaConsumer.consumer.subscribe(Arrays.asList(topic))
      var timeouts = 0

      println(s"THE TOPIC IS : $topic")

      while (true) {

        println("consumer loop running, wait for messages")
        // read records with a short timeout. If we time out, we don't really care.
        val records : ConsumerRecords[String, String] = closeableKafkaConsumer.consumer.poll(200)
        val recordCount = records.count()
        if (recordCount == 0) {
          timeouts = timeouts + 1
        } else {

          println(s"Got $recordCount records after $timeouts timeouts\n")
          timeouts = 0
        }

        val it = records.iterator()
        while(it.hasNext()) {
          val record : ConsumerRecord[String,String] = it.next()
          val recordTopic = record.topic()
          if(recordTopic == topic) {
            val message = readTopicJson(record,topic)
            message.map(x =>  {
              println(s"Message about to be RX published is $x")
              topicSubject.onNext(x)
              consumer.commitSync()
            })
          }
          else {
            println(s"Unknown message seen for topic '$recordTopic' .....crazy stuff")
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
      if(closeableKafkaConsumer != null) {
        closeableKafkaConsumer.closeConsumer()
      }
    }
  }

  protected def readJsonResponse[T](record: ConsumerRecord[String,String], topicDescription : String)(implicit reader: Reads[T]) : Option[T] = {
    try {
      println(s"$topicDescription >")
      Some(Json.parse(record.value()).as[T])
    }
    catch {
      case throwable: Throwable =>
        val st = throwable.getStackTrace()
        println(s"Got exception : $st")
        None
    }
  }

  def getMessageStream() : Observable[T]  = {
    topicSubject.asInstanceOf[Observable[T]]
  }

  override def close() : Unit = {
    if(closeableKafkaConsumer != null) {
      closeableKafkaConsumer.closeConsumer()
    }
  }


  def readTopicJson(record : ConsumerRecord[String,String], topic : String) : Option[T]

}
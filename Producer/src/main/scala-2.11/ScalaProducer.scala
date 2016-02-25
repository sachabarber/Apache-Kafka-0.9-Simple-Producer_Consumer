import org.apache.kafka.clients.producer.KafkaProducer
import java.util.Properties
import com.google.common.io.Resources
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object ScalaProducer {
  def main(args: Array[String]): Unit = {
    val scalaProducer = new ScalaProducer()
    scalaProducer.run(args)
  }
}

/**
  * This producer will send a bunch of messages to topic "fast-messages". Every so often,
  * it will send a message to "heartBeat-messages". This shows how messages can be sent to
  * multiple topics. On the receiving end, we will see both kinds of messages but will
  * also see how the two topics aren't really synchronized.
  */
class ScalaProducer {

  def run(args: Array[String]) : Unit = {

    println("Press enter to start producer")
    scala.io.StdIn.readLine()

    var producer : KafkaProducer[String, String] = null
    var closeableKafkaProducer : CloseableKafkaProducer = null

    try {
      val props = Resources.getResource("producer.props").openStream()
      val properties = new Properties()
      properties.load(props)
      producer = new KafkaProducer[String,String](properties)
      closeableKafkaProducer = new CloseableKafkaProducer(producer)

      //"fast-messages"
      val fastMessageRunnable = new FastMessageRunnable("fast-messages",closeableKafkaProducer)
      val fastMessageRunnerScheduler = Executors.newSingleThreadScheduledExecutor()
      fastMessageRunnerScheduler.scheduleAtFixedRate(fastMessageRunnable, 0, 3, TimeUnit.SECONDS);

      //"order-placed-messages"
      val orderPlacedMessageRunnable = new OrderPlacedMessageRunnable("order-placed-messages",closeableKafkaProducer)
      val orderPlacedMessageScheduler = Executors.newSingleThreadScheduledExecutor()
      orderPlacedMessageScheduler.scheduleAtFixedRate(orderPlacedMessageRunnable, 0, 1, TimeUnit.SECONDS);

      println("producing messages")
      scala.io.StdIn.readLine()

    }
    catch {
        case throwable : Throwable =>
          val st = throwable.getStackTrace()
          println(s"Got exception : $st")
    }
    finally {
      if(closeableKafkaProducer != null) {
        closeableKafkaProducer.closeProducer()
      }
    }
  }
}

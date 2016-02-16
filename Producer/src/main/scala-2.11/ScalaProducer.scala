import org.apache.kafka.clients.producer.{KafkaProducer,ProducerRecord}

import java.io.{IOException, InputStream}
import java.util.Properties
import com.google.common.io.Resources

object ScalaProducer {
  def main(args: Array[String]): Unit = {
    val scalaProducer = new ScalaProducer()
    scalaProducer.run(args)
  }
}

class ScalaProducer {

  def run(args: Array[String]) : Unit = {

    var producer : KafkaProducer[String, String] = null
    try {
      val props = Resources.getResource("producer.props").openStream()
      val properties = new Properties()
      properties.load(props)
      producer = new KafkaProducer[String,String](properties)
    }

    try
    {
      for( i <- 0 to 1000000) {
        // send lots of messages
        producer.send(new ProducerRecord[String, String](
                "fast-messages",
                String.format("{\"type\":\"test\", \"t\":%.3f, \"k\":%d}", System.nanoTime() * 1e-9, i)))

        // every so often send to a different topic
        if (i % 1000 == 0) {
          producer.send(new ProducerRecord[String, String](
                "fast-messages",
                String.format("{\"type\":\"marker\", \"t\":%.3f, \"k\":%d}", System.nanoTime() * 1e-9, i)))

          producer.send(new ProducerRecord[String, String](
                "summary-markers",
                String.format("{\"type\":\"other\", \"t\":%.3f, \"k\":%d}", System.nanoTime() * 1e-9, i)))
        }
        producer.flush()
        println("\"Sent msg number : %s", i)
      }
    }
    catch
      {
        case throwable : Throwable =>
          println("Got exception : %s", throwable.getStackTrace())
      }
    finally
    {
      producer.close()
    }
  }
}

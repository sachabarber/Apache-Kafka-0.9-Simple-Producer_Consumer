import org.apache.kafka.clients.producer.ProducerRecord

abstract class MessageRunnable extends Runnable {

  val topic : String
  val closeableKafkaProducer : CloseableKafkaProducer

  override def run() : Unit = {
    try {
      val message = produceMessage()
      println("running > " + message)
      closeableKafkaProducer.producer.send(new ProducerRecord[String, String](topic, message))
      closeableKafkaProducer.producer.flush()
      println(s"Sent message on topic '$topic'")
    }
    catch {
      case throwable: Throwable => {
        val errMessage = throwable.getMessage
        println(s"Got exception : $errMessage")
        closeableKafkaProducer.closeProducer()
      }
    }
  }

  def produceMessage() : String
}
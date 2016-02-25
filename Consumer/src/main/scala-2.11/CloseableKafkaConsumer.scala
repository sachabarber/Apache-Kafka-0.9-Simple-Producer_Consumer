import org.apache.kafka.clients.consumer.KafkaConsumer

class CloseableKafkaConsumer(val consumer : KafkaConsumer[String, String]) {
  var isClosed : Boolean = false

  def closeConsumer() : Unit = {
    if(!isClosed) {
      consumer.close()
    }
  }
}

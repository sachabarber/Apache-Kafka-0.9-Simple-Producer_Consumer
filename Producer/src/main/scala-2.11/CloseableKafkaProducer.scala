import org.apache.kafka.clients.producer.KafkaProducer

class CloseableKafkaProducer(val producer : KafkaProducer[String, String]) {
  var isClosed : Boolean = false

  def closeProducer() : Unit = {
    if(!isClosed) {
      producer.close()
    }
  }
}

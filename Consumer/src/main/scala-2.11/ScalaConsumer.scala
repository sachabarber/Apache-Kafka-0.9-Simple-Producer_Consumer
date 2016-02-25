object ScalaConsumer {
  def main(args: Array[String]): Unit = {
    //val scalaConsumer = new GenericKafkaConsumer[FastMessage](Consumers.fastMessageTopic)
    //scalaConsumer.run()

    val scalaConsumer = new FastMessageKafkaConsumer()
  }
}
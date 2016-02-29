object ScalaConsumer {

  def main(args: Array[String]): Unit = {
   val scalaConsumer = new FastMessageKafkaConsumer()
   val subs = scalaConsumer.getMessageStream()
      .subscribe(x => {
      println(s"RX SUBJECT stuff working, got this FAST MESSAGE : $x")
    })
    scalaConsumer.startConsuming()
  }
}
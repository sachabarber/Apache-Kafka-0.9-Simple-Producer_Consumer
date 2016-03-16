import Messages.FastMessage

object ScalaConsumer {

  def main(args: Array[String]): Unit = {

    //val repo = new GenericRepository()
    var subs = GenericRepository.GetMessageStream[FastMessage](Consumers.fastMessageTopic)
      .subscribe(x => {
        println(s"RX SUBJECT stuff working, got this FAST MESSAGE : $x")
      })
  }
}
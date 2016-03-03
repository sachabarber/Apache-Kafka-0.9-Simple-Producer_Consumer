import Messages.FastMessage

object ScalaConsumer {

  def main(args: Array[String]): Unit = {

    val repo = new GenericRepository()
    var subs = repo.GetMessageStream[FastMessage](Consumers.fastMessageTopic)
      .subscribe(x => {
        println(s"RX SUBJECT stuff working, got this FAST MESSAGE : $x")
      })




//    val messageClient = new MessageClient()
//    var subs = messageClient.getMessageStreamForTopic[FastMessage](Consumers.fastMessageTopic)
//      .subscribe(x => {
//              println(s"RX SUBJECT stuff working, got this FAST MESSAGE : $x")
//            })
//
//    Thread.sleep(2000)
//    println("================")
//    subs.unsubscribe()
//    println(" UN SUBSCRIBED ")
//
//    Thread.sleep(2000)
//    println("================")
//    println(" SUBSCRIBED AGAIN ")
//    subs = messageClient.getMessageStreamForTopic[FastMessage](Consumers.fastMessageTopic)
//      .subscribe(x => {
//        println(s"RX SUBJECT stuff working, got this FAST MESSAGE : $x")
//      })
  }
}
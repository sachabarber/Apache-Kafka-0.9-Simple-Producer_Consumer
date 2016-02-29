import java.util.concurrent.{Executors, ExecutorService}

import Messages.FastMessage

object ScalaConsumer {


  def main(args: Array[String]): Unit = {
   val scalaConsumer = new FastMessageKafkaConsumer()
   val subs = scalaConsumer.getMessageStream()
      //.subscribeOn(rx.lang.scala.schedulers.ExecutionContextScheduler(scala.concurrent.ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())))
      .subscribe(x => {
      println(s"RX SUBJECT stuff working, got this FAST MESSAGE : $x")
    })

    scalaConsumer.run()




//    scalaConsumer.pushOneOut(FastMessage("TEST XXXXX",2))
//    scala.io.StdIn.readLine()

//    val messageClient = new MessageClient()
//    messageClient.getMessageStreamForTopic(Consumers.fastMessageTopic).subscribe(x => {
//      println(s"RX stuff working, got this FAST MESSAGE : $x")
//    })

  }
}
package Messages

import play.api.libs.json.Json

//==============================
// serialize to json
//==============================
// val json = Json.toJson(HeartBeatMessage(new java.util.Date()))
//==============================
// deserialize
//==============================
// var otherHB = json.as[HeartBeatMessage]
// val timeStamp = otherHB.timeOfMessage
// println(s"The name of the other heartbeat one is timeStamp")
case class HeartBeatMessage(timeOfMessage: java.util.Date)

object HeartBeatMessageJsonImplicits {

  implicit val heartBeatMessageFmt = Json.format[HeartBeatMessage]
  implicit val heartBeatMessageWrites = Json.writes[HeartBeatMessage]
  implicit val heartBeatMessageReads = Json.reads[HeartBeatMessage]
}
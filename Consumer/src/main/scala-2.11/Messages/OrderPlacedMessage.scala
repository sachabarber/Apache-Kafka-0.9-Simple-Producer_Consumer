package Messages

import play.api.libs.json.Json

//==============================
// serialize to json
//==============================
// val json = Json.toJson(OrderPlacedMessage(new java.util.Date()))
//==============================
// deserialize
//==============================
// var theOrder = json.as[OrderPlacedMessage]
// val timeStamp = theOrder.timeOfMessage
// println(s"The name of the other is $timeStamp")
case class OrderPlacedMessage(timeOfMessage: java.util.Date)

object OrderPlacedMessageJsonImplicits {
  implicit val orderPlacedMessageFmt = Json.format[OrderPlacedMessage]
  implicit val orderPlacedMessageWrites = Json.writes[OrderPlacedMessage]
  implicit val orderPlacedMessageReads = Json.reads[OrderPlacedMessage]
}
package Messages

import play.api.libs.json.Json

//==============================
// serialize to json
//==============================
// val json = Json.toJson(FastMessage("test", 30))
//==============================
// deserialize
//==============================
// var otherFast = json.as[FastMessage]
// val name = otherFast.name
// println(s"The name of the other fast one is $name")
case class FastMessage(name: String, number: Int)

object FastMessageJsonImplicits {
  implicit val fastMessageFmt = Json.format[FastMessage]
  implicit val fastMessageWrites = Json.writes[FastMessage]
  implicit val fastMessageReads = Json.reads[FastMessage]
}
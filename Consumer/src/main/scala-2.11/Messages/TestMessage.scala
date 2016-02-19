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
case class TestMessage(name: String, number: Int)

object TestMessageJsonImplicits {
  implicit val testMessageFmt = Json.format[TestMessage]
  implicit val testMessageWrites = Json.writes[TestMessage]
  implicit val testMessageReads = Json.reads[TestMessage]
}
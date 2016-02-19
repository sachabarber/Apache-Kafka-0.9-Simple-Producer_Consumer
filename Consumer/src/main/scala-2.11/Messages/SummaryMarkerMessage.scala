package Messages

import play.api.libs.json.Json

//==============================
// serialize to json
//==============================
// val json = Json.toJson(SummaryMarkerMessage("test", 30))
//==============================
// deserialize
//==============================
// var otherMarker = json.as[SummaryMarkerMessage]
// val name = otherMarker.name
// println(s"The name of the other marker one is $name")
case class SummaryMarkerMessage(name: String, number: Int)

object SummaryMarkerMessageJsonImplicits {
  implicit val summaryMarkerMessageFmt = Json.format[SummaryMarkerMessage]
  implicit val summaryMarkerMessageWrites = Json.writes[SummaryMarkerMessage]
  implicit val summaryMarkerMessageReads = Json.reads[SummaryMarkerMessage]
}
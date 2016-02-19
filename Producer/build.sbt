name := "ScalaProducer"

version := "1.0"

scalaVersion := "2.11.7"


libraryDependencies ++=Seq(
    "org.apache.kafka"              %       "kafka-clients"               %   "0.9.0.0",
    "com.typesafe.play"             %       "play-json_2.11"              %   "2.4.6"       ,
    "com.google.guava"              %       "guava"                       %   "19.0"
)


name := "ScalaProducer"

version := "1.0"

scalaVersion := "2.11.7"


libraryDependencies ++=Seq(
  "org.apache.kafka"              %       "kafka-clients"     %   "0.9.0.0",
  "com.fasterxml.jackson.core"    %       "jackson-databind"  %   "2.7.1-1",
  "com.google.guava"              %       "guava"             %   "19.0"
)


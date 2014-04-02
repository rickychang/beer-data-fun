organization := "me.rickychang"

name := "beer-data-fun"

version := "0.1-SNAPSHOT"

resolvers += "spray repo" at "http://repo.spray.io"

libraryDependencies ++= Seq(
  "org.jsoup" % "jsoup" % "1.7.2",
  "io.spray" % "spray-can" % "1.3.1",
  "com.typesafe.akka" %% "akka-actor" % "2.3.1"
)



name := "bot"
scalaVersion := "2.12.4"
version := "1.0"
scalacOptions := Seq("-feature", "-unchecked", "-deprecation", "-encoding", "utf8")


resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies ++= Seq(
  "com.github.mukel" %% "telegrambot4s" % "v3.0.4",
  "org.iq80.leveldb" % "leveldb" % "0.9",
  "org.scalactic" %% "scalactic" % "3.0.0",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  "org.scaldi" %% "scaldi" % "0.5.8",
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  "com.typesafe.akka" %% "akka-slf4j" % "2.5.7",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.4.0",
  "com.twitter" %%	"chill" %	"0.9.2"
)

name := "bot"

version := "1.0"

scalaVersion := "2.11.8"
enablePlugins(JavaAppPackaging)

resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies += "com.github.mukel" %% "telegrambot4s" % "v1.2.1"
libraryDependencies += "org.iq80.leveldb" % "leveldb" % "0.9"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"
libraryDependencies += "org.scaldi" %% "scaldi" % "0.5.7"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.7"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.4.0"




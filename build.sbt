name := "bot"

version := "1.0"

scalaVersion := "2.11.8"

resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies += "com.github.mukel" %% "telegrambot4s" % "v1.2.1"
libraryDependencies += "org.iq80.leveldb" % "leveldb" % "0.9"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"
libraryDependencies += "org.scaldi" %% "scaldi" % "0.5.7"
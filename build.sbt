
name := "pimpv_bot"
scalaVersion := "2.12.6"
version := "1.0"
scalacOptions := Seq("-feature", "-unchecked", "-deprecation", "-encoding", "utf8")


resolvers ++= Seq(
  "jitpack" at "https://jitpack.io",
  Resolver.sonatypeRepo("staging"),
  DefaultMavenRepository
)

libraryDependencies ++= Seq(
  "com.bot4s" %% "telegram-core" % "4.0.0-RC1",
  "com.bot4s" %% "telegram-akka" % "4.0.0-RC1",
  "org.iq80.leveldb" % "leveldb" % "0.9",
  "org.scalactic" %% "scalactic" % "3.0.0",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  "org.scaldi" %% "scaldi" % "0.5.8",
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  "com.typesafe.akka" %% "akka-slf4j" % "2.5.7",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0",
  "com.twitter" %%	"chill" %	"0.9.2"
)

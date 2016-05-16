name := "TLT"

version := "1.0"

scalaVersion := "2.11.8"

resolvers += Resolver.sonatypeRepo("snapshots")
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

libraryDependencies += "info.mukel" %% "telegrambot4s" % "1.0.3-SNAPSHOT"

// For Akka 2.4.x and Scala 2.11.x
libraryDependencies += "com.enragedginger" %% "akka-quartz-scheduler" % "1.5.0-akka-2.4.x"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4-SNAPSHOT"

libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.6.4"
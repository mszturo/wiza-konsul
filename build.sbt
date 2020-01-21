name := """play-scala-slick-example"""

version := "2.7.x"

lazy val root = (project in file(".")).enablePlugins(PlayScala).enablePlugins(PlayJava)

scalaVersion := "2.13.0"

lazy val macwire = Seq(
  "com.softwaremill.macwire" %% "macros" % "2.3.3" % "provided",
  "com.softwaremill.macwire" %% "macrosakka" % "2.3.3" % "provided",
  "com.softwaremill.macwire" %% "util" % "2.3.3",
  "com.softwaremill.macwire" %% "proxy" % "2.3.3"
)

libraryDependencies += guice
libraryDependencies += "com.typesafe.play" %% "play-slick" % "4.0.2"
libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "4.0.2"

libraryDependencies += "com.h2database" % "h2" % "1.4.199"

libraryDependencies ++= macwire

libraryDependencies += specs2 % Test

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-Xfatal-warnings"
)

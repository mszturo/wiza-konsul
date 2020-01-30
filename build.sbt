name := """play-scala-slick-example"""

version := "2.7.x"

lazy val root = (project in file(".")).enablePlugins(PlayScala).enablePlugins(PlayJava)

scalaVersion := "2.13.0"

libraryDependencies += guice
libraryDependencies += "com.typesafe.play" %% "play-slick" % "4.0.2"
libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "4.0.2"

libraryDependencies += "com.h2database" % "h2" % "1.4.199"

libraryDependencies += "junit" % "junit" % "4.13"
libraryDependencies += "org.mockito" % "mockito-all" % "1.10.19" % Test

libraryDependencies += specs2 % Test

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-Xfatal-warnings"
)

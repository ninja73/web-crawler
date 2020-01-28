name := "web-crawler"

version := "0.1"

scalaVersion := "2.13.1"

scalacOptions ++= Seq("-unchecked", "-deprecation")

assemblyOutputPath in assembly := file(s"assembly/${name.value}-${version.value}.jar")

//logging
libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
)

//cli
libraryDependencies ++= Seq("com.github.scopt" %% "scopt" % "3.7.1")

//multi thread
libraryDependencies += "io.monix" %% "monix" % "3.1.0"

//parser
libraryDependencies +=  "org.jsoup" % "jsoup" % "1.12.1"

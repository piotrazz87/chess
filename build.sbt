name := "chess"

version := "0.1"

scalaVersion := "2.13.3"

lazy val catsVersion="2.1.1"
lazy val scalaTestVersion="3.1.1"
lazy val logbackClassicVersion="1.2.3"
lazy val scalaLoggingVersion="3.9.2"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature"
)

libraryDependencies++=Seq(
  "org.typelevel" %% "cats-core" % catsVersion,
  "org.typelevel" %% "cats-effect" % "2.1.4",
  "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,
  "ch.qos.logback" % "logback-classic" % logbackClassicVersion,
  "org.scalatest" %% "scalatest" % scalaTestVersion % Test
)


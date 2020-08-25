name := "chess"

version := "0.1"

scalaVersion := "2.13.3"

lazy val catsVersion = "2.1.1"
lazy val scalaTestVersion = "3.1.1"
lazy val logbackClassicVersion = "1.2.3"
lazy val scalaLoggingVersion = "3.9.2"
lazy val catsEffectVersion = "2.1.4"
lazy val mockitoVersion = "3.5.5"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature"
)

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % catsVersion,
  "org.typelevel" %% "cats-effect" % catsEffectVersion,
  "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,
  "ch.qos.logback" % "logback-classic" % logbackClassicVersion,
  "org.mockito" % "mockito-core" % mockitoVersion % Test,
  "org.scalatest" %% "scalatest" % scalaTestVersion % Test
)

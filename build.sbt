name := "chess"

version := "0.1"

scalaVersion := "2.13.3"

lazy val catsVersion="2.1.1"
lazy val scalaTestVersion="3.1.1"

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
  "org.scalatest" %% "scalatest" % scalaTestVersion % Test
)


organization in ThisBuild := "co.flagly"
version      in ThisBuild := "0.1.0-SNAPSHOT"
scalaVersion in ThisBuild := "2.12.8"

lazy val `flagly-core` = project in file(".")

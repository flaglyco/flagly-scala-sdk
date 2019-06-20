organization in ThisBuild := "co.flagly"
version      in ThisBuild := "0.1.0-SNAPSHOT"
scalaVersion in ThisBuild := "2.12.8"

lazy val `flagly-scala-sdk` = (project in file("."))
  .settings(
    libraryDependencies ++= Seq(
      "co.flagly"             %% "flagly-core"                      % "0.1.0-SNAPSHOT",
      "com.softwaremill.sttp" %% "core"                             % "1.6.0",
      "com.softwaremill.sttp" %% "async-http-client-backend-future" % "1.6.0",
      "com.softwaremill.sttp" %% "play-json"                        % "1.6.0",
      "org.scalatest"         %% "scalatest"                        % "3.0.5" % Test
    )
  )

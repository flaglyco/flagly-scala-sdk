lazy val `flagly-scala-sdk` = (project in file("."))
  .settings(
    libraryDependencies ++= Seq(
      "co.flagly"                     % "flagly-core"                      % "0.2.0",
      "com.softwaremill.sttp.client" %% "core"                             % "2.0.0-RC3",
      "com.softwaremill.sttp.client" %% "async-http-client-backend-future" % "2.0.0-RC3",
      "com.softwaremill.sttp.client" %% "circe"                            % "2.0.0-RC3",
      "dev.akif"                     %% "e-circe"                          % "0.2.3",
      "org.scalatest"                %% "scalatest"                        % "3.1.0" % Test
    )
  )

resolvers += Resolver.jcenterRepo

description          in ThisBuild := "Scala SDK of Flagly"
homepage             in ThisBuild := Some(url("https://flagly.co"))
startYear            in ThisBuild := Some(2019)
licenses             in ThisBuild := Seq("MIT" -> url("https://opensource.org/licenses/MIT"))
organization         in ThisBuild := "co.flagly"
organizationName     in ThisBuild := "Flagly"
organizationHomepage in ThisBuild := Some(url("https://flagly.co"))
developers           in ThisBuild := List(Developer("makiftutuncu", "Mehmet Akif Tütüncü", "m.akif.tutuncu@gmail.com", url("https://akif.dev")))
scmInfo              in ThisBuild := Some(ScmInfo(url("https://github.com/flaglyco/flagly-scala-sdk"), "git@github.com:makiftutuncu/flagly-scala-sdk.git"))

publishMavenStyle       := true
exportJars              := true
publishArtifact in Test := false
pomIncludeRepository    := { _ => false }
bintrayOrganization     := Some("flaglyco")
bintrayRepository       := "flagly-scala-sdk"

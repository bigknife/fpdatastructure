val circeVersion = "0.9.3"
lazy val fpdatastructure = project.in(file(""))
  .settings(
    organization := "fssi",
    scalaVersion := "2.12.4",
    crossScalaVersions := Seq("2.11.12", "2.12.4"),
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5" % "test",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser"
    ).map(_ % circeVersion)
  )

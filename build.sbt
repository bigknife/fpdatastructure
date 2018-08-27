lazy val fpdatastructure = Project("fpdatastructure", file(""))
  .settings(
    organization := "fssi",
    scalaVersion := "2.12.4",
    crossScalaVersions := Seq("2.11.12", "2.12.4"),
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5" % "test",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
  )

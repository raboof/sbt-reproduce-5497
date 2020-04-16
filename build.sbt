//codegen
val a = Project("a", file("a"))
  .settings(
    crossScalaVersions := Seq("2.12.10"),
  )

//runtime
val b = Project("b", file("b"))
  .settings(
    crossScalaVersions := Seq("2.12.10", "2.13.1"),
    compile in Compile := {
      (fullClasspath in Compile in ProjectRef(file("."), "c")).value
      (compile in Compile).value
    }
  )

//sbt-akka-grpc
val c = Project("c", file("c"))
  .enablePlugins(SbtPlugin)
  .dependsOn(a)
  .settings(
    crossScalaVersions := Seq("2.12.10"),
    libraryDependencies += 
      // some library that is not available on 2.13
      "com.typesafe.akka" %% "akka-actor" % "2.5.0"
  )

val root = Project("reproduce-5497", file("."))
  .aggregate(a, b, c)
  .settings(
    crossScalaVersions := Nil
  )

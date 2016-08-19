import ScalaJSHelper._

lazy val baseSettings = Seq(
  scalaVersion := "2.11.8",
  scalacOptions := Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-unchecked",
    "-Xfatal-warnings",
    "-Xlint",
    "-Ybackend:GenBCode",
    "-Ydelambdafy:method",
    "-Yinline-warnings",
    "-Yno-adapted-args",
    "-Yrangepos",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Ywarn-unused-import",
    "-Ywarn-value-discard"
  )
)

lazy val root = project.in(file("."))
  .settings(baseSettings)
  .aggregate(ace, client)
  .dependsOn(ace, client)

lazy val ace = project
  .settings(baseSettings)
  .settings(
    scalacOptions -= "-Ywarn-dead-code",
    jsDependencies ++= Seq(
      "org.webjars.bower" % "ace-builds" % "1.2.3" % "compile"// 1.2.5
        / "src/ace.js"
        minified "src-min/ace.js"
        commonJSName "Ace",
      "org.webjars.bower" % "ace-builds" % "1.2.3" % "compile"// 1.2.5
        / "src/mode-scala.js"
        minified "src-min/mode-scala.js"
        commonJSName "AceScala"
    ),
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.1"
    )
  )
  .enablePlugins(ScalaJSPlugin)

lazy val client = project
  .settings(baseSettings)
  .settings(
    jsDependencies ++= Seq( 
     "org.webjars.bower" % "react" % "15.2.1" % "compile" 
        / "react-with-addons.js"
        minified "react-with-addons.min.js"
        commonJSName "React",
      "org.webjars.bower" % "react" % "15.2.1" % "compile"
        /         "react-dom.js"
        minified  "react-dom.min.js"
        dependsOn "react-with-addons.js"
        commonJSName "ReactDOM",
      "org.webjars.bower" % "react" % "15.2.1" % "compile"
        /         "react-dom-server.js"
        minified  "react-dom-server.min.js"
        dependsOn "react-dom.js"
        commonJSName "ReactDOMServer"
    ),
    libraryDependencies ++= Seq(
      "com.github.japgolly.scalacss"      %%% "core"      % "0.4.1",
      "com.github.japgolly.scalacss"      %%% "ext-react" % "0.4.1",
      "com.github.japgolly.scalajs-react" %%% "extra"     % "0.11.1"
    )

  )
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(ace)

lazy val web = project
  .settings(baseSettings)
  .settings(packageScalaJS(client))
  .settings(
    reStart <<= reStart.dependsOn(WebKeys.assets in Assets),
    unmanagedResourceDirectories in Compile += (WebKeys.public in Assets).value,
    libraryDependencies += "com.typesafe.akka" %% "akka-http-experimental" % "2.4.9"
  )
  .dependsOn(client)
  .enablePlugins(SbtWeb)
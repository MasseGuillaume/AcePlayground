import ScalaJSHelper._
import org.scalajs.sbtplugin.JSModuleID

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

def aceD(artifact: String, name: String): JSModuleID =
  "org.webjars.bower" % "ace-builds" % "1.2.5" % "compile" / s"src-noconflict/$artifact.js" minified s"src-min-noconflict/$artifact.js" commonJSName name

def react(artifact: String, name: String): JSModuleID = 
  "org.webjars.bower" % "react" % "15.2.1" % "compile" / s"$artifact.js" minified s"$artifact.min.js" commonJSName name

def react(artifact: String, name: String, depends: String): JSModuleID =
  react(artifact, name).dependsOn(s"$depends.js")

lazy val ace = project
  .settings(baseSettings)
  .settings(
    scalacOptions -= "-Ywarn-dead-code",
    jsDependencies ++= Seq(
      aceD("ace", "Ace"),
      aceD("mode-scala", "AceScala"),
      aceD("theme-solarized_dark", "AceSolarizedDark"),
      aceD("theme-solarized_light", "AceSolarizedLight")
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
      react("react-with-addons", "React"),
      react("react-dom", "ReactDOM", "react-with-addons"),
      react("react-dom-server", "ReactDOMServer", "react-dom")
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
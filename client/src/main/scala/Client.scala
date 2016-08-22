import org.scalajs.dom
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router._

import scalacss.Defaults._
import scalacss.ext.CssReset
import scalacss.mutable.{GlobalRegistry, Register}


class Solarized(implicit r: Register) extends StyleSheet.Inline()(r) {
  import dsl._

  // dark
  val base00  = c"#657b83"
  val base01  = c"#586e75"
  val base02  = c"#073642"
  val base03  = c"#002b36"

  // light
  val base0   = c"#839496"
  val base1   = c"#93a1a1"
  val base2   = c"#eee8d5"
  val base3   = c"#fdf6e3"

  // common
  val yellow  = c"#b58900"
  val orange  = c"#cb4b16"
  val red     = c"#dc322f"
  val magenta = c"#d33682"
  val violet  = c"#6c71c4"
  val blue    = c"#268bd2"
  val cyan    = c"#2aa198"
  val green   = c"#859900"
}


object GlobalStyle extends StyleSheet.Standalone {
  import dsl._

  val solarized = new Solarized
  import solarized._


  "html" - (
    height(100.%%)
  )

  "body" - (
    height(100.%%),
    margin.`0`,
    padding.`0`
  )

  ".ace_scroller" - (
    padding(24.px)
  )

  ".ace_editor ::-webkit-scrollbar" - (
    height(12.px),
    width(12.px)
  )

  ".ace-solarized-dark ::-webkit-scrollbar-thumb" - (
    boxShadow := "1px 1px 5px 1px rgba(0, 0, 0, 0.75)"
  )

  ".ace-solarized-dark ::-webkit-scrollbar-track, .ace-solarized-dark ::-webkit-scrollbar-corner" - (
    backgroundColor(base03)
  )

  ".ace-solarized-dark ::-webkit-scrollbar-thumb" - (
    backgroundColor(base02)
  )

  ".ace-solarized-light ::-webkit-scrollbar-thumb" - (
    boxShadow := "1px 1px 5px 1px rgba(200, 200, 200, 0.75)"
  )

  ".ace-solarized-light ::-webkit-scrollbar-track, .ace-solarized-light ::-webkit-scrollbar-corner" - (
    backgroundColor(base3)
  )
  
  ".ace-solarized-light ::-webkit-scrollbar-thumb" - (
    backgroundColor(base2)
  )

  CssReset.normaliseCss
}

object AppCSS {
  def load() = {
    val style = dom.document.createElement("style")
    style.innerHTML = GlobalStyle.render
    dom.document.head.appendChild(style)

    import scalacss.ScalaCssReact._
    GlobalRegistry.register(
      App.Style,
      AceEditor.Style
    )
    GlobalRegistry.onRegistration(_.addToDocument())
  }
}

sealed trait Page
case object Home extends Page

object Client extends JSApp {
  val routerConfig = RouterConfigDsl[Page].buildConfig { dsl =>
    import dsl._

    ( trimSlashes
        | staticRoute(root, Home) ~> render(App())
    ) 
      .notFound(redirectToPage(Home)(Redirect.Replace))
      .renderWith(layout)
  }

  def layout(c: RouterCtl[Page], r: Resolution[Page]) =
    r.render()
    
  @JSExport
  override def main(): Unit = {
    AppCSS.load()
    ReactDOM.render(
      Router(BaseUrl.fromWindowOrigin_/, routerConfig.logToConsole)(),
      dom.document.body
    )
    ()
  }
}
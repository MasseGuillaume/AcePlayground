import org.scalajs.dom
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router._

import scalacss.Defaults._
import scalacss.Defaults._
import scalacss.ext.CssReset
import scalacss.mutable.GlobalRegistry
import scalacss.ScalaCssReact._


object GlobalStyle extends StyleSheet.Inline {
  import dsl._

  style(
    unsafeRoot("html")(
      height(100.%%)
    ),
    unsafeRoot("body")(
      height(100.%%),
      margin.`0`,
      padding.`0`
    )
  ) + style(CssReset.normaliseCss)
}

object AppCSS {
  def load() = {
    GlobalRegistry.register(
      GlobalStyle,
      HomeView.Style
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
        | staticRoute(root, Home) ~> render(HomeView())
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
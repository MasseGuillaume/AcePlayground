import japgolly.scalajs.react._, vdom.all._

import scalacss.Defaults._
import scalacss.ScalaCssReact._

object App {

  object Style extends StyleSheet.Inline {
    import dsl._
    val code = style(height(100.%%))
  }


  case class State(dark: Boolean = true, code: String = "") {
    def toogleTheme = copy(dark = !dark)
  }

  class Backend(scope: BackendScope[_, State]) {
    def toogleTheme(e: ReactEventI)   = scope.modState(_.toogleTheme)
    def templateOne(e: ReactEventI)   = scope.modState(_.copy(code = "code 1"))
    def templateTwo(e: ReactEventI)   = scope.modState(_.copy(code = "code 2"))
    def templateThree(e: ReactEventI) = scope.modState(_.copy(code = "code 3")) 
  }

  val SideBar = ReactComponentB[(State, Backend)]("SideBar")
    .render_P { case (state, backend) =>
      val label = if(state.dark) "dark" else "light"

      ul(
        li(button(onClick ==> backend.toogleTheme)(label)),
        li(button(onClick ==> backend.templateOne)("template 1")),
        li(button(onClick ==> backend.templateTwo)("template 2")),
        li(button(onClick ==> backend.templateThree)("template 3"))
      )
    }
    .build

  val component = ReactComponentB[Unit]("App")
    .initialState(State())
    .backend(new Backend(_))
    .renderPS((scope, _, state) =>
      div(Style.code)(
        SideBar((state, scope.backend)),
        AceEditor(state)
      )
    )
    .build

  def apply() = component()
}
import japgolly.scalajs.react._, vdom.all._

import scalacss.Defaults._
import scalacss.ScalaCssReact._

import com.scalawarrior.scalajs.ace._

import org.scalajs.dom.raw.Element

object EditorView {
  object Style extends StyleSheet.Inline {
    import dsl._

    val code = style(
      height(100.%%),
      margin.`0`
    )
  }

  def mount(el: Element) = {
    val editor = ace.edit(el)
    editor.setTheme("ace/theme/solarized_dark")
    editor.setFontSize("16px")
    editor.renderer.setShowGutter(false)
    editor.setHighlightActiveLine(false)
    editor.getSession().setMode("ace/mode/scala")
    editor.setShowPrintMargin(false)
    ()
  }

  val component = ReactComponentB.static("Home View", pre(Style.code)("1+1"))
    .componentDidMount(scope => CallbackTo[Unit](mount(scope.getDOMNode)))
    .build

  def apply() = component()
}
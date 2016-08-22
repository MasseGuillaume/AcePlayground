import japgolly.scalajs.react._, vdom.all._

import scalacss.Defaults._
import scalacss.ScalaCssReact._

import com.scalawarrior.scalajs.ace._

import org.scalajs.dom.raw.Element

object AceEditor {
  object Style extends StyleSheet.Inline {
    import dsl._

    val code = style(
      height(100.%%),
      margin.`0`
    )
  }

  private[AceEditor] case class EditorState(editor: Option[Editor] = None)
  private[AceEditor] class Backend(scope: BackendScope[App.State, EditorState]) {
    private def mount(el: Element) = {
      val editor = ace.edit(el)
      editor.setTheme("ace/theme/solarized_dark")
      editor.setFontSize("16px")
      editor.renderer.setShowGutter(false)
      editor.setHighlightActiveLine(false)
      editor.getSession().setMode("ace/mode/scala")
      editor.setShowPrintMargin(false)
      editor
    }
    def start = scope.modState(_.copy(editor = Some(mount(scope.getDOMNode))))
  }

  val component = ReactComponentB[App.State]("AceEditor")
    .initialState(EditorState())
    .backend(new Backend(_))
    .render( _ => pre(Style.code))
    .componentWillReceiveProps(v => CallbackTo[Unit]{
      val current = v.currentProps
      val next = v.nextProps
      val state = v.currentState

      if(current.dark != next.dark) {
        val label = 
          if(next.dark) "dark"
          else "light"
        state.editor.foreach(_.setTheme(s"ace/theme/solarized_$label"))
      }

      if(current.code != next.code) {
        state.editor.foreach(_.getSession().setValue(next.code))
      }

      ()
    })
    .componentDidMount(_.backend.start)
    .build
 
  def apply(state: App.State) = component(state)
}
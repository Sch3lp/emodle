import be.swsb.common.json.PuzzleSetJson
import kotlinx.browser.document
import react.FC
import react.Props
import react.RBuilder
import react.create
import react.dom.client.createRoot
import react.dom.html.ReactHTML
import react.router.dom.BrowserRouter

fun main() {
    val container = document.createElement("div")
    document.body!!.appendChild(container)
    createRoot(container).render(App.create())
}

val emodleSets = listOf(
    PuzzleSetJson("""⚡️🐱🐱🐱🐱"""),
    PuzzleSetJson("""⚡️🐱🐱🐱🐱"""),
    PuzzleSetJson("""⚡️🐱🐱🐱🐱"""),
    PuzzleSetJson("""⚡️🐱🐱🐱🐱"""),
    PuzzleSetJson("""⚡️🐱🐱🐱🐱"""),
)
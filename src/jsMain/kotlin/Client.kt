import kotlinx.browser.document
import react.create
import react.dom.client.createRoot

fun main() {
    val container = document.createElement("div")
    document.body!!.appendChild(container)

    val welcome = Welcome.create {
        name = "Kotlin/JS"
    }
    val grid = MainGrid.create {
        header = "Emodle!"
        middle = Middle.create {}
        footer = "Made with <3 in Kotlin by Sch3lp"
    }
    createRoot(container).render(welcome)
}
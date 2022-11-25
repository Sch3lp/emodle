package be.swsb.ui.pages

import kotlinx.html.*

fun HTML.page(bodyDefinition: BODY.() -> Unit) {
    head {
        title { +"Emodle!" }
        meta { charset = "UTF-8" }
        link(rel = "stylesheet", href = "/styles.css", type = "text/css")
        script {
            src = "https://unpkg.com/htmx.org@1.8.4"
            integrity = "sha384-wg5Y/JwF7VxGk4zLsJEcAojRtlVp1FKKdGy1qN+OMtdq72WRvX/EdRdqg/LOhYeV"
            attributes["crossorigin"] = "anonymous"
        }
    }
    body { bodyDefinition() }
}
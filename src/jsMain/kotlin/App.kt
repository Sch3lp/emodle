import csstype.NamedColor
import csstype.pct
import emotion.react.css
import kotlinx.coroutines.MainScope
import react.*
import react.dom.html.ReactHTML.div
import react.router.Route
import react.router.Routes
import react.router.dom.BrowserRouter

val scope = MainScope()

val App = FC<Props> {

    val HomePage : ReactNode by kotlin.lazy { Home.create() }
    val CreatePage : ReactNode by kotlin.lazy { Create.create() }
    div {
        css {
            height = 100.pct
            width = 100.pct
            backgroundColor = NamedColor.darkorchid
        }
        BrowserRouter {
            Suspense {
                fallback { div { +"Loading..." } }
                Routes {
                    Route {
                        path = "/"
                        element = HomePage
                    }
                    Route {
                        path = "/create"
                        element = CreatePage
                    }
                }
            }
        }
    }

}
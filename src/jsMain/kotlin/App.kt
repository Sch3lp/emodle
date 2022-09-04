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
import components.CreatePuzzle
import react.FC
import react.Props
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.h1

val Create = FC<Props> {
    h1 {
        +"Emodle of the Day!"
        a {
            href = "create"
            +"Create your own!"
        }
    }
    CreatePuzzle {}
}
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.useState

val App = FC<Props> {
    var (currentSet, setCurrentSet) = useState(1)

    val guessHandler = { guess: String ->
        setCurrentSet(currentSet++)
        console.log(currentSet)
    }

    div {
        h1 {
            + "Emodle of the Day!"
        }
        EmodleOfTheDay {
            set = currentSet
        }
        Guess {
            onSubmit = guessHandler
        }
    }
}
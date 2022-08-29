import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1

val App = FC<Props> {
    val guessHandler = { guess: String ->
        console.log(guess)
    }

    div {
        h1 {
            + "Emodle of the Day!"
        }
        EmodleOfTheDay()
        Guess {
            onSubmit = guessHandler
        }
    }
}
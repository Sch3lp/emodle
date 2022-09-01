import be.swsb.common.json.PuzzleSetJson
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.useState

val App = FC<Props> {
    var currentSet by useState(1)

    val guessHandler = { guess: String ->
        currentSet += 1
        console.log(currentSet)
    }

    div {
        h1 {
            + "Emodle of the Day!"
        }
        EmodleOfTheDay {
            setsToShow = emodleSets.take(currentSet)
        }
        Guess {
            onSubmit = guessHandler
        }
    }
}
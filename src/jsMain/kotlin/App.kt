import be.swsb.common.json.PuzzleSetJson
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.useEffect
import react.useState

private val scope = MainScope()
val App = FC<Props> {
    var currentSet by useState(1)

    var sets by useState(emptyList<PuzzleSetJson>())

    useEffect {
        scope.launch {
            sets = getPuzzle(currentSet)
        }
    }

    val guessHandler = { guess: String ->
        currentSet += 1
        console.log(currentSet)
    }

    div {
        h1 {
            + "Emodle of the Day!"
        }
        EmodleOfTheDay {
            setsToShow = sets
        }
        Guess {
            onSubmit = guessHandler
        }
    }
}
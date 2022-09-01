import be.swsb.common.json.PuzzleSetJson
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.*
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1

private val scope = MainScope()
private const val maxGuesses = 5

val App = FC<Props> {
    var currentSet by useState(1)
    var sets by useState(emptyList<PuzzleSetJson>())
    var isSolved by useState(false)

    useEffectOnce {
        scope.launch {
            sets = getPuzzle(currentSet)
        }
    }

    val guessHandler: (String) -> Unit = { guess: String ->
        scope.launch {
            console.log("About to make the $currentSet guess.")
            isSolved = answer(guess, currentSet)
            if (!isSolved) {
                var set = currentSet
                console.log("set = $set")
                set+=1
                console.log("set after +=1: $set")
                if (set <= maxGuesses) {
                    sets = getPuzzle(set)
                }
                currentSet+=1
            }
        }
        Unit
    }

    div {
        h1 {
            + "Emodle of the Day!"
        }
        EmodleOfTheDay {
            setsToShow = sets
        }
        if (isSolved) {
            h1 {
                +"You did it! You solved the Emodle of the day!"
            }
        } else {
            if (currentSet <= maxGuesses) {
                Guess {
                    onSubmit = guessHandler
                }
            } else {
                h1 {
                    +"You lost! You suck at Emodle!"
                }
            }
        }
    }
}
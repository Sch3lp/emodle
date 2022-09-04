import be.swsb.common.json.PuzzleSetJson
import components.CreatePuzzle
import components.EmodleOfTheDay
import components.Guess
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.FC
import react.Props
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.useEffectOnce
import react.useState
import services.answer
import services.getPuzzle

private const val maxGuesses = 5

val Home = FC<Props> {
    var currentSet by useState(1)
    var sets by useState(emptyList<PuzzleSetJson>())
    var isSolved by useState(false)

    useEffectOnce {
        scope.launch {
            sets = getPuzzle(currentSet)
        }
    }

    val guessHandler: (String) -> Unit = { guess: String ->
        var set = currentSet
        scope.launch {
            val result = answer(guess, currentSet).also { isSolved = it }
            if (!result) {
                set++
                if (set <= maxGuesses) {
                    sets = getPuzzle(set)
                }
                currentSet = set
            } else {
                sets = getPuzzle(maxGuesses)
            }
        }
    }

    div {
        h1 {
            +"Emodle of the Day!"
            a {
                href = "create"
                +"Create your own!"
            }
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
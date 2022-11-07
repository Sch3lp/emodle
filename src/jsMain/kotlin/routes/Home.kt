import be.swsb.common.json.PuzzleSetJson
import components.CreatePuzzle
import components.EmodleOfTheDay
import components.Guess
import csstype.*
import emotion.react.css
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.h2
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
        css {
            display = Display.grid
            gap = 20.px
            gridTemplateAreas = GridTemplateAreas(
                ident(". header ."),
                ident(". content ."),
                ident(". footer ."),
            )
        }
        h1 {
            +"Emodle of the Day!"
            a {
                href = "create"
                +"Create your own!"
            }
            css { gridArea = ident("header") }
        }
        div {
            EmodleOfTheDay { setsToShow = sets }
            css { gridArea = ident("content") }
        }
        div {
            if (isSolved) {
                h2 { +"You did it! You solved the Emodle of the day!" }
            } else {
                if (currentSet <= maxGuesses) {
                    Guess { onSubmit = guessHandler }
                } else {
                    h2 { +"You lost! You suck at Emodle!" }
                }
            }
            css { gridArea = ident("footer") }
        }
    }
}
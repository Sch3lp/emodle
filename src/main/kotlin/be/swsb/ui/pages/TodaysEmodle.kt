package be.swsb.ui.pages

import be.swsb.application.*
import be.swsb.application.HintIndex
import be.swsb.ui.htmx.*
import be.swsb.ui.htmx.HxTarget.*
import kotlinx.css.*
import kotlinx.html.*


fun HTML.TodaysEmodle(id: String = "TodaysEmodle") = page {
    div("TodaysEmodle") {
        this.id = id
        h1 { +"Emodle of the Day!" }
        a {
            href = "create"
            +"Create your own!"
        }
        div {
            hxTarget(`this`)
            val hintIndex = HintIndex(1)
            EmodleOfTheDay(hintIndex = hintIndex)
            GuessInput(hintIndex = hintIndex, guessResult = false)
        }
    }
}

const val GuessInputFormId = "guessFormId"
fun DIV.GuessInput(hintIndex: HintIndex, guessResult: Boolean) {
    if (guessResult) {
        p {
            id = GuessInputFormId
            +"🎉🎊🥳 You guessed correctly! 🎉🎊🥳"
        }
    } else {
        if (hintIndex.isMax()) {
            p {
                id = GuessInputFormId
                +"😭😭😭 Better luck next time! 😭😭😭"
            }
        } else {
            form {
                id = GuessInputFormId
                hxPost("/puzzle/2022/8/10/${hintIndex.value}")
                hxTarget(`this`)
                hxSwap(HxSwap.Multi(id, EmodleOfTheDayId))
                p { +"Your guess:" }
                input {
                    type = InputType.text
                    name = "guess"
                    autoFocus = true
                }
            }
        }
    }
}

const val EmodleOfTheDayId = "emodleOfTheDayId"
fun DIV.EmodleOfTheDay(id: String = EmodleOfTheDayId, hintIndex: HintIndex) {
    val usedHints = puzzles.find(Year(2022), Month(8), Day(10))?.take(hintIndex)
    div {
        this.id = id
        usedHints?.forEachIndexed { idx, hint ->
            p { +"${idx + 1}. ${hint.value}" }
        } ?: p { +"No hints found." }
    }
}

fun CssBuilder.TodaysEmodle() {
    rule("TodaysEmodle") {
        display = Display.grid
        gap = 20.px
        gridTemplateAreas = GridTemplateAreas(
            """
        . header .
        . content .
        . footer .            
        """.trimIndent()
        )
    }
    rule("TodaysEmodle.h1") {
        area { +"header" }
    }
}
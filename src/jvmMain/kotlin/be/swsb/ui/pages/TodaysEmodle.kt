package be.swsb.ui.pages

import be.swsb.application.*
import be.swsb.application.Set
import be.swsb.ui.htmx.*
import be.swsb.ui.htmx.HxSwap.OuterHTML
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
            val set = Set(1)
            EmodleOfTheDay(set = set)
            Guess(set = set)
        }
    }
}

fun DIV.Guess(set: Set) {
    p { +"Your guess:" }
    form {
        hxPost("/puzzle/2022/8/10/${set.value}")
        hxTarget(`this`)
        hxSwap(OuterHTML)
        input {
            type = InputType.text
            name = "value"
        }
    }
}

fun DIV.EmodleOfTheDay(id: String = "EmodleOfTheDay", set: Set) {
    val sets = puzzles.find(Year(2022), Month(8), Day(10))?.take(set)
    div {
        this.id = id
        sets?.forEachIndexed { idx, set ->
            p { +"${idx + 1}. ${set.value}" }
        } ?: p { +"No sets found." }
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
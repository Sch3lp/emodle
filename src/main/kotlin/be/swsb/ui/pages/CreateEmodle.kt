package be.swsb.ui.pages

import be.swsb.ui.htmx.HxTarget
import be.swsb.ui.htmx.hxPost
import be.swsb.ui.htmx.hxTarget
import kotlinx.html.*

const val CreateEmodleId = "CreateEmodle"
fun HTML.CreateEmodle(id: String = CreateEmodleId) = page {
    div {
        this.id = id
        h2 { +"Create your puzzle!" }
        form {
            hxPost("/puzzles")
            hxTarget(HxTarget.`this`)
            p {
                +"Movie "
                input {
                    type = InputType.text
                    required = true
                    name = "enteredSolution"
                }
            }
            (1..5).forEach { idx ->
                p {
                    +"Hint $idx "
                    input {
                        type = InputType.text
                        required = true
                        name = "hint$idx"
                    }
                }
            }
            input {
                type = InputType.submit
                value = "Submit"
            }
            a {
                href = "/"
                target = "_parent"
                button {
                    type = ButtonType.button
                    +"Cancel"
                }
            }
        }
    }
}
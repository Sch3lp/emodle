package be.swsb.ui.pages

import kotlinx.html.*

fun HTML.CreateEmodle(id: String = "CreateEmodle") = page {
    div {
        h2 { +"Your puzzle" }
        form {
            p {
                +"Movie "
                input {
                    type = InputType.text
                    name = "enteredSolution"
                }
            }
            (1..5).forEach { idx ->
                p {
                    +"Hint $idx "
                    input {
                        type = InputType.text
                        name = "hint$idx"
                    }
                }
            }
            input {
                type = InputType.submit
                value = "Create"
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
package be.swsb.ui.pages

import kotlinx.html.*

fun HTML.CreateEmodle(id: String = "CreateEmodle") = page {
    div {
        h1 { +"Create your own Emodle!" }
        form {

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
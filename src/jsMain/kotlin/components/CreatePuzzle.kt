package components

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.w3c.dom.HTMLFormElement
import org.w3c.dom.HTMLInputElement
import react.FC
import react.Props
import react.dom.events.ChangeEvent
import react.dom.events.ChangeEventHandler
import react.dom.events.FormEventHandler
import react.dom.html.InputType
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.h3
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label
import react.dom.html.ReactHTML.p
import react.useState
import services.createPuzzle

private val scope = MainScope()

val CreatePuzzle = FC<Props> { props ->
    val setsWithHandlers: List<Pair<String, (event: ChangeEvent<HTMLInputElement>) -> Unit>> = (1..5).map {
        var set by useState("")
        val handler: ChangeEventHandler<HTMLInputElement> = { event ->
            set = event.target.value
        }
        set to handler
    }
    var enteredSolution by useState("")
    val solutionHandler: ChangeEventHandler<HTMLInputElement> = { event -> enteredSolution = event.target.value }

    val submitHandler: FormEventHandler<HTMLFormElement> = {
        it.preventDefault()
        val emojiSets = setsWithHandlers.map { (set, _) -> set }
        val solution = enteredSolution
        scope.launch {
            createPuzzle(solution, emojiSets)
        }
    }

    div {
        h2 { +"Your puzzle" }
        form {
            onSubmit = submitHandler
            p {
                +"Solution:"
                input {
                    type = InputType.text
                    onChange = solutionHandler
                    value = enteredSolution
                }
            }
            setsWithHandlers.forEachIndexed { idx, (set, handler) ->
                p {
                    +"Set ${idx + 1}:"
                    input {
                        type = InputType.text
                        onChange = handler
                        value = set
                    }
                }
            }
            input {
                type = InputType.submit
                value = "Create"
            }
        }
    }
}
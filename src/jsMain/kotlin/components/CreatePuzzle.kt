package components

import kotlinx.coroutines.launch
import org.w3c.dom.HTMLFormElement
import org.w3c.dom.HTMLInputElement
import react.FC
import react.Props
import react.dom.events.ChangeEventHandler
import react.dom.events.FormEventHandler
import react.dom.html.InputType
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.p
import react.useState
import scope
import services.createPuzzle

val CreatePuzzle = FC<Props> { props ->
    val emojiSetsInputs: List<Pair<String, ChangeEventHandler<HTMLInputElement>>> = (1..5).map {
        var set by useState("")
        val handler: ChangeEventHandler<HTMLInputElement> = { event ->
            set = event.target.value
        }
        set to handler
    }
    var enteredSolution by useState("")
    val solutionHandler: ChangeEventHandler<HTMLInputElement> = { event -> enteredSolution = event.target.value }

    var creationFeedback: String? by useState(null)

    val submitHandler: FormEventHandler<HTMLFormElement> = {
        it.preventDefault()
        val emojiSets = emojiSetsInputs.map { (set, _) -> set }
        val solution = enteredSolution
        scope.launch {
            val response = createPuzzle(solution, emojiSets)
            creationFeedback = response
        }
    }

    if (creationFeedback != null) {
        h2 {
            +creationFeedback!!
        }
    } else {
        div {
            h2 { +"Your puzzle" }
            form {
                onSubmit = submitHandler
                p {
                    +"Movie"
                    input {
                        type = InputType.text
                        onChange = solutionHandler
                        value = enteredSolution
                    }
                }
                emojiSetsInputs.forEachIndexed { idx, (set, handler) ->
                    p {
                        +"Set ${idx + 1}"
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
}
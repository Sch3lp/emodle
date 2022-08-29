import org.w3c.dom.HTMLFormElement
import org.w3c.dom.HTMLInputElement
import react.FC
import react.Props
import react.dom.events.ChangeEventHandler
import react.dom.events.FormEventHandler
import react.dom.html.InputType
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.p
import react.useState

external interface GuessProps : Props {
    var onSubmit: (String) -> Unit
}

val Guess = FC<GuessProps> { props ->
    val (guess, setGuess) = useState("")

    val submitHandler: FormEventHandler<HTMLFormElement> = {
        it.preventDefault()
        setGuess("")
        props.onSubmit(guess)
    }

    val changeHandler: ChangeEventHandler<HTMLInputElement> = {
        setGuess(it.target.value)
    }

    div {
        p { +"Your guess:" }
        form {
            onSubmit = submitHandler
            input {
                type = InputType.text
                onChange = changeHandler
                value = guess
            }
        }
    }
}
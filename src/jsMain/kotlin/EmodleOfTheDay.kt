import react.FC
import react.Props
import react.dom.html.ReactHTML.p

external interface EmodleOfTheDayProps: Props {
    var set: Int
}

val EmodleOfTheDay = FC<EmodleOfTheDayProps> { props ->
    emodleSets.take(props.set).mapIndexed { idx, set ->
        p {
            +"${idx+1}. ${set.value}"
        }
    }
}
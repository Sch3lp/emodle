import react.FC
import react.Props
import react.dom.html.ReactHTML.p

val EmodleOfTheDay = FC<Props> {
    emodleSets.mapIndexed { idx, set ->
        p {
            +"${idx+1}. ${set.value}"
        }
    }
}
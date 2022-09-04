package components

import be.swsb.common.json.PuzzleSetJson
import react.FC
import react.Props
import react.dom.html.ReactHTML.p

external interface EmodleOfTheDayProps: Props {
    var setsToShow: List<PuzzleSetJson>
}

val EmodleOfTheDay = FC<EmodleOfTheDayProps> { props ->
    props.setsToShow.mapIndexed { idx, set ->
        p {
            +"${idx+1}. ${set.value}"
        }
    }
}
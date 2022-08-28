import react.FC
import react.Props
import react.ReactElement
import react.dom.html.ReactHTML.div
import react.useState

external interface MainGridProps : Props {
    var header: String
    var middle: ReactElement<MiddleProps>
    var footer: String
}

val MainGrid = FC<MainGridProps> { props ->
    var header by useState(props.header)
    var footer by useState(props.footer)

    div {
        + header
    }
    div {
        props.middle
    }
    div {
        + footer
    }
}
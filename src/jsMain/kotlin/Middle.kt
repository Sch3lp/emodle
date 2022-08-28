import react.FC
import react.Props

external interface MiddleProps : Props {

}

val Middle = FC<MiddleProps> { props ->
    "state goes here!"
}
package be.swsb.ui.htmx

import kotlinx.html.HTMLTag

enum class HxTarget(val value: String) {
    `this`("this")
}
fun HTMLTag.hxTarget(target: HxTarget) {
    attributes += "hx-target" to target.value
}

fun HTMLTag.hxPost(url: String) {
    attributes += "hx-post" to url
}

sealed class HxSwap(val value: String) {
    object OuterHTML: HxSwap("outerHTML")
    class Multi(vararg ids: String): HxSwap("multi:${ids.joinToString(",") { id -> "#$id:outerHTML" }}")
}

fun HTMLTag.hxSwap(hxSwap: HxSwap) {
    attributes += "hx-swap" to hxSwap.value
}

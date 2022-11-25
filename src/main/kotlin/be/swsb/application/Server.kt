package be.swsb.application

import be.swsb.application.Puzzle.Companion.aPuzzle
import be.swsb.ui.pages.TodaysEmodle
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.pipeline.*
import kotlinx.css.*
import kotlinx.serialization.json.Json

data class EmodleCookie(val guesses: Int = 0)

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
            })
        }
        install(CORS) {
            allowMethod(HttpMethod.Get)
            allowMethod(HttpMethod.Post)
            allowMethod(HttpMethod.Delete)
            anyHost()
        }
        install(Compression) {
            gzip()
        }
        install(Sessions) {
            cookie<EmodleCookie>("EMODLE_COOKIE")
        }

        routing {
            uiRoutes()
            static("/favicon.ico") {
                resource("favicon.ico")
            }
            static("/static") {
                resources()
            }
            trace { application.log.info(it.buildText()) }
        }
    }.start(wait = true)
}

suspend inline fun ApplicationCall.respondCss(builder: CssBuilder.() -> Unit) {
    this.respondText(CssBuilder().apply(builder).toString(), ContentType.Text.CSS)
}

private fun Routing.uiRoutes() {
    route("/") {
        get {
            call.respondHtml(HttpStatusCode.OK) {
                TodaysEmodle()
            }
        }
    }
    route("/puzzle/{year}/{month}/{day}/{hintindex}") {
        post {
            withValidatedParams { year, month, day, hintindex ->
                val guess = call.receiveParameters()["guess"].toString()
                val result = puzzles.find(year, month, day)?.check(Guess(guess))
                result?.let { call.respond(it) } ?: call.respond(
                    HttpStatusCode.NotFound,
                    "Can't find puzzle for $year/$month/$day."
                )
            }
        }
    }
    get("/styles.css") {
        call.respondCss {
            TodaysEmodle()
        }
    }
}


//private fun Routing.apiRoutes() {
//    ///api/puzzle/2022/08/10?set=1
//    route("/api/puzzle/{year}/{month}/{day}/{set}") {
//        get {
//            withValidatedParams { year, month, day, set ->
//                val requestedEmojiSet = puzzles.find(year, month, day)?.take(set)
//                requestedEmojiSet?.let { emojiSets -> call.respond(emojiSets.asJson()) }
//                    ?: call.respond(HttpStatusCode.NotFound, "Can't find puzzle for $year/$month/$day for set $set.")
//            }
//        }
//        post {
//            withValidatedParams { year, month, day, set ->
//                val guess = call.receive<GuessJson>().asGuess()
//                val result = puzzles.find(year, month, day)?.check(guess)
//                result?.let { call.respond(it) } ?: call.respond(
//                    HttpStatusCode.NotFound,
//                    "Can't find puzzle for $year/$month/$day."
//                )
//            }
//        }
//    }
//    route("/api/puzzle") {
//        post {
//            val createPuzzleJson = call.receive<CreatePuzzleJson>()
//            val date: LocalDateTime = puzzles.append(aPuzzle(createPuzzleJson.solution) {
//                createPuzzleJson.emojiSets.forEach { +it }
//            }).atStartOfDay()
//            call.respond(
//                HttpStatusCode.Created,
//                "Your Puzzle will be provided on ${httpDateFormat.format(date)}."
//            )
//        }
//    }
//}

val puzzles: Puzzles =
    assemble {
        on(2022, 8, 10) thereIs aPuzzle("The Grey") {
            +"""🛩 💥 ❄️ 🙍‍♂️‍️ 🐺"""
            +"""⚡️ 🐱 🐱 🐱 🐱"""
            +"""⚡️ 🐱 🐱 🐱 🐱"""
            +"""⚡️ 🐱 🐱 🐱 🐱"""
            +"""⚡️ 🐱 🐱 🐱 🐱"""
        }
    }

private suspend fun PipelineContext<Unit, ApplicationCall>.withValidatedParams(
    respond: suspend PipelineContext<Unit, ApplicationCall>.(Year, Month, Day, HintIndex) -> Unit
): Unit {
    val year = Year(call.parameters["year"])
    val month = Month(call.parameters["month"])
    val day = Day(call.parameters["day"])
    val hintIndex = HintIndex(call.parameters["hints"])
    if (year == null || month == null || day == null || hintIndex == null) {
        call.respond(BadRequest, "Illegal arguments provided: $year/$month/$day/$hintIndex.")
    } else {
        respond(year, month, day, hintIndex)
    }
}
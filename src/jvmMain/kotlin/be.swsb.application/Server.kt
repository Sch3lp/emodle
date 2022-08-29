package be.swsb.application

import be.swsb.application.Puzzle.Companion.aPuzzle
import be.swsb.common.json.CreatePuzzleJson
import be.swsb.common.json.GuessJson
import be.swsb.common.json.PuzzleSetJson
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.html.*
import io.ktor.server.http.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.Netty
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
import java.time.LocalDate

fun HTML.index() {
    head {
        title("Emodle!")
    }
    body {
        div {
            id = "root"
        }
        script(src = "/static/emodle.js") {}
    }
}

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        routing {
            get("/") {
                call.respondHtml(HttpStatusCode.OK, HTML::index)
            }
            ///api/puzzle/2022/08/10?set=1
            route("/api/puzzle/{year}/{month}/{day}") {
                get {
                    val year = Year(call.parameters["year"])
                    val month = Month(call.parameters["month"])
                    val day = Day(call.parameters["day"])
                    if (year == null || month == null || day == null) {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            "Illegal arguments provided: $year/$month/$day."
                        )
                    } else {
                        val set = call.request.queryParameters.get("set")
                        val getEmojiSet: SetProvider = setProviderFrom(set)
                        val requestedEmojiSet = puzzles.find(year, month, day)?.getEmojiSet()
                        requestedEmojiSet?.let { emojiSet -> call.respond(emojiSet.asJson()) }
                            ?: call.respond(HttpStatusCode.NotFound, "Can't find puzzle for $year/$month/$day for set $set.")
                    }
                }
                post {
                    val year = Year(call.parameters["year"])
                    val month = Month(call.parameters["month"])
                    val day = Day(call.parameters["day"])
                    if (year == null || month == null || day == null) {
                        call.respond(HttpStatusCode.BadRequest, "Illegal arguments provided: $year/$month/$day.")
                    } else {
                        val guess = call.receive<GuessJson>().asGuess()
                        val result = puzzles.find(year, month, day)?.check(guess)
                        result?.let { call.respond(it) }
                            ?: call.respond(HttpStatusCode.NotFound, "Can't find puzzle for $year/$month/$day.")
                    }
                }
            }
            route("/api/puzzle") {
                post {
                    val createPuzzleJson = call.receive<CreatePuzzleJson>()
                    val date : LocalDate = puzzles.append(aPuzzle(createPuzzleJson.solution) {
                        createPuzzleJson.emojiSets.forEach { +it }
                    })
                    call.respond(HttpStatusCode.Created, "Your Puzzle will be provided on ${httpDateFormat.format(date)}.")
                }
            }
            static("/static") {
                resources()
            }
        }
    }.start(wait = true)
}

val puzzles: Puzzles =
    assemble {
        on(2022, 8, 10) thereIs aPuzzle("The Grey") {
            +"""🛩💥❄️🙍‍♂️🐺"""
            +"""🎉🎉🎉🎉🎉"""
            +"""🎉🎉🎉🎉🎉"""
            +"""🎉🎉🎉🎉🎉"""
            +"""🎉🎉🎉🎉🎉"""
        }
    }

private fun EmojiSet.asJson() = PuzzleSetJson(value)
private fun GuessJson.asGuess() = Guess(this.value)
typealias SetProvider = Puzzle.() -> EmojiSet?

fun setProviderFrom(set: String?): SetProvider {
    val nullProvider : SetProvider = { null }
    return when (set) {
        "1" -> Puzzle::first
        "2" -> Puzzle::second
        "3" -> Puzzle::third
        "4" -> Puzzle::fourth
        "5" -> Puzzle::fifth
        else -> nullProvider
    }
}
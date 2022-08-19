package be.swsb.application

import be.swsb.application.Puzzle.Companion.aPuzzle
import be.swsb.common.json.GuessJson
import be.swsb.common.json.PuzzleSetJson
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.html.*
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
            +"Guess today's Emodle!"
        }
        div {
            id = "root"
        }
        script(src = "/static/emodle.js") {}
    }
}

val puzzles: Puzzles =
    assemble {
        on(2022, 8, 10) thereIs aPuzzle("The Grey") {
            +"""🛩💥❄️🙍‍♂️🐺"""
            +"""🎉🎉🎉🎉🎉"""
        }
    }

private fun EmojiSet.asJson() = PuzzleSetJson(value)
private fun GuessJson.asGuess() = Guess(this.value)

private fun puzzleGetSetFrom(setParam: String?)

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
                            "Illegal arguments provided: $year/$month/$day"
                        )
                    } else {
                        val set = call.request.queryParameters.get("set")
                        val getSet: Puzzle.() -> EmojiSet = puzzleGetSetFrom(set)
                        val firstSet = puzzles.find(year, month, day)?.getSet()
                        firstSet?.let { set -> call.respond(set.asJson()) }
                            ?: call.respond(HttpStatusCode.NotFound, "Can't find puzzle for $year/$month/$day.")
                    }
                }
                post {
                    val year = Year(call.parameters["year"])
                    val month = Month(call.parameters["month"])
                    val day = Day(call.parameters["day"])
                    if (year == null || month == null || day == null) {
                        call.respond(HttpStatusCode.BadRequest, "Illegal arguments provided: $year/$month/$day")
                    } else {
                        val guess = call.receive<GuessJson>().asGuess()
                        val result = puzzles.find(year, month, day)?.check(guess)
                        result?.let { call.respond(it) }
                            ?: call.respond(HttpStatusCode.NotFound, "Can't find puzzle for $year/$month/$day.")
                    }
                }
            }
            static("/static") {
                resources()
            }
        }
    }.start(wait = true)
}
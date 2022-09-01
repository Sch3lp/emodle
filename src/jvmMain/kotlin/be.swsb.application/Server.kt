package be.swsb.application

import be.swsb.application.Puzzle.Companion.aPuzzle
import be.swsb.common.json.CreatePuzzleJson
import be.swsb.common.json.GuessJson
import be.swsb.common.json.PuzzleSetJson
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.html.*
import io.ktor.server.http.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.html.*
import kotlinx.serialization.json.Json
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
            get("/") {
                call.respondHtml(HttpStatusCode.OK, HTML::index)
            }
            ///api/puzzle/2022/08/10?set=1
            route("/api/puzzle/{year}/{month}/{day}/{set}") {
                get {
                    val year = Year(call.parameters["year"])
                    val month = Month(call.parameters["month"])
                    val day = Day(call.parameters["day"])
                    val set = Set(call.parameters["set"])
                    if (year == null || month == null || day == null || set == null) {
                        call.respond(BadRequest, "Illegal arguments provided: $year/$month/$day/$set.")
                    } else {
                        val requestedEmojiSet = puzzles.find(year, month, day)?.take(set)
                        requestedEmojiSet?.let { emojiSets -> call.respond(emojiSets.asJson()) }
                            ?: call.respond(
                                HttpStatusCode.NotFound,
                                "Can't find puzzle for $year/$month/$day for set $set."
                            )
                    }
                }
                post {
                    val year = Year(call.parameters["year"])
                    val month = Month(call.parameters["month"])
                    val day = Day(call.parameters["day"])
                    val set = Set(call.parameters["set"])
                    if (year == null || month == null || day == null || set == null) {
                        call.respond(BadRequest, "Illegal arguments provided: $year/$month/$day/$set.")
                    } else {
                        val guess = call.receive<GuessJson>().asGuess().also { println("Guess received: $it") }
                        val result = puzzles.find(year, month, day)?.check(guess)
                        result?.let { call.respond(it) }
                            ?: call.respond(HttpStatusCode.NotFound, "Can't find puzzle for $year/$month/$day.")
                    }
                }
            }
            route("/api/puzzle") {
                post {
                    val createPuzzleJson = call.receive<CreatePuzzleJson>()
                    val date: LocalDate = puzzles.append(aPuzzle(createPuzzleJson.solution) {
                        createPuzzleJson.emojiSets.forEach { +it }
                    })
                    call.respond(
                        HttpStatusCode.Created,
                        "Your Puzzle will be provided on ${httpDateFormat.format(date)}."
                    )
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
            +"""⚡️🐱🐱🐱🐱"""
            +"""⚡️🐱🐱🐱🐱"""
            +"""⚡️🐱🐱🐱🐱"""
            +"""⚡️🐱🐱🐱🐱"""
        }
    }

private fun EmojiSet.asJson() = PuzzleSetJson(value)
private fun List<EmojiSet>.asJson() = this.map(EmojiSet::asJson)
private fun GuessJson.asGuess() = Guess(this.value)
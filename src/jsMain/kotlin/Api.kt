import be.swsb.common.json.PuzzleSetJson
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.browser.window

val endpoint = window.location.origin

val jsonClient = HttpClient {
    install(ContentNegotiation) {
        json()
    }
}

private val year = 2022
private val month = 8
private val day = 10
private fun puzzleSetPath(set: Int): String
    = "/api/puzzle/$year/$month/$day?set=$set"

suspend fun getPuzzle(currentSet: Int): List<PuzzleSetJson> {
    return jsonClient.get(endpoint + puzzleSetPath(currentSet)).body()
}

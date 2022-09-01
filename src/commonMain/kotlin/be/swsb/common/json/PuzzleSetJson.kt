package be.swsb.common.json

import kotlinx.serialization.Serializable

@Serializable
data class PuzzleSetJson(val value: String)
@Serializable
data class GuessJson(val value: String)
@Serializable
data class CreatePuzzleJson(
    val solution: String,
    val emojiSets: List<String>
)
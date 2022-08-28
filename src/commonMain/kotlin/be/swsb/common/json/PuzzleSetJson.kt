package be.swsb.common.json

data class PuzzleSetJson(val value: String)
data class GuessJson(val value: String)
data class CreatePuzzleJson(
    val solution: String,
    val emojiSets: List<String>
)
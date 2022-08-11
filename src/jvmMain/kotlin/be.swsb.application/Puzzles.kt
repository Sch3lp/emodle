package be.swsb.application

import java.time.LocalDate

class Puzzle private constructor(val solution: Solution, private val sets: List<EmojiSet>) {
    val first = sets.first()
    operator fun component1(): EmojiSet = first
    fun check(guess: Guess): Boolean = guess.solves(solution)

    companion object {
        fun aPuzzle(solution: String, sets: EmojiSetsBuilder.() -> Unit): Puzzle {
            return Puzzle(Solution(solution), EmojiSetsBuilder().apply(sets).build())
        }
        class EmojiSetsBuilder {
            private val sets: MutableList<EmojiSet> = mutableListOf()
            internal operator fun String.unaryPlus() {
                sets.add(EmojiSet(this))
            }
            fun build(): List<EmojiSet> = sets
        }
    }
}
@JvmInline value class Solution(val value: String) {
    init {
        require(value.isNotBlank()) { "Value should not be empty." }
    }
}
@JvmInline value class EmojiSet(val value: String) {
    init {
        require(value.isNotBlank()) { "Value should not be empty." }
    }
}
@JvmInline value class Guess(val value: String) {
    init {
        require(value.isNotBlank()) { "Value should not be empty." }
    }
    fun solves(solution: Solution) = solution.value.equals(value, true)
}

class Puzzles(private val puzzleMap: Map<LocalDate, Puzzle>) {
    fun find(year: Int, month: Int, day: Int) : Puzzle? =
        puzzleMap.get(LocalDate.of(year,month,day))
}

fun assemble(scaffold: PuzzlesBuilder.() -> Unit): Puzzles {
    val builder = PuzzlesBuilder()
    builder.scaffold()
    return builder.build()
}
class PuzzlesBuilder {
    private val puzzleMap: MutableMap<LocalDate, Puzzle> = mutableMapOf()

    fun on(year: Int, month: Int, day: Int) = PuzzleBuilder(LocalDate.of(year, month, day))
    fun build(): Puzzles = Puzzles(puzzleMap)

    inner class PuzzleBuilder(private val on: LocalDate) {
        init {
            require(puzzleMap[on] == null) { "There's already a puzzle on $on."}
        }
        infix fun thereIs(puzzle: Puzzle) {
            puzzleMap[on] = puzzle
        }
    }
}

@JvmInline value class Year(val value: Int) {
    init {
        require(value >= 0) { "No negative years supported." }
    }
}
@JvmInline value class Month(val value: Int) {
    init {
        require(value in 1..12) { "A Month should be between 1 and 12." }
    }
}
@JvmInline value class Day(val value: Int) {
    init {
        require(value in 1..31) { "A Day should be between 1 and 31." }
    }
}

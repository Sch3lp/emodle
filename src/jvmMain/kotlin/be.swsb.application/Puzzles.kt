package be.swsb.application

import java.time.LocalDate

class Puzzle private constructor(val solution: Solution, private val sets: List<EmojiSet>) {
    val first get() = sets[0]
    val second get() = sets[1]
    val third get() = sets[2]
    val fourth get() = sets[3]
    val fifth get() = sets[4]

    fun check(guess: Guess): Boolean = guess.solves(solution)

    init {
        require(sets.size == 5) { "A Puzzle needs exactly 5 sets of emoji's" }
    }

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

@JvmInline
value class Solution(val value: String) {
    init {
        require(value.isNotBlank()) { "Value should not be empty." }
    }
}

@JvmInline
value class EmojiSet(val value: String) {
    init {
        require(value.isNotBlank()) { "Value should not be empty." }
    }
}

@JvmInline
value class Guess(val value: String) {
    init {
        require(value.isNotBlank()) { "Value should not be empty." }
    }

    fun solves(solution: Solution) = solution.value.equals(value, true)
}

class Puzzles(private val puzzleMap: Map<LocalDate, Puzzle>) {
    fun find(year: Year, month: Month, day: Day): Puzzle? =
        puzzleMap[LocalDate.of(year.value, month.value, day.value)]
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
            require(puzzleMap[on] == null) { "There's already a puzzle on $on." }
        }

        infix fun thereIs(puzzle: Puzzle) {
            puzzleMap[on] = puzzle
        }
    }
}

@JvmInline
value class Year(val value: Int) {
    init {
        require(value >= 0) { "No negative years supported." }
    }

    companion object {
        operator fun invoke(value: String?) = value?.let { Year(it.toInt()) }
    }
}

@JvmInline
value class Month(val value: Int) {
    init {
        require(value in 1..12) { "A Month should be between 1 and 12." }
    }

    companion object {
        operator fun invoke(value: String?) = value?.let { Month(it.toInt()) }
    }
}

@JvmInline
value class Day(val value: Int) {
    init {
        require(value in 1..31) { "A Day should be between 1 and 31." }
    }

    companion object {
        operator fun invoke(value: String?) = value?.let { Day(it.toInt()) }
    }
}

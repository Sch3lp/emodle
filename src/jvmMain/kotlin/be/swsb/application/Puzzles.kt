package be.swsb.application

import java.time.LocalDate

class Puzzle private constructor(private val solution: Solution, private val sets: List<EmojiSet>) {

    fun take(set: Set) = sets.take(set.value)
    fun check(guess: Guess): Boolean = guess.solves(solution)

    init {
        require(sets.size == 5) { "A Puzzle needs exactly 5 sets of emoji's." }
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
        require(value.isNotBlank()) { "A Solution should not be empty." }
    }
}

@JvmInline
value class EmojiSet(val value: String) {
    private val emojis
        get() = value.split(" ").filterNot { it.isBlank() }

    init {
        require(value.isNotBlank()) { "An EmojiSet should not be empty." }
        require(emojis.size == 5) { "An EmojiSet should exactly 5 emoji's." }
        require(emojis.all { it.isEmoji() }) { "An EmojiSet should only contain emoji's." }
    }
}

@JvmInline
value class Guess(private val value: String) {
    fun solves(solution: Solution) = solution.value.equals(value, true)
}

class Puzzles(private val puzzleMap: MutableMap<LocalDate, Puzzle>) {
    init {
        require(puzzleMap.isNotEmpty()) { "Cannot create Puzzles with an empty map." }
    }

    fun find(year: Year, month: Month, day: Day): Puzzle? =
        puzzleMap[LocalDate.of(year.value, month.value, day.value)]

    fun append(aNewPuzzle: Puzzle): LocalDate {
        val lastDate = puzzleMap.entries.last().key
        val puzzleDate = if (lastDate.isBeforeOrEqual(LocalDate.now())) {
            LocalDate.now().plusDays(1)
        } else {
            lastDate.plusDays(1)
        }
        puzzleMap[puzzleDate] = aNewPuzzle
        return puzzleDate
    }
}

private fun LocalDate.isBeforeOrEqual(other: LocalDate) = this.isBefore(other) || this.isEqual(other)

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

@JvmInline
value class Set(val value: Int) {
    init {
        require(value in 1..5) { "A Set should be between 1 and 5." }
    }

    companion object {
        operator fun invoke(value: String?) = value?.let { Set(it.toInt()) }
    }
}

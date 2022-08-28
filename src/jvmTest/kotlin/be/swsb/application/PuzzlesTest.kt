package be.swsb.application

import be.swsb.application.Puzzle.Companion.aPuzzle
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PuzzlesTest {

    @Nested
    inner class Guessing {
        @Test
        internal fun `A correct guess returns true`() {
            assertTrue {
                aDefaultPuzzle("Snarf").check(Guess("Snarf"))
            }
        }

        @Test
        internal fun `An incorrect guess returns false`() {
            assertFalse {
                aDefaultPuzzle("Snarf").check(Guess("snorf"))
            }
        }

        @Test
        internal fun `An guesses are evaluated ignoring case`() {
            assertTrue {
                aDefaultPuzzle("Snarf").check(Guess("snarf"))
            }
        }
    }

    @Nested
    inner class PuzzleQuerying {
        @Test
        internal fun `When no Puzzle found for given date, returns null`() {
            val puzzles = assemble {
                on(1981, 6, 18) thereIs aPuzzle("Birthday") {
                    +"""🎂🎂🎂🎂🎂"""
                    +"""🎉🎉🎉🎉🎉"""
                    +"""🎉🎉🎉🎉🎉"""
                    +"""🎉🎉🎉🎉🎉"""
                    +"""🎉🎉🎉🎉🎉"""
                }
            }

            assertNull(puzzles.find(Year(2022), Month(6), Day(18)))
        }
    }
}

fun aDefaultPuzzle(solution: String) = aPuzzle(solution) {
    +"""🎂🎂🎂🎂🎂"""
    +"""🎂🎂🎂🎂🎂"""
    +"""🎂🎂🎂🎂🎂"""
    +"""🎂🎂🎂🎂🎂"""
    +"""🎂🎂🎂🎂🎂"""
}
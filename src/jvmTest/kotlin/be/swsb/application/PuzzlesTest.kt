package be.swsb.application

import be.swsb.application.Puzzle.Companion.aPuzzle
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException
import java.time.LocalDate
import kotlin.test.*

class PuzzlesTest {

    @Nested
    inner class Creating {
        @Test
        internal fun `When attempting to create a Puzzle containing a single non-emoji in a Set, it fails`() {
            val exception = assertThrows<IllegalArgumentException> {
                aPuzzle("Snarf") {
                    +"""⚡️ 🐱 🐱 🐱 🐱"""
                    +"""⚡️ 🐱 🐱 🐱 🐱"""
                    +"""⚡️ 🐱 🐱 🐱 🐱"""
                    +"""⚡️ 🐱 🐱 🐱 🐱"""
                    +"""⚡️ 🐱 🐱 S 🐱"""
                }
            }
            assertEquals("An EmojiSet should only contain emoji's.", exception.message)
        }

        @Test
        internal fun `When attempting to create a Puzzle containing a blank Set, it fails`() {
            val exception = assertThrows<IllegalArgumentException> {
                aPuzzle("Snarf") {
                    +"""⚡️ 🐱 🐱 🐱 🐱"""
                    +"""⚡️ 🐱 🐱 🐱 🐱"""
                    +"""⚡️ 🐱 🐱 🐱 🐱"""
                    +"""⚡️ 🐱 🐱 🐱 🐱"""
                    +""" """
                }
            }
            assertEquals("An EmojiSet should not be empty.", exception.message)
        }

        @Test
        internal fun `When attempting to create a Puzzle containing more than 5 emoji's in a Set, it fails`() {
            val exception = assertThrows<IllegalArgumentException> {
                aPuzzle("Snarf") {
                    +"""⚡️ 🐱 🐱 🐱 🐱"""
                    +"""⚡️ 🐱 🐱 🐱 🐱"""
                    +"""⚡️ 🐱 🐱 🐱 🐱"""
                    +"""⚡️ 🐱 🐱 🐱 🐱"""
                    +"""⚡️ 🐱 🐱 🐱 🐱 🐱"""
                }
            }
            assertEquals("An EmojiSet should exactly 5 emoji's.", exception.message)
        }

        @Test
        internal fun `When attempting to create a Puzzle containing less than 5 emoji's in a Set, it fails`() {
            val exception = assertThrows<IllegalArgumentException> {
                aPuzzle("Snarf") {
                    +"""⚡️ 🐱 🐱 🐱 🐱"""
                    +"""⚡️ 🐱 🐱 🐱 🐱"""
                    +"""⚡️ 🐱 🐱 🐱 🐱"""
                    +"""⚡️ 🐱 🐱 🐱 🐱"""
                    +"""⚡️ 🐱 🐱 🐱 """
                }
            }
            assertEquals("An EmojiSet should exactly 5 emoji's.", exception.message)
        }

        @Test
        internal fun `When attempting to create a Puzzle with less than 5 Sets, it fails`() {
            val exception = assertThrows<IllegalArgumentException> {
                aPuzzle("Snarf") {
                    +"""⚡️ 🐱 🐱 🐱 🐱"""
                    +"""⚡️ 🐱 🐱 🐱 🐱"""
                    +"""⚡️ 🐱 🐱 🐱 🐱"""
                    +"""⚡️ 🐱 🐱 🐱 🐱"""
                }
            }
            assertEquals("A Puzzle needs exactly 5 sets of emoji's.", exception.message)
        }
    }

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
                    +"""🎂 🎂 🎂 🎂 🎂"""
                    +"""🎉 🎉 🎉 🎉 🎉"""
                    +"""🎉 🎉 🎉 🎉 🎉"""
                    +"""🎉 🎉 🎉 🎉 🎉"""
                    +"""🎉 🎉 🎉 🎉 🎉"""
                }
            }

            assertNull(puzzles.find(Year(2022), Month(6), Day(18)))
        }
    }


    @Nested
    inner class PuzzleAppending {
        @Test
        internal fun `When appending a Puzzle to an unpopular puzzles and the last puzzle's day has passed, new puzzle's date will be tomorrow`() {
            val puzzles = assemble {
                on(1981, 6, 18) thereIs aPuzzle("Birthday") {
                    +"""🎂 🎂 🎂 🎂 🎂"""
                    +"""🎉 🎉 🎉 🎉 🎉"""
                    +"""🎉 🎉 🎉 🎉 🎉"""
                    +"""🎉 🎉 🎉 🎉 🎉"""
                    +"""🎉 🎉 🎉 🎉 🎉"""
                }
            }
            puzzles.append(aPuzzle("Snarf") {
                +"""⚡️ 🐱 🐱 🐱 🐱"""
                +"""⚡️ 🐱 🐱 🐱 🐱"""
                +"""⚡️ 🐱 🐱 🐱 🐱"""
                +"""⚡️ 🐱 🐱 🐱 🐱"""
                +"""⚡️ 🐱 🐱 🐱 🐱"""
            })
            val today = LocalDate.now()
            val tomorrow = today.plusDays(1)
            assertNull(puzzles.find(Year(today.year), Month(today.monthValue), Day(today.dayOfMonth)))
            assertNotNull(puzzles.find(Year(tomorrow.year), Month(tomorrow.monthValue), Day(tomorrow.dayOfMonth)))
        }

        @Test
        internal fun `When appending a Puzzle to a puzzles and the last puzzle's day is exactly today, new puzzle's date will be tomorrow`() {
            val today = LocalDate.now()
            val puzzles = assemble {
                on(today.year, today.monthValue, today.dayOfMonth) thereIs aPuzzle("Birthday") {
                    +"""🎂 🎂 🎂 🎂 🎂"""
                    +"""🎉 🎉 🎉 🎉 🎉"""
                    +"""🎉 🎉 🎉 🎉 🎉"""
                    +"""🎉 🎉 🎉 🎉 🎉"""
                    +"""🎉 🎉 🎉 🎉 🎉"""
                }
            }
            puzzles.append(aPuzzle("Snarf") {
                +"""⚡️ 🐱 🐱 🐱 🐱"""
                +"""⚡️ 🐱 🐱 🐱 🐱"""
                +"""⚡️ 🐱 🐱 🐱 🐱"""
                +"""⚡️ 🐱 🐱 🐱 🐱"""
                +"""⚡️ 🐱 🐱 🐱 🐱"""
            })
            val tomorrow = today.plusDays(1)
            assertNotNull(puzzles.find(Year(tomorrow.year), Month(tomorrow.monthValue), Day(tomorrow.dayOfMonth)))
        }

        @Test
        internal fun `When appending a Puzzle to a popular puzzles, new puzzle's date will be the day after the last puzzle`() {
            val today = LocalDate.now()
            val tomorrow = today.plusDays(1)
            val puzzles = assemble {
                on(tomorrow.year, tomorrow.monthValue, tomorrow.dayOfMonth) thereIs aPuzzle("Birthday") {
                    +"""🎂 🎂 🎂 🎂 🎂"""
                    +"""🎉 🎉 🎉 🎉 🎉"""
                    +"""🎉 🎉 🎉 🎉 🎉"""
                    +"""🎉 🎉 🎉 🎉 🎉"""
                    +"""🎉 🎉 🎉 🎉 🎉"""
                }
            }
            puzzles.append(aPuzzle("Snarf") {
                +"""⚡️ 🐱 🐱 🐱 🐱"""
                +"""⚡️ 🐱 🐱 🐱 🐱"""
                +"""⚡️ 🐱 🐱 🐱 🐱"""
                +"""⚡️ 🐱 🐱 🐱 🐱"""
                +"""⚡️ 🐱 🐱 🐱 🐱"""
            })
            val theDayAfterTomorrow = tomorrow.plusDays(1)
            assertNotNull(puzzles.find(Year(theDayAfterTomorrow.year), Month(theDayAfterTomorrow.monthValue), Day(theDayAfterTomorrow.dayOfMonth)))
        }
    }
}

fun aDefaultPuzzle(solution: String) = aPuzzle(solution) {
    +"""🎂 🎂 🎂 🎂 🎂"""
    +"""🎂 🎂 🎂 🎂 🎂"""
    +"""🎂 🎂 🎂 🎂 🎂"""
    +"""🎂 🎂 🎂 🎂 🎂"""
    +"""🎂 🎂 🎂 🎂 🎂"""
}
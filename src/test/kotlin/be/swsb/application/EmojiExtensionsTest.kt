package be.swsb.application

import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EmojiExtensionsTest {

    @Test
    fun `isEmoji for single emoji's`() {
        emojiExamples.forEach { example ->
            assertTrue(""""$example" should be considered an emoji but it wasn't""") { example.isEmoji() }
        }
        nonEmojiExamples.forEach { example ->
            assertFalse(""""$example" should be not considered an emoji but it was""") { example.isEmoji() }
        }
    }

    @Test
    fun `isEmoji operation after splitting a string on a blank string`() {
        "π π€ π¨βπ©βπ¦βπ¦ π¨πΏ".split(" ").forEach { example ->
            assertTrue(""""$example" should be considered an emoji but it wasn't""") { example.isEmoji() }
        }
    }
}

private val emojiExamples = listOf("π", "π€", "π¨βπ©βπ¦βπ¦", "π¨πΏ", "π¦π²", "π΄ββ οΈ", "π²", "1οΈβ£")
private val nonEmojiExamples = listOf("", " ", "s", ":smirk:", "string with πextra") + ('0'..'z').map { "$it" }
package kleinert.edn.reader

import kleinert.edn.EDN
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EDNReaderStringTest {
    @Test
    fun parseStringBasicTest() {
        run {
            val it = EDN.read("\"\"")
            Assertions.assertTrue(it is String)
        }
        run {
            val it = EDN.read("\"\"")
            Assertions.assertEquals("", it)
        }
        run {
            val it = EDN.read("\"abc\"")
            Assertions.assertEquals("abc", it)
        }
    }

    @Test
    fun parseStringEscapeSequenceTest() {
        run {
            val it = EDN.read("\"\\n\"")
            Assertions.assertEquals("\n", it)
        }
        run {
            val it = EDN.read("\"\\n\"")
            Assertions.assertEquals(listOf("", ""), (it as String).lines())
        }
        run {
            val it = EDN.read("\"\\t\"")
            Assertions.assertEquals("\t", it)
        }

        run {
            val it = EDN.read("\"\\t\"")
            Assertions.assertEquals("\t", it)
        }
        run {
            val it = EDN.read("\"\\b\"")
            Assertions.assertEquals("\b", it)
        }
        run {
            val it = EDN.read("\"\\r\"")
            Assertions.assertEquals("\r", it)
        }
        run {
            val it = EDN.read("\"\\\"\"")
            Assertions.assertEquals("\"", it)
        }

        run {
            val it = EDN.read("\"\\\\\"")
            Assertions.assertEquals("\\", it)
        }
        run {
            val it = EDN.read("\"\\\\\\\\\"")
            Assertions.assertEquals("\\\\", it)
        }
        run {
            val it = EDN.read(
                """
            "\\"
        """.trimIndent()
            )
            Assertions.assertEquals("\\", it)
        }
        run {
            val it = EDN.read(
                """
            "\\\\"
        """.trimIndent()
            )
            Assertions.assertEquals("\\\\", it)
        }

        run {
            val it = EDN.read("\"\\t\\t\"")
            Assertions.assertEquals("\t\t", it)
        }
    }

    @Test
    fun parseStringUnicodeSequenceTest() {
        run {
            val it = EDN.read("\"🎁\"")
            Assertions.assertEquals("🎁", it)
        }
        run {
            val it = EDN.read("\"\\uD83C\\uDF81\"")
            Assertions.assertEquals("🎁", it)
        }
        run {
            val it = EDN.read("\"\\uD83C\\uDF81\"")
            Assertions.assertEquals("🎁", it)
        }
        run {
            val it = EDN.read("\"\\x0001F381\"")
            Assertions.assertEquals("🎁", it)
        }
    }

    @Test
    fun parseStringUnclosedTest() {
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("\"") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("\"abc") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("\"\"\"") }
    }
}

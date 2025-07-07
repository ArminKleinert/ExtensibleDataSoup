package kleinert.soap

import kleinert.soap.edn.EDN
import kleinert.soap.edn.EdnReaderException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EDNReaderStringTest {
    @Test
    fun parseStringBasicTest() {
        EDN.read("\"\"").let { Assertions.assertInstanceOf(String::class.java, it) }
        EDN.read("\"\"").let { Assertions.assertEquals("", it) }
        EDN.read("\"abc\"").let { Assertions.assertEquals("abc", it) }
    }

    @Test
    fun parseStringEscapeSequenceTest() {
        EDN.read("\"\\n\"").let { Assertions.assertEquals("\n", it) }
        EDN.read("\"\\n\"").let { Assertions.assertEquals(listOf("", ""), (it as String).lines()) }
        EDN.read("\"\\t\"").let { Assertions.assertEquals("\t", it) }

        EDN.read("\"\\t\"").let { Assertions.assertEquals("\t", it) }
        EDN.read("\"\\b\"").let { Assertions.assertEquals("\b", it) }
        EDN.read("\"\\r\"").let { Assertions.assertEquals("\r", it) }
        EDN.read("\"\\\"\"").let { Assertions.assertEquals("\"", it) }

        EDN.read("\"\\\\\"").let { Assertions.assertEquals("\\", it) }
        EDN.read("\"\\\\\\\\\"").let { Assertions.assertEquals("\\\\", it) }
        EDN.read(
            """
            "\\"
        """.trimIndent()
        ).let { Assertions.assertEquals("\\", it) }
        EDN.read(
            """
            "\\\\"
        """.trimIndent()
        ).let { Assertions.assertEquals("\\\\", it) }

        EDN.read("\"\\t\\t\"").let { Assertions.assertEquals("\t\t", it) }
    }

    @Test
    fun parseStringUnicodeSequenceTest() {
        EDN.read("\"🎁\"").let { Assertions.assertEquals("🎁", it) }
        EDN.read("\"\\uD83C\\uDF81\"").let { Assertions.assertEquals("🎁", it) }
        EDN.read("\"\\uD83C\\uDF81\"").let { Assertions.assertEquals("🎁", it) }
        EDN.read("\"\\x0001F381\"").let { Assertions.assertEquals("🎁", it) }
    }

    @Test
    fun parseStringUnclosedTest() {
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("\"") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("\"abc") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("\"\"\"") }
    }
}

package kleinert.edn.reader

import kleinert.edn.EDN
import kleinert.edn.EDNSoapOptions
import kleinert.edn.ExtendedEDNDecoders
import kleinert.edn.data.Keyword
import kleinert.edn.data.PersistentList
import kleinert.edn.data.Symbol
import kleinert.edn.reader.EdnReaderException
import kleinert.edn.reader.EdnReaderException.EdnClassConversionError
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class EDNReaderStringTest {
    @Test
    fun parseStringBasicTest() {
        EDN.read("\"\"").let { Assertions.assertTrue(it is String) }
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

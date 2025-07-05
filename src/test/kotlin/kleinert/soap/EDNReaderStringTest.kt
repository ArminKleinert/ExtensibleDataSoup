package kleinert.soap

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigInteger

class EDNReaderStringTest {
    private fun soap(s: String): Any? {
        return EDNSoapReader.readString(s, EDNSoapOptions.defaultOptions)
    }

    private fun soapE(s: String): Any? {
        return EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions)
    }

    @Test
    fun parseStringBasicTest() {
        soap("\"\"").let { assertInstanceOf(String::class.java, it) }
        soap("\"\"").let { assertEquals("", it) }
        soap("\"abc\"").let { assertEquals("abc", it) }
    }

    @Test
    fun parseStringEscapeSequenceTest() {
        soap("\"\\n\"").let { assertEquals("\n", it) }
        soap("\"\\n\"").let { assertEquals(listOf("", ""), (it as String).lines()) }
        soap("\"\\t\"").let { assertEquals("\t", it) }

        soap("\"\\t\"").let { assertEquals("\t", it) }
        soap("\"\\b\"").let { assertEquals("\b", it) }
        soap("\"\\r\"").let { assertEquals("\r", it) }
        soap("\"\\\"\"").let { assertEquals("\"", it) }

        soap("\"\\\\\"").let { assertEquals("\\", it) }
        soap("\"\\\\\\\\\"").let { assertEquals("\\\\", it) }
        soap("""
            "\\"
        """.trimIndent()).let { assertEquals("\\", it) }
        soap("""
            "\\\\"
        """.trimIndent()).let { assertEquals("\\\\", it) }

        soap("\"\\t\\t\"").let { assertEquals("\t\t", it) }
    }

    @Test
    fun parseStringUnicodeSequenceTest() {
        soap("\"🎁\"").let { assertEquals("🎁", it) }
        soap("\"\\uD83C\\uDF81\"").let { assertEquals("🎁", it) }
        soap("\"\\uD83C\\uDF81\"").let { assertEquals("🎁", it) }
        soap("\"\\x0001F381\"").let { assertEquals("🎁", it) }
    }

    @Test
    fun parseStringUnclosedTest() {
        assertThrows(EdnReaderException::class.java) { soap("\"") }
        assertThrows(EdnReaderException::class.java) { soap("\"abc") }
        assertThrows(EdnReaderException::class.java) { soap("\"\"\"") }
    }
}

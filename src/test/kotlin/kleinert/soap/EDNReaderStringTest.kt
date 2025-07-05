package kleinert.soap

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions

class EDNReaderStringTest {
    private fun soap(s: String): Any? {
        return EDNSoapReader.readString(s, EDNSoapOptions.defaultOptions)
    }

    private fun soapE(s: String): Any? {
        return EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions)
    }

    @Test
    fun parseStringBasicTest() {
        soap("\"\"").let { Assertions.assertInstanceOf(String::class.java, it) }
        soap("\"\"").let { Assertions.assertEquals("", it) }
        soap("\"abc\"").let { Assertions.assertEquals("abc", it) }
    }

    @Test
    fun parseStringEscapeSequenceTest() {
        soap("\"\\n\"").let { Assertions.assertEquals("\n", it) }
        soap("\"\\n\"").let { Assertions.assertEquals(listOf("", ""), (it as String).lines()) }
        soap("\"\\t\"").let { Assertions.assertEquals("\t", it) }

        soap("\"\\t\"").let { Assertions.assertEquals("\t", it) }
        soap("\"\\b\"").let { Assertions.assertEquals("\b", it) }
        soap("\"\\r\"").let { Assertions.assertEquals("\r", it) }
        soap("\"\\\"\"").let { Assertions.assertEquals("\"", it) }

        soap("\"\\\\\"").let { Assertions.assertEquals("\\", it) }
        soap("\"\\\\\\\\\"").let { Assertions.assertEquals("\\\\", it) }
        soap("""
            "\\"
        """.trimIndent()).let { Assertions.assertEquals("\\", it) }
        soap("""
            "\\\\"
        """.trimIndent()).let { Assertions.assertEquals("\\\\", it) }

        soap("\"\\t\\t\"").let { Assertions.assertEquals("\t\t", it) }
    }

    @Test
    fun parseStringUnicodeSequenceTest() {
        soap("\"🎁\"").let { Assertions.assertEquals("🎁", it) }
        soap("\"\\uD83C\\uDF81\"").let { Assertions.assertEquals("🎁", it) }
        soap("\"\\uD83C\\uDF81\"").let { Assertions.assertEquals("🎁", it) }
        soap("\"\\x0001F381\"").let { Assertions.assertEquals("🎁", it) }
    }

    @Test
    fun parseStringUnclosedTest() {
        Assertions.assertThrows(EdnReaderException::class.java) { soap("\"") }
        Assertions.assertThrows(EdnReaderException::class.java) { soap("\"abc") }
        Assertions.assertThrows(EdnReaderException::class.java) { soap("\"\"\"") }
    }
}

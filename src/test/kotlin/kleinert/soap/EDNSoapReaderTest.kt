package kleinert.soap

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigInteger

class EDNSoapReaderTest {
    private fun soap(s: String): Any? {
        return EDNSoapReader.readString(s, EDNSoapOptions.defaultOptions)
    }

    private fun soapE(s: String): Any? {
        return EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions)
    }

    @Test
    fun parseNumberDefaultOptionsTest() {
        soap("0").let { assertEquals(0L, it) }
        soap("00").let { assertEquals(0L, it) }
        soap("+0").let { assertEquals(0L, it) }
        soap("-0").let { assertEquals(0L, it) }

        soap("255").let { assertEquals(255L, it) }
        soap("0377").let { assertEquals(255L, it) }
        soap("0xFF").let { assertEquals(255L, it) }
        soap("0b11111111").let { assertEquals(255L, it) }
        soap("+255").let { assertEquals(255L, it) }
        soap("+0377").let { assertEquals(255L, it) }
        soap("+0xFF").let { assertEquals(255L, it) }
        soap("+0b11111111").let { assertEquals(255L, it) }
        soap("-255").let { assertEquals(-255L, it) }
        soap("-0377").let { assertEquals(-255L, it) }
        soap("-0xFF").let { assertEquals(-255L, it) }
        soap("-0b11111111").let { assertEquals(-255L, it) }

        soap("0N").let { assertEquals(BigInteger.ZERO, it) }
        soap("00N").let { assertEquals(BigInteger.ZERO, it) }
        soap("+0N").let { assertEquals(BigInteger.ZERO, it) }
        soap("-0N").let { assertEquals(BigInteger.ZERO, it) }

        val tff = BigInteger.valueOf(255L)
        soap("255N").let { assertEquals(tff, it) }
        soap("0377N").let { assertEquals(tff, it) }
        soap("0xFFN").let { assertEquals(tff, it) }
        soap("0b11111111N").let { assertEquals(tff, it) }
        soap("+255N").let { assertEquals(tff, it) }
        soap("+0377N").let { assertEquals(tff, it) }
        soap("+0xFFN").let { assertEquals(tff, it) }
        soap("+0b11111111N").let { assertEquals(tff, it) }
        soap("-255N").let { assertEquals(-tff, it) }
        soap("-0377N").let { assertEquals(-tff, it) }
        soap("-0xFFN").let { assertEquals(-tff, it) }
        soap("-0b11111111N").let { assertEquals(-tff, it) }
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
        soap(
            """
            "\\"
        """.trimMargin()
        ).let { assertEquals("\\", it) }

        soap(
            """
            "\\\\"
        """.trimMargin()
        ).let { assertEquals("\\\\", it) }

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

    @Test
    fun parseDiscardTest() {
        soap("#_1 2").let { assertEquals(2L, it) }

        // Discard tags ignore spaces.
        soap("#_ 1 2").let { assertEquals(2L, it) }
        soap("#_      1 2").let { assertEquals(2L, it) }

        // Discard ignores newlines
        soap("#_\n1 2").let { assertEquals(2L, it) }

        // Discard is right-associative
        soap("#_ #_ 1 2 3").let { assertEquals(3L, it) }

        // Discard ignores comments
        soap("#_ ; abc\n 1 2").let { assertEquals(2L, it) }

        // Discard is nothing. When discard appears in a list, vector, set, or map, it does nothing
        soap("( #_ 1 #_ 2 #_ 3 )").let {
            assertTrue(it is Iterable<*>)
            assertTrue((it as Iterable<*>).toList().isEmpty())
        }
        soap("[#_1 #_2 #_3]").let {
            assertTrue(it is List<*>)
            assertTrue((it as List<*>).isEmpty())
        }
        soap("#{#_1 #_1 #_1}").let {
            assertTrue(it is Set<*>)
            assertTrue((it as Set<*>).isEmpty())
        }
        soap("{#_1 #_1 #_1}").let {
            assertTrue(it is Map<*,*>)
            assertTrue((it as Map<*,*>).isEmpty())
        }

        // Discard in strings does nothing
        soap("\"#_\"").let { assertEquals("#_", it) }
        soap("\"#_1\"").let { assertEquals("#_1", it) }
    }
}

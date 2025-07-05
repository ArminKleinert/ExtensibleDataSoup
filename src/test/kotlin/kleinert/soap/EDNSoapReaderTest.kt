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
    fun parseBigInt() {
        soap("0N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(BigInteger.ZERO, it)
        }
        soap("+0N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(BigInteger.ZERO, it)
        }
        soap("-0N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(BigInteger.ZERO, it)
        }
        soap("00N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(BigInteger.ZERO, it)
        }
        soap("+00N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(BigInteger.ZERO, it)
        }
        soap("-00N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(BigInteger.ZERO, it)
        }
        soap("0o0N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(BigInteger.ZERO, it)
        }
        soap("+0o0N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(BigInteger.ZERO, it)
        }
        soap("-0o0N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(BigInteger.ZERO, it)
        }
        soap("0b0N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(BigInteger.ZERO, it)
        }
        soap("+0b0N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(BigInteger.ZERO, it)
        }
        soap("-0b0N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(BigInteger.ZERO, it)
        }
        soap("0x0N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(BigInteger.ZERO, it)
        }
        soap("+0x0N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(BigInteger.ZERO, it)
        }
        soap("-0x0N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(BigInteger.ZERO, it)
        }

        soap("1N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(BigInteger.ONE, it)
        }
        soap("+1N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(BigInteger.ONE, it)
        }
        soap("-1N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(BigInteger.ONE.negate(), it)
        }
        soap("01N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(BigInteger.ONE, it)
        }
        soap("+01N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(BigInteger.ONE, it)
        }
        soap("-01N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(BigInteger.ONE.negate(), it)
        }
        soap("0o1N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(BigInteger.ONE, it)
        }
        soap("+0o1N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(BigInteger.ONE, it)
        }
        soap("-0o1N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(BigInteger.ONE.negate(), it)
        }
        soap("0b1N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(BigInteger.ONE, it)
        }
        soap("+0b1N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(BigInteger.ONE, it)
        }
        soap("-0b1N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(BigInteger.ONE.negate(), it)
        }
        soap("0x1N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(BigInteger.ONE, it)
        }
        soap("+0x1N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(BigInteger.ONE, it)
        }
        soap("-0x1N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(BigInteger.ONE.negate(), it)
        }

        val bigint128 = BigInteger.valueOf(128L)
        soap("128N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(bigint128, it)
        }
        soap("+128N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(bigint128, it)
        }
        soap("-128N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(bigint128.negate(), it)
        }
        soap("0200N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(bigint128, it)
        }
        soap("+0200N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(bigint128, it)
        }
        soap("-0200N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(bigint128.negate(), it)
        }
        soap("0o200N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(bigint128, it)
        }
        soap("+0o200N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(bigint128, it)
        }
        soap("-0o200N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(bigint128.negate(), it)
        }
        soap("0b10000000N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(bigint128, it)
        }
        soap("+0b10000000N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(bigint128, it)
        }
        soap("-0b10000000N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(bigint128.negate(), it)
        }
        soap("0x80N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(bigint128, it)
        }
        soap("+0x80N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(bigint128, it)
        }
        soap("-0x80N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(bigint128.negate(), it)
        }

        val bigint255 = BigInteger.valueOf(255L)
        soap("255N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(bigint255, it)
        }
        soap("+255N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(bigint255, it)
        }
        soap("-255N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(bigint255.negate(), it)
        }
        soap("0377N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(bigint255, it)
        }
        soap("+0377N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(bigint255, it)
        }
        soap("-0377N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(bigint255.negate(), it)
        }
        soap("0o377N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(bigint255, it)
        }
        soap("+0o377N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(bigint255, it)
        }
        soap("-0o377N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(bigint255.negate(), it)
        }
        soap("0b11111111N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(bigint255, it)
        }
        soap("+0b11111111N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(bigint255, it)
        }
        soap("-0b11111111N").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(bigint255.negate(), it)
        }
        soap("0xFFN").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(bigint255, it)
        }
        soap("+0xFFN").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(bigint255, it)
        }
        soap("-0xFFN").let {
            assertInstanceOf(BigInteger::class.java, it)
            assertEquals(bigint255.negate(), it)
        }
    }

    @Test
    fun parseIntegerDecimal() {
        soap("0").let {
            assertTrue(it is Long)
            assertEquals(0L, it)
        }
        soap("00").let {
            assertTrue(it is Long)
            assertEquals(0L, it)
        }
        soap("+0").let {
            assertTrue(it is Long)
            assertEquals(0L, it)
        }
        soap("-0").let {
            assertTrue(it is Long)
            assertEquals(0L, it)
        }

        soap("1").let {
            assertTrue(it is Long)
            assertEquals(1L, it)
        }
        soap("+1").let {
            assertTrue(it is Long)
            assertEquals(1L, it)
        }
        soap("-1").let {
            assertTrue(it is Long)
            assertEquals(-1L, it)
        }

        soap("128").let {
            assertTrue(it is Long)
            assertEquals(128L, it)
        }
        soap("+128").let {
            assertTrue(it is Long)
            assertEquals(128L, it)
        }
        soap("-128").let {
            assertTrue(it is Long)
            assertEquals(-128L, it)
        }

        soap("255").let {
            assertTrue(it is Long)
            assertEquals(255L, it)
        }
        soap("+255").let {
            assertTrue(it is Long)
            assertEquals(255L, it)
        }
        soap("-255").let {
            assertTrue(it is Long)
            assertEquals(-255L, it)
        }
    }

    @Test
    fun parseIntegerOctal() {
        // Implicit base
        soap("00").let {
            assertTrue(it is Long)
            assertEquals(0L, it)
        }
        soap("+00").let {
            assertTrue(it is Long)
            assertEquals(0L, it)
        }
        soap("-00").let {
            assertTrue(it is Long)
            assertEquals(0L, it)
        }

        soap("01").let {
            assertTrue(it is Long)
            assertEquals(1L, it)
        }
        soap("+01").let {
            assertTrue(it is Long)
            assertEquals(1L, it)
        }
        soap("-01").let {
            assertTrue(it is Long)
            assertEquals(-1L, it)
        }

        soap("0200").let {
            assertTrue(it is Long)
            assertEquals(128L, it)
        }
        soap("+0200").let {
            assertTrue(it is Long)
            assertEquals(128L, it)
        }
        soap("-0200").let {
            assertTrue(it is Long)
            assertEquals(-128L, it)
        }

        soap("0377").let {
            assertTrue(it is Long)
            assertEquals(255L, it)
        }
        soap("+0377").let {
            assertTrue(it is Long)
            assertEquals(255L, it)
        }
        soap("-0377").let {
            assertTrue(it is Long)
            assertEquals(-255L, it)
        }

        // Explicit base

        soap("0o0").let {
            assertTrue(it is Long)
            assertEquals(0L, it)
        }
        soap("+0o0").let {
            assertTrue(it is Long)
            assertEquals(0L, it)
        }
        soap("-0o0").let {
            assertTrue(it is Long)
            assertEquals(0L, it)
        }

        soap("0o1").let {
            assertTrue(it is Long)
            assertEquals(1L, it)
        }
        soap("+0o1").let {
            assertTrue(it is Long)
            assertEquals(1L, it)
        }
        soap("-0o1").let {
            assertTrue(it is Long)
            assertEquals(-1L, it)
        }

        soap("0o200").let {
            assertTrue(it is Long)
            assertEquals(128L, it)
        }
        soap("+0o200").let {
            assertTrue(it is Long)
            assertEquals(128L, it)
        }
        soap("-0o200").let {
            assertTrue(it is Long)
            assertEquals(-128L, it)
        }

        soap("0o377").let {
            assertTrue(it is Long)
            assertEquals(255L, it)
        }
        soap("+0o377").let {
            assertTrue(it is Long)
            assertEquals(255L, it)
        }
        soap("-0o377").let {
            assertTrue(it is Long)
            assertEquals(-255L, it)
        }
    }

    @Test
    fun parseIntegerBinary() {
        soap("0b0").let {
            assertTrue(it is Long)
            assertEquals(0L, it)
        }
        soap("+0b0").let {
            assertTrue(it is Long)
            assertEquals(0L, it)
        }
        soap("-0b0").let {
            assertTrue(it is Long)
            assertEquals(0L, it)
        }

        soap("0b1").let {
            assertTrue(it is Long)
            assertEquals(1L, it)
        }
        soap("+0b1").let {
            assertTrue(it is Long)
            assertEquals(1L, it)
        }
        soap("-0b1").let {
            assertTrue(it is Long)
            assertEquals(-1L, it)
        }

        soap("0b10000000").let {
            assertTrue(it is Long)
            assertEquals(128L, it)
        }
        soap("+0b10000000").let {
            assertTrue(it is Long)
            assertEquals(128L, it)
        }
        soap("-0b10000000").let {
            assertTrue(it is Long)
            assertEquals(-128L, it)
        }

        soap("0b11111111").let {
            assertTrue(it is Long)
            assertEquals(255L, it)
        }
        soap("+0b11111111").let {
            assertTrue(it is Long)
            assertEquals(255L, it)
        }
        soap("-0b11111111").let {
            assertTrue(it is Long)
            assertEquals(-255L, it)
        }
    }

    @Test
    fun parseIntegerHex() {
        soap("0x0").let {
            assertTrue(it is Long)
            assertEquals(0L, it)
        }
        soap("+0x0").let {
            assertTrue(it is Long)
            assertEquals(0L, it)
        }
        soap("-0x0").let {
            assertTrue(it is Long)
            assertEquals(0L, it)
        }

        soap("0x1").let {
            assertTrue(it is Long)
            assertEquals(1L, it)
        }
        soap("+0x1").let {
            assertTrue(it is Long)
            assertEquals(1L, it)
        }
        soap("-0x1").let {
            assertTrue(it is Long)
            assertEquals(-1L, it)
        }

        soap("0x80").let {
            assertTrue(it is Long)
            assertEquals(128L, it)
        }
        soap("+0x80").let {
            assertTrue(it is Long)
            assertEquals(128L, it)
        }
        soap("-0x80").let {
            assertTrue(it is Long)
            assertEquals(-128L, it)
        }

        soap("0xFF").let {
            assertTrue(it is Long)
            assertEquals(255L, it)
        }
        soap("+0xFF").let {
            assertTrue(it is Long)
            assertEquals(255L, it)
        }
        soap("-0xFF").let {
            assertTrue(it is Long)
            assertEquals(-255L, it)
        }
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
    fun parseDiscardSimpleTest() {
        soap("#_1 2").let { assertEquals(2L, it) }
    }

    @Test
    fun parseDiscardTest() {
        soap("#_1 2").let { assertEquals(2L, it) }

        // Discard tags ignore spaces.
        soap("#_ 1 2").let { assertEquals(2L, it) }
        soap("#_      1 2").let { assertEquals(2L, it) }

        // Discard ignores newlines
        soap("#_\n1 2").let { assertEquals(2L, it) }

        // Discard ignores comments
        soap("#_ ; abc\n 1 2").let { assertEquals(2L, it) }

        // Discard in strings does nothing
        soap("\"#_\"").let { assertEquals("#_", it) }
        soap("\"#_1\"").let { assertEquals("#_1", it) }
    }

    @Test
    fun parseDiscardAssociativityTest() {
        // Discard is right-associative
        soap("#_ #_ 1 2 3").let { assertEquals(3L, it) }
    }

    @Test
    fun parseDiscardInCollectionTest() {
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
            assertTrue(it is Map<*, *>)
            assertTrue((it as Map<*, *>).isEmpty())
        }
    }

    @Test
    fun parseEmptyList() {
        // Normal
        soap("()").let {
            assertTrue(it is Iterable<*>)
            assertFalse((it as Iterable<*>).iterator().hasNext())
        }

        // Whitespace does not matter
        soap("(  )").let {
            assertTrue(it is Iterable<*>)
            assertFalse((it as Iterable<*>).iterator().hasNext())
        }
        soap("(\t \n)").let {
            assertTrue(it is Iterable<*>)
            assertFalse((it as Iterable<*>).iterator().hasNext())
        }
        soap("(\n)").let {
            assertTrue(it is Iterable<*>)
            assertFalse((it as Iterable<*>).iterator().hasNext())
        }
    }

    @Test
    fun parseEmptyVector() {
        // Normal
        soap("[]").let {
            assertTrue(it is List<*>)
            assertTrue((it as List<*>).isEmpty())
        }

        // Whitespace does not matter
        soap("[  ]").let {
            assertTrue(it is List<*>)
            assertTrue((it as List<*>).isEmpty())
        }
        soap("[\t \n]").let {
            assertTrue(it is List<*>)
            assertTrue((it as List<*>).isEmpty())
        }
        soap("[\n]").let {
            assertTrue(it is List<*>)
            assertTrue((it as List<*>).isEmpty())
        }
    }

    @Test
    fun parseEmptySet() {
        // Normal
        soap("#{}").let {
            assertTrue(it is Set<*>)
            assertTrue((it as Set<*>).isEmpty())
        }

        // Whitespace does not matter
        soap("#{  }").let {
            assertTrue(it is Set<*>)
            assertTrue((it as Set<*>).isEmpty())
        }
        soap("#{\t \n}").let {
            assertTrue(it is Set<*>)
            assertTrue((it as Set<*>).isEmpty())
        }
        soap("#{\n}").let {
            assertTrue(it is Set<*>)
            assertTrue((it as Set<*>).isEmpty())
        }
    }

    @Test
    fun parseEmptyMap() {
        // Normal
        soap("{}").let {
            assertTrue(it is Map<*, *>)
            assertTrue((it as Map<*, *>).isEmpty())
        }

        // Whitespace does not matter
        soap("{  }").let {
            assertTrue(it is Map<*, *>)
            assertTrue((it as Map<*, *>).isEmpty())
        }
        soap("{\t \n}").let {
            assertTrue(it is Map<*, *>)
            assertTrue((it as Map<*, *>).isEmpty())
        }
        soap("{\n}").let {
            assertTrue(it is Map<*, *>)
            assertTrue((it as Map<*, *>).isEmpty())
        }
    }

    @Test
    fun parseListSimple() {
        soap("(\\a)").let {
            assertTrue(it is Iterable<*>)
            assertEquals(listOf<Any?>('a'), (it as Iterable<*>).toList())
        }
        soap("(\\a \\b)").let {
            assertTrue(it is Iterable<*>)
            assertEquals(listOf('a', 'b'), (it as Iterable<*>).toList())
        }
        soap("(())").let {
            assertTrue(it is Iterable<*>)
            assertEquals(listOf(listOf<Any?>()), (it as Iterable<*>).toList())
        }
    }

    @Test
    fun parseVectorSimple() {
        soap("[\\a]").let {
            assertTrue(it is List<*>)
            assertEquals(listOf('a'), (it as List<*>))
        }
        soap("[\\a \\b]").let {
            assertTrue(it is Iterable<*>)
            assertEquals(listOf('a', 'b'), (it as List<*>))
        }
        soap("[[]]").let {
            assertTrue(it is Iterable<*>)
            assertEquals(listOf(listOf<Any?>()), (it as List<*>))
        }
    }

    @Test
    fun parseSetSimple() {
        soap("#{\\a \\b}").let {
            assertTrue(it is Set<*>)
            assertEquals(setOf('a', 'b'), (it as Set<*>))
        }
        soap("#{ \\a }").let {
            assertTrue(it is Set<*>)
            assertEquals(setOf('a'), (it as Set<*>))
        }
        soap("#{\\a #{}}").let {
            assertTrue(it is Set<*>)
            assertEquals(setOf('a', setOf<Any?>()), (it as Set<*>))
        }
    }

    @Test
    fun parseSetFastSimple() {
        EDNSoapReader.readString("#{\\a \\b}", EDNSoapOptions.extendedOptions.copy(useFasterSetConstruction = true))
            .let {
                assertTrue(it is Set<*>)
                assertEquals(setOf('a', 'b'), (it as Set<*>))
            }
        EDNSoapReader.readString("#{ \\a }", EDNSoapOptions.extendedOptions.copy(useFasterSetConstruction = true))
            .let {
                assertTrue(it is Set<*>)
                assertEquals(setOf('a'), (it as Set<*>))
            }
        EDNSoapReader.readString("#{\\a #{}}", EDNSoapOptions.extendedOptions.copy(useFasterSetConstruction = true))
            .let {
                assertTrue(it is Set<*>)
                assertEquals(setOf('a', setOf<Any?>()), (it as Set<*>))
            }
    }

    @Test
    fun parseMapSimple() {
        soap("{\\a \\b}").let {
            assertTrue(it is Map<*, *>)
            assertEquals(mapOf('a' to 'b').entries, (it as Map<*, *>).entries)
        }
        soap("{\\a {}}").let {
            assertTrue(it is Map<*, *>)
            assertEquals(mapOf('a' to mapOf<Any?, Any?>()).entries, (it as Map<*, *>).entries)
        }
        soap("{{} {}}").let {
            assertTrue(it is Map<*, *>)
            assertEquals(mapOf<Any?, Any?>(mapOf<Any?, Any?>() to mapOf<Any?, Any?>()), (it as Map<*, *>))
        }
    }
}

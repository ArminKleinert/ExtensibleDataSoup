package kleinert.soap.edn

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class EDNSoapReaderTest {
    @Test
    fun parseStringBasicTest() {
        assertInstanceOf(String::class.java, EDN.read("\"\""))
        assertEquals("", EDN.read("\"\""))
        assertEquals("abc", EDN.read("\"abc\""))
    }

    @Test
    fun parseStringEscapeSequenceTest() {
        assertEquals("\n", EDN.read("\"\\n\""))
        assertEquals(listOf("", ""), (EDN.read("\"\\n\"") as String).lines())
        assertEquals("\t", EDN.read("\"\\t\""))

        assertEquals("\t", EDN.read("\"\\t\""))
        assertEquals("\b", EDN.read("\"\\b\""))
        assertEquals("\r", EDN.read("\"\\r\""))
        assertEquals("\"", EDN.read("\"\\\"\""))

        assertEquals("\\", EDN.read("\"\\\\\""))
        assertEquals("\\\\", EDN.read("\"\\\\\\\\\""))
        EDN.read(
            """
            "\\"
        """.trimMargin()
        ).let { assertEquals("\\", it) }

        EDN.read(
            """
            "\\\\"
        """.trimMargin()
        ).let { assertEquals("\\\\", it) }

        assertEquals("\t\t", EDN.read("\"\\t\\t\""))
    }

    @Test
    fun parseStringUnicodeSequenceTest() {
        assertEquals("🎁", EDN.read("\"🎁\""))
        assertEquals("🎁", EDN.read("\"\\uD83C\\uDF81\""))
        assertEquals("🎁", EDN.read("\"\\uD83C\\uDF81\""))
        assertEquals("🎁", EDN.read("\"\\x0001F381\""))
    }

    @Test
    fun parseStringUnclosedTest() {
        assertThrows(EdnReaderException::class.java) { EDN.read("\"") }
        assertThrows(EdnReaderException::class.java) { EDN.read("\"abc") }
        assertThrows(EdnReaderException::class.java) { EDN.read("\"\"\"") }
    }

    @Test
    fun parseDiscardSimpleTest() {
        assertEquals(2L, EDN.read("#_1 2"))
    }

    @Test
    fun parseDiscardTest() {
        assertEquals(2L, EDN.read("#_1 2"))

        // Discard tags ignore spaces.
        assertEquals(2L, EDN.read("#_ 1 2"))
        assertEquals(2L, EDN.read("#_      1 2"))

        // Discard ignores newlines
        assertEquals(2L, EDN.read("#_\n1 2"))

        // Discard ignores comments
        assertEquals(2L, EDN.read("#_ ; abc\n 1 2"))

        // Discard in strings does nothing
        assertEquals("#_", EDN.read("\"#_\""))
        assertEquals("#_1", EDN.read("\"#_1\""))
    }

    @Test
    fun parseDiscardAssociativityTest() {
        // Discard is right-associative
        assertEquals(3L, EDN.read("#_ #_ 1 2 3"))
    }

    @Test
    fun parseDiscardInCollectionTest() {
        // Discard is nothing. When discard appears in a list, vector, set, or map, it does nothing
        EDN.read("( #_ 1 #_ 2 #_ 3 )").let {
            assertTrue(it is Iterable<*>)
            assertTrue((it as Iterable<*>).toList().isEmpty())
        }
        EDN.read("[#_1 #_2 #_3]").let {
            assertTrue(it is List<*>)
            assertTrue((it as List<*>).isEmpty())
        }
        EDN.read("#{#_1 #_1 #_1}").let {
            assertTrue(it is Set<*>)
            assertTrue((it as Set<*>).isEmpty())
        }
        EDN.read("{#_1 #_1 #_1}").let {
            assertTrue(it is Map<*, *>)
            assertTrue((it as Map<*, *>).isEmpty())
        }
    }

    @Test
    fun parseEmptySet() {
        // Normal
        EDN.read("#{}").let {
            assertTrue(it is Set<*>)
            assertTrue((it as Set<*>).isEmpty())
        }

        // Whitespace does not matter
        EDN.read("#{  }").let {
            assertTrue(it is Set<*>)
            assertTrue((it as Set<*>).isEmpty())
        }
        EDN.read("#{\t \n}").let {
            assertTrue(it is Set<*>)
            assertTrue((it as Set<*>).isEmpty())
        }
        EDN.read("#{\n}").let {
            assertTrue(it is Set<*>)
            assertTrue((it as Set<*>).isEmpty())
        }
    }

    @Test
    fun parseEmptyMap() {
        // Normal
        EDN.read("{}").let {
            assertTrue(it is Map<*, *>)
            assertTrue((it as Map<*, *>).isEmpty())
        }

        // Whitespace does not matter
        EDN.read("{  }").let {
            assertTrue(it is Map<*, *>)
            assertTrue((it as Map<*, *>).isEmpty())
        }
        EDN.read("{\t \n}").let {
            assertTrue(it is Map<*, *>)
            assertTrue((it as Map<*, *>).isEmpty())
        }
        EDN.read("{\n}").let {
            assertTrue(it is Map<*, *>)
            assertTrue((it as Map<*, *>).isEmpty())
        }
    }

    @Test
    fun parseListSimple() {
        EDN.read("(\\a)").let {
            assertTrue(it is Iterable<*>)
            assertEquals(listOf<Any?>('a'), (it as Iterable<*>).toList())
        }
        EDN.read("(\\a \\b)").let {
            assertTrue(it is Iterable<*>)
            assertEquals(listOf('a', 'b'), (it as Iterable<*>).toList())
        }
        EDN.read("(())").let {
            assertTrue(it is Iterable<*>)
            assertEquals(listOf(listOf<Any?>()), (it as Iterable<*>).toList())
        }
    }

    @Test
    fun parseVectorSimple() {
        EDN.read("[\\a]").let {
            assertTrue(it is List<*>)
            assertEquals(listOf('a'), (it as List<*>))
        }
        EDN.read("[\\a \\b]").let {
            assertTrue(it is Iterable<*>)
            assertEquals(listOf('a', 'b'), (it as List<*>))
        }
        EDN.read("[[]]").let {
            assertTrue(it is Iterable<*>)
            assertEquals(listOf(listOf<Any?>()), (it as List<*>))
        }
    }

    @Test
    fun parseSetSimple() {
        EDN.read("#{\\a \\b}").let {
            assertTrue(it is Set<*>)
            assertEquals(setOf('a', 'b'), (it as Set<*>))
        }
        EDN.read("#{ \\a }").let {
            assertTrue(it is Set<*>)
            assertEquals(setOf('a'), (it as Set<*>))
        }
        EDN.read("#{\\a #{}}").let {
            assertTrue(it is Set<*>)
            assertEquals(setOf('a', setOf<Any?>()), (it as Set<*>))
        }
    }

    @Test
    fun parseMapSimple() {
        EDN.read("{\\a \\b}").let {
            assertTrue(it is Map<*, *>)
            assertEquals(mapOf('a' to 'b').entries, (it as Map<*, *>).entries)
        }
        EDN.read("{\\a {}}").let {
            assertTrue(it is Map<*, *>)
            assertEquals(mapOf('a' to mapOf<Any?, Any?>()).entries, (it as Map<*, *>).entries)
        }
        EDN.read("{{} {}}").let {
            assertTrue(it is Map<*, *>)
            assertEquals(mapOf<Any?, Any?>(mapOf<Any?, Any?>() to mapOf<Any?, Any?>()), (it as Map<*, *>))
        }
    }
}

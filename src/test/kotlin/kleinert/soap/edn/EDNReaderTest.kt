package kleinert.soap.edn

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EDNReaderTest {
    @Test
    fun parseStringBasicTest() {
        Assertions.assertTrue(EDN.read("\"\"") is String)
        Assertions.assertEquals("", EDN.read("\"\""))
        Assertions.assertEquals("abc", EDN.read("\"abc\""))
    }

    @Test
    fun parseStringEscapeSequenceTest() {
        Assertions.assertEquals("\n", EDN.read("\"\\n\""))
        Assertions.assertEquals(listOf("", ""), (EDN.read("\"\\n\"") as String).lines())
        Assertions.assertEquals("\t", EDN.read("\"\\t\""))

        Assertions.assertEquals("\t", EDN.read("\"\\t\""))
        Assertions.assertEquals("\b", EDN.read("\"\\b\""))
        Assertions.assertEquals("\r", EDN.read("\"\\r\""))
        Assertions.assertEquals("\"", EDN.read("\"\\\"\""))

        Assertions.assertEquals("\\", EDN.read("\"\\\\\""))
        Assertions.assertEquals("\\\\", EDN.read("\"\\\\\\\\\""))
        EDN.read(
            """
            "\\"
        """.trimMargin()
        ).let { Assertions.assertEquals("\\", it) }

        EDN.read(
            """
            "\\\\"
        """.trimMargin()
        ).let { Assertions.assertEquals("\\\\", it) }

        Assertions.assertEquals("\t\t", EDN.read("\"\\t\\t\""))
    }

    @Test
    fun parseStringUnicodeSequenceTest() {
        Assertions.assertEquals("🎁", EDN.read("\"🎁\""))
        Assertions.assertEquals("🎁", EDN.read("\"\\uD83C\\uDF81\""))
        Assertions.assertEquals("🎁", EDN.read("\"\\uD83C\\uDF81\""))
        Assertions.assertEquals("🎁", EDN.read("\"\\x0001F381\""))
    }

    @Test
    fun parseStringUnclosedTest() {
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("\"") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("\"abc") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("\"\"\"") }
    }

    @Test
    fun parseDiscardSimpleTest() {
        Assertions.assertEquals(2L, EDN.read("#_1 2"))
    }

    @Test
    fun parseDiscardTest() {
        Assertions.assertEquals(2L, EDN.read("#_1 2"))

        // Discard tags ignore spaces.
        Assertions.assertEquals(2L, EDN.read("#_ 1 2"))
        Assertions.assertEquals(2L, EDN.read("#_      1 2"))

        // Discard ignores newlines
        Assertions.assertEquals(2L, EDN.read("#_\n1 2"))

        // Discard ignores comments
        Assertions.assertEquals(2L, EDN.read("#_ ; abc\n 1 2"))

        // Discard in strings does nothing
        Assertions.assertEquals("#_", EDN.read("\"#_\""))
        Assertions.assertEquals("#_1", EDN.read("\"#_1\""))
    }

    @Test
    fun parseDiscardAssociativityTest() {
        // Discard is right-associative
        Assertions.assertEquals(3L, EDN.read("#_ #_ 1 2 3"))
    }

    @Test
    fun parseDiscardInCollectionTest() {
        // Discard is nothing. When discard appears in a list, vector, set, or map, it does nothing
        EDN.read("( #_ 1 #_ 2 #_ 3 )").let {
            Assertions.assertTrue(it is Iterable<*>)
            Assertions.assertTrue((it as Iterable<*>).toList().isEmpty())
        }
        EDN.read("[#_1 #_2 #_3]").let {
            Assertions.assertTrue(it is List<*>)
            Assertions.assertTrue((it as List<*>).isEmpty())
        }
        EDN.read("#{#_1 #_1 #_1}").let {
            Assertions.assertTrue(it is Set<*>)
            Assertions.assertTrue((it as Set<*>).isEmpty())
        }
        EDN.read("{#_1 #_1 #_1}").let {
            Assertions.assertTrue(it is Map<*, *>)
            Assertions.assertTrue((it as Map<*, *>).isEmpty())
        }
    }

    @Test
    fun parseEmptySet() {
        // Normal
        EDN.read("#{}").let {
            Assertions.assertTrue(it is Set<*>)
            Assertions.assertTrue((it as Set<*>).isEmpty())
        }

        // Whitespace does not matter
        EDN.read("#{  }").let {
            Assertions.assertTrue(it is Set<*>)
            Assertions.assertTrue((it as Set<*>).isEmpty())
        }
        EDN.read("#{\t \n}").let {
            Assertions.assertTrue(it is Set<*>)
            Assertions.assertTrue((it as Set<*>).isEmpty())
        }
        EDN.read("#{\n}").let {
            Assertions.assertTrue(it is Set<*>)
            Assertions.assertTrue((it as Set<*>).isEmpty())
        }
    }

    @Test
    fun parseEmptyMap() {
        // Normal
        EDN.read("{}").let {
            Assertions.assertTrue(it is Map<*, *>)
            Assertions.assertTrue((it as Map<*, *>).isEmpty())
        }

        // Whitespace does not matter
        EDN.read("{  }").let {
            Assertions.assertTrue(it is Map<*, *>)
            Assertions.assertTrue((it as Map<*, *>).isEmpty())
        }
        EDN.read("{\t \n}").let {
            Assertions.assertTrue(it is Map<*, *>)
            Assertions.assertTrue((it as Map<*, *>).isEmpty())
        }
        EDN.read("{\n}").let {
            Assertions.assertTrue(it is Map<*, *>)
            Assertions.assertTrue((it as Map<*, *>).isEmpty())
        }
    }

    @Test
    fun parseListSimple() {
        EDN.read("(\\a)").let {
            Assertions.assertTrue(it is Iterable<*>)
            Assertions.assertEquals(listOf<Any?>('a'), (it as Iterable<*>).toList())
        }
        EDN.read("(\\a \\b)").let {
            Assertions.assertTrue(it is Iterable<*>)
            Assertions.assertEquals(listOf('a', 'b'), (it as Iterable<*>).toList())
        }
        EDN.read("(())").let {
            Assertions.assertTrue(it is Iterable<*>)
            Assertions.assertEquals(listOf(listOf<Any?>()), (it as Iterable<*>).toList())
        }
    }

    @Test
    fun parseVectorSimple() {
        EDN.read("[\\a]").let {
            Assertions.assertTrue(it is List<*>)
            Assertions.assertEquals(listOf('a'), (it as List<*>))
        }
        EDN.read("[\\a \\b]").let {
            Assertions.assertTrue(it is Iterable<*>)
            Assertions.assertEquals(listOf('a', 'b'), (it as List<*>))
        }
        EDN.read("[[]]").let {
            Assertions.assertTrue(it is Iterable<*>)
            Assertions.assertEquals(listOf(listOf<Any?>()), (it as List<*>))
        }
    }

    @Test
    fun parseSetSimple() {
        EDN.read("#{\\a \\b}").let {
            Assertions.assertTrue(it is Set<*>)
            Assertions.assertEquals(setOf('a', 'b'), (it as Set<*>))
        }
        EDN.read("#{ \\a }").let {
            Assertions.assertTrue(it is Set<*>)
            Assertions.assertEquals(setOf('a'), (it as Set<*>))
        }
        EDN.read("#{\\a #{}}").let {
            Assertions.assertTrue(it is Set<*>)
            Assertions.assertEquals(setOf('a', setOf<Any?>()), (it as Set<*>))
        }
    }

    @Test
    fun parseMapSimple() {
        EDN.read("{\\a \\b}").let {
            Assertions.assertTrue(it is Map<*, *>)
            Assertions.assertEquals(mapOf('a' to 'b').entries, (it as Map<*, *>).entries)
        }
        EDN.read("{\\a {}}").let {
            Assertions.assertTrue(it is Map<*, *>)
            Assertions.assertEquals(mapOf('a' to mapOf<Any?, Any?>()).entries, (it as Map<*, *>).entries)
        }
        EDN.read("{{} {}}").let {
            Assertions.assertTrue(it is Map<*, *>)
            Assertions.assertEquals(mapOf<Any?, Any?>(mapOf<Any?, Any?>() to mapOf<Any?, Any?>()), (it as Map<*, *>))
        }
    }
}

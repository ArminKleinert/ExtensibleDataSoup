package kleinert.edn.reader

import kleinert.edn.EDN
import kleinert.edn.EDNSoupOptions
import kleinert.edn.data.Symbol
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EDNReaderRefTest {
    private fun parse(s: String) =
        EDN.read(
            s,
            EDNSoupOptions.defaultOptions.copy(
                allowReferences = true
            )
        )

    @Test
    fun parseRefUndefTest() {
        Assertions.assertThrows(EdnReaderException::class.java) { parse("#ref A") }
        Assertions.assertThrows(EdnReaderException::class.java) { parse("#ref B") }
    }

    @Test
    fun parseIllegalRefTest() {
        Assertions.assertThrows(EdnReaderException::class.java) { parse("#ref") }
        Assertions.assertThrows(EdnReaderException::class.java) { parse("#ref :A") }
        Assertions.assertThrows(EdnReaderException::class.java) { parse("#ref 0.1") }
        Assertions.assertThrows(EdnReaderException::class.java) { parse("#ref 1") }
        Assertions.assertThrows(EdnReaderException::class.java) { parse("#ref 1N") }
        Assertions.assertThrows(EdnReaderException::class.java) { parse("#ref 1M") }
        Assertions.assertThrows(EdnReaderException::class.java) { parse("#ref \"\"") }
        Assertions.assertThrows(EdnReaderException::class.java) { parse("#ref ()") }
        Assertions.assertThrows(EdnReaderException::class.java) { parse("#ref []") }
        Assertions.assertThrows(EdnReaderException::class.java) { parse("#ref {}") }
        Assertions.assertThrows(EdnReaderException::class.java) { parse("#ref #{}") }
    }

    @Test
    fun parseRefUsingOutsideTest() {
        val options = EDNSoupOptions.defaultOptions.copy(allowReferences = true)
        val refs = mapOf<Symbol, Any?>(Symbol["A"] to 1L, Symbol["B"] to 2L)

        Assertions.assertEquals(1L, EDN.read("#ref A", options.copy(referenceTable = refs)))
        Assertions.assertEquals(2L, EDN.read("#ref B", options.copy(referenceTable = refs)))
    }

    @Test
    fun parseRefUsesReferencesOutsideTest() {
        val options = EDNSoupOptions.defaultOptions.copy(allowReferences = true)
        val value = listOf(1L, 2L, 3L)
        val refs = mapOf<Symbol, Any?>(Symbol["A"] to value)

        Assertions.assertEquals(value , EDN.read("#ref A", options.copy(referenceTable = refs)))

        val temp = EDN.read("[#ref A #ref A]", options.copy(referenceTable = refs))
        temp as List<*>
        Assertions.assertTrue(value === temp[0])
        Assertions.assertTrue(value === temp[1])
    }

    @Test
    fun parseRefIsDynamicTest() {
        val options = EDNSoupOptions.defaultOptions.copy(allowReferences = true)
        val refs = mapOf<Symbol, Any?>(Symbol["A"] to 1L, Symbol["B"] to Symbol["A"], Symbol["C"] to Symbol["B"])

        Assertions.assertEquals(Symbol["A"], EDN.read("#ref B", options.copy(referenceTable = refs)))
        Assertions.assertEquals(Symbol["B"], EDN.read("#ref C", options.copy(referenceTable = refs)))
        Assertions.assertEquals(Symbol["A"], EDN.read("#ref #ref C", options.copy(referenceTable = refs)))
        Assertions.assertEquals(1L, EDN.read("#ref #ref B", options.copy(referenceTable = refs)))
        Assertions.assertEquals(1L, EDN.read("#ref #ref #ref C", options.copy(referenceTable = refs)))
    }

    @Test
    fun parseDefRefInSameParseTest() {
        val options = EDNSoupOptions.defaultOptions.copy(allowReferences = true)

        val text = "#ref A"
        Assertions.assertEquals(22L, EDN.read(text,options.copy(referenceTable =  mapOf(Symbol["A"] to 22L))))

        val text2 = "#ref A"
        Assertions.assertEquals(listOf(1L, 2L), EDN.read(text2, options.copy(referenceTable =  mapOf(Symbol["A"] to listOf(1L, 2L)))))

        val text3 = "#ref A"
        Assertions.assertEquals(listOf(5L, 6L), EDN.read(text3, options.copy(referenceTable = mapOf(Symbol["A"] to listOf(5L, 6L), Symbol["B"] to 44L))))

        val temp = EDN.read("[#ref A #ref A]", options.copy(referenceTable =  mapOf(Symbol["A"] to 22L)))
        Assertions.assertEquals(listOf(22L, 22L), temp)
    }

    @Test
    fun refFromConfig() {
        val options = EDNSoupOptions.defaultOptions.copy(allowReferences = true)

        val configText = "{ A 1, B 2, C 3 }"
        val config: Map<Symbol, Any?> = EDN.read(configText, options) as Map<Symbol, Any?>

        val text = "[#ref A #ref B #ref C]"
        Assertions.assertEquals(listOf(1L, 2L, 3L), EDN.read(text, options.copy(referenceTable =  config)))
    }
}

package kleinert.edn.reader

import kleinert.edn.EDN
import kleinert.edn.EDNSoupOptions
import kleinert.edn.data.Symbol
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EDNReaderDefRefTest {
    private fun parse(s: String) =
        EDN.read(
            s,
            EDNSoupOptions.defaultOptions.copy(
                allowDefinitionsAndReferences = true
            )
        )

    @Test
    fun parseDefLonelyTest() {
        Assertions.assertThrows(EdnReaderException::class.java) { parse("#def") }
        Assertions.assertThrows(EdnReaderException::class.java) { parse("#def [A 22]") }
    }

    @Test
    fun parseDefEmptyTest() {
        Assertions.assertThrows(EdnReaderException::class.java) { parse("#def") }
    }

    @Test
    fun parseDefIllegalInputTypeTest() {
        Assertions.assertThrows(EdnReaderException::class.java) { parse("[] #def A") }
        Assertions.assertThrows(EdnReaderException::class.java) { parse("[] #def :A") }
        Assertions.assertThrows(EdnReaderException::class.java) { parse("[] #def 0.1") }
        Assertions.assertThrows(EdnReaderException::class.java) { parse("[] #def 1") }
        Assertions.assertThrows(EdnReaderException::class.java) { parse("[] #def 1N") }
        Assertions.assertThrows(EdnReaderException::class.java) { parse("[] #def 1M") }
        Assertions.assertThrows(EdnReaderException::class.java) { parse("[] #def \"\"") }
        Assertions.assertThrows(EdnReaderException::class.java) { parse("[] #def {}") }
        Assertions.assertThrows(EdnReaderException::class.java) { parse("[] #def #{}") }
    }

    @Test
    fun parseDefIllegalInputPairTest() {
        // Empty input
        Assertions.assertThrows(EdnReaderException::class.java) { parse("[] #def ()") }
        Assertions.assertThrows(EdnReaderException::class.java) { parse("[] #def []") }

        // Name only
        Assertions.assertThrows(EdnReaderException::class.java) { parse("[] #def (A)") }
        Assertions.assertThrows(EdnReaderException::class.java) { parse("[] #def [A]") }

        // First is not a symbol
        Assertions.assertThrows(EdnReaderException::class.java) { parse("[] #def (1 2)") }
        Assertions.assertThrows(EdnReaderException::class.java) { parse("[] #def [1 2]") }

        // Too many inputs
        Assertions.assertThrows(EdnReaderException::class.java) { parse("[] #def (A 1 2)") }
        Assertions.assertThrows(EdnReaderException::class.java) { parse("[] #def [A 1 2]") }
    }

    @Test
    fun parseDefReturnsNothingInputTest() {
        Assertions.assertEquals(1L, parse("1 #def [A 22]"))
        Assertions.assertEquals(1L, parse("#def [A 22] 1"))
    }

    @Test
    fun parseDefAlreadyExistsTest() {
        Assertions.assertThrows(EdnReaderException::class.java) { parse("1 #def [A 22] #def [A 33]") }
        Assertions.assertThrows(EdnReaderException::class.java) { parse("#def [A 22] 1 #def [A 33]") }
        Assertions.assertThrows(EdnReaderException::class.java) { parse("#def [A 22] #def [A 33] 1") }
    }

    @Test
    fun parseDefIsPersistentTest() {
        val options = EDNSoupOptions.defaultOptions.copy(allowDefinitionsAndReferences = true)
        val refs = mutableMapOf<Symbol, Any?>()

        Assertions.assertEquals(1L, EDN.read("1 #def [A 22]", options, references = refs))
        Assertions.assertEquals(refs[Symbol["A"]], 22L)

        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("2 #def [A 1]", options, references = refs) }
    }

    @Test
    fun parseRefUndefTest() {
        Assertions.assertThrows(EdnReaderException::class.java) { parse("#ref A") }
        Assertions.assertThrows(EdnReaderException::class.java) { parse("#def [A 11] #ref B") }
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
        val options = EDNSoupOptions.defaultOptions.copy(allowDefinitionsAndReferences = true)
        val refs = mutableMapOf<Symbol, Any?>(Symbol["A"] to 1L, Symbol["B"] to 2L)

        Assertions.assertEquals(1L, EDN.read("#ref A", options, references = refs))
        Assertions.assertEquals(2L, EDN.read("#ref B", options, references = refs))
    }

    @Test
    fun parseRefUsesReferencesOutsideTest() {
        val options = EDNSoupOptions.defaultOptions.copy(allowDefinitionsAndReferences = true)
        val value = listOf(1, 2, 3)
        val refs = mutableMapOf<Symbol, Any?>(Symbol["A"] to value)

        Assertions.assertTrue(value === EDN.read("#ref A", options, references = refs))

        val temp = EDN.read("[#ref A #ref A]", options, references = refs)
        temp as List<*>
        Assertions.assertTrue(value === temp[0])
        Assertions.assertTrue(value === temp[1])
    }

    @Test
    fun parseRefIsDynamicTest() {
        val options = EDNSoupOptions.defaultOptions.copy(allowDefinitionsAndReferences = true)
        val refs = mutableMapOf<Symbol, Any?>(Symbol["A"] to 1L, Symbol["B"] to Symbol["A"], Symbol["C"] to Symbol["B"])

        Assertions.assertEquals(Symbol["A"], EDN.read("#ref B", options, references = refs))
        Assertions.assertEquals(Symbol["B"], EDN.read("#ref C", options, references = refs))
        Assertions.assertEquals(Symbol["A"], EDN.read("#ref #ref C", options, references = refs))
        Assertions.assertEquals(1L, EDN.read("#ref #ref B", options, references = refs))
        Assertions.assertEquals(1L, EDN.read("#ref #ref #ref C", options, references = refs))
    }

    @Test
    fun parseDefRefInSameParseTest() {
        val options = EDNSoupOptions.defaultOptions.copy(allowDefinitionsAndReferences = true)

        val text = "#def [A 22]\n#ref A"
        Assertions.assertEquals(22L, EDN.read(text, options))

        val text2 = "#def [A [1 2]]\n#ref A"
        Assertions.assertEquals(listOf(1L, 2L), EDN.read(text2, options))

        val text3 = "#def [A [5 6]]\n#def [B 44]\n#ref A"
        Assertions.assertEquals(listOf(5L, 6L), EDN.read(text3, options))

        val temp = EDN.read("#def [A 22]\n[#ref A #ref A]", options)
        Assertions.assertEquals(listOf(22L, 22L), temp)
    }

    @Test
    fun parseDefRefInDifferentParsesTest() {
        val options = EDNSoupOptions.defaultOptions.copy(allowDefinitionsAndReferences = true)
        val refs = mutableMapOf<Symbol, Any?>()

        val text = "#def [A 22]\n#def [B 33]\ntrue"
        Assertions.assertEquals(true, EDN.read(text, options, refs))
        Assertions.assertEquals(22L, refs[Symbol["A"]])
        Assertions.assertEquals(33L, refs[Symbol["B"]])

        Assertions.assertEquals(22L, EDN.read("#ref A", options, refs))
        Assertions.assertEquals(33L, EDN.read("#ref B", options, refs))

        Assertions.assertEquals(44L, EDN.read("#def [C 44]\n#ref C", options, refs))
        Assertions.assertEquals(22L, refs[Symbol["A"]])
        Assertions.assertEquals(33L, refs[Symbol["B"]])
        Assertions.assertEquals(44L, refs[Symbol["C"]])
    }
}

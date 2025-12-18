package kleinert.edn.reader

import kleinert.edn.EDN
import kleinert.edn.data.Keyword
import kleinert.edn.data.Ratio
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.BigInteger

class EDNReaderMacrosTest {
    @Test
    fun parseUnknownMacroTest() {
        val options = EDN.defaultOptions.copy(dispatchMacros = EDNReaderPredefMacros.preDefined)
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("#macro 22", options) }
    }

    @Test
    fun parseIllegalArgumentTest() {
        val options =
            EDN.defaultOptions.copy(dispatchMacros = mapOf("macro" to { args -> require(args is Long); args }))
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("#macro []", options) }
    }

    @Test
    fun parseSimpleMacroTest() {
        val options = EDN.defaultOptions.copy(dispatchMacros = mapOf("str" to Any?::toString))
        Assertions.assertEquals("111", EDN.read("#str 111", options))
        Assertions.assertEquals("[]", EDN.read("#str []", options))
    }

    @Test
    fun parseExampleMacroTest() {
        fun plusLong(args: Any?) =
            if (args is Iterable<*>) {
                var res = 0L
                for (it in args) res += (it as? Long) ?: throw IllegalArgumentException()
                res
            } else {
                args
            }

        val options = EDN.defaultOptions.copy(dispatchMacros = mapOf("plusLong" to ::plusLong))
        Assertions.assertEquals(1110L, EDN.read("#plusLong [111 222 333 444]", options))
    }

    @Test
    fun parseMacroPredefMergeTest() {
        val options = EDN.defaultOptions.copy(dispatchMacros = EDNReaderPredefMacros.preDefined)
        Assertions.assertEquals(
            mapOf(Keyword["a"] to Keyword["b"], Keyword["c"] to Keyword["g"], Keyword["e"] to Keyword["f"]),
            EDN.read("#merge [{:a :b :c :d} {:c :g :e :f}]", options))
    }
}

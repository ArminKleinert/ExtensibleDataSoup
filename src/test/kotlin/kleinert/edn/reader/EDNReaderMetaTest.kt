package kleinert.edn.reader

import kleinert.edn.EDN
import kleinert.edn.EDNSoapOptions
import kleinert.edn.ExtendedEDNDecoders
import kleinert.edn.data.IObj
import kleinert.edn.data.Keyword
import kleinert.edn.data.PersistentList
import kleinert.edn.data.Symbol
import kleinert.edn.reader.EdnReaderException
import kleinert.edn.reader.EdnReaderException.EdnClassConversionError
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class EDNReaderMetaTest {
    @Test
    fun parseMetaErrors() {
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("^") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("^abc") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("^[]") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("^[] abc") }
    }

    @Test
    fun parseDispatchMetaErrors() {
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("#^") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("#^abc") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("#^[]") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("#^[] abc") }
    }

    @Test
    fun parseMetaSymbolTest() {
        EDN.read("^a b").let {
            Assertions.assertTrue(it is IObj<*>)
            it as IObj<*>
            Assertions.assertEquals(mapOf(Keyword["tag"] to Symbol.symbol("a")), it.meta)
            Assertions.assertEquals(Symbol.symbol("b"), it.obj)
        }
    }

    @Test
    fun parseMetaStringTest() {
        EDN.read("^\"a\" b").let {
            Assertions.assertTrue(it is IObj<*>)
            it as IObj<*>
            Assertions.assertEquals(mapOf(Keyword["tag"] to "a"), it.meta)
            Assertions.assertEquals(Symbol.symbol("b"), it.obj)
        }
    }

    @Test
    fun parseMetaKeywordTest() {
        EDN.read("^:a b").let {
            Assertions.assertTrue(it is IObj<*>)
            it as IObj<*>
            Assertions.assertEquals(mapOf(Keyword["a"] to true), it.meta)
            Assertions.assertEquals(Symbol.symbol("b"), it.obj)
        }
    }

    @Test
    fun parseMetaMapTest() {
        EDN.read("^{\"a\" \"c\"} b").let {
            Assertions.assertTrue(it is IObj<*>)
            it as IObj<*>
            Assertions.assertEquals(mapOf("a" to "c"), it.meta)
            Assertions.assertEquals(Symbol.symbol("b"), it.obj)
        }
    }

    @Test
    fun parseDispatchMetaSymbolTest() {
        EDN.read("#^a b").let {
            Assertions.assertTrue(it is IObj<*>)
            it as IObj<*>
            Assertions.assertEquals(mapOf(Keyword["tag"] to Symbol.symbol("a")), it.meta)
            Assertions.assertEquals(Symbol.symbol("b"), it.obj)
        }
    }

    @Test
    fun parseDispatchMetaStringTest() {
        EDN.read("#^\"a\" b").let {
            Assertions.assertTrue(it is IObj<*>)
            it as IObj<*>
            Assertions.assertEquals(mapOf(Keyword["tag"] to "a"), it.meta)
            Assertions.assertEquals(Symbol.symbol("b"), it.obj)
        }
    }

    @Test
    fun parseDispatchMetaKeywordTest() {
        EDN.read("#^:a b").let {
            Assertions.assertTrue(it is IObj<*>)
            it as IObj<*>
            Assertions.assertEquals(mapOf(Keyword["a"] to true), it.meta)
            Assertions.assertEquals(Symbol.symbol("b"), it.obj)
        }
    }

    @Test
    fun parseDispatchMetaMapTest() {
        EDN.read("#^{\"a\" \"c\"} b").let {
            Assertions.assertTrue(it is IObj<*>)
            it as IObj<*>
            Assertions.assertEquals(mapOf("a" to "c"), it.meta)
            Assertions.assertEquals(Symbol.symbol("b"), it.obj)
        }
    }
}

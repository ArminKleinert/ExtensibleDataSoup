package kleinert.edn.reader

import kleinert.edn.EDN
import kleinert.edn.data.IObj
import kleinert.edn.data.Keyword
import kleinert.edn.data.Symbol
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EDNReaderMetaTest {
    private val opts = EDN.defaultOptions.copy(allowMetadata = true)

    @Test
    fun parseMetaErrors() {
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("^", opts) }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("^abc", opts) }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("^[]", opts) }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("^[] abc", opts) }
    }

    @Test
    fun parseDispatchMetaErrors() {
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("#^") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("#^abc") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("#^[]") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("#^[] abc") }

        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("#^", opts) }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("#^abc", opts) }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("#^[]", opts) }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("#^[] abc", opts) }
    }

    @Test
    fun parseMetaSymbolTest() {
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("^a b") }

        run {
            val it = EDN.read("^a b", opts)
            Assertions.assertTrue(it is IObj<*>)
            it as IObj<*>
            Assertions.assertEquals(mapOf(Keyword["tag"] to Symbol.symbol("a")), it.meta)
            Assertions.assertEquals(Symbol.symbol("b"), it.obj)
        }
    }

    @Test
    fun parseMetaStringTest() {
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("^\"a\" b") }

        run {
            val it = EDN.read("^\"a\" b", opts)
            Assertions.assertTrue(it is IObj<*>)
            it as IObj<*>
            Assertions.assertEquals(mapOf(Keyword["tag"] to "a"), it.meta)
            Assertions.assertEquals(Symbol.symbol("b"), it.obj)
        }
    }

    @Test
    fun parseMetaKeywordTest() {
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("^:a b") }

        run {
            val it = EDN.read("^:a b", opts)
            Assertions.assertTrue(it is IObj<*>)
            it as IObj<*>
            Assertions.assertEquals(mapOf(Keyword["a"] to true), it.meta)
            Assertions.assertEquals(Symbol.symbol("b"), it.obj)
        }
    }

    @Test
    fun parseMetaMapTest() {
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("^{\"a\" \"c\"} b") }

        run {
            val it = EDN.read("^{\"a\" \"c\"} b", opts)
            Assertions.assertTrue(it is IObj<*>)
            it as IObj<*>
            Assertions.assertEquals(mapOf("a" to "c"), it.meta)
            Assertions.assertEquals(Symbol.symbol("b"), it.obj)
        }
    }

    @Test
    fun parseDispatchMetaSymbolTest() {
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("#^a b") }

        run {
            val it = EDN.read("#^a b", opts)
            Assertions.assertTrue(it is IObj<*>)
            it as IObj<*>
            Assertions.assertEquals(mapOf(Keyword["tag"] to Symbol.symbol("a")), it.meta)
            Assertions.assertEquals(Symbol.symbol("b"), it.obj)
        }
    }

    @Test
    fun parseDispatchMetaStringTest() {
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("#^\"a\" b") }

        run {
            val it = EDN.read("#^\"a\" b", opts)
            Assertions.assertTrue(it is IObj<*>)
            it as IObj<*>
            Assertions.assertEquals(mapOf(Keyword["tag"] to "a"), it.meta)
            Assertions.assertEquals(Symbol.symbol("b"), it.obj)
        }
    }

    @Test
    fun parseDispatchMetaKeywordTest() {
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("#^:a b") }

        run {
            val it = EDN.read("#^:a b", opts)
            Assertions.assertTrue(it is IObj<*>)
            it as IObj<*>
            Assertions.assertEquals(mapOf(Keyword["a"] to true), it.meta)
            Assertions.assertEquals(Symbol.symbol("b"), it.obj)
        }
    }

    @Test
    fun parseDispatchMetaMapTest() {
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("#^{\"a\" \"c\"} b") }

        run {
            val it = EDN.read("#^{\"a\" \"c\"} b", opts)
            Assertions.assertTrue(it is IObj<*>)
            it as IObj<*>
            Assertions.assertEquals(mapOf("a" to "c"), it.meta)
            Assertions.assertEquals(Symbol.symbol("b"), it.obj)
        }
    }
}

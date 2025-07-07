package kleinert.soap

import kleinert.soap.data.Symbol
import kleinert.soap.edn.EDN
import kleinert.soap.edn.EdnReaderException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EDNReaderSymbolTest {
    @Test
    fun parseSymbolBasicTest() {
        run {
            val text = "ab"
            val it = EDN.read(text)
            val symbol = Symbol.parse(text)!!
            Assertions.assertInstanceOf(Symbol::class.java, it)
            it as Symbol
            Assertions.assertEquals(symbol, it)
            Assertions.assertEquals(symbol.prefix, it.prefix)
            Assertions.assertEquals(symbol.name, it.name)
        }
        run {
            val text = "a1"
            val it = EDN.read(text)
            val symbol = Symbol.parse(text)!!
            Assertions.assertInstanceOf(Symbol::class.java, it)
            it as Symbol
            Assertions.assertEquals(symbol, it)
            Assertions.assertEquals(symbol.prefix, it.prefix)
            Assertions.assertEquals(symbol.name, it.name)
        }
    }

    @Test
    fun parseSymbolWithNamespaceTest() {
        run {
            val text = "a/b"
            val it = EDN.read(text)
            val symbol = Symbol.parse(text)!!
            Assertions.assertInstanceOf(Symbol::class.java, it)
            it as Symbol
            Assertions.assertEquals(symbol, it)
            Assertions.assertEquals(symbol.prefix, it.prefix)
            Assertions.assertEquals(symbol.name, it.name)
        }
    }

    @Test
    fun parseSymbolSymbolsTest() {
        run {
            val text = "+"
            val it = EDN.read(text)
            val symbol = Symbol.parse(text)!!
            Assertions.assertInstanceOf(Symbol::class.java, it)
            it as Symbol
            Assertions.assertEquals(symbol, it)
            Assertions.assertEquals(symbol.prefix, it.prefix)
            Assertions.assertEquals(symbol.name, it.name)
        }
        run {
            val text = "+-"
            val it = EDN.read(text)
            val symbol = Symbol.parse(text)!!
            Assertions.assertInstanceOf(Symbol::class.java, it)
            it as Symbol
            Assertions.assertEquals(symbol, it)
            Assertions.assertEquals(symbol.prefix, it.prefix)
            Assertions.assertEquals(symbol.name, it.name)
        }
        run {
            val text = "->"
            val it = EDN.read(text)
            val symbol = Symbol.parse(text)!!
            Assertions.assertInstanceOf(Symbol::class.java, it)
            it as Symbol
            Assertions.assertEquals(symbol, it)
            Assertions.assertEquals(symbol.prefix, it.prefix)
            Assertions.assertEquals(symbol.name, it.name)
        }
        run {
            val text = "==="
            val it = EDN.read(text)
            val symbol = Symbol.parse(text)!!
            Assertions.assertInstanceOf(Symbol::class.java, it)
            it as Symbol
            Assertions.assertEquals(symbol, it)
            Assertions.assertEquals(symbol.prefix, it.prefix)
            Assertions.assertEquals(symbol.name, it.name)
        }
    }

    @Test
    fun parseSymbolSymbolsMixTest() {
        run {
            val text = "a+"
            val it = EDN.read(text)
            val symbol = Symbol.parse(text)!!
            Assertions.assertInstanceOf(Symbol::class.java, it)
            it as Symbol
            Assertions.assertEquals(symbol, it)
            Assertions.assertEquals(symbol.prefix, it.prefix)
            Assertions.assertEquals(symbol.name, it.name)
        }
        run {
            val text = "-a"
            val it = EDN.read(text)
            val symbol = Symbol.parse(text)!!
            Assertions.assertInstanceOf(Symbol::class.java, it)
            it as Symbol
            Assertions.assertEquals(symbol, it)
            Assertions.assertEquals(symbol.prefix, it.prefix)
            Assertions.assertEquals(symbol.name, it.name)
        }
    }

    @Test
    fun parseSymbolUTFTest() {
        run { // 'λ' fits into simple chars.
            val text = "λ"
            val it = EDN.read(text)
            val symbol = Symbol.parse(text)!!
            Assertions.assertInstanceOf(Symbol::class.java, it)
            it as Symbol
            Assertions.assertEquals(symbol, it)
            Assertions.assertEquals(symbol.prefix, it.prefix)
            Assertions.assertEquals(symbol.name, it.name)
        }
        run { // '🎁' does not fit into simple chars, requiring options.allowUTFSymbols.
            val text = "🎁"
            val it = EDN.read(text, EDN.extendedOptions)
            val symbol = Symbol.parse(text, true)!!
            Assertions.assertInstanceOf(Symbol::class.java, it)
            it as Symbol
            Assertions.assertEquals(symbol, it)
            Assertions.assertEquals(symbol.prefix, it.prefix)
            Assertions.assertEquals(symbol.name, it.name)
        }
    }

    @Test
    fun parseInvalidSymbolTest() {
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("\uD83C\uDF81") } // UTF-16 only valid with extension.
    }
}

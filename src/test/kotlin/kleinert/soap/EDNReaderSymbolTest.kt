package kleinert.soap

import kleinert.soap.data.Symbol
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class EDNReaderSymbolTest {
    private fun soap(s: String): Any? {
        return EDNSoapReader.readString(s, EDNSoapOptions.defaultOptions)
    }

    private fun soapE(s: String): Any? {
        return EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions)
    }


    @Test
    fun parseSymbolBasicTest() {
        run {
            val text = "ab"
            val it = soap(text)
            val symbol = Symbol.parse(text)!!
            assertInstanceOf(Symbol::class.java, it)
            it as Symbol
            assertEquals(symbol, it)
            assertEquals(symbol.prefix, it.prefix)
            assertEquals(symbol.name, it.name)
        }
        run {
            val text = "a1"
            val it = soap(text)
            val symbol = Symbol.parse(text)!!
            assertInstanceOf(Symbol::class.java, it)
            it as Symbol
            assertEquals(symbol, it)
            assertEquals(symbol.prefix, it.prefix)
            assertEquals(symbol.name, it.name)
        }
    }

    @Test
    fun parseSymbolWithNamespaceTest() {
        run {
            val text = "a/b"
            val it = soap(text)
            val symbol = Symbol.parse(text)!!
            assertInstanceOf(Symbol::class.java, it)
            it as Symbol
            assertEquals(symbol, it)
            assertEquals(symbol.prefix, it.prefix)
            assertEquals(symbol.name, it.name)
        }
    }

    @Test
    fun parseSymbolSymbolsTest() {
        run {
            val text = "+"
            val it = soap(text)
            val symbol = Symbol.parse(text)!!
            assertInstanceOf(Symbol::class.java, it)
            it as Symbol
            assertEquals(symbol, it)
            assertEquals(symbol.prefix, it.prefix)
            assertEquals(symbol.name, it.name)
        }
        run {
            val text = "+-"
            val it = soap(text)
            val symbol = Symbol.parse(text)!!
            assertInstanceOf(Symbol::class.java, it)
            it as Symbol
            assertEquals(symbol, it)
            assertEquals(symbol.prefix, it.prefix)
            assertEquals(symbol.name, it.name)
        }
        run {
            val text = "->"
            val it = soap(text)
            val symbol = Symbol.parse(text)!!
            assertInstanceOf(Symbol::class.java, it)
            it as Symbol
            assertEquals(symbol, it)
            assertEquals(symbol.prefix, it.prefix)
            assertEquals(symbol.name, it.name)
        }
        run {
            val text = "==="
            val it = soap(text)
            val symbol = Symbol.parse(text)!!
            assertInstanceOf(Symbol::class.java, it)
            it as Symbol
            assertEquals(symbol, it)
            assertEquals(symbol.prefix, it.prefix)
            assertEquals(symbol.name, it.name)
        }
    }

    @Test
    fun parseSymbolSymbolsMixTest() {
        run {
            val text = "a+"
            val it = soap(text)
            val symbol = Symbol.parse(text)!!
            assertInstanceOf(Symbol::class.java, it)
            it as Symbol
            assertEquals(symbol, it)
            assertEquals(symbol.prefix, it.prefix)
            assertEquals(symbol.name, it.name)
        }
        run {
            val text = "-a"
            val it = soap(text)
            val symbol = Symbol.parse(text)!!
            assertInstanceOf(Symbol::class.java, it)
            it as Symbol
            assertEquals(symbol, it)
            assertEquals(symbol.prefix, it.prefix)
            assertEquals(symbol.name, it.name)
        }
    }

    @Test
    fun parseSymbolUTFTest() {
        run { // 'λ' fits into simple chars.
            val text = "λ"
            val it = soap(text)
            val symbol = Symbol.parse(text)!!
            assertInstanceOf(Symbol::class.java, it)
            it as Symbol
            assertEquals(symbol, it)
            assertEquals(symbol.prefix, it.prefix)
            assertEquals(symbol.name, it.name)
        }
        run { // '🎁' does not fit into simple chars, requiring options.allowUTFSymbols.
            val text = "🎁"
            val it = soapE(text)
            val symbol = Symbol.parse(text, true)!!
            assertInstanceOf(Symbol::class.java, it)
            it as Symbol
            assertEquals(symbol, it)
            assertEquals(symbol.prefix, it.prefix)
            assertEquals(symbol.name, it.name)
        }
    }

    @Test
    fun parseInvalidSymbolTest() {
        assertThrows(EdnReaderException::class.java) { soap("\uD83C\uDF81") } // UTF-8 only valid with extension.
    }
}

package kleinert.soap

import kleinert.soap.data.Keyword
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class EDNReaderKeywordTest {
    private fun soap(s: String): Any? {
        return EDNSoapReader.readString(s, EDNSoapOptions.defaultOptions)
    }

    private fun soapE(s: String): Any? {
        return EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions)
    }

    @Test
    fun parseKeywordBasicTest() {
        run {
            val text = ":ab"
            val it = soap(text)
            val keyword = Keyword.parse(text)!!
            assertInstanceOf(Keyword::class.java, it)
            it as Keyword
            assertEquals(keyword, it)
            assertEquals(keyword.prefix, it.prefix)
            assertEquals(keyword.name, it.name)
        }
        run {
            val text = ":a1"
            val it = soap(text)
            val keyword = Keyword.parse(text)!!
            assertInstanceOf(Keyword::class.java, it)
            it as Keyword
            assertEquals(keyword, it)
            assertEquals(keyword.prefix, it.prefix)
            assertEquals(keyword.name, it.name)
        }
    }

    @Test
    fun parseKeywordWithNamespaceTest() {
        run {
            val text = ":a/b"
            val it = soap(text)
            val keyword = Keyword.parse(text)!!
            assertInstanceOf(Keyword::class.java, it)
            it as Keyword
            assertEquals(keyword, it)
            assertEquals(keyword.prefix, it.prefix)
            assertEquals(keyword.name, it.name)
        }
    }

    @Test
    fun parseKeywordSymbolsTest() {
        run {
            val text = ":+"
            val it = soap(text)
            val keyword = Keyword.parse(text)!!
            assertInstanceOf(Keyword::class.java, it)
            it as Keyword
            assertEquals(keyword, it)
            assertEquals(keyword.prefix, it.prefix)
            assertEquals(keyword.name, it.name)
        }
        run {
            val text = ":+-"
            val it = soap(text)
            val keyword = Keyword.parse(text)!!
            assertInstanceOf(Keyword::class.java, it)
            it as Keyword
            assertEquals(keyword, it)
            assertEquals(keyword.prefix, it.prefix)
            assertEquals(keyword.name, it.name)
        }
        run {
            val text = ":->"
            val it = soap(text)
            val keyword = Keyword.parse(text)!!
            assertInstanceOf(Keyword::class.java, it)
            it as Keyword
            assertEquals(keyword, it)
            assertEquals(keyword.prefix, it.prefix)
            assertEquals(keyword.name, it.name)
        }
        run {
            val text = ":==="
            val it = soap(text)
            val keyword = Keyword.parse(text)!!
            assertInstanceOf(Keyword::class.java, it)
            it as Keyword
            assertEquals(keyword, it)
            assertEquals(keyword.prefix, it.prefix)
            assertEquals(keyword.name, it.name)
        }
    }

    @Test
    fun parseKeywordSymbolsMixTest() {
        soap(":a+").let {
            assertInstanceOf(Keyword::class.java, it)
            assertEquals(Keyword.parse(":a+"), it)
        }
        soap(":-a").let {
            assertInstanceOf(Keyword::class.java, it)
            assertEquals(Keyword.parse(":-a"), it)
        }
        run {
            val text = ":a+"
            val it = soap(text)
            val keyword = Keyword.parse(text)!!
            assertInstanceOf(Keyword::class.java, it)
            it as Keyword
            assertEquals(keyword, it)
            assertEquals(keyword.prefix, it.prefix)
            assertEquals(keyword.name, it.name)
        }
        run {
            val text = ":-a"
            val it = soap(text)
            val keyword = Keyword.parse(text)!!
            assertInstanceOf(Keyword::class.java, it)
            it as Keyword
            assertEquals(keyword, it)
            assertEquals(keyword.prefix, it.prefix)
            assertEquals(keyword.name, it.name)
        }
    }

    @Test
    fun parseKeywordUTFTest() {
        run { // 'λ' fits into simple chars.
            val text = ":λ"
            val it = soap(text)
            val keyword = Keyword.parse(text)!!
            assertInstanceOf(Keyword::class.java, it)
            it as Keyword
            assertEquals(keyword, it)
            assertEquals(keyword.prefix, it.prefix)
            assertEquals(keyword.name, it.name)
        }
        run { // '🎁' does not fit into simple chars, requiring options.allowUTFSymbols.
            val text = ":🎁"
            val it = soapE(text)
            val keyword = Keyword.parse(text, true)!!
            assertInstanceOf(Keyword::class.java, it)
            it as Keyword
            assertEquals(keyword, it)
            assertEquals(keyword.prefix, it.prefix)
            assertEquals(keyword.name, it.name)
        }
    }

    @Test
    fun parseInvalidKeywordTest() {
        assertThrows(EdnReaderException::class.java) { soap(":") } // Only colon is invalid.
        assertThrows(EdnReaderException::class.java) { soap(":/") } // Colon+slash is invalid.

        assertThrows(EdnReaderException::class.java) { soap("::") } // Double colon is invalid.
        assertThrows(EdnReaderException::class.java) { soap("::abc") } // Double colon is invalid.

        assertThrows(EdnReaderException::class.java) { soap(":\uD83C\uDF81") } // UTF-8 only valid with extension.
    }
}

package kleinert.soap.edn

import kleinert.soap.data.Keyword
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EDNReaderKeywordTest {
    @Test
    fun parseKeywordBasicTest() {
        run {
            val text = ":ab"
            val it = EDN.read(text)
            val keyword = Keyword.parse(text)!!
            Assertions.assertTrue(it is Keyword)
            it as Keyword
            Assertions.assertEquals(keyword, it)
            Assertions.assertEquals(keyword.prefix, it.prefix)
            Assertions.assertEquals(keyword.name, it.name)
        }
        run {
            val text = ":a1"
            val it = EDN.read(text)
            val keyword = Keyword.parse(text)!!
            Assertions.assertTrue(it is Keyword)
            it as Keyword
            Assertions.assertEquals(keyword, it)
            Assertions.assertEquals(keyword.prefix, it.prefix)
            Assertions.assertEquals(keyword.name, it.name)
        }
    }

    @Test
    fun parseKeywordWithNamespaceTest() {
        run {
            val text = ":a/b"
            val it = EDN.read(text)
            val keyword = Keyword.parse(text)!!
            Assertions.assertTrue(it is Keyword)
            it as Keyword
            Assertions.assertEquals(keyword, it)
            Assertions.assertEquals(keyword.prefix, it.prefix)
            Assertions.assertEquals(keyword.name, it.name)
        }
    }

    @Test
    fun parseKeywordSymbolsTest() {
        run {
            val text = ":+"
            val it = EDN.read(text)
            val keyword = Keyword.parse(text)!!
            Assertions.assertTrue(it is Keyword)
            it as Keyword
            Assertions.assertEquals(keyword, it)
            Assertions.assertEquals(keyword.prefix, it.prefix)
            Assertions.assertEquals(keyword.name, it.name)
        }
        run {
            val text = ":+-"
            val it = EDN.read(text)
            val keyword = Keyword.parse(text)!!
            Assertions.assertTrue(it is Keyword)
            it as Keyword
            Assertions.assertEquals(keyword, it)
            Assertions.assertEquals(keyword.prefix, it.prefix)
            Assertions.assertEquals(keyword.name, it.name)
        }
        run {
            val text = ":->"
            val it = EDN.read(text)
            val keyword = Keyword.parse(text)!!
            Assertions.assertTrue(it is Keyword)
            it as Keyword
            Assertions.assertEquals(keyword, it)
            Assertions.assertEquals(keyword.prefix, it.prefix)
            Assertions.assertEquals(keyword.name, it.name)
        }
        run {
            val text = ":==="
            val it = EDN.read(text)
            val keyword = Keyword.parse(text)!!
            Assertions.assertTrue(it is Keyword)
            it as Keyword
            Assertions.assertEquals(keyword, it)
            Assertions.assertEquals(keyword.prefix, it.prefix)
            Assertions.assertEquals(keyword.name, it.name)
        }
    }

    @Test
    fun parseKeywordSymbolsMixTest() {
        EDN.read(":a+").let {
            Assertions.assertTrue(it is Keyword)
            Assertions.assertEquals(Keyword.parse(":a+"), it)
        }
        EDN.read(":-a").let {
            Assertions.assertTrue(it is Keyword)
            Assertions.assertEquals(Keyword.parse(":-a"), it)
        }
        run {
            val text = ":a+"
            val it = EDN.read(text)
            val keyword = Keyword.parse(text)!!
            Assertions.assertTrue(it is Keyword)
            it as Keyword
            Assertions.assertEquals(keyword, it)
            Assertions.assertEquals(keyword.prefix, it.prefix)
            Assertions.assertEquals(keyword.name, it.name)
        }
        run {
            val text = ":-a"
            val it = EDN.read(text)
            val keyword = Keyword.parse(text)!!
            Assertions.assertTrue(it is Keyword)
            it as Keyword
            Assertions.assertEquals(keyword, it)
            Assertions.assertEquals(keyword.prefix, it.prefix)
            Assertions.assertEquals(keyword.name, it.name)
        }
    }

    @Test
    fun parseKeywordUTFTest() {
        run { // 'λ' fits into simple chars.
            val text = ":λ"
            val it = EDN.read(text)
            val keyword = Keyword.parse(text)!!
            Assertions.assertTrue(it is Keyword)
            it as Keyword
            Assertions.assertEquals(keyword, it)
            Assertions.assertEquals(keyword.prefix, it.prefix)
            Assertions.assertEquals(keyword.name, it.name)
        }
        run { // '🎁' does not fit into simple chars, requiring options.allowUTFSymbols.
            val text = ":🎁"
            val it = EDN.read(text, EDN.extendedOptions)
            val keyword = Keyword.parse(text, true)!!
            Assertions.assertTrue(it is Keyword)
            it as Keyword
            Assertions.assertEquals(keyword, it)
            Assertions.assertEquals(keyword.prefix, it.prefix)
            Assertions.assertEquals(keyword.name, it.name)
        }
    }

    @Test
    fun parseInvalidKeywordTest() {
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read(":") } // Only colon is invalid.
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read(":/") } // Colon+slash is invalid.

        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("::") } // Double colon is invalid.
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("::abc") } // Double colon is invalid.

        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read(":\uD83C\uDF81") } // UTF-8 only valid with extension.
    }
}

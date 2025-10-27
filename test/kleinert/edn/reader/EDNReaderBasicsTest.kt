package kleinert.edn.reader

import kleinert.edn.EDN
import kleinert.edn.EDNSoapOptions
import kleinert.edn.ExtendedEDNDecoders
import kleinert.edn.data.Keyword
import kleinert.edn.data.PersistentList
import kleinert.edn.data.Symbol
import kleinert.edn.reader.EdnReaderException
import kleinert.edn.reader.EdnReaderException.EdnClassConversionError
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class EDNReaderBasicsTest {
    @Test
    fun parseCommentTest() {
        // Empty output
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read(";") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read(";abc") }

        // Example
        Assertions.assertEquals(1L, EDN.read(";abc\n1"))

        // The expression "\\u8" is invalid, but is ignored in the comment
        Assertions.assertEquals(1L, EDN.read(";\\u8\n1"))

        // Test an error I found: When a comment appears just before the terminator of a sequence,
        // the sequence is not terminated correctly.
        Assertions.assertEquals(listOf<Any?>(), EDN.read("(;\n)"))
        Assertions.assertEquals(listOf<Any?>(), EDN.read("[;\n]"))
        Assertions.assertEquals(mapOf<Any?,Any?>(), EDN.read("{;\n}"))
        Assertions.assertEquals(setOf<Any?>(), EDN.read("#{;\n}"))
    }

    @Test
    fun parseCommaTest() {
        // Empty output
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read(",") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read(",,,,,,,,,,,,,,,,,,,") }

        // Example
        Assertions.assertEquals(1L, EDN.read(", 1"))
        Assertions.assertEquals(1L, EDN.read(",,,,,,, ,,,,,, 1"))
        Assertions.assertEquals(1L, EDN.read("1"))
        Assertions.assertEquals(1L, EDN.read("1 ,,,,,, ,,,,,,,"))
        Assertions.assertEquals(1L, EDN.read(",1 ,"))

        Assertions.assertEquals(listOf(1L), EDN.read("[,1 ,]"))
        Assertions.assertEquals(listOf(1L, 1L), EDN.read("[1,1]"))
    }

    @Test
    fun parseDirectConstantsTest() {
        Assertions.assertEquals(false, EDN.read("false"))
        Assertions.assertEquals(true, EDN.read("true"))
        Assertions.assertEquals(null, EDN.read("nil"))
    }
}

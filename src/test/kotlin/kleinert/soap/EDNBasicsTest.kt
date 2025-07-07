package kleinert.soap

import kleinert.soap.edn.EDN
import kleinert.soap.edn.EdnReaderException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EDNBasicsTest {
    @Test
    fun parseCommentTest() {
        // Empty output
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read(";") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read(";abc") }

        // Example
        Assertions.assertEquals(1L, EDN.read(";abc\n1"))

        // The expression "\\u8" is invalid, but is ignored in the comment
        Assertions.assertEquals(1L, EDN.read(";\\u8\n1"))
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

package kleinert.soap

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EDNBasicsTest {
    private fun soap(s: String): Any? {
        return EDNSoapReader.readString(s, EDNSoapOptions.defaultOptions)
    }

    private fun soapE(s: String): Any? {
        return EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions)
    }

    @Test
    fun parseCommentTest() {
        // Empty output
        Assertions.assertThrows(EdnReaderException::class.java) { soap(";") }
        Assertions.assertThrows(EdnReaderException::class.java) { soap(";abc") }

        // Example
        Assertions.assertEquals(1L, soap(";abc\n1"))

        // The expression "\\u8" is invalid, but is ignored in the comment
        Assertions.assertEquals(1L, soap(";\\u8\n1"))
    }

    @Test
    fun parseCommaTest() {
        // Empty output
        Assertions.assertThrows(EdnReaderException::class.java) { soap(",") }
        Assertions.assertThrows(EdnReaderException::class.java) { soap(",,,,,,,,,,,,,,,,,,,") }

        // Example
        Assertions.assertEquals(1L, soap(", 1"))
        Assertions.assertEquals(1L, soap(",,,,,,, ,,,,,, 1"))
        Assertions.assertEquals(1L, soap("1"))
        Assertions.assertEquals(1L, soap("1 ,,,,,, ,,,,,,,"))
        Assertions.assertEquals(1L, soap(",1 ,"))

        Assertions.assertEquals(listOf(1L), soap("[,1 ,]"))
        Assertions.assertEquals(listOf(1L, 1L), soap("[1,1]"))
    }
}

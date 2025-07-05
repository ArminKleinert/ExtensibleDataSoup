package kleinert.soap

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

class EDNReaderSimpleDispatchTest {
    private fun soap(s: String): Any? {
        return EDNSoapReader.readString(s, EDNSoapOptions.defaultOptions)
    }

    private fun soapE(s: String): Any? {
        return EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions)
    }

    @Test
    fun parseSymbolicTest() {
        Assertions.assertTrue((soap("##NaN") as Double).isNaN())
        Assertions.assertTrue((soap("##-NaN") as Double).isNaN())
        Assertions.assertEquals(Double.POSITIVE_INFINITY, soap("##INF"))
        Assertions.assertEquals(Double.NEGATIVE_INFINITY, soap("##-INF"))
    }

    @Test
    fun parseTimeTest() {
        Assertions.assertThrows(EdnReaderException::class.java) { soap("##time") }
        Assertions.assertInstanceOf(LocalDateTime::class.java, soapE("##time"))
    }

    @Test
    fun parseDispatchChar() {
        // Only possible with extensions
        Assertions.assertThrows(EdnReaderException::class.java) { soap("#\\a") }
        Assertions.assertThrows(EdnReaderException::class.java) { soap("#\\o41") }
        Assertions.assertThrows(EdnReaderException::class.java) { soap("#\\u03BB") }
        Assertions.assertThrows(EdnReaderException::class.java) { soap("#\\u000003bb") }
        Assertions.assertThrows(EdnReaderException::class.java) { soap("#\\x000003bb") }

        // Simple dispatch "char" (string)
        Assertions.assertEquals("a", soapE("#\\a"))

        // Octal dispatch "char" (string)
        Assertions.assertEquals("!", soapE("#\\o41"))
        Assertions.assertEquals("!", soapE("#\\o041"))
        Assertions.assertEquals("ǂ", soapE("#\\o702"))

        // Hexadecimal dispatch "char" (string)
        Assertions.assertEquals("λ", soapE("#\\u03BB"))
        Assertions.assertEquals("λ", soapE("#\\u000003bb"))
        Assertions.assertEquals("λ", soapE("#\\x000003bb"))
        Assertions.assertEquals("ῷ", soapE("#\\u1ff7"))
        Assertions.assertEquals("ῷ", soapE("#\\u00001ff7"))
        Assertions.assertEquals("ῷ", soapE("#\\x00001ff7"))

        // Hexadecimal dispatch "char" (string) over 2^16
        Assertions.assertEquals("\uD83C\uDF81", soapE("#\\u0001F381")) // Wrapped present 🎁
        Assertions.assertEquals("\uD83C\uDF81", soapE("#\\x0001F381")) // Wrapped present 🎁
        Assertions.assertEquals("\uD83E\uDFF0", soapE("#\\u0001FBF0")) // Segmented Digit Zero
        Assertions.assertEquals("\uD83E\uDFF0", soapE("#\\x0001FBF0")) // Segmented Digit Zero
    }

    @Test
    fun parseDiscardTest() {
        // Empty after discard is resolved
        Assertions.assertThrows(EdnReaderException::class.java) { soap("#_") }
        Assertions.assertThrows(EdnReaderException::class.java) { soap("#_a") }

        Assertions.assertEquals(1L, soap("#_a 1"))
        Assertions.assertEquals(listOf<Any?>(), soap("[#_a #_b]"))
        Assertions.assertEquals(listOf<Any?>(), soap("[#_ #_a b]"))
    }

    @Test
    fun parseUuidTest() {
        Assertions.assertThrows(EdnReaderException::class.java) { soap("#uuid") } // Empty
        Assertions.assertThrows(EdnReaderException::class.java) { soap("#uuid 1") } // Wrong type (need string)
        Assertions.assertThrows(EdnReaderException::class.java) { soap("#uuid \"\"") } // Invalid format

        Assertions.assertEquals(
            UUID.fromString("f81d4fae-7dec-11d0-a765-00a0c91e6bf6"),
            soap("#uuid \"f81d4fae-7dec-11d0-a765-00a0c91e6bf6\"")
        )
    }

    @Test
    fun parseInstTest() {
        Assertions.assertThrows(EdnReaderException::class.java) { soap("#inst") } // Empty
        Assertions.assertThrows(EdnReaderException::class.java) { soap("#inst 1") } // Wrong type (need string)
        Assertions.assertThrows(EdnReaderException::class.java) { soap("#inst \"\"") } // Invalid format

        Assertions.assertEquals(
            Instant.parse("1985-04-12T23:20:50.520Z"),
            soap("#inst \"1985-04-12T23:20:50.52Z\"")
        )
    }
}

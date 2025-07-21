package kleinert.soap.edn

import kleinert.soap.data.Char32
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

class EDNReaderDispatchSimpleTest {
    @Test
    fun parseSymbolicTest() {
        Assertions.assertTrue((EDN.read("##NaN") as Double).isNaN())
        Assertions.assertTrue((EDN.read("##-NaN") as Double).isNaN())
        Assertions.assertEquals(Double.POSITIVE_INFINITY, EDN.read("##INF"))
        Assertions.assertEquals(Double.NEGATIVE_INFINITY, EDN.read("##-INF"))
    }

//    @Test
//    fun parseTimeTest() {
//        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("##time") }
//        Assertions.assertInstanceOf(LocalDateTime::class.java, EDN.read("##time", EDN.extendedOptions))
//    }

    @Test
    fun parseDispatchChar() {
        // Only possible with extensions
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("#\\a") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("#\\o41") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("#\\u03BB") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("#\\u000003bb") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("#\\x000003bb") }

        // Simple dispatch "char" (string)
        Assertions.assertEquals(Char32('a'.code), EDN.read("#\\a", EDN.extendedOptions))

        // Octal dispatch "char" (string)
        Assertions.assertEquals(Char32('!'.code), EDN.read("#\\o41", EDN.extendedOptions))
        Assertions.assertEquals(Char32('!'.code), EDN.read("#\\o041", EDN.extendedOptions))
        Assertions.assertEquals(Char32('ǂ'.code), EDN.read("#\\o702", EDN.extendedOptions))

        // Hexadecimal dispatch "char" (string)
        Assertions.assertEquals(Char32('λ'.code), EDN.read("#\\u03BB", EDN.extendedOptions))
        Assertions.assertEquals(Char32('λ'.code), EDN.read("#\\u000003bb", EDN.extendedOptions))
        Assertions.assertEquals(Char32('λ'.code), EDN.read("#\\x000003bb", EDN.extendedOptions))
        Assertions.assertEquals(Char32('ῷ'.code), EDN.read("#\\u1ff7", EDN.extendedOptions))
        Assertions.assertEquals(Char32('ῷ'.code), EDN.read("#\\u00001ff7", EDN.extendedOptions))
        Assertions.assertEquals(Char32('ῷ'.code), EDN.read("#\\x00001ff7", EDN.extendedOptions))

        // Hexadecimal dispatch "char" (string) over 2^16
        Assertions.assertEquals(Char32.valueOf("\uD83C\uDF81"), EDN.read("#\\u0001F381", EDN.extendedOptions)) // Wrapped present 🎁
        Assertions.assertEquals(Char32.valueOf("\uD83C\uDF81"), EDN.read("#\\x0001F381", EDN.extendedOptions)) // Wrapped present 🎁
        Assertions.assertEquals(Char32.valueOf("\uD83E\uDFF0"), EDN.read("#\\u0001FBF0", EDN.extendedOptions)) // Segmented Digit Zero
        Assertions.assertEquals(Char32.valueOf("\uD83E\uDFF0"), EDN.read("#\\x0001FBF0", EDN.extendedOptions)) // Segmented Digit Zero
    }

    @Test
    fun parseDiscardTest() {
        // Empty after discard is resolved
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("#_") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("#_a") }

        Assertions.assertEquals(1L, EDN.read("#_a 1"))
        Assertions.assertEquals(listOf<Any?>(), EDN.read("[#_a #_b]"))
        Assertions.assertEquals(listOf<Any?>(), EDN.read("[#_ #_a b]"))
    }

    @Test
    fun parseUuidTest() {
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("#uuid") } // Empty
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("#uuid 1") } // Wrong type (need string)
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("#uuid \"\"") } // Invalid format

        Assertions.assertEquals(
            UUID.fromString("f81d4fae-7dec-11d0-a765-00a0c91e6bf6"),
            EDN.read("#uuid \"f81d4fae-7dec-11d0-a765-00a0c91e6bf6\"")
        )
    }

    @Test
    fun parseInstTest() {
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("#inst") } // Empty
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("#inst 1") } // Wrong type (need string)
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("#inst \"\"") } // Invalid format

        Assertions.assertEquals(
            Instant.parse("1985-04-12T23:20:50.520Z"),
            EDN.read("#inst \"1985-04-12T23:20:50.52Z\"")
        )
    }
}

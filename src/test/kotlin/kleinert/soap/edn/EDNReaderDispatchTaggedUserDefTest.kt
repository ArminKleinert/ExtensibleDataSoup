package kleinert.soap.edn

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EDNReaderDispatchTaggedUserDefTest {
    @Test
    fun parseDecoderNameErrorTest() {
        // invalid name
        run {
            val decoders = mapOf("pair" to { e: Any? -> e })
            val options = EDN.defaultOptions.copy(ednClassDecoders = decoders)
            Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("1", options) }
        }

        // Valid name with option
        run {
            val decoders = mapOf("pair" to { e: Any? -> e })
            val options =
                EDN.defaultOptions.copy(allowMoreEncoderDecoderNames = true, ednClassDecoders = decoders)
            EDN.read("1", options)
        }
    }

    @Test
    fun parseDecoderTest() {
        fun mapOrListToPair(elem: Any?): Any = when (elem) {
            is Map<*, *> -> (elem["first"] to elem["second"])
            is List<*> -> elem[0] to elem[1]
            else -> throw IllegalArgumentException()
        }

        val decoders = mapOf("my/pair" to ::mapOrListToPair)

        Assertions.assertEquals(
            4L to 5L,
            EDN.read(
                "#my/pair {\"first\" 4 \"second\" 5}",
                EDN.defaultOptions.copy(ednClassDecoders = decoders)
            )
        )
        Assertions.assertEquals(
            4L to 5L,
            EDN.read(
                "#my/pair [4 5]",
                EDN.defaultOptions.copy(ednClassDecoders = decoders)
            )
        )
    }
}

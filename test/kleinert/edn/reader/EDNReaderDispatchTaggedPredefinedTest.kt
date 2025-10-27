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

class EDNReaderDispatchTaggedPredefinedTest {
    private fun parse(s: String) =
        EDN.read(
            s,
            EDNSoapOptions.defaultOptions.copy(
                allowMoreEncoderDecoderNames = true,
                ednClassDecoders = ExtendedEDNDecoders.arrayDecoders + ExtendedEDNDecoders.listDecoders + ExtendedEDNDecoders.prettyDecoders
            )
        )

    /*
            "bitset" to ::setToBitSet,
            "packed2D" to ::packed2dList
            "pretty" to { EDNSoapWriter.pprintS(it) },
     */

    @Test
    fun parseArrayDecodersFailureTest() {
        // Wrong type
        Assertions.assertThrows(EdnClassConversionError::class.java) { parse("#bytearray \"\"") }
        Assertions.assertThrows(EdnClassConversionError::class.java) { parse("#shortarray \"\"") }
        Assertions.assertThrows(EdnClassConversionError::class.java) { parse("#intarray \"\"") }
        Assertions.assertThrows(EdnClassConversionError::class.java) { parse("#longarray \"\"") }
        Assertions.assertThrows(EdnClassConversionError::class.java) { parse("#floatarray \"\"") }
        Assertions.assertThrows(EdnClassConversionError::class.java) { parse("#doublearray \"\"") }
        Assertions.assertThrows(EdnClassConversionError::class.java) { parse("#bigintarray \"\"") }
        Assertions.assertThrows(EdnClassConversionError::class.java) { parse("#bigdecimalarray \"\"") }
        Assertions.assertThrows(EdnClassConversionError::class.java) { parse("#stringarray \"\"") }
        Assertions.assertThrows(EdnClassConversionError::class.java) { parse("#array \"\"") }
        Assertions.assertThrows(EdnClassConversionError::class.java) { parse("#array2d \"\"") }

        // Wrong type
        Assertions.assertThrows(EdnClassConversionError::class.java) { parse("#bytearray [a b]") }
        Assertions.assertThrows(EdnClassConversionError::class.java) { parse("#shortarray [a b]") }
        Assertions.assertThrows(EdnClassConversionError::class.java) { parse("#intarray [a b]") }
        Assertions.assertThrows(EdnClassConversionError::class.java) { parse("#longarray [a b]") }
        Assertions.assertThrows(EdnClassConversionError::class.java) { parse("#floatarray [a b]") }
        Assertions.assertThrows(EdnClassConversionError::class.java) { parse("#doublearray [a b]") }
        Assertions.assertThrows(EdnClassConversionError::class.java) { parse("#bigintarray [a b]") }
        Assertions.assertThrows(EdnClassConversionError::class.java) { parse("#bigdecimalarray [a b]") }
        Assertions.assertThrows(EdnClassConversionError::class.java) { parse("#array2d [a b]") }
    }

    @Test
    fun parseNumericArrayDecodersTest() {
        Assertions.assertArrayEquals(ByteArray(4) { it.toByte() }, parse("#bytearray [0 1 2 3]") as ByteArray)
        Assertions.assertArrayEquals(ShortArray(4) { it.toShort() }, parse("#shortarray [0 1 2 3]") as ShortArray)
        Assertions.assertArrayEquals(IntArray(4) { it }, parse("#intarray [0 1 2 3]") as IntArray)
        Assertions.assertArrayEquals(LongArray(4) { it.toLong() }, parse("#longarray [0 1 2 3]") as LongArray)
        Assertions.assertArrayEquals(Array(4) { it.toBigInteger() }, parse("#bigintarray [0 1 2 3]") as Array<*>)

        Assertions.assertArrayEquals(FloatArray(4) { it.toFloat() }, parse("#floatarray [0 1 2 3]") as FloatArray)
        Assertions.assertArrayEquals(
            FloatArray(4) { it.toFloat() },
            parse("#floatarray [0.0 1.0 2.0 3.0]") as FloatArray
        )
        Assertions.assertArrayEquals(DoubleArray(4) { it.toDouble() }, parse("#doublearray [0 1 2 3]") as DoubleArray)
        Assertions.assertArrayEquals(
            DoubleArray(4) { it.toDouble() },
            parse("#doublearray [0.0 1.0 2.0 3.0]") as DoubleArray
        )
        Assertions.assertArrayEquals(Array(4) { it.toLong().toBigDecimal() }, parse("#bigdecimalarray [0 1 2 3]") as Array<*>)
        Assertions.assertArrayEquals(
            arrayOf(BigDecimal("0.0"), BigDecimal("1.0"), BigDecimal(2L), BigDecimal(3)),
            parse("#bigdecimalarray [0.0 1.0M 2M 3]") as Array<*>
        )
    }

    @Test
    fun parseArrayDecodersTest() {
        Assertions.assertArrayEquals(
            Array(0) { it.toString() },
            parse("#stringarray []") as Array<*>
        )
        Assertions.assertArrayEquals(
            Array(4) { it.toString() },
            parse("#stringarray [0 1 2 3]") as Array<*>
        )

        Assertions.assertArrayEquals(
            Array(4) { it.toString() },
            parse("#stringarray [\"0\" \"1\" \"2\" \"3\"]") as Array<*>
        )

        Assertions.assertArrayEquals(
            arrayOf<Any?>(),
            parse("#array []") as Array<*>
        )

        Assertions.assertArrayEquals(
            arrayOf(Symbol.symbol("a"), Keyword.parse(":a"), 1L),
            parse("#array [a :a 1]") as Array<*>
        )

        Assertions.assertArrayEquals(
            arrayOf(Symbol.symbol("a"), Keyword.parse(":a"), 1L),
            parse("#array [a :a 1]") as Array<*>
        )

        Assertions.assertArrayEquals(
            arrayOf<Array<Any?>>(),
            parse("#array2d []") as Array<*>
        )

        Assertions.assertArrayEquals(
            arrayOf<Array<Any?>>(arrayOf()),
            parse("#array2d [[]]") as Array<*>
        )

        Assertions.assertArrayEquals(
            arrayOf<Array<Any?>>(arrayOf(1L), arrayOf(2L, 3L), arrayOf(4L, 5L, 6L)),
            parse("#array2d [[1] [2 3] [4 5 6]]") as Array<*>
        )
    }
}

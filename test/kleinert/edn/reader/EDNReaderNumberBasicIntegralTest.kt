package kleinert.edn.reader

import kleinert.edn.EDN
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigInteger

class EDNReaderNumberBasicIntegralTest {
    private val allowMoreNumberPrefixes = EDN.defaultOptions.copy(moreNumberPrefixes = true)
    private val allowZeros = EDN.defaultOptions.copy(allowZeroPrefix = true)

    @Test
    fun parseIntegerDecimal() {
        run {
            val it = EDN.read("0")
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        run {
            val it = EDN.read("00", allowZeros)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        run {
            val it = EDN.read("+0")
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        run {
            val it = EDN.read("-0")
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }

        run {
            val it = EDN.read("1")
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        run {
            val it = EDN.read("+1")
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        run {
            val it = EDN.read("-1")
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-1L, it)
        }

        run {
            val it = EDN.read("128")
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        run {
            val it = EDN.read("+128")
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        run {
            val it = EDN.read("-128")
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-128L, it)
        }

        run {
            val it = EDN.read("255")
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        run {
            val it = EDN.read("+255")
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        run {
            val it = EDN.read("-255")
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-255L, it)
        }
    }

    @Test
    fun parseIntegerOctal() {
        run {
            val it = EDN.read("0o0", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        run {
            val it = EDN.read("+0o0", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        run {
            val it = EDN.read("-0o0", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }

        run {
            val it = EDN.read("0o1", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        run {
            val it = EDN.read("+0o1", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        run {
            val it = EDN.read("-0o1", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-1L, it)
        }

        run {
            val it = EDN.read("0o200", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        run {
            val it = EDN.read("+0o200", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        run {
            val it = EDN.read("-0o200", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-128L, it)
        }

        run {
            val it = EDN.read("0o377", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        run {
            val it = EDN.read("+0o377", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        run {
            val it = EDN.read("-0o377", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-255L, it)
        }
    }

    @Test
    fun parseIntegerBinary() {
        run {
            val it = EDN.read("0b0", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        run {
            val it = EDN.read("+0b0", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        run {
            val it = EDN.read("-0b0", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }

        run {
            val it = EDN.read("0b1", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        run {
            val it = EDN.read("+0b1", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        run {
            val it = EDN.read("-0b1", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-1L, it)
        }

        run {
            val it = EDN.read("0b10000000", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        run {
            val it = EDN.read("+0b10000000", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        run {
            val it = EDN.read("-0b10000000", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-128L, it)
        }

        run {
            val it = EDN.read("0b11111111", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        run {
            val it = EDN.read("+0b11111111", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        run {
            val it = EDN.read("-0b11111111", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-255L, it)
        }
    }

    @Test
    fun parseIntegerHex() {
        run {
            val it = EDN.read("0x0", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        run {
            val it = EDN.read("+0x0", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        run {
            val it = EDN.read("-0x0", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }

        run {
            val it = EDN.read("0x1", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        run {
            val it = EDN.read("+0x1", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        run {
            val it = EDN.read("-0x1", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-1L, it)
        }

        run {
            val it = EDN.read("0x80", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        run {
            val it = EDN.read("+0x80", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        run {
            val it = EDN.read("-0x80", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-128L, it)
        }

        run {
            val it = EDN.read("0xFF", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        run {
            val it = EDN.read("+0xFF", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        run {
            val it = EDN.read("-0xFF", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-255L, it)
        }
    }

    @Test
    fun parseBigInt() {
        run {
            val it = EDN.read("0N")
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        run {
            val it = EDN.read("+0N")
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        run {
            val it = EDN.read("-0N")
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }

        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("00N") }
        run {
            val it = EDN.read("00N", allowZeros)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("+00N") }
        run {
            val it = EDN.read("+00N", allowZeros)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("-00N") }
        run {
            val it = EDN.read("-00N", allowZeros)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        run {
            val it = EDN.read("0o0N", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        run {
            val it = EDN.read("+0o0N", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        run {
            val it = EDN.read("-0o0N", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        run {
            val it = EDN.read("0b0N", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        run {
            val it = EDN.read("+0b0N", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        run {
            val it = EDN.read("-0b0N", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        run {
            val it = EDN.read("0x0N", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        run {
            val it = EDN.read("+0x0N", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        run {
            val it = EDN.read("-0x0N", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }

        run {
            val it = EDN.read("1N")
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        run {
            val it = EDN.read("+1N")
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        run {
            val it = EDN.read("-1N")
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE.negate(), it)
        }
        run {
            val it = EDN.read("01N", allowZeros)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        run {
            val it = EDN.read("+01N", allowZeros)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        run {
            val it = EDN.read("-01N", allowZeros)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE.negate(), it)
        }
        run {
            val it = EDN.read("0o1N", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        run {
            val it = EDN.read("+0o1N", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        run {
            val it = EDN.read("-0o1N", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE.negate(), it)
        }
        run {
            val it = EDN.read("0b1N", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        run {
            val it = EDN.read("+0b1N", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        run {
            val it = EDN.read("-0b1N", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE.negate(), it)
        }
        run {
            val it = EDN.read("0x1N", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        run {
            val it = EDN.read("+0x1N", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        run {
            val it = EDN.read("-0x1N", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE.negate(), it)
        }

        val bigint128 = BigInteger.valueOf(128L)
        run {
            val it = EDN.read("128N")
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint128, it)
        }
        run {
            val it = EDN.read("+128N")
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint128, it)
        }
        run {
            val it = EDN.read("-128N")
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint128.negate(), it)
        }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("0200N") }
        run {
            val it = EDN.read("0200N", allowZeros)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.valueOf(200L), it)
        }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("+0200N") }
        run {
            val it = EDN.read("+0200N", allowZeros)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.valueOf(200L), it)
        }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("-0200N") }
        run {
            val it = EDN.read("-0200N", allowZeros)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.valueOf(200L).negate(), it)
        }
        run {
            val it = EDN.read("0o200N", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint128, it)
        }
        run {
            val it = EDN.read("+0o200N", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint128, it)
        }
        run {
            val it = EDN.read("-0o200N", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint128.negate(), it)
        }
        run {
            val it = EDN.read("0b10000000N", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint128, it)
        }
        run {
            val it = EDN.read("+0b10000000N", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint128, it)
        }
        run {
            val it = EDN.read("-0b10000000N", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint128.negate(), it)
        }
        run {
            val it = EDN.read("0x80N", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint128, it)
        }
        run {
            val it = EDN.read("+0x80N", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint128, it)
        }
        run {
            val it = EDN.read("-0x80N", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint128.negate(), it)
        }

        val bigint255 = BigInteger.valueOf(255L)
        run {
            val it = EDN.read("255N")
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint255, it)
        }
        run {
            val it = EDN.read("+255N")
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint255, it)
        }
        run {
            val it = EDN.read("-255N")
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint255.negate(), it)
        }
        run {
            val it = EDN.read("0o377N", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint255, it)
        }
        run {
            val it = EDN.read("+0o377N", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint255, it)
        }
        run {
            val it = EDN.read("-0o377N", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint255.negate(), it)
        }
        run {
            val it = EDN.read("0b11111111N", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint255, it)
        }
        run {
            val it = EDN.read("+0b11111111N", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint255, it)
        }
        run {
            val it = EDN.read("-0b11111111N", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint255.negate(), it)
        }
        run {
            val it = EDN.read("0xFFN", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint255, it)
        }
        run {
            val it = EDN.read("+0xFFN", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint255, it)
        }
        run {
            val it = EDN.read("-0xFFN", allowMoreNumberPrefixes)
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint255.negate(), it)
        }
    }
}

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
        EDN.read("0").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        EDN.read("00", allowZeros).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        EDN.read("+0").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        EDN.read("-0").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }

        EDN.read("1").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        EDN.read("+1").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        EDN.read("-1").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-1L, it)
        }

        EDN.read("128").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        EDN.read("+128").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        EDN.read("-128").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-128L, it)
        }

        EDN.read("255").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        EDN.read("+255").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        EDN.read("-255").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-255L, it)
        }
    }

    @Test
    fun parseIntegerOctal() {
        EDN.read("0o0", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        EDN.read("+0o0", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        EDN.read("-0o0", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }

        EDN.read("0o1", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        EDN.read("+0o1", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        EDN.read("-0o1", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-1L, it)
        }

        EDN.read("0o200", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        EDN.read("+0o200", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        EDN.read("-0o200", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-128L, it)
        }

        EDN.read("0o377", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        EDN.read("+0o377", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        EDN.read("-0o377", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-255L, it)
        }
    }

    @Test
    fun parseIntegerBinary() {
        EDN.read("0b0", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        EDN.read("+0b0", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        EDN.read("-0b0", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }

        EDN.read("0b1", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        EDN.read("+0b1", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        EDN.read("-0b1", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-1L, it)
        }

        EDN.read("0b10000000", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        EDN.read("+0b10000000", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        EDN.read("-0b10000000", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-128L, it)
        }

        EDN.read("0b11111111", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        EDN.read("+0b11111111", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        EDN.read("-0b11111111", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-255L, it)
        }
    }

    @Test
    fun parseIntegerHex() {
        EDN.read("0x0", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        EDN.read("+0x0", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        EDN.read("-0x0", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }

        EDN.read("0x1", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        EDN.read("+0x1", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        EDN.read("-0x1", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-1L, it)
        }

        EDN.read("0x80", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        EDN.read("+0x80", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        EDN.read("-0x80", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-128L, it)
        }

        EDN.read("0xFF", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        EDN.read("+0xFF", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        EDN.read("-0xFF", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-255L, it)
        }
    }

    @Test
    fun parseBigInt() {
        EDN.read("0N").let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        EDN.read("+0N").let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        EDN.read("-0N").let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }

        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("00N") }
        EDN.read("00N", allowZeros).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("+00N") }
        EDN.read("+00N", allowZeros).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("-00N") }
        EDN.read("-00N", allowZeros).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        EDN.read("0o0N", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        EDN.read("+0o0N", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        EDN.read("-0o0N", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        EDN.read("0b0N", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        EDN.read("+0b0N", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        EDN.read("-0b0N", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        EDN.read("0x0N", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        EDN.read("+0x0N", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        EDN.read("-0x0N", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }

        EDN.read("1N").let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        EDN.read("+1N").let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        EDN.read("-1N").let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE.negate(), it)
        }
        EDN.read("01N", allowZeros).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        EDN.read("+01N", allowZeros).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        EDN.read("-01N", allowZeros).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE.negate(), it)
        }
        EDN.read("0o1N", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        EDN.read("+0o1N", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        EDN.read("-0o1N", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE.negate(), it)
        }
        EDN.read("0b1N", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        EDN.read("+0b1N", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        EDN.read("-0b1N", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE.negate(), it)
        }
        EDN.read("0x1N", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        EDN.read("+0x1N", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        EDN.read("-0x1N", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE.negate(), it)
        }

        val bigint128 = BigInteger.valueOf(128L)
        EDN.read("128N").let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint128, it)
        }
        EDN.read("+128N").let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint128, it)
        }
        EDN.read("-128N").let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint128.negate(), it)
        }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("0200N") }
        EDN.read("0200N", allowZeros).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.valueOf(200L), it)
        }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("+0200N") }
        EDN.read("+0200N", allowZeros).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.valueOf(200L), it)
        }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("-0200N") }
        EDN.read("-0200N", allowZeros).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.valueOf(200L).negate(), it)
        }
        EDN.read("0o200N", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint128, it)
        }
        EDN.read("+0o200N", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint128, it)
        }
        EDN.read("-0o200N", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint128.negate(), it)
        }
        EDN.read("0b10000000N", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint128, it)
        }
        EDN.read("+0b10000000N", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint128, it)
        }
        EDN.read("-0b10000000N", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint128.negate(), it)
        }
        EDN.read("0x80N", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint128, it)
        }
        EDN.read("+0x80N", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint128, it)
        }
        EDN.read("-0x80N", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint128.negate(), it)
        }

        val bigint255 = BigInteger.valueOf(255L)
        EDN.read("255N").let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint255, it)
        }
        EDN.read("+255N").let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint255, it)
        }
        EDN.read("-255N").let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint255.negate(), it)
        }
        EDN.read("0o377N", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint255, it)
        }
        EDN.read("+0o377N", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint255, it)
        }
        EDN.read("-0o377N", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint255.negate(), it)
        }
        EDN.read("0b11111111N", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint255, it)
        }
        EDN.read("+0b11111111N", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint255, it)
        }
        EDN.read("-0b11111111N", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint255.negate(), it)
        }
        EDN.read("0xFFN", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint255, it)
        }
        EDN.read("+0xFFN", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint255, it)
        }
        EDN.read("-0xFFN", allowMoreNumberPrefixes).let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint255.negate(), it)
        }
    }
}

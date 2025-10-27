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
import java.math.BigInteger

class EDNReaderNumberBasicIntegralTest {
    private val allowMoreNumberPrefixes = EDN.defaultOptions.copy(moreNumberPrefixes = true)

    @Test
    fun parseIntegerDecimal() {
        EDN.read("0").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        EDN.read("00").let {
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
        // Implicit base
        EDN.read("00").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        EDN.read("+00").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        EDN.read("-00").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }

        EDN.read("01").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        EDN.read("+01").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        EDN.read("-01").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-1L, it)
        }

        EDN.read("0200").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        EDN.read("+0200").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        EDN.read("-0200").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-128L, it)
        }

        EDN.read("0377").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        EDN.read("+0377").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        EDN.read("-0377").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-255L, it)
        }

        // Explicit base

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
        EDN.read("0x0").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        EDN.read("+0x0").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        EDN.read("-0x0").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }

        EDN.read("0x1").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        EDN.read("+0x1").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        EDN.read("-0x1").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-1L, it)
        }

        EDN.read("0x80").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        EDN.read("+0x80").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        EDN.read("-0x80").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-128L, it)
        }

        EDN.read("0xFF").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        EDN.read("+0xFF").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        EDN.read("-0xFF").let {
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
        EDN.read("00N").let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        EDN.read("+00N").let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        EDN.read("-00N").let {
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
        EDN.read("0x0N").let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        EDN.read("+0x0N").let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        EDN.read("-0x0N").let {
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
        EDN.read("01N").let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        EDN.read("+01N").let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        EDN.read("-01N").let {
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
        EDN.read("0x1N").let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        EDN.read("+0x1N").let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        EDN.read("-0x1N").let {
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
        EDN.read("0200N").let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint128, it)
        }
        EDN.read("+0200N").let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint128, it)
        }
        EDN.read("-0200N").let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint128.negate(), it)
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
        EDN.read("0x80N").let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint128, it)
        }
        EDN.read("+0x80N").let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint128, it)
        }
        EDN.read("-0x80N").let {
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
        EDN.read("0377N").let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint255, it)
        }
        EDN.read("+0377N").let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint255, it)
        }
        EDN.read("-0377N").let {
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
        EDN.read("0xFFN").let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint255, it)
        }
        EDN.read("+0xFFN").let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint255, it)
        }
        EDN.read("-0xFFN").let {
            Assertions.assertTrue(it is BigInteger)
            Assertions.assertEquals(bigint255.negate(), it)
        }
    }
}

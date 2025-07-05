package kleinert.soap

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigInteger

class EDNReaderNumberBasicIntegralTest {
    private fun soap(s: String): Any? {
        return EDNSoapReader.readString(s, EDNSoapOptions.defaultOptions)
    }

    private fun soapE(s: String): Any? {
        return EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions)
    }

    @Test
    fun parseIntegerDecimal() {
        soap("0").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        soap("00").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        soap("+0").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        soap("-0").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }

        soap("1").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        soap("+1").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        soap("-1").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-1L, it)
        }

        soap("128").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        soap("+128").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        soap("-128").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-128L, it)
        }

        soap("255").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        soap("+255").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        soap("-255").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-255L, it)
        }
    }

    @Test
    fun parseIntegerOctal() {
        // Implicit base
        soap("00").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        soap("+00").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        soap("-00").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }

        soap("01").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        soap("+01").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        soap("-01").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-1L, it)
        }

        soap("0200").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        soap("+0200").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        soap("-0200").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-128L, it)
        }

        soap("0377").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        soap("+0377").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        soap("-0377").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-255L, it)
        }

        // Explicit base

        soap("0o0").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        soap("+0o0").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        soap("-0o0").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }

        soap("0o1").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        soap("+0o1").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        soap("-0o1").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-1L, it)
        }

        soap("0o200").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        soap("+0o200").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        soap("-0o200").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-128L, it)
        }

        soap("0o377").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        soap("+0o377").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        soap("-0o377").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-255L, it)
        }
    }

    @Test
    fun parseIntegerBinary() {
        soap("0b0").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        soap("+0b0").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        soap("-0b0").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }

        soap("0b1").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        soap("+0b1").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        soap("-0b1").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-1L, it)
        }

        soap("0b10000000").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        soap("+0b10000000").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        soap("-0b10000000").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-128L, it)
        }

        soap("0b11111111").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        soap("+0b11111111").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        soap("-0b11111111").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-255L, it)
        }
    }

    @Test
    fun parseIntegerHex() {
        soap("0x0").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        soap("+0x0").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        soap("-0x0").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }

        soap("0x1").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        soap("+0x1").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        soap("-0x1").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-1L, it)
        }

        soap("0x80").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        soap("+0x80").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        soap("-0x80").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-128L, it)
        }

        soap("0xFF").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        soap("+0xFF").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        soap("-0xFF").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-255L, it)
        }
    }

    @Test
    fun parseBigInt() {
        soap("0N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        soap("+0N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        soap("-0N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        soap("00N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        soap("+00N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        soap("-00N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        soap("0o0N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        soap("+0o0N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        soap("-0o0N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        soap("0b0N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        soap("+0b0N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        soap("-0b0N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        soap("0x0N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        soap("+0x0N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        soap("-0x0N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }

        soap("1N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        soap("+1N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        soap("-1N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ONE.negate(), it)
        }
        soap("01N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        soap("+01N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        soap("-01N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ONE.negate(), it)
        }
        soap("0o1N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        soap("+0o1N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        soap("-0o1N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ONE.negate(), it)
        }
        soap("0b1N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        soap("+0b1N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        soap("-0b1N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ONE.negate(), it)
        }
        soap("0x1N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        soap("+0x1N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        soap("-0x1N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ONE.negate(), it)
        }

        val bigint128 = BigInteger.valueOf(128L)
        soap("128N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint128, it)
        }
        soap("+128N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint128, it)
        }
        soap("-128N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint128.negate(), it)
        }
        soap("0200N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint128, it)
        }
        soap("+0200N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint128, it)
        }
        soap("-0200N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint128.negate(), it)
        }
        soap("0o200N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint128, it)
        }
        soap("+0o200N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint128, it)
        }
        soap("-0o200N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint128.negate(), it)
        }
        soap("0b10000000N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint128, it)
        }
        soap("+0b10000000N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint128, it)
        }
        soap("-0b10000000N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint128.negate(), it)
        }
        soap("0x80N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint128, it)
        }
        soap("+0x80N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint128, it)
        }
        soap("-0x80N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint128.negate(), it)
        }

        val bigint255 = BigInteger.valueOf(255L)
        soap("255N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint255, it)
        }
        soap("+255N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint255, it)
        }
        soap("-255N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint255.negate(), it)
        }
        soap("0377N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint255, it)
        }
        soap("+0377N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint255, it)
        }
        soap("-0377N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint255.negate(), it)
        }
        soap("0o377N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint255, it)
        }
        soap("+0o377N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint255, it)
        }
        soap("-0o377N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint255.negate(), it)
        }
        soap("0b11111111N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint255, it)
        }
        soap("+0b11111111N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint255, it)
        }
        soap("-0b11111111N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint255.negate(), it)
        }
        soap("0xFFN").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint255, it)
        }
        soap("+0xFFN").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint255, it)
        }
        soap("-0xFFN").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint255.negate(), it)
        }
    }
}

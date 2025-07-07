package kleinert.soap

import kleinert.soap.edn.EDN
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigInteger

class EDNReaderNumberBasicIntegralTest {
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

        EDN.read("0o0").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        EDN.read("+0o0").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        EDN.read("-0o0").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }

        EDN.read("0o1").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        EDN.read("+0o1").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        EDN.read("-0o1").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-1L, it)
        }

        EDN.read("0o200").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        EDN.read("+0o200").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        EDN.read("-0o200").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-128L, it)
        }

        EDN.read("0o377").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        EDN.read("+0o377").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        EDN.read("-0o377").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-255L, it)
        }
    }

    @Test
    fun parseIntegerBinary() {
        EDN.read("0b0").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        EDN.read("+0b0").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }
        EDN.read("-0b0").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(0L, it)
        }

        EDN.read("0b1").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        EDN.read("+0b1").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(1L, it)
        }
        EDN.read("-0b1").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-1L, it)
        }

        EDN.read("0b10000000").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        EDN.read("+0b10000000").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(128L, it)
        }
        EDN.read("-0b10000000").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(-128L, it)
        }

        EDN.read("0b11111111").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        EDN.read("+0b11111111").let {
            Assertions.assertTrue(it is Long)
            Assertions.assertEquals(255L, it)
        }
        EDN.read("-0b11111111").let {
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
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        EDN.read("+0N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        EDN.read("-0N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        EDN.read("00N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        EDN.read("+00N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        EDN.read("-00N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        EDN.read("0o0N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        EDN.read("+0o0N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        EDN.read("-0o0N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        EDN.read("0b0N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        EDN.read("+0b0N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        EDN.read("-0b0N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        EDN.read("0x0N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        EDN.read("+0x0N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }
        EDN.read("-0x0N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ZERO, it)
        }

        EDN.read("1N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        EDN.read("+1N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        EDN.read("-1N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ONE.negate(), it)
        }
        EDN.read("01N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        EDN.read("+01N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        EDN.read("-01N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ONE.negate(), it)
        }
        EDN.read("0o1N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        EDN.read("+0o1N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        EDN.read("-0o1N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ONE.negate(), it)
        }
        EDN.read("0b1N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        EDN.read("+0b1N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        EDN.read("-0b1N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ONE.negate(), it)
        }
        EDN.read("0x1N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        EDN.read("+0x1N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ONE, it)
        }
        EDN.read("-0x1N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(BigInteger.ONE.negate(), it)
        }

        val bigint128 = BigInteger.valueOf(128L)
        EDN.read("128N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint128, it)
        }
        EDN.read("+128N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint128, it)
        }
        EDN.read("-128N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint128.negate(), it)
        }
        EDN.read("0200N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint128, it)
        }
        EDN.read("+0200N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint128, it)
        }
        EDN.read("-0200N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint128.negate(), it)
        }
        EDN.read("0o200N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint128, it)
        }
        EDN.read("+0o200N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint128, it)
        }
        EDN.read("-0o200N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint128.negate(), it)
        }
        EDN.read("0b10000000N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint128, it)
        }
        EDN.read("+0b10000000N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint128, it)
        }
        EDN.read("-0b10000000N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint128.negate(), it)
        }
        EDN.read("0x80N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint128, it)
        }
        EDN.read("+0x80N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint128, it)
        }
        EDN.read("-0x80N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint128.negate(), it)
        }

        val bigint255 = BigInteger.valueOf(255L)
        EDN.read("255N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint255, it)
        }
        EDN.read("+255N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint255, it)
        }
        EDN.read("-255N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint255.negate(), it)
        }
        EDN.read("0377N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint255, it)
        }
        EDN.read("+0377N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint255, it)
        }
        EDN.read("-0377N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint255.negate(), it)
        }
        EDN.read("0o377N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint255, it)
        }
        EDN.read("+0o377N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint255, it)
        }
        EDN.read("-0o377N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint255.negate(), it)
        }
        EDN.read("0b11111111N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint255, it)
        }
        EDN.read("+0b11111111N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint255, it)
        }
        EDN.read("-0b11111111N").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint255.negate(), it)
        }
        EDN.read("0xFFN").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint255, it)
        }
        EDN.read("+0xFFN").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint255, it)
        }
        EDN.read("-0xFFN").let {
            Assertions.assertInstanceOf(BigInteger::class.java, it)
            Assertions.assertEquals(bigint255.negate(), it)
        }
    }
}

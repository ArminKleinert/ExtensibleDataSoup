package kleinert.soap

import kleinert.soap.data.Ratio
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.BigInteger

class RatioTest {
    @Test
    fun parseTest() {
        Ratio.valueOf(2, 2)
    }

    @Test
    fun parseConstructorErrorTest() {
        Assertions.assertThrows(IllegalArgumentException::class.java) { Ratio.valueOf(1, 0) }
    }

    @Test
    fun parseConstructorTest() {
        run {
            val ratio = Ratio.valueOf(2, 2)
            Assertions.assertEquals(1, ratio.num)
            Assertions.assertEquals(1, ratio.den)

            val (n, d) = ratio
            Assertions.assertEquals(ratio.num, n)
            Assertions.assertEquals(ratio.den, d)
        }
        run {
            val ratio = Ratio.valueOf(2, 4)
            Assertions.assertEquals(1, ratio.num)
            Assertions.assertEquals(2, ratio.den)

            val (n, d) = ratio
            Assertions.assertEquals(ratio.num, n)
            Assertions.assertEquals(ratio.den, d)
        }
        run {
            Assertions.assertTrue(Ratio.ZERO > Ratio.valueOf(-2, 2))
            Assertions.assertTrue(Ratio.ZERO > Ratio.valueOf(2, -2))
            Assertions.assertTrue(Ratio.valueOf(2, 2) == Ratio.valueOf(-2, -2))
        }
    }

    @Test
    fun parseFullEqualityTest() {
        run {
            val ratio = Ratio.valueOf(0)
            Assertions.assertEquals(ratio, 0.toByte())
            Assertions.assertEquals(ratio, 0.toShort())
            Assertions.assertEquals(ratio, 0)
            Assertions.assertEquals(ratio, 0L)
            Assertions.assertEquals(ratio, BigInteger.ZERO)
            Assertions.assertEquals(ratio, 0.0.toFloat())
            Assertions.assertEquals(ratio, 0.0)
            Assertions.assertEquals(ratio, BigDecimal.ZERO)
        }
        run {
            val ratio = Ratio.valueOf(1)
            Assertions.assertEquals(ratio, 1.toByte())
            Assertions.assertEquals(ratio, 1.toShort())
            Assertions.assertEquals(ratio, 1)
            Assertions.assertEquals(ratio, 1L)
            Assertions.assertEquals(ratio, BigInteger.ONE)
            Assertions.assertEquals(ratio, 1.0.toFloat())
            Assertions.assertEquals(ratio, 1.0)
            Assertions.assertEquals(ratio, BigDecimal.ONE)
        }
        run {
            val ratio = Ratio.valueOf(-1)
            Assertions.assertEquals(ratio, (-1).toByte())
            Assertions.assertEquals(ratio, (-1).toShort())
            Assertions.assertEquals(ratio, -1)
            Assertions.assertEquals(ratio, -1L)
            Assertions.assertEquals(ratio, BigInteger.ONE.negate())
            Assertions.assertEquals(ratio, (-1.0).toFloat())
            Assertions.assertEquals(ratio, -1.0)
            Assertions.assertEquals(ratio, BigDecimal.ONE.negate())
        }
    }

    @Test
    fun parseRatioEqualityTest() {
        run {
            val ratio = Ratio.valueOf(3, 2)
            Assertions.assertEquals(Ratio.valueOf(3, 2), ratio)
            Assertions.assertNotEquals(ratio, 1.toByte())
            Assertions.assertNotEquals(ratio, 1.toShort())
            Assertions.assertNotEquals(ratio, 1)
            Assertions.assertNotEquals(ratio, 1L)
            Assertions.assertNotEquals(ratio, BigInteger.ONE)
            Assertions.assertEquals(ratio, 1.5.toFloat())
            Assertions.assertEquals(ratio, 1.5)
            Assertions.assertEquals(ratio, BigDecimal.valueOf(1.5))
        }
        run {
            val ratio = Ratio.valueOf(-3, 2)
            Assertions.assertEquals(Ratio.valueOf(-3, 2), ratio)
            Assertions.assertNotEquals(ratio, (-1).toByte())
            Assertions.assertNotEquals(ratio, (-1).toShort())
            Assertions.assertNotEquals(ratio, -1)
            Assertions.assertNotEquals(ratio, -1L)
            Assertions.assertNotEquals(ratio, BigInteger.ONE.negate())
            Assertions.assertEquals(ratio, (-1.5).toFloat())
            Assertions.assertEquals(ratio, -1.5)
            Assertions.assertEquals(ratio, BigDecimal.valueOf(1.5).negate())
        }
    }
}

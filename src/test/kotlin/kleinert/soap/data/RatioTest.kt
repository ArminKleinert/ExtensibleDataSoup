package kleinert.soap.data

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.BigInteger

class RatioTest {
    private val zero = Ratio.ZERO
    private val one = Ratio.valueOf(1)
    private val half = Ratio.valueOf(1, 2)
    private val oneAndHalf = Ratio.valueOf(3, 2)
    private val two = Ratio.valueOf(2)

    @Test
    fun valueOfIntInt() {
        Assertions.assertThrows(IllegalArgumentException::class.java) { Ratio.valueOf(5, 0) }

        val r1 = Ratio.valueOf(3, 3)
        Assertions.assertEquals(1, r1.num)
        Assertions.assertEquals(1, r1.den)

        val r2 = Ratio.valueOf(-3, 3)
        Assertions.assertEquals(-1, r2.num)
        Assertions.assertEquals(1, r2.den)

        val r3 = Ratio.valueOf(3, -3)
        Assertions.assertEquals(-1, r3.num)
        Assertions.assertEquals(1, r3.den)

        val r4 = Ratio.valueOf(3, -5)
        Assertions.assertEquals(-3, r4.num)
        Assertions.assertEquals(5, r4.den)
    }

    @Test
    fun valueOfLongLong() {
        Assertions.assertThrows(IllegalArgumentException::class.java) { Ratio.valueOf(5L, 0L) }

        val r1 = Ratio.valueOf(3L, 3L)
        Assertions.assertEquals(1L, r1.num)
        Assertions.assertEquals(1L, r1.den)

        val r2 = Ratio.valueOf(-3L, 3L)
        Assertions.assertEquals(-1L, r2.num)
        Assertions.assertEquals(1L, r2.den)

        val r3 = Ratio.valueOf(3L, -3L)
        Assertions.assertEquals(-1L, r3.num)
        Assertions.assertEquals(1L, r3.den)

        val r4 = Ratio.valueOf(3L, -5L)
        Assertions.assertEquals(-3L, r4.num)
        Assertions.assertEquals(5L, r4.den)
    }

    @Test
    fun valueOfBigInt() {
        val r1 = Ratio.valueOf(BigInteger.valueOf(0))
        Assertions.assertEquals(0L, r1.num)
        Assertions.assertEquals(1L, r1.den)

        val r2 = Ratio.valueOf(BigInteger.TEN)
        Assertions.assertEquals(10L, r2.num)
        Assertions.assertEquals(1L, r2.den)

        val r3 = Ratio.valueOf(BigInteger.TEN.negate())
        Assertions.assertEquals(-10L, r3.num)
        Assertions.assertEquals(1L, r3.den)
    }

    @Test
    fun valueOfStringOrNull() {
        Assertions.assertEquals(Ratio.valueOf(5), Ratio.valueOfOrNull("5")!!)
        Assertions.assertEquals(Ratio.valueOf(5, 3), Ratio.valueOfOrNull("5/3")!!)

        Assertions.assertEquals(Ratio.valueOf(-5), Ratio.valueOfOrNull("-5")!!)
        Assertions.assertEquals(Ratio.valueOf(-5, 3), Ratio.valueOfOrNull("-5/3")!!)
        Assertions.assertEquals(Ratio.valueOf(-5, 3), Ratio.valueOfOrNull("5/-3")!!)

        Assertions.assertEquals(null, Ratio.valueOfOrNull(""))
        Assertions.assertEquals(null, Ratio.valueOfOrNull("/"))
        Assertions.assertEquals(null, Ratio.valueOfOrNull("a"))
        Assertions.assertEquals(null, Ratio.valueOfOrNull("1/"))
    }

    @Test
    fun valueOfString() {
        Assertions.assertEquals(Ratio.valueOf(5), Ratio.valueOf("5"))
        Assertions.assertEquals(Ratio.valueOf(5, 3), Ratio.valueOf("5/3"))

        Assertions.assertEquals(Ratio.valueOf(-5), Ratio.valueOf("-5"))
        Assertions.assertEquals(Ratio.valueOf(-5, 3), Ratio.valueOf("-5/3"))
        Assertions.assertEquals(Ratio.valueOf(-5, 3), Ratio.valueOf("5/-3"))

        Assertions.assertThrows(NumberFormatException::class.java) { Ratio.valueOf("") }
        Assertions.assertThrows(NumberFormatException::class.java) { Ratio.valueOf("/") }
        Assertions.assertThrows(NumberFormatException::class.java) { Ratio.valueOf("a") }
        Assertions.assertThrows(NumberFormatException::class.java) { Ratio.valueOf("1/") }
    }

    @Test
    fun valueOfDouble() {
        Assertions.assertEquals(Ratio.valueOf(0), Ratio.valueOf(0.0))
        Assertions.assertEquals(Ratio.valueOf(1), Ratio.valueOf(1.0))
        Assertions.assertEquals(Ratio.valueOf(3, 2), Ratio.valueOf(1.5))
        Assertions.assertEquals(Ratio.valueOf(-3, 2), Ratio.valueOf(-1.5))

        val pi = 3.141592653589793
        Assertions.assertEquals(Ratio.valueOf(16, 5), Ratio.valueOf(pi, 1E-1))
        Assertions.assertEquals(Ratio.valueOf(22, 7), Ratio.valueOf(pi, 1E-2))
        Assertions.assertEquals(Ratio.valueOf(201, 64), Ratio.valueOf(pi, 1E-3))
        Assertions.assertEquals(Ratio.valueOf(333, 106), Ratio.valueOf(pi, 1E-4))
        Assertions.assertEquals(Ratio.valueOf(312689, 99532), Ratio.valueOf(pi, 1E-10))
        Assertions.assertEquals(Ratio.valueOf(16, 5), Ratio.estimate(pi, 1E-1))
        Assertions.assertEquals(Ratio.valueOf(22, 7), Ratio.estimate(pi, 1E-2))
        Assertions.assertEquals(Ratio.valueOf(201, 64), Ratio.estimate(pi, 1E-3))
        Assertions.assertEquals(Ratio.valueOf(333, 106), Ratio.estimate(pi, 1E-4))
        Assertions.assertEquals(Ratio.valueOf(312689, 99532), Ratio.estimate(pi, 1E-10))
    }

    @Test
    fun testToString() {
    }

    @Test
    fun toByte() {
        Assertions.assertEquals(0.toByte(), zero.toByte())
        Assertions.assertEquals(1.toByte(), one.toByte())
        Assertions.assertEquals(0.toByte(), half.toByte())
        Assertions.assertEquals(1.toByte(), oneAndHalf.toByte())
    }

    @Test
    fun toShort() {
        Assertions.assertEquals(0.toShort(), zero.toShort())
        Assertions.assertEquals(1.toShort(), one.toShort())
        Assertions.assertEquals(0.toShort(), half.toShort())
        Assertions.assertEquals(1.toShort(), oneAndHalf.toShort())
    }

    @Test
    fun toInt() {
        Assertions.assertEquals(0, zero.toInt())
        Assertions.assertEquals(1, one.toInt())
        Assertions.assertEquals(0, half.toInt())
        Assertions.assertEquals(1, oneAndHalf.toInt())
    }

    @Test
    fun toLong() {
        val zero = Ratio.ZERO
        val one = Ratio.valueOf(1)
        val half = Ratio.valueOf(1, 2)
        val oneAndHalf = Ratio.valueOf(3, 2)

        Assertions.assertEquals(0L, zero.toLong())
        Assertions.assertEquals(1L, one.toLong())
        Assertions.assertEquals(0L, half.toLong())
        Assertions.assertEquals(1L, oneAndHalf.toLong())
    }

    @Test
    fun toFloat() {
        val zero = Ratio.ZERO
        val one = Ratio.valueOf(1)
        val half = Ratio.valueOf(1, 2)
        val oneAndHalf = Ratio.valueOf(3, 2)

        Assertions.assertEquals(0.0.toFloat(), zero.toFloat())
        Assertions.assertEquals(1.0.toFloat(), one.toFloat())
        Assertions.assertEquals(0.5.toFloat(), half.toFloat())
        Assertions.assertEquals(1.5.toFloat(), oneAndHalf.toFloat())
    }

    @Test
    fun toDouble() {
        Assertions.assertEquals(0.0, zero.toDouble())
        Assertions.assertEquals(1.0, one.toDouble())
        Assertions.assertEquals(0.5, half.toDouble())
        Assertions.assertEquals(1.5, oneAndHalf.toDouble())
    }

    @Test
    fun toBigDecimal() {
        Assertions.assertEquals(BigDecimal.valueOf(0L), zero.toBigDecimal())
        Assertions.assertEquals(BigDecimal.valueOf(1L), one.toBigDecimal())
        Assertions.assertEquals(BigDecimal.valueOf(0.5), half.toBigDecimal())
        Assertions.assertEquals(BigDecimal.valueOf(1.5), oneAndHalf.toBigDecimal())
    }

    @Test
    fun toBigInteger() {
        val zero = Ratio.ZERO
        val one = Ratio.valueOf(1)
        val half = Ratio.valueOf(1, 2)
        val oneAndHalf = Ratio.valueOf(3, 2)

        Assertions.assertEquals(BigInteger.valueOf(0L), zero.toBigInteger())
        Assertions.assertEquals(BigInteger.valueOf(1L), one.toBigInteger())
        Assertions.assertEquals(BigInteger.valueOf(0L), half.toBigInteger())
        Assertions.assertEquals(BigInteger.valueOf(1L), oneAndHalf.toBigInteger())
    }

    @Test
    fun mediant() {
        Assertions.assertEquals(Ratio.ZERO, Ratio.valueOf(0, 1).mediant(Ratio.valueOf(0, 1)))
        Assertions.assertEquals(Ratio.valueOf(5, 2), Ratio.valueOf(1, 1).mediant(Ratio.valueOf(4, 1)))
    }

    @Test
    fun abs() {
        Assertions.assertEquals(Ratio.valueOf(5, 1), Ratio.valueOf(5, 1).abs())
        Assertions.assertEquals(Ratio.valueOf(5, 1), Ratio.valueOf(-5, 1).abs())
        Assertions.assertEquals(Ratio.valueOf(5, 1), Ratio.valueOf(5, -1).abs())
    }

    @Test
    fun reciprocal() {
        Assertions.assertEquals(Ratio.valueOf(1, 5), Ratio.valueOf(5, 1).reciprocal())
        Assertions.assertEquals(Ratio.valueOf(-1, 5), Ratio.valueOf(-5, 1).reciprocal())
        Assertions.assertThrows(IllegalArgumentException::class.java) { Ratio.valueOf(0, 1).reciprocal() }
    }

    @Test
    fun negate() {
        Assertions.assertEquals(Ratio.valueOf(-5, 1), Ratio.valueOf(5, 1).negate())
        Assertions.assertEquals(Ratio.valueOf(5, 1), Ratio.valueOf(-5, 1).negate())
        Assertions.assertEquals(Ratio.valueOf(5, 1), Ratio.valueOf(5, -1).negate())
    }

    @Test
    operator fun unaryMinus() {
        Assertions.assertEquals(Ratio.valueOf(-5, 1), -Ratio.valueOf(5, 1))
        Assertions.assertEquals(Ratio.valueOf(5, 1), -Ratio.valueOf(-5, 1))
        Assertions.assertEquals(Ratio.valueOf(5, 1), -Ratio.valueOf(5, -1))
    }

    @Test
    fun plus() {
        Assertions.assertEquals(Ratio.ZERO, zero + zero)
        Assertions.assertEquals(one, zero + one)
        Assertions.assertEquals(two, one + one)
        Assertions.assertEquals(two, half + oneAndHalf)
    }

    @Test
    fun minus() {
        Assertions.assertEquals(Ratio.ZERO, zero - zero)
        Assertions.assertEquals(-one, zero - one)
        Assertions.assertEquals(zero, one - one)
        Assertions.assertEquals(one, oneAndHalf - half)
    }

    @Test
    fun times() {
        val zero = Ratio.ZERO
        val one = Ratio.valueOf(1)
        val half = Ratio.valueOf(1, 2)
        val oneAndHalf = Ratio.valueOf(3, 2)

        Assertions.assertEquals(Ratio.ZERO, zero * one)
        Assertions.assertEquals(Ratio.ZERO, zero * oneAndHalf)

        Assertions.assertEquals(half, one * half)
        Assertions.assertEquals(half, half * one)

        Assertions.assertEquals(oneAndHalf, Ratio.valueOf(3) * half)
    }

    @Test
    fun div() {
        Assertions.assertEquals(Ratio.ZERO, zero / one)
        Assertions.assertEquals(half, one / two)
        Assertions.assertEquals(two, one / half)
    }

    @Test
    fun compareTo() {
        Assertions.assertEquals(1, zero.compareTo(-one))
        Assertions.assertEquals(-1, zero.compareTo(one))
        Assertions.assertEquals(0, zero.compareTo(zero))
        Assertions.assertEquals(1, one.compareTo(zero))
    }

    @Test
    fun getNum() {
        Assertions.assertEquals(0L, Ratio.ZERO.num)
        Assertions.assertEquals(33L, Ratio.valueOf(33L).num)
        Assertions.assertEquals(5L, Ratio.valueOf(5, 2).num)
        Assertions.assertEquals(-5L, Ratio.valueOf(5, -2).num)

        val (n1, _) = Ratio.valueOf(33L)
        Assertions.assertEquals(33L, n1)

        val (n2, _) = Ratio.valueOf(5, 2)
        Assertions.assertEquals(5L, n2)
    }

    @Test
    fun getDen() {
        Assertions.assertEquals(1L, Ratio.ZERO.den)
        Assertions.assertEquals(1L, Ratio.valueOf(33L).den)
        Assertions.assertEquals(2L, Ratio.valueOf(5, 2).den)
        Assertions.assertEquals(2L, Ratio.valueOf(5, -2).den)

        val (_, d1) = Ratio.valueOf(33L)
        Assertions.assertEquals(1L, d1)

        val (_, d2) = Ratio.valueOf(5, 2)
        Assertions.assertEquals(2L, d2)
    }

    @Test
    fun equalsFullTest() {
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
    fun equalsRatioTest() {
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
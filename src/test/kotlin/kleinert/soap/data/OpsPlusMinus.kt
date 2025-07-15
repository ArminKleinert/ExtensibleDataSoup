package kleinert.soap.data

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.BigInteger

class OpsPlusMinus {
    @Test
    fun plusMinusByte() {
        val one = 1.toByte()
        val three = 3.toByte()
        val negThree = (-3).toByte()

        Assertions.assertEquals(three, Ops.plus(three))
        Assertions.assertEquals(0, Ops.compare(negThree, Ops.minus(three)))

        for (n in listOf(1.toByte(), 1.toShort(), 1, 1.toLong(), 1.toFloat(), 1.0)) {
            Assertions.assertEquals(0, Ops.compare(2, Ops.plus(one, n)))
            Assertions.assertEquals(0, Ops.compare(-2, Ops.plus(negThree, n)))
            Assertions.assertEquals(0, Ops.compare(2, Ops.minus(three, n)))
            Assertions.assertEquals(0, Ops.compare(0, Ops.minus(one, n)))
        }

        for (n in listOf(BigInteger.ONE, BigDecimal.ONE, Ratio.valueOf(1L), Complex.ONE)) {
            Assertions.assertEquals(0, Ops.compare(2, Ops.plus(one, n)))
            Assertions.assertEquals(0, Ops.compare(-2, Ops.plus(negThree, n)))
            Assertions.assertEquals(0, Ops.compare(2, Ops.minus(three, n)))
            Assertions.assertEquals(0, Ops.compare(0, Ops.minus(one, n)))
        }
    }

    @Test
    fun plusMinusShort() {
        val one = 1.toByte()
        val three = 3.toByte()
        val negThree = (-3).toByte()

        Assertions.assertEquals(three, Ops.plus(three))
        Assertions.assertEquals(0, Ops.compare(negThree, Ops.minus(three)))

        for (n in listOf(1.toByte(), 1.toShort(), 1, 1.toLong(), 1.toFloat(), 1.0)) {
            Assertions.assertEquals(0, Ops.compare(2, Ops.plus(one, n)))
            Assertions.assertEquals(0, Ops.compare(-2, Ops.plus(negThree, n)))
            Assertions.assertEquals(0, Ops.compare(2, Ops.minus(three, n)))
            Assertions.assertEquals(0, Ops.compare(0, Ops.minus(one, n)))
        }

        for (n in listOf(BigInteger.ONE, BigDecimal.ONE, Ratio.valueOf(1L), Complex.ONE)) {
            Assertions.assertEquals(0, Ops.compare(2, Ops.plus(one, n)))
            Assertions.assertEquals(0, Ops.compare(-2, Ops.plus(negThree, n)))
            Assertions.assertEquals(0, Ops.compare(2, Ops.minus(three, n)))
            Assertions.assertEquals(0, Ops.compare(0, Ops.minus(one, n)))
        }
    }

    @Test
    fun plusMinusInt() {
        val one = 1.toByte()
        val three = 3.toByte()
        val negThree = (-3).toByte()

        Assertions.assertEquals(three, Ops.plus(three))
        Assertions.assertEquals(0, Ops.compare(negThree, Ops.minus(three)))

        for (n in listOf(1.toByte(), 1.toShort(), 1, 1.toLong(), 1.toFloat(), 1.0)) {
            Assertions.assertEquals(0, Ops.compare(2, Ops.plus(one, n)))
            Assertions.assertEquals(0, Ops.compare(-2, Ops.plus(negThree, n)))
            Assertions.assertEquals(0, Ops.compare(2, Ops.minus(three, n)))
            Assertions.assertEquals(0, Ops.compare(0, Ops.minus(one, n)))
        }

        for (n in listOf(BigInteger.ONE, BigDecimal.ONE, Ratio.valueOf(1L), Complex.ONE)) {
            Assertions.assertEquals(0, Ops.compare(2, Ops.plus(one, n)))
            Assertions.assertEquals(0, Ops.compare(-2, Ops.plus(negThree, n)))
            Assertions.assertEquals(0, Ops.compare(2, Ops.minus(three, n)))
            Assertions.assertEquals(0, Ops.compare(0, Ops.minus(one, n)))
        }
    }

    @Test
    fun plusMinusLong() {
        val one = 1.toByte()
        val three = 3.toByte()
        val negThree = (-3).toByte()

        Assertions.assertEquals(three, Ops.plus(three))
        Assertions.assertEquals(0, Ops.compare(negThree, Ops.minus(three)))

        for (n in listOf(1.toByte(), 1.toShort(), 1, 1.toLong(), 1.toFloat(), 1.0)) {
            Assertions.assertEquals(0, Ops.compare(2, Ops.plus(one, n)))
            Assertions.assertEquals(0, Ops.compare(-2, Ops.plus(negThree, n)))
            Assertions.assertEquals(0, Ops.compare(2, Ops.minus(three, n)))
            Assertions.assertEquals(0, Ops.compare(0, Ops.minus(one, n)))
        }

        for (n in listOf(BigInteger.ONE, BigDecimal.ONE, Ratio.valueOf(1L), Complex.ONE)) {
            Assertions.assertEquals(0, Ops.compare(2, Ops.plus(one, n)))
            Assertions.assertEquals(0, Ops.compare(-2, Ops.plus(negThree, n)))
            Assertions.assertEquals(0, Ops.compare(2, Ops.minus(three, n)))
            Assertions.assertEquals(0, Ops.compare(0, Ops.minus(one, n)))
        }
    }

    @Test
    fun plusMinusFloat() {
        val one = 1.toByte()
        val three = 3.toByte()
        val negThree = (-3).toByte()

        Assertions.assertEquals(three, Ops.plus(three))
        Assertions.assertEquals(0, Ops.compare(negThree, Ops.minus(three)))

        for (n in listOf(1.toByte(), 1.toShort(), 1, 1.toLong(), 1.toFloat(), 1.0)) {
            Assertions.assertEquals(0, Ops.compare(2, Ops.plus(one, n)))
            Assertions.assertEquals(0, Ops.compare(-2, Ops.plus(negThree, n)))
            Assertions.assertEquals(0, Ops.compare(2, Ops.minus(three, n)))
            Assertions.assertEquals(0, Ops.compare(0, Ops.minus(one, n)))
        }

        for (n in listOf(BigInteger.ONE, BigDecimal.ONE, Ratio.valueOf(1L), Complex.ONE)) {
            Assertions.assertEquals(0, Ops.compare(2, Ops.plus(one, n)))
            Assertions.assertEquals(0, Ops.compare(-2, Ops.plus(negThree, n)))
            Assertions.assertEquals(0, Ops.compare(2, Ops.minus(three, n)))
            Assertions.assertEquals(0, Ops.compare(0, Ops.minus(one, n)))
        }
    }

    @Test
    fun plusMinusDouble() {
        val one = 1.toByte()
        val three = 3.toByte()
        val negThree = (-3).toByte()

        Assertions.assertEquals(three, Ops.plus(three))
        Assertions.assertEquals(0, Ops.compare(negThree, Ops.minus(three)))

        for (n in listOf(1.toByte(), 1.toShort(), 1, 1.toLong(), 1.toFloat(), 1.0)) {
            Assertions.assertEquals(0, Ops.compare(2, Ops.plus(one, n)))
            Assertions.assertEquals(0, Ops.compare(-2, Ops.plus(negThree, n)))
            Assertions.assertEquals(0, Ops.compare(2, Ops.minus(three, n)))
            Assertions.assertEquals(0, Ops.compare(0, Ops.minus(one, n)))
        }

        for (n in listOf(BigInteger.ONE, BigDecimal.ONE, Ratio.valueOf(1L), Complex.ONE)) {
            Assertions.assertEquals(0, Ops.compare(2, Ops.plus(one, n)))
            Assertions.assertEquals(0, Ops.compare(-2, Ops.plus(negThree, n)))
            Assertions.assertEquals(0, Ops.compare(2, Ops.minus(three, n)))
            Assertions.assertEquals(0, Ops.compare(0, Ops.minus(one, n)))
        }
    }

    @Test
    fun plusMinusBigInt() {
        val one = 1.toByte()
        val three = 3.toByte()
        val negThree = (-3).toByte()

        Assertions.assertEquals(three, Ops.plus(three))
        Assertions.assertEquals(0, Ops.compare(negThree, Ops.minus(three)))

        for (n in listOf(1.toByte(), 1.toShort(), 1, 1.toLong(), 1.toFloat(), 1.0)) {
            Assertions.assertEquals(0, Ops.compare(2, Ops.plus(one, n)))
            Assertions.assertEquals(0, Ops.compare(-2, Ops.plus(negThree, n)))
            Assertions.assertEquals(0, Ops.compare(2, Ops.minus(three, n)))
            Assertions.assertEquals(0, Ops.compare(0, Ops.minus(one, n)))
        }

        for (n in listOf(BigInteger.ONE, BigDecimal.ONE, Ratio.valueOf(1L), Complex.ONE)) {
            Assertions.assertEquals(0, Ops.compare(2, Ops.plus(one, n)))
            Assertions.assertEquals(0, Ops.compare(-2, Ops.plus(negThree, n)))
            Assertions.assertEquals(0, Ops.compare(2, Ops.minus(three, n)))
            Assertions.assertEquals(0, Ops.compare(0, Ops.minus(one, n)))
        }
    }

    @Test
    fun plusMinusBigDecimal() {
        val one = 1.toByte()
        val three = 3.toByte()
        val negThree = (-3).toByte()

        Assertions.assertEquals(three, Ops.plus(three))
        Assertions.assertEquals(0, Ops.compare(negThree, Ops.minus(three)))

        for (n in listOf(1.toByte(), 1.toShort(), 1, 1.toLong(), 1.toFloat(), 1.0)) {
            Assertions.assertEquals(0, Ops.compare(2, Ops.plus(one, n)))
            Assertions.assertEquals(0, Ops.compare(-2, Ops.plus(negThree, n)))
            Assertions.assertEquals(0, Ops.compare(2, Ops.minus(three, n)))
            Assertions.assertEquals(0, Ops.compare(0, Ops.minus(one, n)))
        }

        for (n in listOf(BigInteger.ONE, BigDecimal.ONE, Ratio.valueOf(1L), Complex.ONE)) {
            Assertions.assertEquals(0, Ops.compare(2, Ops.plus(one, n)))
            Assertions.assertEquals(0, Ops.compare(-2, Ops.plus(negThree, n)))
            Assertions.assertEquals(0, Ops.compare(2, Ops.minus(three, n)))
            Assertions.assertEquals(0, Ops.compare(0, Ops.minus(one, n)))
        }
    }

    @Test
    fun plusMinusRatio() {
        val one = 1.toByte()
        val three = 3.toByte()
        val negThree = (-3).toByte()

        Assertions.assertEquals(three, Ops.plus(three))
        Assertions.assertEquals(0, Ops.compare(negThree, Ops.minus(three)))

        for (n in listOf(1.toByte(), 1.toShort(), 1, 1.toLong(), 1.toFloat(), 1.0)) {
            Assertions.assertEquals(0, Ops.compare(2, Ops.plus(one, n)))
            Assertions.assertEquals(0, Ops.compare(-2, Ops.plus(negThree, n)))
            Assertions.assertEquals(0, Ops.compare(2, Ops.minus(three, n)))
            Assertions.assertEquals(0, Ops.compare(0, Ops.minus(one, n)))
        }

        for (n in listOf(BigInteger.ONE, BigDecimal.ONE, Ratio.valueOf(1L), Complex.ONE)) {
            Assertions.assertEquals(0, Ops.compare(2, Ops.plus(one, n)))
            Assertions.assertEquals(0, Ops.compare(-2, Ops.plus(negThree, n)))
            Assertions.assertEquals(0, Ops.compare(2, Ops.minus(three, n)))
            Assertions.assertEquals(0, Ops.compare(0, Ops.minus(one, n)))
        }
    }

    @Test
    fun plusMinusComplex() {
        val one = 1.toByte()
        val three = 3.toByte()
        val negThree = (-3).toByte()

        Assertions.assertEquals(three, Ops.plus(three))
        Assertions.assertEquals(0, Ops.compare(negThree, Ops.minus(three)))

        for (n in listOf(1.toByte(), 1.toShort(), 1, 1.toLong(), 1.toFloat(), 1.0)) {
            Assertions.assertEquals(0, Ops.compare(2, Ops.plus(one, n)))
            Assertions.assertEquals(0, Ops.compare(-2, Ops.plus(negThree, n)))
            Assertions.assertEquals(0, Ops.compare(2, Ops.minus(three, n)))
            Assertions.assertEquals(0, Ops.compare(0, Ops.minus(one, n)))
        }

        for (n in listOf(BigInteger.ONE, BigDecimal.ONE, Ratio.valueOf(1L), Complex.ONE)) {
            Assertions.assertEquals(0, Ops.compare(2, Ops.plus(one, n)))
            Assertions.assertEquals(0, Ops.compare(-2, Ops.plus(negThree, n)))
            Assertions.assertEquals(0, Ops.compare(2, Ops.minus(three, n)))
            Assertions.assertEquals(0, Ops.compare(0, Ops.minus(one, n)))
        }
    }

    @Test
    fun plusMinusComplexFail() {
    }
}
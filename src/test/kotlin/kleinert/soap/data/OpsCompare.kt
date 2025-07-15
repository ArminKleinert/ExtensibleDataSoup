package kleinert.soap.data

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.BigInteger

class OpsCompare {
    @Test
    fun compareByte() {
        val three = 3.toByte()
        val zero = 0.toByte()
        val negThree = (-3).toByte()

        for (n in listOf(0.toByte(), 0.toShort(), 0, 0.toLong(), 0.toFloat(), 0.0)) {
            Assertions.assertEquals(1, Ops.compare(three, n))
            Assertions.assertEquals(0, Ops.compare(zero, n))
            Assertions.assertEquals(-1, Ops.compare(negThree, n))

            Assertions.assertTrue(Ops.greaterThan(three, n))
            Assertions.assertFalse(Ops.greaterThan(zero, n))
            Assertions.assertFalse(Ops.greaterThan(negThree, n))

            Assertions.assertTrue(Ops.greaterThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.greaterThanOrEqualTo(zero, n))
            Assertions.assertFalse(Ops.greaterThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.lessThan(three, n))
            Assertions.assertFalse(Ops.lessThan(zero, n))
            Assertions.assertTrue(Ops.lessThan(negThree, n))

            Assertions.assertFalse(Ops.lessThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(zero, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.equals(three, n))
            Assertions.assertTrue(Ops.equals(zero, n))
            Assertions.assertFalse(Ops.equals(negThree, n))
        }

        for (n in listOf(BigInteger.ZERO, BigDecimal.ZERO, Ratio.ZERO, Complex.ZERO)) {
            Assertions.assertEquals(1, Ops.compare(three, n))
            Assertions.assertEquals(0, Ops.compare(zero, n))
            Assertions.assertEquals(-1, Ops.compare(negThree, n))

            Assertions.assertTrue(Ops.greaterThan(three, n))
            Assertions.assertFalse(Ops.greaterThan(zero, n))
            Assertions.assertFalse(Ops.greaterThan(negThree, n))

            Assertions.assertTrue(Ops.greaterThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.greaterThanOrEqualTo(zero, n))
            Assertions.assertFalse(Ops.greaterThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.lessThan(three, n))
            Assertions.assertFalse(Ops.lessThan(zero, n))
            Assertions.assertTrue(Ops.lessThan(negThree, n))

            Assertions.assertFalse(Ops.lessThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(zero, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.equals(three, n))
            Assertions.assertTrue(Ops.equals(zero, n))
            Assertions.assertFalse(Ops.equals(negThree, n))
        }

        for (o in listOf(listOf<Int>(), "abc", Pair(1, 2), Complex.valueOf(1, 1))) {
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThan(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThan(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThan(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThanOrEqualTo(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThanOrEqualTo(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThanOrEqualTo(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThan(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThan(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThan(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThanOrEqualTo(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThanOrEqualTo(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThanOrEqualTo(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.equals(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.equals(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.equals(negThree, o) }
        }
    }

    @Test
    fun compareShort() {
        val three = 3.toShort()
        val zero = 0.toShort()
        val negThree = (-3).toShort()

        for (n in listOf(0.toByte(), 0.toShort(), 0, 0.toLong(), 0.toFloat(), 0.0)) {
            Assertions.assertEquals(1, Ops.compare(three, n))
            Assertions.assertEquals(0, Ops.compare(zero, n))
            Assertions.assertEquals(-1, Ops.compare(negThree, n))

            Assertions.assertTrue(Ops.greaterThan(three, n))
            Assertions.assertFalse(Ops.greaterThan(zero, n))
            Assertions.assertFalse(Ops.greaterThan(negThree, n))

            Assertions.assertTrue(Ops.greaterThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.greaterThanOrEqualTo(zero, n))
            Assertions.assertFalse(Ops.greaterThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.lessThan(three, n))
            Assertions.assertFalse(Ops.lessThan(zero, n))
            Assertions.assertTrue(Ops.lessThan(negThree, n))

            Assertions.assertFalse(Ops.lessThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(zero, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.equals(three, n))
            Assertions.assertTrue(Ops.equals(zero, n))
            Assertions.assertFalse(Ops.equals(negThree, n))
        }

        for (n in listOf(BigInteger.ZERO, BigDecimal.ZERO, Ratio.ZERO, Complex.ZERO)) {
            Assertions.assertEquals(1, Ops.compare(three, n))
            Assertions.assertEquals(0, Ops.compare(zero, n))
            Assertions.assertEquals(-1, Ops.compare(negThree, n))

            Assertions.assertTrue(Ops.greaterThan(three, n))
            Assertions.assertFalse(Ops.greaterThan(zero, n))
            Assertions.assertFalse(Ops.greaterThan(negThree, n))

            Assertions.assertTrue(Ops.greaterThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.greaterThanOrEqualTo(zero, n))
            Assertions.assertFalse(Ops.greaterThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.lessThan(three, n))
            Assertions.assertFalse(Ops.lessThan(zero, n))
            Assertions.assertTrue(Ops.lessThan(negThree, n))

            Assertions.assertFalse(Ops.lessThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(zero, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.equals(three, n))
            Assertions.assertTrue(Ops.equals(zero, n))
            Assertions.assertFalse(Ops.equals(negThree, n))
        }

        for (o in listOf(listOf<Int>(), "abc", Pair(1, 2), Complex.valueOf(1, 1))) {
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThan(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThan(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThan(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThanOrEqualTo(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThanOrEqualTo(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThanOrEqualTo(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThan(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThan(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThan(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThanOrEqualTo(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThanOrEqualTo(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThanOrEqualTo(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.equals(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.equals(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.equals(negThree, o) }
        }
    }

    @Test
    fun compareInt() {
        val three = 3
        val zero = 0
        val negThree = (-3)

        for (n in listOf(0.toByte(), 0.toShort(), 0, 0.toLong(), 0.toFloat(), 0.0)) {
            Assertions.assertEquals(1, Ops.compare(three, n))
            Assertions.assertEquals(0, Ops.compare(zero, n))
            Assertions.assertEquals(-1, Ops.compare(negThree, n))

            Assertions.assertTrue(Ops.greaterThan(three, n))
            Assertions.assertFalse(Ops.greaterThan(zero, n))
            Assertions.assertFalse(Ops.greaterThan(negThree, n))

            Assertions.assertTrue(Ops.greaterThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.greaterThanOrEqualTo(zero, n))
            Assertions.assertFalse(Ops.greaterThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.lessThan(three, n))
            Assertions.assertFalse(Ops.lessThan(zero, n))
            Assertions.assertTrue(Ops.lessThan(negThree, n))

            Assertions.assertFalse(Ops.lessThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(zero, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.equals(three, n))
            Assertions.assertTrue(Ops.equals(zero, n))
            Assertions.assertFalse(Ops.equals(negThree, n))
        }

        for (n in listOf(BigInteger.ZERO, BigDecimal.ZERO, Ratio.ZERO, Complex.ZERO)) {
            Assertions.assertEquals(1, Ops.compare(three, n))
            Assertions.assertEquals(0, Ops.compare(zero, n))
            Assertions.assertEquals(-1, Ops.compare(negThree, n))

            Assertions.assertTrue(Ops.greaterThan(three, n))
            Assertions.assertFalse(Ops.greaterThan(zero, n))
            Assertions.assertFalse(Ops.greaterThan(negThree, n))

            Assertions.assertTrue(Ops.greaterThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.greaterThanOrEqualTo(zero, n))
            Assertions.assertFalse(Ops.greaterThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.lessThan(three, n))
            Assertions.assertFalse(Ops.lessThan(zero, n))
            Assertions.assertTrue(Ops.lessThan(negThree, n))

            Assertions.assertFalse(Ops.lessThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(zero, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.equals(three, n))
            Assertions.assertTrue(Ops.equals(zero, n))
            Assertions.assertFalse(Ops.equals(negThree, n))
        }

        for (o in listOf(listOf<Int>(), "abc", Pair(1, 2), Complex.valueOf(1, 1))) {
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThan(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThan(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThan(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThanOrEqualTo(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThanOrEqualTo(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThanOrEqualTo(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThan(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThan(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThan(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThanOrEqualTo(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThanOrEqualTo(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThanOrEqualTo(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.equals(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.equals(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.equals(negThree, o) }
        }
    }

    @Test
    fun compareLong() {
        val three = 3.toLong()
        val zero = 0.toLong()
        val negThree = (-3).toLong()

        for (n in listOf(0.toByte(), 0.toShort(), 0, 0.toLong(), 0.toFloat(), 0.0)) {
            Assertions.assertEquals(1, Ops.compare(three, n))
            Assertions.assertEquals(0, Ops.compare(zero, n))
            Assertions.assertEquals(-1, Ops.compare(negThree, n))

            Assertions.assertTrue(Ops.greaterThan(three, n))
            Assertions.assertFalse(Ops.greaterThan(zero, n))
            Assertions.assertFalse(Ops.greaterThan(negThree, n))

            Assertions.assertTrue(Ops.greaterThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.greaterThanOrEqualTo(zero, n))
            Assertions.assertFalse(Ops.greaterThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.lessThan(three, n))
            Assertions.assertFalse(Ops.lessThan(zero, n))
            Assertions.assertTrue(Ops.lessThan(negThree, n))

            Assertions.assertFalse(Ops.lessThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(zero, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.equals(three, n))
            Assertions.assertTrue(Ops.equals(zero, n))
            Assertions.assertFalse(Ops.equals(negThree, n))
        }

        for (n in listOf(BigInteger.ZERO, BigDecimal.ZERO, Ratio.ZERO, Complex.ZERO)) {
            Assertions.assertEquals(1, Ops.compare(three, n))
            Assertions.assertEquals(0, Ops.compare(zero, n))
            Assertions.assertEquals(-1, Ops.compare(negThree, n))

            Assertions.assertTrue(Ops.greaterThan(three, n))
            Assertions.assertFalse(Ops.greaterThan(zero, n))
            Assertions.assertFalse(Ops.greaterThan(negThree, n))

            Assertions.assertTrue(Ops.greaterThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.greaterThanOrEqualTo(zero, n))
            Assertions.assertFalse(Ops.greaterThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.lessThan(three, n))
            Assertions.assertFalse(Ops.lessThan(zero, n))
            Assertions.assertTrue(Ops.lessThan(negThree, n))

            Assertions.assertFalse(Ops.lessThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(zero, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.equals(three, n))
            Assertions.assertTrue(Ops.equals(zero, n))
            Assertions.assertFalse(Ops.equals(negThree, n))
        }

        for (o in listOf(listOf<Int>(), "abc", Pair(1, 2), Complex.valueOf(1, 1))) {
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThan(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThan(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThan(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThanOrEqualTo(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThanOrEqualTo(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThanOrEqualTo(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThan(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThan(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThan(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThanOrEqualTo(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThanOrEqualTo(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThanOrEqualTo(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.equals(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.equals(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.equals(negThree, o) }
        }
    }

    @Test
    fun compareFloat() {
        val three = 3.toFloat()
        val zero = 0.toFloat()
        val negThree = (-3).toFloat()

        for (n in listOf(0.toByte(), 0.toShort(), 0, 0.toLong(), 0.toFloat(), 0.0)) {
            Assertions.assertEquals(1, Ops.compare(three, n))
            Assertions.assertEquals(0, Ops.compare(zero, n))
            Assertions.assertEquals(-1, Ops.compare(negThree, n))

            Assertions.assertTrue(Ops.greaterThan(three, n))
            Assertions.assertFalse(Ops.greaterThan(zero, n))
            Assertions.assertFalse(Ops.greaterThan(negThree, n))

            Assertions.assertTrue(Ops.greaterThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.greaterThanOrEqualTo(zero, n))
            Assertions.assertFalse(Ops.greaterThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.lessThan(three, n))
            Assertions.assertFalse(Ops.lessThan(zero, n))
            Assertions.assertTrue(Ops.lessThan(negThree, n))

            Assertions.assertFalse(Ops.lessThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(zero, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.equals(three, n))
            Assertions.assertTrue(Ops.equals(zero, n))
            Assertions.assertFalse(Ops.equals(negThree, n))
        }

        for (n in listOf(BigInteger.ZERO, BigDecimal.ZERO, Ratio.ZERO, Complex.ZERO)) {
            Assertions.assertEquals(1, Ops.compare(three, n))
            Assertions.assertEquals(0, Ops.compare(zero, n))
            Assertions.assertEquals(-1, Ops.compare(negThree, n))

            Assertions.assertTrue(Ops.greaterThan(three, n))
            Assertions.assertFalse(Ops.greaterThan(zero, n))
            Assertions.assertFalse(Ops.greaterThan(negThree, n))

            Assertions.assertTrue(Ops.greaterThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.greaterThanOrEqualTo(zero, n))
            Assertions.assertFalse(Ops.greaterThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.lessThan(three, n))
            Assertions.assertFalse(Ops.lessThan(zero, n))
            Assertions.assertTrue(Ops.lessThan(negThree, n))

            Assertions.assertFalse(Ops.lessThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(zero, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.equals(three, n))
            Assertions.assertTrue(Ops.equals(zero, n))
            Assertions.assertFalse(Ops.equals(negThree, n))
        }

        for (o in listOf(listOf<Int>(), "abc", Pair(1, 2), Complex.valueOf(1, 1))) {
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThan(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThan(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThan(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThanOrEqualTo(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThanOrEqualTo(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThanOrEqualTo(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThan(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThan(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThan(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThanOrEqualTo(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThanOrEqualTo(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThanOrEqualTo(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.equals(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.equals(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.equals(negThree, o) }
        }
    }

    @Test
    fun compareDouble() {
        val three = 3.toDouble()
        val zero = 0.toDouble()
        val negThree = (-3).toDouble()

        for (n in listOf(0.toByte(), 0.toShort(), 0, 0.toLong(), 0.toFloat(), 0.0)) {
            Assertions.assertEquals(1, Ops.compare(three, n))
            Assertions.assertEquals(0, Ops.compare(zero, n))
            Assertions.assertEquals(-1, Ops.compare(negThree, n))

            Assertions.assertTrue(Ops.greaterThan(three, n))
            Assertions.assertFalse(Ops.greaterThan(zero, n))
            Assertions.assertFalse(Ops.greaterThan(negThree, n))

            Assertions.assertTrue(Ops.greaterThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.greaterThanOrEqualTo(zero, n))
            Assertions.assertFalse(Ops.greaterThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.lessThan(three, n))
            Assertions.assertFalse(Ops.lessThan(zero, n))
            Assertions.assertTrue(Ops.lessThan(negThree, n))

            Assertions.assertFalse(Ops.lessThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(zero, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.equals(three, n))
            Assertions.assertTrue(Ops.equals(zero, n))
            Assertions.assertFalse(Ops.equals(negThree, n))
        }

        for (n in listOf(BigInteger.ZERO, BigDecimal.ZERO, Ratio.ZERO, Complex.ZERO)) {
            Assertions.assertEquals(1, Ops.compare(three, n))
            Assertions.assertEquals(0, Ops.compare(zero, n))
            Assertions.assertEquals(-1, Ops.compare(negThree, n))

            Assertions.assertTrue(Ops.greaterThan(three, n))
            Assertions.assertFalse(Ops.greaterThan(zero, n))
            Assertions.assertFalse(Ops.greaterThan(negThree, n))

            Assertions.assertTrue(Ops.greaterThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.greaterThanOrEqualTo(zero, n))
            Assertions.assertFalse(Ops.greaterThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.lessThan(three, n))
            Assertions.assertFalse(Ops.lessThan(zero, n))
            Assertions.assertTrue(Ops.lessThan(negThree, n))

            Assertions.assertFalse(Ops.lessThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(zero, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.equals(three, n))
            Assertions.assertTrue(Ops.equals(zero, n))
            Assertions.assertFalse(Ops.equals(negThree, n))
        }

        for (o in listOf(listOf<Int>(), "abc", Pair(1, 2), Complex.valueOf(1, 1))) {
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThan(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThan(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThan(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThanOrEqualTo(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThanOrEqualTo(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThanOrEqualTo(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThan(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThan(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThan(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThanOrEqualTo(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThanOrEqualTo(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThanOrEqualTo(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.equals(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.equals(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.equals(negThree, o) }
        }
    }

    @Test
    fun compareBigInt() {
        val three = BigInteger.valueOf(3L)
        val zero = BigInteger.ZERO
        val negThree = BigInteger.valueOf(-3L)

        for (n in listOf(0.toByte(), 0.toShort(), 0, 0.toLong(), 0.toFloat(), 0.0)) {
            Assertions.assertEquals(1, Ops.compare(three, n))
            Assertions.assertEquals(0, Ops.compare(zero, n))
            Assertions.assertEquals(-1, Ops.compare(negThree, n))

            Assertions.assertTrue(Ops.greaterThan(three, n))
            Assertions.assertFalse(Ops.greaterThan(zero, n))
            Assertions.assertFalse(Ops.greaterThan(negThree, n))

            Assertions.assertTrue(Ops.greaterThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.greaterThanOrEqualTo(zero, n))
            Assertions.assertFalse(Ops.greaterThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.lessThan(three, n))
            Assertions.assertFalse(Ops.lessThan(zero, n))
            Assertions.assertTrue(Ops.lessThan(negThree, n))

            Assertions.assertFalse(Ops.lessThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(zero, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.equals(three, n))
            Assertions.assertTrue(Ops.equals(zero, n))
            Assertions.assertFalse(Ops.equals(negThree, n))
        }

        for (n in listOf(BigInteger.ZERO, BigDecimal.ZERO, Ratio.ZERO, Complex.ZERO)) {
            Assertions.assertEquals(1, Ops.compare(three, n))
            Assertions.assertEquals(0, Ops.compare(zero, n))
            Assertions.assertEquals(-1, Ops.compare(negThree, n))

            Assertions.assertTrue(Ops.greaterThan(three, n))
            Assertions.assertFalse(Ops.greaterThan(zero, n))
            Assertions.assertFalse(Ops.greaterThan(negThree, n))

            Assertions.assertTrue(Ops.greaterThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.greaterThanOrEqualTo(zero, n))
            Assertions.assertFalse(Ops.greaterThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.lessThan(three, n))
            Assertions.assertFalse(Ops.lessThan(zero, n))
            Assertions.assertTrue(Ops.lessThan(negThree, n))

            Assertions.assertFalse(Ops.lessThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(zero, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.equals(three, n))
            Assertions.assertTrue(Ops.equals(zero, n))
            Assertions.assertFalse(Ops.equals(negThree, n))
        }

        for (o in listOf(listOf<Int>(), "abc", Pair(1, 2), Complex.valueOf(1, 1))) {
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThan(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThan(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThan(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThanOrEqualTo(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThanOrEqualTo(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThanOrEqualTo(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThan(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThan(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThan(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThanOrEqualTo(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThanOrEqualTo(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThanOrEqualTo(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.equals(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.equals(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.equals(negThree, o) }
        }
    }

    @Test
    fun compareBigDecimal() {
        val three = BigDecimal.valueOf(3L)
        val zero = BigDecimal.ZERO
        val negThree = BigDecimal.valueOf(-3L)

        for (n in listOf(0.toByte(), 0.toShort(), 0, 0.toLong(), 0.toFloat(), 0.0)) {
            Assertions.assertEquals(1, Ops.compare(three, n))
            Assertions.assertEquals(0, Ops.compare(zero, n))
            Assertions.assertEquals(-1, Ops.compare(negThree, n))

            Assertions.assertTrue(Ops.greaterThan(three, n))
            Assertions.assertFalse(Ops.greaterThan(zero, n))
            Assertions.assertFalse(Ops.greaterThan(negThree, n))

            Assertions.assertTrue(Ops.greaterThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.greaterThanOrEqualTo(zero, n))
            Assertions.assertFalse(Ops.greaterThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.lessThan(three, n))
            Assertions.assertFalse(Ops.lessThan(zero, n))
            Assertions.assertTrue(Ops.lessThan(negThree, n))

            Assertions.assertFalse(Ops.lessThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(zero, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.equals(three, n))
            Assertions.assertTrue(Ops.equals(zero, n))
            Assertions.assertFalse(Ops.equals(negThree, n))
        }

        for (n in listOf(BigInteger.ZERO, BigDecimal.ZERO, Ratio.ZERO, Complex.ZERO)) {
            Assertions.assertEquals(1, Ops.compare(three, n))
            Assertions.assertEquals(0, Ops.compare(zero, n))
            Assertions.assertEquals(-1, Ops.compare(negThree, n))

            Assertions.assertTrue(Ops.greaterThan(three, n))
            Assertions.assertFalse(Ops.greaterThan(zero, n))
            Assertions.assertFalse(Ops.greaterThan(negThree, n))

            Assertions.assertTrue(Ops.greaterThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.greaterThanOrEqualTo(zero, n))
            Assertions.assertFalse(Ops.greaterThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.lessThan(three, n))
            Assertions.assertFalse(Ops.lessThan(zero, n))
            Assertions.assertTrue(Ops.lessThan(negThree, n))

            Assertions.assertFalse(Ops.lessThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(zero, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.equals(three, n))
            Assertions.assertTrue(Ops.equals(zero, n))
            Assertions.assertFalse(Ops.equals(negThree, n))
        }

        for (o in listOf(listOf<Int>(), "abc", Pair(1, 2), Complex.valueOf(1, 1))) {
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThan(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThan(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThan(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThanOrEqualTo(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThanOrEqualTo(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThanOrEqualTo(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThan(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThan(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThan(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThanOrEqualTo(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThanOrEqualTo(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThanOrEqualTo(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.equals(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.equals(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.equals(negThree, o) }
        }
    }

    @Test
    fun compareRatio() {
        val three = Ratio.valueOf(3)
        val zero = Ratio.ZERO
        val negThree = Ratio.valueOf(-3L)

        for (n in listOf(0.toByte(), 0.toShort(), 0, 0.toLong(), 0.toFloat(), 0.0)) {
            Assertions.assertEquals(1, Ops.compare(three, n))
            Assertions.assertEquals(0, Ops.compare(zero, n))
            Assertions.assertEquals(-1, Ops.compare(negThree, n))

            Assertions.assertTrue(Ops.greaterThan(three, n))
            Assertions.assertFalse(Ops.greaterThan(zero, n))
            Assertions.assertFalse(Ops.greaterThan(negThree, n))

            Assertions.assertTrue(Ops.greaterThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.greaterThanOrEqualTo(zero, n))
            Assertions.assertFalse(Ops.greaterThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.lessThan(three, n))
            Assertions.assertFalse(Ops.lessThan(zero, n))
            Assertions.assertTrue(Ops.lessThan(negThree, n))

            Assertions.assertFalse(Ops.lessThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(zero, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.equals(three, n))
            Assertions.assertTrue(Ops.equals(zero, n))
            Assertions.assertFalse(Ops.equals(negThree, n))
        }

        for (n in listOf(BigInteger.ZERO, BigDecimal.ZERO, Ratio.ZERO, Complex.ZERO)) {
            Assertions.assertEquals(1, Ops.compare(three, n))
            Assertions.assertEquals(0, Ops.compare(zero, n))
            Assertions.assertEquals(-1, Ops.compare(negThree, n))

            Assertions.assertTrue(Ops.greaterThan(three, n))
            Assertions.assertFalse(Ops.greaterThan(zero, n))
            Assertions.assertFalse(Ops.greaterThan(negThree, n))

            Assertions.assertTrue(Ops.greaterThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.greaterThanOrEqualTo(zero, n))
            Assertions.assertFalse(Ops.greaterThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.lessThan(three, n))
            Assertions.assertFalse(Ops.lessThan(zero, n))
            Assertions.assertTrue(Ops.lessThan(negThree, n))

            Assertions.assertFalse(Ops.lessThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(zero, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.equals(three, n))
            Assertions.assertTrue(Ops.equals(zero, n))
            Assertions.assertFalse(Ops.equals(negThree, n))
        }

        for (o in listOf(listOf<Int>(), "abc", Pair(1, 2), Complex.valueOf(1, 1))) {
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThan(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThan(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThan(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThanOrEqualTo(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThanOrEqualTo(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThanOrEqualTo(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThan(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThan(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThan(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThanOrEqualTo(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThanOrEqualTo(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThanOrEqualTo(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.equals(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.equals(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.equals(negThree, o) }
        }
    }

    @Test
    fun compareComplex() {
        val three = Complex.valueOf(3)
        val zero = Complex.ZERO
        val negThree = Complex.valueOf(-3)

        for (n in listOf(0.toByte(), 0.toShort(), 0, 0.toLong(), 0.toFloat(), 0.0)) {
            Assertions.assertEquals(1, Ops.compare(three, n))
            Assertions.assertEquals(0, Ops.compare(zero, n))
            Assertions.assertEquals(-1, Ops.compare(negThree, n))

            Assertions.assertTrue(Ops.greaterThan(three, n))
            Assertions.assertFalse(Ops.greaterThan(zero, n))
            Assertions.assertFalse(Ops.greaterThan(negThree, n))

            Assertions.assertTrue(Ops.greaterThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.greaterThanOrEqualTo(zero, n))
            Assertions.assertFalse(Ops.greaterThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.lessThan(three, n))
            Assertions.assertFalse(Ops.lessThan(zero, n))
            Assertions.assertTrue(Ops.lessThan(negThree, n))

            Assertions.assertFalse(Ops.lessThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(zero, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.equals(three, n))
            Assertions.assertTrue(Ops.equals(zero, n))
            Assertions.assertFalse(Ops.equals(negThree, n))
        }

        for (n in listOf(BigInteger.ZERO, BigDecimal.ZERO, Ratio.ZERO, Complex.ZERO)) {
            Assertions.assertEquals(1, Ops.compare(three, n))
            Assertions.assertEquals(0, Ops.compare(zero, n))
            Assertions.assertEquals(-1, Ops.compare(negThree, n))

            Assertions.assertTrue(Ops.greaterThan(three, n))
            Assertions.assertFalse(Ops.greaterThan(zero, n))
            Assertions.assertFalse(Ops.greaterThan(negThree, n))

            Assertions.assertTrue(Ops.greaterThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.greaterThanOrEqualTo(zero, n))
            Assertions.assertFalse(Ops.greaterThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.lessThan(three, n))
            Assertions.assertFalse(Ops.lessThan(zero, n))
            Assertions.assertTrue(Ops.lessThan(negThree, n))

            Assertions.assertFalse(Ops.lessThanOrEqualTo(three, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(zero, n))
            Assertions.assertTrue(Ops.lessThanOrEqualTo(negThree, n))

            Assertions.assertFalse(Ops.equals(three, n))
            Assertions.assertTrue(Ops.equals(zero, n))
            Assertions.assertFalse(Ops.equals(negThree, n))
        }

        for (o in listOf(listOf<Int>(), "abc", Pair(1, 2), Complex.valueOf(1, 1))) {
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThan(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThan(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThan(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThanOrEqualTo(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThanOrEqualTo(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.greaterThanOrEqualTo(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThan(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThan(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThan(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThanOrEqualTo(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThanOrEqualTo(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.lessThanOrEqualTo(negThree, o) }

            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.equals(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.equals(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.equals(negThree, o) }
        }
    }

    @Test
    fun compareComplexFail() {
        val three = Complex.valueOf(3, 1)
        val zero = Complex.ZERO + Complex.I
        val negThree = Complex.valueOf(-3, -1)

        for (n in listOf(0.toByte(), 0.toShort(), 0, 0.toLong(), 0.toFloat(), 0.0)) {
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(three, n) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(zero, n) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(negThree, n) }
        }

        for (n in listOf(BigInteger.ZERO, BigDecimal.ZERO, Ratio.ZERO, Complex.ZERO)) {
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(three, n) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(zero, n) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(negThree, n) }
        }

        for (o in listOf(listOf<Int>(), "abc", Pair(1, 2), Complex.valueOf(1, 1))) {
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(three, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(zero, o) }
            Assertions.assertThrows(IllegalArgumentException::class.java) { Ops.compare(negThree, o) }
        }
    }
}

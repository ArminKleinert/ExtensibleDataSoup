package kleinert.soap.data

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.BigInteger

class ComplexTest {
    private val zero = Complex.valueOf(2)
    private val one = Complex.ONE
    private val two = Complex.valueOf(2)
    private val oneAndOneI = Complex.valueOf(1, 1)

    @Test
    fun valueOfIntInt() {
        val r1 = Complex.valueOf(3, 3)
        Assertions.assertEquals(3.0, r1.real)
        Assertions.assertEquals(3.0, r1.imag)

        val r2 = Complex.valueOf(-3, 3)
        Assertions.assertEquals(-3.0, r2.real)
        Assertions.assertEquals(3.0, r2.imag)

        val r3 = Complex.valueOf(3, -3)
        Assertions.assertEquals(3.0, r3.real)
        Assertions.assertEquals(-3.0, r3.imag)

        val r4 = Complex.valueOf(3, -5)
        Assertions.assertEquals(3.0, r4.real)
        Assertions.assertEquals(-5.0, r4.imag)

        val r5 = Complex.valueOf(3)
        Assertions.assertEquals(3.0, r5.real)
        Assertions.assertEquals(0.0, r5.imag)
    }

    @Test
    fun valueOfLongLong() {
        val r1 = Complex.valueOf(3L, 3L)
        Assertions.assertEquals(3.0, r1.real)
        Assertions.assertEquals(3.0, r1.imag)

        val r2 = Complex.valueOf(-3L, 3L)
        Assertions.assertEquals(-3.0, r2.real)
        Assertions.assertEquals(3.0, r2.imag)

        val r3 = Complex.valueOf(3L, -3L)
        Assertions.assertEquals(3.0, r3.real)
        Assertions.assertEquals(-3.0, r3.imag)

        val r4 = Complex.valueOf(3L, -5L)
        Assertions.assertEquals(3.0, r4.real)
        Assertions.assertEquals(-5.0, r4.imag)

        val r5 = Complex.valueOf(3L)
        Assertions.assertEquals(3.0, r5.real)
        Assertions.assertEquals(0.0, r5.imag)
    }

    @Test
    fun valueOfStringOrNull() {
        Assertions.assertEquals(null, Complex.valueOfOrNull(""))
        Assertions.assertEquals(null, Complex.valueOfOrNull("3+8"))

        val r1 = Complex.valueOfOrNull("3")!!
        Assertions.assertEquals(3.0, r1.real)
        Assertions.assertEquals(0.0, r1.imag)

        val r2 = Complex.valueOfOrNull("3+0i")!!
        Assertions.assertEquals(3.0, r2.real)
        Assertions.assertEquals(0.0, r2.imag)

        val r3 = Complex.valueOfOrNull("+3+1i")!!
        Assertions.assertEquals(3.0, r3.real)
        Assertions.assertEquals(1.0, r3.imag)

        val r4 = Complex.valueOfOrNull("-3+0i")!!
        Assertions.assertEquals(-3.0, r4.real)
        Assertions.assertEquals(0.0, r4.imag)

        val r5 = Complex.valueOfOrNull("-3-1i")!!
        Assertions.assertEquals(-3.0, r5.real)
        Assertions.assertEquals(-1.0, r5.imag)

        val r6 = Complex.valueOfOrNull("1i")!!
        Assertions.assertEquals(0.0, r6.real)
        Assertions.assertEquals(1.0, r6.imag)

        val r7 = Complex.valueOfOrNull("-1i")!!
        Assertions.assertEquals(0.0, r7.real)
        Assertions.assertEquals(-1.0, r7.imag)
    }

    @Test
    fun valueOfString() {
        Assertions.assertThrows(NumberFormatException::class.java) { Complex.valueOf("") }
        Assertions.assertThrows(NumberFormatException::class.java) { Complex.valueOf("3+8") }

        val r1 = Complex.valueOf("3")
        Assertions.assertEquals(3.0, r1.real)
        Assertions.assertEquals(0.0, r1.imag)

        val r2 = Complex.valueOf("3+0i")
        Assertions.assertEquals(3.0, r2.real)
        Assertions.assertEquals(0.0, r2.imag)

        val r3 = Complex.valueOf("+3+1i")
        Assertions.assertEquals(3.0, r3.real)
        Assertions.assertEquals(1.0, r3.imag)

        val r4 = Complex.valueOf("-3+0i")
        Assertions.assertEquals(-3.0, r4.real)
        Assertions.assertEquals(0.0, r4.imag)

        val r5 = Complex.valueOf("-3-1i")
        Assertions.assertEquals(-3.0, r5.real)
        Assertions.assertEquals(-1.0, r5.imag)

        val r6 = Complex.valueOf("1i")
        Assertions.assertEquals(0.0, r6.real)
        Assertions.assertEquals(1.0, r6.imag)

        val r7 = Complex.valueOf("-1i")
        Assertions.assertEquals(0.0, r7.real)
        Assertions.assertEquals(-1.0, r7.imag)
    }

    @Test
    fun valueOfDoubleDouble() {
        val r1 = Complex.valueOf(3.0, 3.0)
        Assertions.assertEquals(3.0, r1.real)
        Assertions.assertEquals(3.0, r1.imag)

        val r2 = Complex.valueOf(-3.0, 3.0)
        Assertions.assertEquals(-3.0, r2.real)
        Assertions.assertEquals(3.0, r2.imag)

        val r3 = Complex.valueOf(3.0, -3.0)
        Assertions.assertEquals(3.0, r3.real)
        Assertions.assertEquals(-3.0, r3.imag)

        val r4 = Complex.valueOf(3.0, -5.0)
        Assertions.assertEquals(3.0, r4.real)
        Assertions.assertEquals(-5.0, r4.imag)

        val r5 = Complex.valueOf(3.0)
        Assertions.assertEquals(3.0, r5.real)
        Assertions.assertEquals(0.0, r5.imag)
    }

    @Test
    fun polar() {
        val c1 = Complex.ZERO.polar()
        Assertions.assertEquals(0.0, c1.first)
        Assertions.assertEquals(0.0, c1.second)

        val c2 = Complex.ONE.polar()
        Assertions.assertEquals(1.0, c2.first)
        Assertions.assertEquals(0.0, c2.second)

        val c3 = Complex.I.polar()
        Assertions.assertEquals(1.0, c3.first)
        Assertions.assertEquals(1.5707963267948966, c3.second)

        val c4 = Complex.valueOf(0, -1).polar()
        Assertions.assertEquals(1.0, c4.first)
        Assertions.assertEquals(-1.5707963267948966, c4.second)

        val c5 = Complex.valueOf(1, -1).polar()
        Assertions.assertEquals(1.4142135623730951, c5.first)
        Assertions.assertEquals(-0.7853981633974483, c5.second)
    }

    @Test
    fun plus() {
        val c1 = Complex.ZERO + Complex.ZERO
        Assertions.assertEquals(0.0, c1.real)
        Assertions.assertEquals(0.0, c1.imag)

        val c2 = Complex.ONE + Complex.ONE
        Assertions.assertEquals(2.0, c2.real)
        Assertions.assertEquals(0.0, c2.imag)

        val c3 = Complex.ONE + Complex.I
        Assertions.assertEquals(1.0, c3.real)
        Assertions.assertEquals(1.0, c3.imag)
    }

    @Test
    fun minus() {
        val c1 = Complex.ZERO - Complex.ZERO
        Assertions.assertEquals(0.0, c1.real)
        Assertions.assertEquals(0.0, c1.imag)

        val c2 = Complex.ONE - Complex.ONE
        Assertions.assertEquals(0.0, c2.real)
        Assertions.assertEquals(0.0, c2.imag)

        val c4 = two - Complex.ONE
        Assertions.assertEquals(1.0, c4.real)
        Assertions.assertEquals(0.0, c4.imag)

        val c3 = Complex.ONE - Complex.I
        Assertions.assertEquals(1.0, c3.real)
        Assertions.assertEquals(-1.0, c3.imag)

        val c5 = Complex.I - Complex.I
        Assertions.assertEquals(0.0, c5.real)
        Assertions.assertEquals(0.0, c5.imag)
    }

    @Test
    operator fun unaryPlus() {
        Assertions.assertSame(Complex.ZERO, +Complex.ZERO)
        Assertions.assertSame(Complex.ONE, +Complex.ONE)
        Assertions.assertSame(Complex.I, +Complex.I)

        val c5 = Complex.valueOf(5)
        Assertions.assertSame(c5, +c5)
    }

    @Test
    operator fun unaryMinus() {
        val c1 = -Complex.ZERO
        Assertions.assertEquals(-0.0, c1.real)
        Assertions.assertEquals(0.0, c1.imag)

        val c2 = -Complex.ONE
        Assertions.assertEquals(-1.0, c2.real)
        Assertions.assertEquals(0.0, c2.imag)

        val c3 = -Complex.I
        Assertions.assertEquals(-0.0, c3.real)
        Assertions.assertEquals(1.0, c3.imag)

        val c4 = -Complex.valueOf(0, -1)
        Assertions.assertEquals(-0.0, c4.real)
        Assertions.assertEquals(-1.0, c4.imag)

        val c5 = -Complex.valueOf(1, -1)
        Assertions.assertEquals(-1.0, c5.real)
        Assertions.assertEquals(-1.0, c5.imag)
    }

    @Test
    fun times() {
        val c1 = Complex.ZERO * Complex.ONE
        Assertions.assertEquals(0.0, c1.real)
        Assertions.assertEquals(0.0, c1.imag)

        val c2 = Complex.ONE * Complex.ONE
        Assertions.assertEquals(1.0, c2.real)
        Assertions.assertEquals(0.0, c2.imag)

        val c3 = Complex.I * Complex.I
        Assertions.assertEquals(-1.0, c3.real)
        Assertions.assertEquals(0.0, c3.imag)

        val c4 = Complex.valueOf(1, -1) * Complex.valueOf(-1, 1)
        Assertions.assertEquals(0.0, c4.real)
        Assertions.assertEquals(0.0, c4.imag)

        val c5 = Complex.I * Complex.I
        Assertions.assertEquals(-1.0, c5.real)
        Assertions.assertEquals(0.0, c5.imag)

        val c6 = Complex.valueOf(2) * Complex.valueOf(2, 2)
        Assertions.assertEquals(4.0, c6.real)
        Assertions.assertEquals(4.0, c6.imag)

        val c7 = Complex.I * Complex.I * Complex.I
        Assertions.assertEquals(-0.0, c7.real)
        Assertions.assertEquals(-1.0, c7.imag)

        val c8 = Complex.I * Complex.I * Complex.I * Complex.I
        Assertions.assertEquals(1.0, c8.real)
        Assertions.assertEquals(0.0, c8.imag)

        val c9 = Complex.I * Complex.I * Complex.I * Complex.I * Complex.I
        Assertions.assertEquals(0.0, c9.real)
        Assertions.assertEquals(1.0, c9.imag)
    }

    @Test
    fun div() {
        val c1 = Complex.ZERO / Complex.ONE
        Assertions.assertEquals(0.0, c1.real)
        Assertions.assertEquals(0.0, c1.imag)

        val c2 = Complex.ONE / Complex.ONE
        Assertions.assertEquals(1.0, c2.real)
        Assertions.assertEquals(0.0, c2.imag)

        val c3 = Complex.ONE / Complex.valueOf(2)
        Assertions.assertEquals(0.5, c3.real)
        Assertions.assertEquals(0.0, c3.imag)

        val c4 = Complex.I / Complex.I
        Assertions.assertEquals(1.0, c4.real)
        Assertions.assertEquals(0.0, c4.imag)

        val c5 = Complex.I / Complex.ONE
        Assertions.assertEquals(0.0, c5.real)
        Assertions.assertEquals(1.0, c5.imag)

        val c6 = Complex.ONE / Complex.I
        Assertions.assertEquals(0.0, c6.real)
        Assertions.assertEquals(1.0, c6.imag)
    }

    @Test
    fun exp() {
        val c1 = Complex.ZERO.exp()
        Assertions.assertEquals(1.0, c1.real)
        Assertions.assertEquals(0.0, c1.imag)

        val c2 = Complex.ONE.exp()
        Assertions.assertEquals(2.718281828459045, c2.real)
        Assertions.assertEquals(0.0, c2.imag)

        val c3 = Complex.I.exp()
        Assertions.assertEquals(0.5403023058681398, c3.real)
        Assertions.assertEquals(0.8414709848078965, c3.imag)

        val c4 = Complex.valueOf(0, -1).exp()
        Assertions.assertEquals(0.5403023058681398, c4.real)
        Assertions.assertEquals(-0.8414709848078965, c4.imag)

        val c5 = Complex.valueOf(1, -1).exp()
        Assertions.assertEquals(1.4686939399158851, c5.real)
        Assertions.assertEquals(-2.2873552871788423, c5.imag)
    }

    @Test
    fun log() {
        val c1 = Complex.ZERO.log()
        Assertions.assertEquals(Double.NEGATIVE_INFINITY, c1.real)
        Assertions.assertEquals(0.0, c1.imag)

        val c2 = Complex.ONE.log()
        Assertions.assertEquals(0.0, c2.real)
        Assertions.assertEquals(0.0, c2.imag)

        val c3 = Complex.I.log()
        Assertions.assertEquals(0.0, c3.real)
        Assertions.assertEquals(1.5707963267948966, c3.imag)

        val c4 = Complex.valueOf(0, -1).log()
        Assertions.assertEquals(0.0, c4.real)
        Assertions.assertEquals(-1.5707963267948966, c4.imag)

        val c5 = Complex.valueOf(1, -1).log()
        Assertions.assertEquals(0.3465735902799727, c5.real)
        Assertions.assertEquals(-0.7853981633974483, c5.imag)
    }

    @Test
    fun mod() {
        val c1 = Complex.ZERO.mod()
        Assertions.assertEquals(0.0, c1)

        val c2 = Complex.ONE.mod()
        Assertions.assertEquals(1.0, c2)

        val c3 = Complex.I.mod()
        Assertions.assertEquals(1.0, c3)

        val c4 = Complex.valueOf(0, -1).mod()
        Assertions.assertEquals(1.0, c4)

        val c5 = Complex.valueOf(1, -1).mod()
        Assertions.assertEquals(1.4142135623730951, c5)
    }

    @Test
    fun arg() {
        val c1 = Complex.ZERO.sqrt()
        Assertions.assertEquals(0.0, c1.real)
        Assertions.assertEquals(0.0, c1.imag)

        val c2 = Complex.ONE.sqrt()
        Assertions.assertEquals(1.0, c2.real)
        Assertions.assertEquals(0.0, c2.imag)

        val c3 = Complex.I.sqrt()
        Assertions.assertEquals(0.7071067811865476, c3.real)
        Assertions.assertEquals(0.7071067811865475, c3.imag)

        val c4 = Complex.valueOf(0, -1).sqrt()
        Assertions.assertEquals(0.7071067811865476, c4.real)
        Assertions.assertEquals(-0.7071067811865475, c4.imag)

        val c5 = Complex.valueOf(1, -1).sqrt()
        Assertions.assertEquals(1.0986841134678098, c5.real)
        Assertions.assertEquals(-0.45508986056222733, c5.imag)
    }

    @Test
    fun sqrt() {
        val c1 = Complex.ZERO.sqrt()
        Assertions.assertEquals(0.0, c1.real)
        Assertions.assertEquals(0.0, c1.imag)

        val c2 = Complex.ONE.sqrt()
        Assertions.assertEquals(1.0, c2.real)
        Assertions.assertEquals(0.0, c2.imag)

        val c3 = Complex.I.sqrt()
        Assertions.assertEquals(0.7071067811865476, c3.real)
        Assertions.assertEquals(0.7071067811865475, c3.imag)

        val c4 = Complex.valueOf(0, -1).sqrt()
        Assertions.assertEquals(0.7071067811865476, c4.real)
        Assertions.assertEquals(-0.7071067811865475, c4.imag)

        val c5 = Complex.valueOf(1, -1).sqrt()
        Assertions.assertEquals(1.0986841134678098, c5.real)
        Assertions.assertEquals(-0.45508986056222733, c5.imag)
    }

    @Test
    fun chs() {
        val c1 = Complex.ZERO.chs()
        Assertions.assertEquals(-0.0, c1.real)
        Assertions.assertEquals(-0.0, c1.imag)

        val c2 = Complex.ONE.chs()
        Assertions.assertEquals(-1.0, c2.real)
        Assertions.assertEquals(-0.0, c2.imag)

        val c3 = Complex.I.chs()
        Assertions.assertEquals(-0.0, c3.real)
        Assertions.assertEquals(-1.0, c3.imag)

        val c4 = Complex.valueOf(0, -1).chs()
        Assertions.assertEquals(-0.0, c4.real)
        Assertions.assertEquals(1.0, c4.imag)

        val c5 = Complex.valueOf(1, -1).chs()
        Assertions.assertEquals(-1.0, c5.real)
        Assertions.assertEquals(1.0, c5.imag)
    }

    @Test
    fun multInv() {
        val c1 = Complex.ZERO.multInv()
        Assertions.assertFalse(c1.real == c1.real) // NaN
        Assertions.assertFalse(c1.imag == c1.imag) // NaN

        val c2 = Complex.ONE.multInv()
        Assertions.assertEquals(1.0, c2.real)
        Assertions.assertEquals(-0.0, c2.imag)

        val c3 = Complex.I.multInv()
        Assertions.assertEquals(0.0, c3.real)
        Assertions.assertEquals(-1.0, c3.imag)

        val c4 = Complex.valueOf(0, -1).multInv()
        Assertions.assertEquals(0.0, c4.real)
        Assertions.assertEquals(1.0, c4.imag)

        val c5 = Complex.valueOf(1, -1).multInv()
        Assertions.assertEquals(0.4999999999999999, c5.real)
        Assertions.assertEquals(0.4999999999999999, c5.imag)
    }

    @Test
    fun conj() {
        val c1 = Complex.ZERO.conj()
        Assertions.assertEquals(0.0, c1.real)
        Assertions.assertEquals(-0.0, c1.imag)

        val c2 = Complex.ONE.conj()
        Assertions.assertEquals(1.0, c2.real)
        Assertions.assertEquals(-0.0, c2.imag)

        val c3 = Complex.I.conj()
        Assertions.assertEquals(0.0, c3.real)
        Assertions.assertEquals(-1.0, c3.imag)

        val c4 = Complex.valueOf(0, -1).conj()
        Assertions.assertEquals(0.0, c4.real)
        Assertions.assertEquals(1.0, c4.imag)

        val c5 = Complex.valueOf(1, -1).conj()
        Assertions.assertEquals(1.0, c5.real)
        Assertions.assertEquals(1.0, c5.imag)
    }

    @Test
    fun abs() {
        val c1 = Complex.ZERO.abs()
        Assertions.assertEquals(0.0, c1)

        val c2 = Complex.ONE.abs()
        Assertions.assertEquals(1.0, c2)

        val c3 = Complex.I.abs()
        Assertions.assertEquals(1.0, c3)

        val c4 = Complex.valueOf(0, -1).abs()
        Assertions.assertEquals(1.0, c4)

        val c5 = Complex.valueOf(2, -2).abs()
        Assertions.assertEquals(kotlin.math.sqrt(8.0), c5)
    }

    @Test
    fun negate() {
        val c1 = Complex.ZERO.negate()
        Assertions.assertEquals(-0.0, c1.real)
        Assertions.assertEquals(0.0, c1.imag)

        val c2 = Complex.ONE.negate()
        Assertions.assertEquals(-1.0, c2.real)
        Assertions.assertEquals(0.0, c2.imag)

        val c3 = Complex.I.negate()
        Assertions.assertEquals(-0.0, c3.real)
        Assertions.assertEquals(1.0, c3.imag)

        val c4 = Complex.valueOf(0, -1).negate()
        Assertions.assertEquals(-0.0, c4.real)
        Assertions.assertEquals(-1.0, c4.imag)

        val c5 = Complex.valueOf(1, -1).negate()
        Assertions.assertEquals(-1.0, c5.real)
        Assertions.assertEquals(-1.0, c5.imag)
    }

    @Test
    fun toByte() {
        Assertions.assertEquals(0.toByte(), Complex.ZERO.toByte())
        Assertions.assertEquals(1.toByte(), Complex.ONE.toByte())
        Assertions.assertEquals((-1).toByte(), Complex.valueOf(-1).toByte())
        Assertions.assertThrows(UnsupportedOperationException::class.java) { Complex.I.toByte() }
    }

    @Test
    fun toShort() {
        Assertions.assertEquals(0.toShort(), Complex.ZERO.toShort())
        Assertions.assertEquals(1.toShort(), Complex.ONE.toShort())
        Assertions.assertEquals((-1).toShort(), Complex.valueOf(-1).toShort())
        Assertions.assertThrows(UnsupportedOperationException::class.java) { Complex.I.toShort() }
    }

    @Test
    fun toInt() {
        Assertions.assertEquals(0, Complex.ZERO.toInt())
        Assertions.assertEquals(1, Complex.ONE.toInt())
        Assertions.assertEquals((-1), Complex.valueOf(-1).toInt())
        Assertions.assertThrows(UnsupportedOperationException::class.java) { Complex.I.toInt() }
    }

    @Test
    fun toLong() {
        Assertions.assertEquals(0.toLong(), Complex.ZERO.toLong())
        Assertions.assertEquals(1.toLong(), Complex.ONE.toLong())
        Assertions.assertEquals((-1).toLong(), Complex.valueOf(-1).toLong())
        Assertions.assertThrows(UnsupportedOperationException::class.java) { Complex.I.toLong() }
    }

    @Test
    fun toFloat() {
        Assertions.assertEquals(0.toFloat(), Complex.ZERO.toFloat())
        Assertions.assertEquals(1.toFloat(), Complex.ONE.toFloat())
        Assertions.assertEquals((-1).toFloat(), Complex.valueOf(-1).toFloat())
        Assertions.assertThrows(UnsupportedOperationException::class.java) { Complex.I.toFloat() }
    }

    @Test
    fun toDouble() {
        Assertions.assertEquals(0.0, Complex.ZERO.toDouble())
        Assertions.assertEquals(1.0, Complex.ONE.toDouble())
        Assertions.assertEquals(-1.0, Complex.valueOf(-1).toDouble())
        Assertions.assertThrows(UnsupportedOperationException::class.java) { Complex.I.toByte() }
    }

    @Test
    fun toBigInteger() {
        Assertions.assertEquals(BigInteger.ZERO, Complex.ZERO.toBigInteger())
        Assertions.assertEquals(BigInteger.ONE, Complex.ONE.toBigInteger())
        Assertions.assertEquals(BigInteger.ONE.negate(), Complex.valueOf(-1).toBigInteger())
        Assertions.assertThrows(UnsupportedOperationException::class.java) { Complex.I.toBigInteger() }
    }

    @Test
    fun toBigDecimal() {
        Assertions.assertEquals(BigDecimal.valueOf(0.0), Complex.ZERO.toBigDecimal())
        Assertions.assertEquals(BigDecimal.valueOf(1.0), Complex.ONE.toBigDecimal())
        Assertions.assertEquals(BigDecimal.valueOf(-1.0), Complex.valueOf(-1).toBigDecimal())
        Assertions.assertThrows(UnsupportedOperationException::class.java) { Complex.I.toBigDecimal() }
    }

    @Test
    fun testToString() {
    }

    @Test
    fun testEquals() {
        Assertions.assertEquals(Complex.ZERO, Complex.ZERO)
        Assertions.assertEquals(Complex.ONE, Complex.ONE)
        Assertions.assertEquals(Complex.I, Complex.I)
        Assertions.assertEquals(Complex.ONE, Complex.valueOf(1, 0))

        Assertions.assertTrue(Complex.ONE.equals(1.toByte()))
        Assertions.assertTrue(Complex.ONE.equals(1.toShort()))
        Assertions.assertTrue(Complex.ONE.equals(1))
        Assertions.assertTrue(Complex.ONE.equals(1L))
        Assertions.assertTrue(Complex.ONE.equals(1.0.toFloat()))
        Assertions.assertTrue(Complex.ONE.equals(1.0))
        Assertions.assertTrue(Complex.ONE.equals(Ratio.valueOf(1)))
        Assertions.assertTrue(Complex.ONE.equals(BigInteger.ONE))
        Assertions.assertTrue(Complex.ONE.equals(BigDecimal.ONE))
    }

    @Test
    fun testHashCode() {
    }

    @Test
    fun getReal() {
        val c1 = Complex.ZERO
        val (r1, _) = c1
        Assertions.assertEquals(0.0, c1.real)
        Assertions.assertEquals(0.0, r1)

        val c2 = Complex.valueOf(5)
        val (r2, _) = c2
        Assertions.assertEquals(5.0, c2.real)
        Assertions.assertEquals(5.0, r2)

        val c3 = Complex.valueOf(-5, 3)
        val (r3, _) = c3
        Assertions.assertEquals(-5.0, c3.real)
        Assertions.assertEquals(-5.0, r3)

        val c4 = Complex.I
        val (r4, _) = c4
        Assertions.assertEquals(0.0, c4.real)
        Assertions.assertEquals(0.0, r4)
    }

    @Test
    fun getImag() {
        val c1 = Complex.ZERO
        val (_, i1) = c1
        Assertions.assertEquals(0.0, c1.imag)
        Assertions.assertEquals(0.0, i1)

        val c2 = Complex.valueOf(5)
        val (_, i2) = c2
        Assertions.assertEquals(0.0, c2.imag)
        Assertions.assertEquals(0.0, i2)

        val c3 = Complex.valueOf(-5, 3)
        val (_, i3) = c3
        Assertions.assertEquals(3.0, c3.imag)
        Assertions.assertEquals(3.0, i3)

        val c4 = Complex.I
        val (_, i4) = c4
        Assertions.assertEquals(1.0, c4.imag)
        Assertions.assertEquals(1.0, i4)
    }

    @Test
    fun isReal() {
        Assertions.assertTrue(Complex.ZERO.isReal)
        Assertions.assertTrue(Complex.valueOf(5).isReal)
        Assertions.assertFalse(Complex.valueOf(-5, 3).isReal)
        Assertions.assertFalse(Complex.I.isReal)
    }
}
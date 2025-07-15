package kleinert.soap.data

import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import kotlin.math.absoluteValue

class Ratio private constructor(var num: Long, val den: Long) : Number(), Comparable<Number> {
    companion object {
        val ZERO: Ratio = Ratio(0, 1)

        fun valueOf(numerator1: Long, denominator1: Long = 1L): Ratio {
            var numerator = numerator1
            var denominator = denominator1

            if (denominator == 0L)
                throw NumberFormatException("kleinert.soap.Ratio with 0 denominator.")

            if (denominator < 0L) {
                // Convert `a/-b` to `-a/b` or `-a/-b` to `a/b`.
                numerator = -numerator
                denominator = -denominator
            }

            // reduce fraction
            if (denominator != 1L) {
                require(denominator > 1)
                val g = gcd(numerator.absoluteValue, denominator)
                numerator /= g
                denominator /= g
            }

            return Ratio(numerator, denominator)
        }

        fun valueOf(numerator: Int, denominator: Int): Ratio =
            valueOf(numerator.toLong(), denominator.toLong())

        fun valueOf(n: BigInteger): Ratio = valueOf(n.toLong(), 1)
        fun valueOf(n: Int): Ratio = valueOf(n.toLong(), 1)
        fun valueOf(n: Double): Ratio = TODO()

        fun valueOf(s: String): Ratio {
            val divIndex = s.indexOf('/')
            val part1 = s.substring(0, divIndex).toLongOrNull()
                ?: throw NumberFormatException("Illegal format for rational number $s.")
            val part2 = s.substring(divIndex + 1).toLongOrNull()
                ?: throw NumberFormatException("Illegal format for rational number $s.")
            return valueOf(part1, part2)
        }

        fun valueOfOrNull(s: String): Ratio? {
            val divIndex = s.indexOf('/')
            val part1 = s.substring(0, divIndex).toLongOrNull() ?: return null
            val part2 = s.substring(divIndex + 1).toLongOrNull() ?: return null
            return valueOf(part1, part2)
        }

        private fun gcd(m: Long, n: Long): Long =
            if (n == 0L) m else gcd(n, m % n)


        private fun lcm(m1: Long, n1: Long): Long {
            val m = if (m1 < 0) -m1 else m1
            val n = if (n1 < 0) -n1 else n1
            return m * (n / gcd(m, n)) // parentheses important to avoid overflow
        }
    }

    init {
        if (den == 0L) throw IllegalArgumentException("Ratio with 0 denominator.")
    }

    operator fun component1() = num
    operator fun component2() = den


    override fun toString(): String = "$num/$den"

    override fun toByte(): Byte = toLong().toByte()
    override fun toShort(): Short = toLong().toShort()
    override fun toInt(): Int = toLong().toInt()
    override fun toLong(): Long = num / den
    override fun toFloat(): Float = toDouble().toFloat()
    override fun toDouble(): Double = num.toDouble() / den.toDouble()

    fun toBigDecimal(mc: MathContext? = MathContext.UNLIMITED): BigDecimal =
        BigDecimal(num, mc).divide(BigDecimal(den, mc))

    fun toBigInteger(): BigInteger = BigInteger.valueOf(num).divide(BigInteger.valueOf(den))

    // return |a|
    fun abs(): Ratio = if (num >= 0) this else negate()

    // return (b, a)
    fun reciprocal() = Ratio(den, num)

    fun negate(): Ratio = Ratio(-num, den)
    operator fun unaryMinus() = negate()

    // return a + b, staving off overflow
    operator fun plus(b: Ratio): Ratio {
        val a: Ratio = this

        // special cases
        if (a.compareTo(ZERO) == 0) return b
        if (b.compareTo(ZERO) == 0) return a

        // Find gcd of numerators and denominators
        val f = gcd(a.num.absoluteValue, b.num)
        val g = gcd(a.den.absoluteValue, b.den)

        // add cross-product terms for numerator
        val s = Ratio(a.num / f * (b.den / g) + b.num / f * (a.den / g), lcm(a.den, b.den))

        // multiply back in
        s.num *= f
        return s
    }

    operator fun minus(b: Ratio): Ratio =
        this.plus(b.negate())

    operator fun times(b: Ratio): Ratio {
        val c = valueOf(this.num, b.den)
        val d = valueOf(b.num, this.den)
        return Ratio(c.num * d.num, c.den * d.den)
    }

    operator fun div(b: Ratio): Ratio =
        this.times(b.reciprocal())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Number) return false

        return when (other) {
            is Ratio -> num == other.num && den == other.den

            is Byte, is Short, is Int, is Long -> den == 1L && num == other.toLong()
            is BigInteger -> den == 1L && num.toBigInteger() == other
            is Float, is Double -> toDouble() == other.toDouble()
            is BigDecimal -> toBigDecimal() == other
            is Complex -> other.isReal && equals(other.real)

            else -> false
        }
    }

    override operator fun compareTo(other: Number): Int = when (other) {
        is Byte, is Short, is Int, is Long, is Float, is Double ->
            (num.toDouble() / den.toDouble()).compareTo(other.toDouble())

        is BigInteger -> toBigDecimal().compareTo(BigDecimal(other))
        is BigDecimal -> toBigDecimal().compareTo(other)

        is Ratio -> {
            val a: Ratio = this
            val lhs = a.num * other.den
            val rhs = a.den * other.num

            if (lhs < rhs) -1
            else if (lhs > rhs) +1
            else 0
        }

        is Complex -> {
            if (!other.isReal) throw IllegalArgumentException()
            toDouble().compareTo(other.real)
        }

        else -> throw IllegalArgumentException()
    }

    override fun hashCode(): Int {
        var result = num.hashCode()
        result = 31 * result + den.hashCode()
        return result
    }
}

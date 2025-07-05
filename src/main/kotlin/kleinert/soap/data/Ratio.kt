package kleinert.soap.data

import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext

data class Ratio private constructor(var num: Long, val den: Long) : Number() {
    companion object {
        val ZERO: Ratio = Ratio(0, 1)

        fun valueOf(numerator1: Long, denominator1: Long=1L): Ratio {
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
            val g = gcd(numerator, denominator)
            numerator /= g
            denominator /= g

            return Ratio(numerator, denominator)
        }

        fun valueOf(numerator: Int, denominator: Int): Ratio =
            Ratio(numerator.toLong(), denominator.toLong())

        fun valueOf(n: BigInteger): Ratio = Ratio(n.toLong(), 1)
        fun valueOf(n: Int): Ratio = Ratio(n.toLong(), 1)

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

        private fun gcd(m1: Long, n1: Long): Long {
            val m = if (m1 < 0) -m1 else m1
            val n = if (n1 < 0) -n1 else n1
            return if ( n==0L) m else gcd(n, m % n)
        }

        private fun lcm(m1: Long, n1: Long): Long {
            val m = if (m1 < 0) -m1 else m1
            val n = if (n1 < 0) -n1 else n1
            return m * (n / gcd(m, n)) // parentheses important to avoid overflow
        }
    }

    init {
        if (den == 0L) throw NumberFormatException("kleinert.soap.Ratio with 0 denominator.")
    }

    override fun toByte(): Byte = toInt().toByte()

    override fun toString(): String = "$num/$den"

    override fun toInt(): Int = this.toDouble().toInt()

    override fun toLong(): Long = toBigInteger().toLong()

    override fun toShort(): Short = toInt().toShort()

    override fun toFloat(): Float = this.toDouble().toFloat()

    override fun toDouble(): Double = toBigDecimal(MathContext.DECIMAL64).toDouble()

    fun toBigDecimal(mc: MathContext? = MathContext.UNLIMITED): BigDecimal =
        BigDecimal(num, mc) / BigDecimal(den, mc)

    fun toBigInteger(): BigInteger = BigInteger.valueOf(num).divide(BigInteger.valueOf(den))

    operator fun compareTo(b: Ratio): Int {
        val a: Ratio = this
        val lhs = a.num * b.den
        val rhs = a.den * b.num
        if (lhs < rhs) return -1
        return if (lhs > rhs) +1 else 0
    }

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
        val f = gcd(a.num, b.num)
        val g = gcd(a.den, b.den)

        // add cross-product terms for numerator
        val s = Ratio(
            a.num / f * (b.den / g) + b.num / f * (a.den / g),
            lcm(a.den, b.den)
        )

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
}

import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext

data class Ratio private constructor(var num: BigInteger, val den: BigInteger) : Number() {

    companion object {
        private val ZERO: Ratio = Ratio(BigInteger.ZERO, BigInteger.ONE)

        fun of(numerator1: BigInteger, denominator1: BigInteger): Ratio {
            var numerator = numerator1
            var denominator = denominator1

            if (denominator == BigInteger.ZERO)
                throw NumberFormatException("Ratio with 0 denominator.")

            if (denominator < BigInteger.ZERO) {
                // Convert `a/-b` to `-a/b` or `-a/-b` to `a/b`.
                numerator = numerator.negate()
                denominator = denominator.negate()
            }

            // reduce fraction
            val g = gcd(numerator, denominator)
            numerator /= g
            denominator /= g

            return Ratio(numerator, denominator)
        }

        fun of(numerator: Int, denominator: Int): Ratio = Ratio(numerator.toBigInteger(), denominator.toBigInteger())
        fun of(numerator: Long, denominator: Long): Ratio = Ratio(numerator.toBigInteger(), denominator.toBigInteger())
        fun of(n: BigInteger): Ratio = Ratio(n, BigInteger.ONE)
        fun of(n: Int): Ratio = Ratio(n.toBigInteger(), BigInteger.ONE)
        fun of(n: Long): Ratio = Ratio(n.toBigInteger(), BigInteger.ONE)

        private fun gcd(m1: BigInteger, n1: BigInteger): BigInteger {
            val m = if (m1 < BigInteger.ZERO) -m1 else m1
            val n = if (n1 < BigInteger.ZERO) -n1 else n1
            return if (BigInteger.ZERO == n) m else gcd(n, m % n)
        }
        private fun lcm(m1: BigInteger, n1: BigInteger): BigInteger {
            val m = if (m1 < BigInteger.ZERO) -m1 else m1
            val n = if (n1 < BigInteger.ZERO) -n1 else n1
            return m * (n / gcd(m, n)) // parentheses important to avoid overflow
        }
    }

    init {
        if (den == BigInteger.ZERO) throw NumberFormatException("Ratio with 0 denominator.")
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

    fun toBigInteger(): BigInteger = num.divide(den)

    operator fun compareTo(b: Ratio): Int {
        val a: Ratio = this
        val lhs = a.num * b.den
        val rhs = a.den * b.num
        if (lhs < rhs) return -1
        return if (lhs > rhs) +1 else 0
    }

    // return |a|
    fun abs(): Ratio = if (num >= BigInteger.ZERO) this else negate()

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
        val c = Ratio.of(this.num, b.den)
        val d = Ratio.of(b.num, this.den)
        return Ratio(c.num * d.num, c.den * d.den)
    }

    operator fun div(b: Ratio): Ratio =
        this.times(b.reciprocal())
}

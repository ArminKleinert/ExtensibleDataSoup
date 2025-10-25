package kleinert.edn.data

import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import kotlin.math.abs
import kotlin.math.absoluteValue

/**
 * @property num
 * @property den
 *
 * @author Armin Kleinert
 */
data class Ratio (val num: Long, val den: Long) : Number(), Comparable<Ratio> {
    companion object {
        val ZERO: Ratio = valueOf(0, 1)

        /**
         * @throws [IllegalArgumentException] if the [denominator] is zero.
         */
        fun valueOf(numerator: Long, denominator: Long = 1L): Ratio {
            var numerator1 = numerator
            var denominator1 = denominator

            if (denominator1 == 0L)
                throw IllegalArgumentException("Ratio with 0 denominator.")

            if (denominator1 < 0L) {
                // Convert `a/-b` to `-a/b` or `-a/-b` to `a/b`.
                numerator1 = -numerator1
                denominator1 = -denominator1
            }

            // reduce fraction
            if (denominator1 != 1L) {
                require(denominator1 > 1)
                val g = gcd(numerator1.absoluteValue, denominator1)
                numerator1 /= g
                denominator1 /= g
            }

            return Ratio(numerator1, denominator1)
        }

        fun valueOf(numerator: Int, denominator: Int = 1): Ratio =
            valueOf(numerator.toLong(), denominator.toLong())

        fun valueOf(n: BigInteger): Ratio =
            valueOf(n.toLong(), 1)

        fun valueOf(x: Double, epsilon: Double = 1E-10): Ratio =
            estimate(x, epsilon)

        fun valueOfOrNull(s: String): Ratio? {
            val parts = s.split('/')
            if (parts.isEmpty() || parts.size > 2)
                return null

            val num = parts[0].toLongOrNull() ?: return null
            val den = if (parts.size == 2) parts[1].toLongOrNull() ?: return null else 1L
            return valueOf(num, den)
        }

        @Throws(NumberFormatException::class)
        fun valueOf(s: String): Ratio {
            return valueOfOrNull(s) ?: throw NumberFormatException("Illegal format for rational number $s.")
        }

        /**
         * Approximate rational number for a double.
         *
         * @param x
         * @param epsilon
         */
        fun estimate(x: Double, epsilon: Double = 1E-10): Ratio {
            val sign = if (x < 0.0) -1 else 1
            val xAbs = x.absoluteValue
            var leftNum = 0L
            var leftDen = 1L
            var rightNum = 1L
            var rightDen = 0L
            var bestNum = 0L // = leftNum
            var bestDen = leftDen
            var bestError = abs(xAbs)

            // do Stern-Brocot binary search
            while (bestError > epsilon) {

                // compute next possible rational approximation
                val mediantNum: Long = leftNum + rightNum
                val mediantDen: Long = leftDen + rightDen
                val mediantDouble = mediantNum / mediantDen.toDouble()

                if (xAbs < mediantDouble) { // go left
                    rightNum = mediantNum
                    rightDen = mediantDen
                } else {
                    // go right
                    leftNum = mediantNum
                    leftDen = mediantDen
                }

                // check if better and update champion
                val error: Double = abs(mediantDouble - xAbs)
                if (error < bestError) {
                    bestNum = mediantNum
                    bestDen = mediantDen
                    bestError = error
                    //print("$bestNum/$bestDen ")
                }
            }

            return valueOf(sign * bestNum, bestDen)
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

    override fun toString(): String = "$num/$den"

    /**
     * @return [num]/[den], rounded down to a Byte.
     */
    override fun toByte(): Byte = toLong().toByte()
    /**
     * @return [num]/[den], rounded down to a Short.
     */
    override fun toShort(): Short = toLong().toShort()
    /**
     * @return [num]/[den], rounded down to an Int.
     */
    override fun toInt(): Int = toLong().toInt()
    /**
     * @return [num]/[den], rounded down to a Long.
     */
    override fun toLong(): Long = num / den
    /**
     * @return [num]/[den] as a Float. Rounding errors are to be expected.
     */
    override fun toFloat(): Float = toDouble().toFloat()
    /**
     * @return [num]/[den] as a Float. Rounding errors are to be expected.
     */
    override fun toDouble(): Double = num.toDouble() / den.toDouble()

    /**
     * @return [num]/[den] as a [BigDecimal]. Rounding errors are to be expected.
     */
    fun toBigDecimal(mc: MathContext? = MathContext.UNLIMITED): BigDecimal =
        BigDecimal(num, mc).divide(BigDecimal(den, mc))

    /**
     * @return [num]/[den], rounded down as a [BigInteger].
     */
    fun toBigInteger(): BigInteger = BigInteger.valueOf(num).divide(BigInteger.valueOf(den))

    fun mediant(s: Ratio): Ratio {
        return valueOf(num + s.num, den + s.den)
    }

    /**
     * @return |[num]|
     */
    fun abs(): Ratio = if (num >= 0) this else negate()

    // return (b, a)
    /**
     * @return [den]/[num] as a [Ratio].
     */
    fun reciprocal() = valueOf(den, num)

    /**
     * Negates the number.
     */
    fun negate(): Ratio = valueOf(-num, den)
    /**
     * Negates the number.
     */
    operator fun unaryMinus() = negate()

    /**
     * @return The sum of this number and [b], staving off overflow.
     */
    operator fun plus(b: Ratio): Ratio {
        val a: Ratio = this

        // special cases
        if (a.compareTo(ZERO) == 0) return b
        if (b.compareTo(ZERO) == 0) return a

        // Find gcd of numerators and denominators
        val f = gcd(a.num.absoluteValue, b.num)
        val g = gcd(a.den.absoluteValue, b.den)

        // add cross-product terms for numerator
        val s = valueOf(a.num / f * (b.den / g) + b.num / f * (a.den / g), lcm(a.den, b.den))

        // multiply back in
        return valueOf(s.num * f, s.den)
    }
    operator fun plus(b: Int): Ratio {
        val s = b * den
        return valueOf(num+s, den)
    }
    operator fun plus(b: Long): Ratio {
        val s = b * den
        return valueOf(num+s, den)
    }

    /**
     * @return Subtract [b] from this number. Equivalent to adding (-[b]).
     */
    operator fun minus(b: Ratio): Ratio =
        this.plus(b.negate())
    operator fun minus(b: Int): Ratio =
        this.plus(-b)
    operator fun minus(b: Long): Ratio =
        this.plus(-b)

    /**
     * @return this number multiplied with [b].
     */
    operator fun times(b: Ratio): Ratio {
        val c = valueOf(this.num, b.den)
        val d = valueOf(b.num, this.den)
        return valueOf(c.num * d.num, c.den * d.den)
    }
    operator fun times(b: Int): Ratio {
        return valueOf(num*b, den)
    }
    operator fun times(b:Long): Ratio {
        return valueOf(num*b, den)
    }
    /**
     * @return this number divided by [b].
     */
    operator fun div(b: Ratio): Ratio =
        this.times(b.reciprocal())

    /**
     * Comparison with various [Number] subtypes. Works for [Byte], [Short], [Int], [Long], [Float], [Double], [BigInteger], [BigDecimal], and [Ratio].
     */
    @Throws(IllegalArgumentException::class)
    override operator fun compareTo(other: Ratio): Int {val a: Ratio = this
        val lhs = a.num * other.den
        val rhs = a.den * other.num

        if (lhs < rhs) return -1
         if (lhs > rhs) return +1
        return 0}
}

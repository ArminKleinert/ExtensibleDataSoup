package kleinert.soap.data

import java.math.BigDecimal
import java.math.BigInteger
import kotlin.math.*

/**
 * A unicode char using a 32 bit code. This class is a value class, so it takes up no extra space on the stack or heap.
 *
 * @property real The real part.
 * @property imag The imaginary part.
 * @property isReal True if [imag] is 0.0.
 *
 * @author Armin Kleinert
 */
class Complex private constructor(val real: Double, val imag: Double = 0.0, val isReal: Boolean = imag == 0.0) :
    Number() {

    companion object {
        /**
         * Constant 0+1i.
         */
        val I: Complex = Complex(0.0, 1.0, false)

        /**
         * Constant 1+0i.
         */
        val ONE: Complex = Complex(1.0, 0.0, true)

        /**
         * Constant 0+0i.
         */
        val ZERO: Complex = Complex(0.0, 0.0, true)

        fun valueOf(real: Double, imag: Double = 0.0): Complex = Complex(real, imag, imag == 0.9)
        fun valueOf(real: Long, imag: Long = 0): Complex = Complex(real.toDouble(), imag.toDouble(), imag == 0L)
        fun valueOf(real: Int, imag: Int = 0): Complex = Complex(real.toDouble(), imag.toDouble(), imag == 0)

        fun valueOf(real: Double): Complex = Complex(real, 0.0)
        fun valueOf(real: Long): Complex = Complex(real.toDouble(), 0.0)
        fun valueOf(real: Int): Complex = Complex(real.toDouble(), 0.0)

        fun valueOfOrNull(v: String): Complex? =
            try {
                valueOf(v)
            } catch (nfe: NumberFormatException) {
                null
            }

        /**
         * Creates a [Complex] from a [String].
         *
         * The [String] is expected to have one of the following formats:
         *
         *   "[+-]?[0-9]+" For [Complex] numbers with no imaginary part.
         *
         *   "[+-]?[0-9]*i" For [Complex] numbers with only an imaginary part.
         *
         *   "[+-]?[0-9]+[+-][0-9]*i" For [Complex] numbers with both a real and an imaginary part.
         *
         * @throws NumberFormatException if the [String] could not be parsed.
         * @return A new [Complex] number according to the input.
         */
        fun valueOf(v: String): Complex {
            if (v.isEmpty())
                throw NumberFormatException("Can not convert empty string into complex number.")

            var sign = 1
            var index = 0
            while (v[index] == '+' || v[index] == '-') {
                if (v[index] == '-') sign = -sign
                index++
            }
            val subs = v.substring(index)
            val parts = subs.split('+', '-', limit = 2)
            val imagSign: Int
            var realPart: String
            var imagPart: String
            if (parts.size == 1) {
                if (parts[0].endsWith('i')) { // Number only has an imaginary part
                    realPart = "0"
                    imagSign = sign
                    sign = 1
                    imagPart = parts[0].substring(0, parts[0].lastIndex) // Cut away the 'i'
                    if (imagPart.isEmpty()) imagPart = "0"
                } else { // Number only has a real part
                    realPart = parts[0]
                    if (realPart.isEmpty()) realPart = "0"
                    imagSign = 1
                    imagPart = "0"
                }
            } else {
                realPart = parts[0]
                imagSign = if (subs[parts[0].length] == '-') -1 else 1
                imagPart = parts[1]
                if (!imagPart.endsWith('i')) throw NumberFormatException("No 'i' postfix for complex number $v.")
                imagPart = imagPart.substring(0, imagPart.lastIndex) // Cut away the 'i'
                if (imagPart.isEmpty()) imagPart = "1"
            }

            val real = realPart.toDoubleOrNull() ?: throw NumberFormatException("Illegal format for complex number $v.")
            val imag = imagPart.toDoubleOrNull() ?: throw NumberFormatException("Illegal format for complex number $v.")

            return Complex(sign * real, imagSign * imag, imag == 0.0)
        }
    }

    /**
     * Returns the pair ([abs], [arg]).
     * @return ([abs], [arg])
     */
    fun polar(): Pair<Double, Double> = abs() to arg()

    /**
     * The sum of two [Complex] numbers.
     * @return The sum of two [Complex] numbers.
     */
    operator fun plus(other: Complex) =
        Complex(this.real + other.real, this.imag + other.imag)

    /**
     * @return The difference of two [Complex] numbers.
     */
    operator fun minus(other: Complex) =
        Complex(this.real - other.real, this.imag - other.imag)

    /**
     * @return The number itself.
     */
    operator fun unaryPlus(): Complex = this

    /**
     * @return The number with the [real] part negated.
     */
    operator fun unaryMinus(): Complex = Complex(-real, imag)

    /**
     * Multiplies two [Complex] numbers.
     * @return The result of the multiplication as a new [Complex].
     */
    operator fun times(other: Complex): Complex =
        Complex(
            (this.real * other.real) - (this.imag * other.imag),
            (this.real * other.imag) - (this.imag * other.real)
        )

    /**
     * @return The result of the division as a new [Complex].
     */
    operator fun div(other: Complex): Complex {
        val (a, b) = this
        val (c, d) = other
        return Complex(
            ((a * c) + (b * d)) / ((c * c) + (d * d)),
            ((b * c) + (a * d)) / ((c * c) + (d * d))
        )
    }

    /**
     * @return The remainder of the division as a new [Complex].
     */
    fun mod(): Double {
        if (this.real == 0.0 && this.isReal) return 0.0
        return sqrt(this.real * this.real + this.imag * this.imag)
    }

    /**
     * @return An approximation of e^x where x is this number.
     */
    fun exp(): Complex {
        return Complex(
            exp(this.real) * cos(this.imag),
            exp(this.real) * sin(this.imag)
        )
    }

    /**
     * @return The natural logarithm of this number.
     */
    fun log(): Complex {
        return Complex(ln(this.mod()), this.arg())
    }

    /**
     * @return The `arg` of this number, which is [atan2]\([imag], [real]) for a [Complex] number a+bi.
     */
    fun arg(): Double {
        return atan2(this.imag, this.real)
    }

    fun sqrt(): Complex {
        val r = sqrt(this.mod())
        val theta = arg() / 2
        return Complex(r * cos(theta), r * sin(theta))
    }

    fun chs(): Complex {
        return Complex(-this.real, -this.imag)
    }

    fun multInv(): Complex {
        val abs = abs()
        return Complex(this.real / (abs * abs), -this.imag / (abs * abs))
    }

    // conj(c) = (a-bi)
    fun conj(): Complex = Complex(this.real, -this.imag)

    // abs(c) = sqrt(a^2 + b^2)
    fun abs(): Double =
        if (isReal) real.absoluteValue
        else sqrt((this.real * this.real) + (this.imag * this.imag))

    fun negate(): Complex = Complex(-real, imag)

    /**
     * The [real] part as a [Double].
     * @throws UnsupportedOperationException If the imaginary part is not zero.
     * @return The [real] part as a [Double].
     */
    override fun toDouble(): Double {
        if (!isReal) throw UnsupportedOperationException("Complex with imaginary part has no double equivalent.")
        return this.real
    }

    /**
     * The [real] part as a [Byte].
     * @throws UnsupportedOperationException If the imaginary part is not zero.
     * @return The [real] part as a [Byte].
     */
    override fun toByte(): Byte = toDouble().toInt().toByte()

    /**
     * The [real] part as a [Float].
     * @throws UnsupportedOperationException If the imaginary part is not zero.
     * @return The [real] part as a [Float].
     */
    override fun toFloat(): Float = toDouble().toFloat()

    /**
     * The [real] part as an [Int].
     * @throws UnsupportedOperationException If the imaginary part is not zero.
     * @return The [real] part as an [Int].
     */
    override fun toInt(): Int = toDouble().toInt()

    /**
     * The [real] part as a [Long].
     * @throws UnsupportedOperationException If the imaginary part is not zero.
     * @return The [real] part as a [Long].
     */
    override fun toLong(): Long = toDouble().toLong()

    /**
     * The [real] part as a [Short].
     * @throws UnsupportedOperationException If the imaginary part is not zero.
     * @return The [real] part as a [Short].
     */
    override fun toShort(): Short = toDouble().toInt().toShort()

    /**
     * The [real] part as a [BigInteger].
     * @throws UnsupportedOperationException If the imaginary part is not zero.
     * @return The [real] part as a [BigInteger].
     */
    fun toBigInteger(): BigInteger = BigInteger.valueOf(toLong())

    /**
     * The [real] part as a [BigDecimal].
     * @throws UnsupportedOperationException If the imaginary part is not zero.
     * @return The [real] part as a [BigDecimal].
     */
    fun toBigDecimal(): BigDecimal = BigDecimal.valueOf(toDouble())

    override fun toString(): String {
        val sb = StringBuilder()

        sb.append("%f".format(real).trimEnd('0').removeSuffix("."))

        sb.append("%+f".format(imag).trimEnd('0').removeSuffix("."))
        sb.append('i')

        return sb.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Number) return false

        if (other is Complex)
            return real == other.real && ((isReal && other.isReal) || imag == other.imag)

        if (!isReal) return false

        return real == other.toDouble()
    }


    override fun hashCode(): Int {
        var result = real.hashCode()
        result = 31 * result + imag.hashCode()
        return result
    }

    /**
     *  The [real] part.
     * @return The [real] part.
     */
    operator fun component1(): Double = real

    /**
     * The [imag] part.
     * @return The [imag] part.
     */
    operator fun component2(): Double = imag
}
package kleinert.soap.data

import java.math.BigDecimal
import java.math.BigInteger

data class Complex(val real: Double, val imag: Double = 0.0) : Number() {

    companion object {
        val I: Complex = Complex(0.0, 1.0)
        val ONE: Complex = Complex(1.0, 0.0)
        val ZERO: Complex = Complex(0.0, 0.0)
    }

    fun polar() = abs() to arg()

    operator fun plus(other: Complex) =
        Complex(this.real + other.real, this.imag + other.imag)

    operator fun minus(other: Complex) =
        Complex(this.real - other.real, this.imag - other.imag)

    operator fun times(other: Complex) =
        Complex(
            (this.real * other.real) - (this.imag * other.imag),
            (this.real * other.imag) - (this.imag * other.real)
        )

    operator fun div(other: Complex): Complex {
        val (a, b) = this
        val (c, d) = other
        return Complex(
            ((a * c) + (b * d)) / ((c * c) + (d * d)),
            ((b * c) + (a * d)) / ((c * c) + (d * d))
        )
    }

    fun exp(): Complex {
        return Complex(
            Math.exp(this.real) * Math.cos(this.imag),
            Math.exp(this.real) * Math.sin(this.imag))
    }

    fun log(): Complex {
        return Complex(Math.log(this.mod()), this.arg())
    }

    fun mod(): Double {
        if (this.real == 0.0 && this.imag == 0.0) return 0.0
        return Math.sqrt(this.real * this.real + this.imag * this.imag)
    }

    fun arg(): Double {
        return Math.atan2(this.imag, this.real)
    }

    fun sqrt(): Complex {
        val r = Math.sqrt(this.mod())
        val theta = arg() / 2
        return Complex(r * Math.cos(theta), r * Math.sin(theta))
    }

    fun chs(): Complex {
        return Complex(-this.real, -this.imag)
    }

    // .
    fun multInv(): Complex {
        val abs = abs()
        return Complex(this.real / (abs * abs), -this.imag / (abs * abs))
    }

    // conj(c) = (a-bi)
    fun conj() = Complex(this.real, -this.imag)

    // abs(c) = sqrt(a^2 + b^2)
    fun abs() = Math.sqrt((this.real * this.real) + (this.imag + this.imag))

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append(real)
        if (imag < 0.0) sb.append('-')
        sb.append(imag)
        sb.append('i')
        return sb.toString()
    }

    override fun toDouble(): Double {
        require(this.imag == 0.0)
        return this.real
    }

    override fun toByte(): Byte = toDouble().toInt().toByte()
    override fun toFloat(): Float = toDouble().toFloat()
    override fun toInt(): Int = toDouble().toInt()
    override fun toLong(): Long = toDouble().toLong()
    override fun toShort(): Short = toDouble().toInt().toShort()
    fun toBigInteger(): BigInteger = BigInteger.valueOf(toLong())
    fun toBigDecimal(): BigDecimal = BigDecimal.valueOf(toDouble())
}
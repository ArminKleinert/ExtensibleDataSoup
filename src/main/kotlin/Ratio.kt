import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext

data class Ratio(val numerator: BigInteger, val denominator: BigInteger) : Number() {
    override fun toByte(): Byte = toInt().toByte()

    override fun toString(): String = "$numerator/$denominator"

    override fun toInt(): Int = this.toDouble().toInt()

    override fun toLong(): Long = toBigInteger().toLong()

    override fun toShort(): Short = toInt().toShort()

    override fun toFloat(): Float = this.toDouble().toFloat()

    override fun toDouble(): Double = toBigDecimal(MathContext.DECIMAL64).toDouble()

    fun toBigDecimal(mc: MathContext? = MathContext.UNLIMITED): BigDecimal =
        BigDecimal(numerator, mc) / BigDecimal(denominator, mc)

    fun toBigInteger(): BigInteger = numerator.divide(denominator)
}

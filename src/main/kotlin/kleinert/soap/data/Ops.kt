package kleinert.soap.data

import java.math.BigDecimal
import java.math.BigInteger

class Ops {
    companion object {
        fun compare(obj: Byte, other: Number) =
            compare(obj.toInt(), other)
        fun compare(obj: Short, other: Number) =
            compare(obj.toInt(), other)

        fun compare(obj: Int, other: Number) = when (other) {
            is Byte -> obj.compareTo(other)
            is Short -> obj.compareTo(other)
            is Int -> obj.compareTo(other)
            is Long -> obj.compareTo(other)
            is Float -> obj.compareTo(other)
            is Double -> obj.compareTo(other)
            is BigInteger -> BigInteger.valueOf(obj.toLong()).compareTo(other)
            is BigDecimal -> BigDecimal.valueOf(obj.toDouble()).compareTo(other)
            is Ratio -> -compare(other, obj)
            is Complex -> -compare(other, obj)
            else -> throw IllegalArgumentException()
        }

        fun compare(obj: Long, other: Number) = when (other) {
            is Byte -> obj.compareTo(other)
            is Short -> obj.compareTo(other)
            is Int -> obj.compareTo(other)
            is Long -> obj.compareTo(other)
            is Float -> obj.compareTo(other)
            is Double -> obj.compareTo(other)
            is BigInteger -> BigInteger.valueOf(obj.toLong()).compareTo(other)
            is BigDecimal -> BigDecimal.valueOf(obj.toDouble()).compareTo(other)
            is Ratio -> -compare(other, obj)
            is Complex -> -compare(other, obj)
            else -> throw IllegalArgumentException()
        }

        fun compare(obj: Float, other: Number) =
            compare(obj.toDouble(), other)
        fun compare(obj: Double, other: Number) = when (other) {
            is Byte -> obj.compareTo(other)
            is Short -> obj.compareTo(other)
            is Int -> obj.compareTo(other)
            is Long -> obj.compareTo(other)
            is Float -> obj.compareTo(other)
            is Double -> obj.compareTo(other)
            is BigInteger -> BigInteger.valueOf(obj.toLong()).compareTo(other)
            is BigDecimal -> BigDecimal.valueOf(obj.toDouble()).compareTo(other)
            is Ratio -> -compare(other, obj)
            is Complex -> -compare(other, obj)
            else -> throw IllegalArgumentException()
        }

        fun compare(obj: BigInteger, other: Number) = when (other) {
            is Byte -> obj.compareTo(BigInteger.valueOf(other.toLong()))
            is Short -> obj.compareTo(BigInteger.valueOf(other.toLong()))
            is Int -> obj.compareTo(BigInteger.valueOf(other.toLong()))
            is Long -> obj.compareTo(BigInteger.valueOf(other.toLong()))
            is Float -> obj.compareTo(BigInteger.valueOf(other.toLong()))
            is Double -> obj.compareTo(BigInteger.valueOf(other.toLong()))
            is BigInteger -> BigInteger.valueOf(obj.toLong()).compareTo(other)
            is BigDecimal -> BigDecimal.valueOf(obj.toDouble()).compareTo(other)
            is Ratio -> -compare(other, obj)
            is Complex -> -compare(other, obj)
            else -> throw IllegalArgumentException()
        }

        fun compare(obj: BigDecimal, other: Number) = when (other) {
            is Byte -> obj.compareTo(BigDecimal.valueOf(other.toDouble()))
            is Short -> obj.compareTo(BigDecimal.valueOf(other.toDouble()))
            is Int -> obj.compareTo(BigDecimal.valueOf(other.toDouble()))
            is Long -> obj.compareTo(BigDecimal.valueOf(other.toLong()))
            is Float -> obj.compareTo(BigDecimal.valueOf(other.toDouble()))
            is Double -> obj.compareTo(BigDecimal.valueOf(other))
            is BigInteger -> BigInteger.valueOf(obj.toLong()).compareTo(other)
            is BigDecimal -> BigDecimal.valueOf(obj.toDouble()).compareTo(other)
            is Ratio -> -compare(other, obj)
            is Complex -> -compare(other, obj)
            else -> throw IllegalArgumentException()
        }

        fun compare(obj: Ratio, other: Number): Int = when (other) {
            is Byte -> (obj.toDouble()).compareTo(other.toDouble())
            is Short -> (obj.toDouble()).compareTo(other.toDouble())
            is Int -> (obj.toDouble()).compareTo(other.toDouble())
            is Long -> (obj.toDouble()).compareTo(other.toDouble())
            is Float -> (obj.toDouble()).compareTo(other.toDouble())
            is Double -> (obj.toDouble()).compareTo(other.toDouble())
            is BigInteger -> obj.toBigDecimal().compareTo(BigDecimal(other))
            is BigDecimal -> obj.toBigDecimal().compareTo(other)
            is Ratio -> obj.compareTo(other)
            is Complex -> -compare(other, obj)
            else -> throw IllegalArgumentException()
        }

        fun compare(obj: Complex, other: Number): Int {
            if (obj.imag != 0.0) throw IllegalArgumentException()
            return when (other) {
                is Byte -> obj.real.compareTo(other)
                is Short -> obj.real.compareTo(other)
                is Int -> obj.real.compareTo(other)
                is Long -> obj.real.compareTo(other)
                is Float -> obj.real.compareTo(other)
                is Double -> obj.real.compareTo(other)
                is BigInteger -> BigDecimal(obj.real).compareTo(BigDecimal(other))
                is BigDecimal -> BigDecimal(obj.real).compareTo(other)
                is Ratio -> obj.real.compareTo(other.toDouble())
                is Complex -> if (other.imag != 0.0) obj.real.compareTo(other.real) else throw IllegalArgumentException()
                else -> throw IllegalArgumentException()
            }
        }

        fun compare(obj: Number, other: Number): Int {
            return when (obj) {
                is Byte -> compare(obj.toInt(), other)
                is Short -> compare(obj.toInt(), other)
                is Int -> compare(obj, other)
                is Long -> compare(obj, other)
                is Float -> compare(obj, other)
                is Double -> compare(obj, other)
                is BigInteger -> compare(obj, other)
                is BigDecimal -> compare(obj, other)
                is Ratio -> compare(obj, other)
                is Complex -> compare(obj, other)
                else -> throw IllegalArgumentException()
            }
        }

        fun compare(obj: CharSequence, other: Any?): Int =
            if (other is CharSequence) obj.toString().compareTo(other.toString())
            else throw IllegalArgumentException()

        fun compare(obj: Any?, other: Any?): Int {
            if (obj is Number && other is Number) return compare(obj, other)
            if (obj is CharSequence) return compare(obj, other)
            throw IllegalArgumentException()
        }
    }
}
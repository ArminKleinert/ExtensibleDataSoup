package kleinert.soap.data

import java.math.BigDecimal
import java.math.BigInteger

class Ops {
    companion object {

        /* Comparisons */

        fun compare(obj: Byte, other: Number) =
            compare(obj.toInt(), other)

        fun compare(obj: Short, other: Number): Int =
            compare(obj.toInt(), other)

        fun compare(obj: Int, other: Number): Int = when (other) {
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
            is BigInteger -> BigInteger.valueOf(obj).compareTo(other)
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
            is BigDecimal -> BigDecimal.valueOf(obj).compareTo(other)
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
            if (!obj.isReal) throw IllegalArgumentException()
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
                is Complex -> if (other.isReal) obj.real.compareTo(other.real) else throw IllegalArgumentException()
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
            if (obj is Char && other is Char) return obj.compareTo(other)
            if (obj is Number && other is Number) return compare(obj, other)
            if (obj is CharSequence) return compare(obj, other)
            throw IllegalArgumentException()
        }

        /* Unary Plus */

        fun plus(obj: Byte): Number = obj
        fun plus(obj: Short): Number = obj
        fun plus(obj: Int): Number = obj
        fun plus(obj: Long): Number = obj
        fun plus(obj: Float): Number = obj
        fun plus(obj: Double): Number = obj
        fun plus(obj: BigInteger): Number = obj
        fun plus(obj: BigDecimal): Number = obj
        fun plus(obj: Ratio): Number = obj
        fun plus(obj: Complex): Number = obj
        fun plus(obj: Number): Number = obj

        /* Binary Plus */

        fun plus(obj: Byte, other: Number): Number =
            plus(obj.toInt(), other)

        fun plus(obj: Short, other: Number): Number =
            plus(obj.toInt(), other)

        fun plus(obj: Int, other: Number): Number = when (other) {
            is Byte -> obj + other
            is Short -> obj + other
            is Int -> obj + other
            is Long -> obj + other
            is Float -> obj + other
            is Double -> obj + other
            is BigInteger -> other + BigInteger.valueOf(obj.toLong())
            is BigDecimal -> other + BigDecimal.valueOf(obj.toLong())
            is Ratio -> Ratio.valueOf(obj) + other
            is Complex -> Complex.valueOf(obj) + other
            else -> throw IllegalArgumentException()
        }

        fun plus(obj: Long, other: Number): Number = when (other) {
            is Byte -> obj + other
            is Short -> obj + other
            is Int -> obj + other
            is Long -> obj + other
            is Float -> obj + other
            is Double -> obj + other
            is BigInteger -> other + BigInteger.valueOf(obj)
            is BigDecimal -> other + BigDecimal.valueOf(obj)
            is Ratio -> Ratio.valueOf(obj) + other
            is Complex -> Complex.valueOf(obj) + other
            else -> throw IllegalArgumentException()
        }

        fun plus(obj: Float, other: Number): Number = plus(obj.toDouble(), other)

        fun plus(obj: Double, other: Number): Number = when (other) {
            is Byte -> obj + other
            is Short -> obj + other
            is Int -> obj + other
            is Long -> obj + other
            is Float -> obj + other
            is Double -> obj + other
            is BigInteger -> BigDecimal.valueOf(obj) + BigDecimal(other)
            is BigDecimal -> BigDecimal.valueOf(obj) + other
            is Ratio -> obj + other.toDouble()
            is Complex -> Complex.valueOf(obj) + other
            else -> throw IllegalArgumentException()
        }

        fun plus(obj: BigInteger, other: Number): Number =
            plus(obj.toInt(), other)

        fun plus(obj: BigDecimal, other: Number): Number =
            plus(obj.toInt(), other)

        fun plus(obj: Ratio, other: Number): Number = when (other) {
            is Byte -> obj + Ratio.valueOf(other.toInt())
            is Short -> obj + Ratio.valueOf(other.toInt())
            is Int -> obj + Ratio.valueOf(other.toInt())
            is Long -> obj + Ratio.valueOf(other)
            is Float -> obj.toDouble() + other
            is Double -> obj.toDouble() + other
            is BigInteger -> obj.toBigInteger() + other
            is BigDecimal -> obj.toBigDecimal() + (other)
            is Ratio -> obj + other
            is Complex -> Complex.valueOf(obj.toDouble()) + other
            else -> throw IllegalArgumentException()
        }

        fun plus(obj: Complex, other: Number): Number = when (other) {
            is Byte -> obj + Complex.valueOf(other.toInt())
            is Short -> obj + Complex.valueOf(other.toInt())
            is Int -> obj + Complex.valueOf(other.toInt())
            is Long -> obj + Complex.valueOf(other)
            is Float -> obj.toDouble() + other
            is Double -> obj.toDouble() + other
            is BigInteger -> obj.toBigInteger() + other
            is BigDecimal -> obj.toBigDecimal() + (other)
            is Ratio -> obj + Complex.valueOf(other.toDouble())
            is Complex -> Complex.valueOf(obj.toDouble()) + other
            else -> throw IllegalArgumentException()
        }

        /* Multiply */

        fun multiply(obj: Byte, other: Number): Number =
            multiply(obj.toInt(), other)

        fun multiply(obj: Short, other: Number): Number =
            multiply(obj.toInt(), other)

        fun multiply(obj: Int, other: Number): Number = when (other) {
            is Byte -> obj * other
            is Short -> obj * other
            is Int -> obj * other
            is Long -> obj * other
            is Float -> obj * other
            is Double -> obj * other
            is BigInteger -> other * BigInteger.valueOf(obj.toLong())
            is BigDecimal -> other * BigDecimal.valueOf(obj.toLong())
            is Ratio -> Ratio.valueOf(obj) * other
            is Complex -> Complex.valueOf(obj) * other
            else -> throw IllegalArgumentException()
        }

        fun multiply(obj: Long, other: Number): Number = when (other) {
            is Byte -> obj * other
            is Short -> obj * other
            is Int -> obj * other
            is Long -> obj * other
            is Float -> obj * other
            is Double -> obj * other
            is BigInteger -> other * BigInteger.valueOf(obj)
            is BigDecimal -> other * BigDecimal.valueOf(obj)
            is Ratio -> Ratio.valueOf(obj) * other
            is Complex -> Complex.valueOf(obj) * other
            else -> throw IllegalArgumentException()
        }

        fun multiply(obj: Float, other: Number): Number = multiply(obj.toDouble(), other)

        fun multiply(obj: Double, other: Number): Number = when (other) {
            is Byte -> obj * other
            is Short -> obj * other
            is Int -> obj * other
            is Long -> obj * other
            is Float -> obj * other
            is Double -> obj * other
            is BigInteger -> BigDecimal.valueOf(obj) * BigDecimal(other)
            is BigDecimal -> BigDecimal.valueOf(obj) * other
            is Ratio -> obj * other.toDouble()
            is Complex -> Complex.valueOf(obj) * other
            else -> throw IllegalArgumentException()
        }

        fun multiply(obj: BigInteger, other: Number): Number =
            multiply(obj.toInt(), other)

        fun multiply(obj: BigDecimal, other: Number): Number =
            multiply(obj.toInt(), other)

        fun multiply(obj: Ratio, other: Number): Number = when (other) {
            is Byte -> obj * Ratio.valueOf(other.toInt())
            is Short -> obj * Ratio.valueOf(other.toInt())
            is Int -> obj * Ratio.valueOf(other.toInt())
            is Long -> obj * Ratio.valueOf(other)
            is Float -> obj.toDouble() * other
            is Double -> obj.toDouble() * other
            is BigInteger -> obj.toBigInteger() * other
            is BigDecimal -> obj.toBigDecimal() * (other)
            is Ratio -> obj * other
            is Complex -> Complex.valueOf(obj.toDouble()) * other
            else -> throw IllegalArgumentException()
        }

        fun multiply(obj: Complex, other: Number): Number = when (other) {
            is Byte -> obj * Complex.valueOf(other.toInt())
            is Short -> obj * Complex.valueOf(other.toInt())
            is Int -> obj * Complex.valueOf(other.toInt())
            is Long -> obj * Complex.valueOf(other)
            is Float -> obj.toDouble() * other
            is Double -> obj.toDouble() * other
            is BigInteger -> obj.toBigInteger() * other
            is BigDecimal -> obj.toBigDecimal() * (other)
            is Ratio -> obj * Complex.valueOf(other.toDouble())
            is Complex -> Complex.valueOf(obj.toDouble()) * other
            else -> throw IllegalArgumentException()
        }

        // Divide //

        fun divide(obj: Byte, other: Number): Number =
            divide(obj.toInt(), other)

        fun divide(obj: Short, other: Number): Number =
            divide(obj.toInt(), other)

        fun divide(obj: Int, other: Number): Number = when (other) {
            is Byte -> obj / other
            is Short -> obj / other
            is Int -> obj / other
            is Long -> obj / other
            is Float -> obj / other
            is Double -> obj / other
            is BigInteger -> other / BigInteger.valueOf(obj.toLong())
            is BigDecimal -> other / BigDecimal.valueOf(obj.toLong())
            is Ratio -> Ratio.valueOf(obj) / other
            is Complex -> Complex.valueOf(obj) / other
            else -> throw IllegalArgumentException()
        }

        fun divide(obj: Long, other: Number): Number = when (other) {
            is Byte -> obj / other
            is Short -> obj / other
            is Int -> obj / other
            is Long -> obj / other
            is Float -> obj / other
            is Double -> obj / other
            is BigInteger -> other / BigInteger.valueOf(obj)
            is BigDecimal -> other / BigDecimal.valueOf(obj)
            is Ratio -> Ratio.valueOf(obj) / other
            is Complex -> Complex.valueOf(obj) / other
            else -> throw IllegalArgumentException()
        }

        fun divide(obj: Float, other: Number): Number = divide(obj.toDouble(), other)

        fun divide(obj: Double, other: Number): Number = when (other) {
            is Byte -> obj / other
            is Short -> obj / other
            is Int -> obj / other
            is Long -> obj / other
            is Float -> obj / other
            is Double -> obj / other
            is BigInteger -> BigDecimal.valueOf(obj) / BigDecimal(other)
            is BigDecimal -> BigDecimal.valueOf(obj) / other
            is Ratio -> obj / other.toDouble()
            is Complex -> Complex.valueOf(obj) / other
            else -> throw IllegalArgumentException()
        }

        fun divide(obj: BigInteger, other: Number): Number =
            divide(obj.toInt(), other)

        fun divide(obj: BigDecimal, other: Number): Number =
            divide(obj.toInt(), other)

        fun divide(obj: Ratio, other: Number): Number = when (other) {
            is Byte -> obj / Ratio.valueOf(other.toInt())
            is Short -> obj / Ratio.valueOf(other.toInt())
            is Int -> obj / Ratio.valueOf(other.toInt())
            is Long -> obj / Ratio.valueOf(other)
            is Float -> obj.toDouble() / other
            is Double -> obj.toDouble() / other
            is BigInteger -> obj.toBigInteger() / other
            is BigDecimal -> obj.toBigDecimal() / (other)
            is Ratio -> obj / other
            is Complex -> Complex.valueOf(obj.toDouble()) / other
            else -> throw IllegalArgumentException()
        }

        fun divide(obj: Complex, other: Number): Number = when (other) {
            is Byte -> obj / Complex.valueOf(other.toInt())
            is Short -> obj / Complex.valueOf(other.toInt())
            is Int -> obj / Complex.valueOf(other.toInt())
            is Long -> obj / Complex.valueOf(other)
            is Float -> obj.toDouble() / other
            is Double -> obj.toDouble() / other
            is BigInteger -> obj.toBigInteger() / other
            is BigDecimal -> obj.toBigDecimal() / (other)
            is Ratio -> obj / Complex.valueOf(other.toDouble())
            is Complex -> Complex.valueOf(obj.toDouble()) / other
            else -> throw IllegalArgumentException()
        }

        /* Unary Minus */

        fun minus(obj: Byte): Number = -obj
        fun minus(obj: Short): Number = -obj
        fun minus(obj: Int): Number = -obj
        fun minus(obj: Long): Number = -obj
        fun minus(obj: Float): Number = -obj
        fun minus(obj: Double): Number = -obj
        fun minus(obj: BigInteger): Number = -obj
        fun minus(obj: BigDecimal): Number = -obj
        fun minus(obj: Ratio): Number = -obj
        fun minus(obj: Complex): Number = -obj
        fun minus(obj: Number): Number = when (obj) {
            is Byte -> -obj
            is Short -> -obj
            is Int -> -obj
            is Long -> -obj
            is Float -> -obj
            is Double -> -obj
            is BigInteger -> -obj
            is BigDecimal -> -obj
            is Ratio -> -obj
            is Complex -> -obj
            else -> throw IllegalArgumentException()
        }

        /* Binary Minus */

        fun minus(obj: Byte, other: Number): Number = plus(obj, minus(other))
        fun minus(obj: Short, other: Number): Number = plus(obj, minus(other))
        fun minus(obj: Int, other: Number): Number = plus(obj, minus(other))
        fun minus(obj: Long, other: Number): Number = plus(obj, minus(other))
        fun minus(obj: Float, other: Number): Number = plus(obj, minus(other))
        fun minus(obj: Double, other: Number): Number = plus(obj, minus(other))
        fun minus(obj: BigInteger, other: Number): Number = plus(obj, minus(other))
        fun minus(obj: BigDecimal, other: Number): Number = plus(obj, minus(other))
        fun minus(obj: Ratio, other: Number): Number = plus(obj, minus(other))
        fun minus(obj: Complex, other: Number): Number = plus(obj, minus(other))
        fun minus(obj: Number, other: Number): Number = when (obj) {
            is Byte -> plus(obj, minus(other))
            is Short -> plus(obj, minus(other))
            is Int -> plus(obj, minus(other))
            is Long -> plus(obj, minus(other))
            is Float -> plus(obj, minus(other))
            is Double -> plus(obj, minus(other))
            is BigInteger -> plus(obj, minus(other))
            is BigDecimal -> plus(obj, minus(other))
            is Ratio -> plus(obj, minus(other))
            is Complex -> plus(obj, minus(other))
            else -> throw IllegalArgumentException()
        }

        /* Less Than */

        fun lessThan(obj: Byte, other: Number): Boolean = compare(obj, other) < 0
        fun lessThan(obj: Short, other: Number): Boolean = compare(obj, other) < 0
        fun lessThan(obj: Int, other: Number): Boolean = compare(obj, other) < 0
        fun lessThan(obj: Long, other: Number): Boolean = compare(obj, other) < 0
        fun lessThan(obj: Float, other: Number): Boolean = compare(obj, other) < 0
        fun lessThan(obj: Double, other: Number): Boolean = compare(obj, other) < 0
        fun lessThan(obj: BigInteger, other: Number): Boolean = compare(obj, other) < 0
        fun lessThan(obj: BigDecimal, other: Number): Boolean = compare(obj, other) < 0
        fun lessThan(obj: Ratio, other: Number): Boolean = compare(obj, other) < 0
        fun lessThan(obj: Complex, other: Number): Boolean = compare(obj, other) < 0

        fun lessThan(obj: Number, other: Number): Boolean = when (obj) {
            is Byte -> lessThan(obj, other)
            is Short -> lessThan(obj, other)
            is Int -> lessThan(obj, other)
            is Long -> lessThan(obj, other)
            is Float -> lessThan(obj, other)
            is Double -> lessThan(obj, other)
            is BigInteger -> lessThan(obj, other)
            is BigDecimal -> lessThan(obj, other)
            is Ratio -> lessThan(obj, other)
            is Complex -> lessThan(obj, other)
            else -> throw IllegalArgumentException()
        }

        fun lessThan(obj: Any?, other: Any?): Boolean =
            if (obj is Number && other is Number) compare(obj, other) < 0
            else compare(obj, other) < 0


        /* Less Than Or Equal To */

        fun lessThanOrEqualTo(obj: Byte, other: Number): Boolean = compare(obj, other) <= 0
        fun lessThanOrEqualTo(obj: Short, other: Number): Boolean = compare(obj, other) <= 0
        fun lessThanOrEqualTo(obj: Int, other: Number): Boolean = compare(obj, other) <= 0
        fun lessThanOrEqualTo(obj: Long, other: Number): Boolean = compare(obj, other) <= 0
        fun lessThanOrEqualTo(obj: Float, other: Number): Boolean = compare(obj, other) <= 0
        fun lessThanOrEqualTo(obj: Double, other: Number): Boolean = compare(obj, other) <= 0
        fun lessThanOrEqualTo(obj: BigInteger, other: Number): Boolean = compare(obj, other) <= 0
        fun lessThanOrEqualTo(obj: BigDecimal, other: Number): Boolean = compare(obj, other) <= 0
        fun lessThanOrEqualTo(obj: Ratio, other: Number): Boolean = compare(obj, other) <= 0
        fun lessThanOrEqualTo(obj: Complex, other: Number): Boolean = compare(obj, other) <= 0

        fun lessThanOrEqualTo(obj: Number, other: Number): Boolean = when (obj) {
            is Byte -> lessThanOrEqualTo(obj, other)
            is Short -> lessThanOrEqualTo(obj, other)
            is Int -> lessThanOrEqualTo(obj, other)
            is Long -> lessThanOrEqualTo(obj, other)
            is Float -> lessThanOrEqualTo(obj, other)
            is Double -> lessThanOrEqualTo(obj, other)
            is BigInteger -> lessThanOrEqualTo(obj, other)
            is BigDecimal -> lessThanOrEqualTo(obj, other)
            is Ratio -> lessThanOrEqualTo(obj, other)
            is Complex -> lessThanOrEqualTo(obj, other)
            else -> throw IllegalArgumentException()
        }

        fun lessThanOrEqualTo(obj: Any?, other: Any?): Boolean =
            if (obj is Number && other is Number) compare(obj, other) <= 0
            else compare(obj, other) <= 0


        /* Greater Than */

        fun greaterThan(obj: Byte, other: Number): Boolean = compare(obj, other) > 0
        fun greaterThan(obj: Short, other: Number): Boolean = compare(obj, other) > 0
        fun greaterThan(obj: Int, other: Number): Boolean = compare(obj, other) > 0
        fun greaterThan(obj: Long, other: Number): Boolean = compare(obj, other) > 0
        fun greaterThan(obj: Float, other: Number): Boolean = compare(obj, other) > 0
        fun greaterThan(obj: Double, other: Number): Boolean = compare(obj, other) > 0
        fun greaterThan(obj: BigInteger, other: Number): Boolean = compare(obj, other) > 0
        fun greaterThan(obj: BigDecimal, other: Number): Boolean = compare(obj, other) > 0
        fun greaterThan(obj: Ratio, other: Number): Boolean = compare(obj, other) > 0
        fun greaterThan(obj: Complex, other: Number): Boolean = compare(obj, other) > 0

        fun greaterThan(obj: Number, other: Number): Boolean = when (obj) {
            is Byte -> greaterThan(obj, other)
            is Short -> greaterThan(obj, other)
            is Int -> greaterThan(obj, other)
            is Long -> greaterThan(obj, other)
            is Float -> greaterThan(obj, other)
            is Double -> greaterThan(obj, other)
            is BigInteger -> greaterThan(obj, other)
            is BigDecimal -> greaterThan(obj, other)
            is Ratio -> greaterThan(obj, other)
            is Complex -> greaterThan(obj, other)
            else -> throw IllegalArgumentException()
        }

        fun greaterThan(obj: Any?, other: Any?): Boolean =
            if (obj is Number && other is Number) compare(obj, other) > 0
            else compare(obj, other) > 0


        /* Greater Than Or Equal To */

        fun greaterThanOrEqualTo(obj: Byte, other: Number): Boolean = compare(obj, other) >= 0
        fun greaterThanOrEqualTo(obj: Short, other: Number): Boolean = compare(obj, other) >= 0
        fun greaterThanOrEqualTo(obj: Int, other: Number): Boolean = compare(obj, other) >= 0
        fun greaterThanOrEqualTo(obj: Long, other: Number): Boolean = compare(obj, other) >= 0
        fun greaterThanOrEqualTo(obj: Float, other: Number): Boolean = compare(obj, other) >= 0
        fun greaterThanOrEqualTo(obj: Double, other: Number): Boolean = compare(obj, other) >= 0
        fun greaterThanOrEqualTo(obj: BigInteger, other: Number): Boolean = compare(obj, other) >= 0
        fun greaterThanOrEqualTo(obj: BigDecimal, other: Number): Boolean = compare(obj, other) >= 0
        fun greaterThanOrEqualTo(obj: Ratio, other: Number): Boolean = compare(obj, other) >= 0
        fun greaterThanOrEqualTo(obj: Complex, other: Number): Boolean = compare(obj, other) >= 0

        fun greaterThanOrEqualTo(obj: Number, other: Number): Boolean = when (obj) {
            is Byte -> greaterThanOrEqualTo(obj, other)
            is Short -> greaterThanOrEqualTo(obj, other)
            is Int -> greaterThanOrEqualTo(obj, other)
            is Long -> greaterThanOrEqualTo(obj, other)
            is Float -> greaterThanOrEqualTo(obj, other)
            is Double -> greaterThanOrEqualTo(obj, other)
            is BigInteger -> greaterThanOrEqualTo(obj, other)
            is BigDecimal -> greaterThanOrEqualTo(obj, other)
            is Ratio -> greaterThanOrEqualTo(obj, other)
            is Complex -> greaterThanOrEqualTo(obj, other)
            else -> throw IllegalArgumentException()
        }

        fun greaterThanOrEqualTo(obj: Any?, other: Any?): Boolean =
            if (obj is Number && other is Number) compare(obj, other) >= 0
            else compare(obj, other) >= 0


        /* Equals */

        fun equals(obj: Byte, other: Number): Boolean = compare(obj, other) == 0
        fun equals(obj: Short, other: Number): Boolean = compare(obj, other) == 0
        fun equals(obj: Int, other: Number): Boolean = compare(obj, other) == 0
        fun equals(obj: Long, other: Number): Boolean = compare(obj, other) == 0
        fun equals(obj: Float, other: Number): Boolean = compare(obj, other) == 0
        fun equals(obj: Double, other: Number): Boolean = compare(obj, other) == 0
        fun equals(obj: BigInteger, other: Number): Boolean = compare(obj, other) == 0
        fun equals(obj: BigDecimal, other: Number): Boolean = compare(obj, other) == 0
        fun equals(obj: Ratio, other: Number): Boolean = compare(obj, other) == 0
        fun equals(obj: Complex, other: Number): Boolean = compare(obj, other) == 0

        fun equals(obj: Number, other: Number): Boolean = when (obj) {
            is Byte -> equals(obj, other)
            is Short -> equals(obj, other)
            is Int -> equals(obj, other)
            is Long -> equals(obj, other)
            is Float -> equals(obj, other)
            is Double -> equals(obj, other)
            is BigInteger -> equals(obj, other)
            is BigDecimal -> equals(obj, other)
            is Ratio -> equals(obj, other)
            is Complex -> equals(obj, other)
            else -> throw IllegalArgumentException()
        }

        fun equals(obj: Any?, other: Any?): Boolean =
            if (obj is Number && other is Number) compare(obj, other) == 0
            else compare(obj, other) == 0
    }
}
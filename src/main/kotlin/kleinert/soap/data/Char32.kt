package kleinert.soap.data

@JvmInline
value class Char32(val code: Int) : Comparable<Char32> {
    companion object {
        val MIN_VALUE = Char32(0)
        val MAX_VALUE = Char32(0x10ffff)

        fun valueOf(c: Int) = Char32(c)

        fun valueOf(c: UInt) = Char32(c.toInt())

        fun valueOf(c: Char) = Char32(c.code)

        fun valueOf(s: String): Char32 {
            val codePointIterator = s.codePoints().iterator()
            if (!codePointIterator.hasNext()) throw IllegalArgumentException("Can not get first char code from empty string.")
            codePointIterator.nextInt()
            if (codePointIterator.hasNext()) throw IllegalArgumentException("Can not get Char32 from string which has more than one code point.")
            return Char32(s.codePointAt(0))
        }
    }

    init {
        require(code in 0..0x10ffff) { "Char code must be between 0 and 0x10ffff (inclusive)" }
    }

    fun dec(): Char32 = Char32(code - 1)
    fun inc(): Char32 = Char32(code + 1)
    operator fun minus(other: Char32): Char32 = Char32(code - other.code)
    operator fun minus(other: Char): Char32 = Char32(code - other.code)
    operator fun minus(other: Int): Char32 = Char32(code - other)
    operator fun plus(other: Int): Char32 = Char32(code + other)
    operator fun plus(other: Char): Char32 = Char32(code + other.code)
    operator fun plus(other: Char32): Char32 = Char32(code + other.code)

    fun toChar(): Char {
        if (code > Char.MAX_VALUE.code)
            throw IllegalArgumentException("Cannot convert Char32 to Char because the char code $code is too big.")
        return Char(code)
    }

    fun toByte(): Byte {
        if (code > Byte.MAX_VALUE)
            throw IllegalArgumentException("Cannot convert Char32 to Byte because the char code $code is too big.")
        return code.toByte()
    }

    fun toShort(): Short {
        if (code > Short.MAX_VALUE)
            throw IllegalArgumentException("Cannot convert Char32 to Short because the char code $code is too big.")
        return code.toShort()
    }

    fun toInt(): Int = code
    fun toLong(): Long = code.toLong()
    fun toFloat(): Float = code.toFloat()
    fun toDouble(): Double = code.toDouble()

    override fun toString(): String = StringBuilder().appendCodePoint(code).toString()

    override fun compareTo(other: Char32): Int = code.compareTo(other.code)
    operator fun rangeTo(other: Char32): Char32Range = Char32Range(this, other)

    operator fun rangeUntil(other: Char32): Char32Range =
        if (other <= MIN_VALUE) Char32Range.EMPTY
        else rangeTo(other - 1)
}

interface Char32Iterator : Iterator<Char32> {
    override fun next(): Char32 = nextChar32()
    fun nextChar(): Char = nextChar32().toChar()
    fun nextInt(): Int = nextChar32().toInt()
    fun nextChar32(): Char32
}

class Char32ProgressionIterator(first: Char32, last: Char32, private val step: Int) : Char32Iterator {
    private val finalElement: Char32 = last
    private var hasNext: Boolean = if (step > 0) first <= last else first >= last
    private var next: Char32 = if (hasNext) first else finalElement

    override fun hasNext(): Boolean = hasNext

    override fun nextChar32(): Char32 {
        IntProgression
        val value = next
        if (value == finalElement) {
            if (!hasNext) throw NoSuchElementException()
            hasNext = false
        } else {
            next += step
        }
        return value
    }
}


/**
 * A progression of values of type `Int`.
 */
open class Char32Progression
internal constructor
    (
    val start: Char32,
    val endInclusive: Char32,
    val step: Int
) : Iterable<Char32> {
    init {
        if (step == 0) throw kotlin.IllegalArgumentException("Step must be non-zero.")
        if (step == Int.MIN_VALUE) throw kotlin.IllegalArgumentException("Step must be greater than Int.MIN_VALUE to avoid overflow on negation.")
    }

    val first: Char32 = start

    val last: Char32
        get() {
            if (step > 0) {
                if (first >= endInclusive)
                    return endInclusive
                val m: Int = ((first.code.mod(step)) - (endInclusive.code.mod(step))).mod(step)
                return endInclusive - m
            }
            if (step < 0) {
                if (start >= endInclusive)
                    return endInclusive
                val c = -step
                val m: Int = ((first.code.mod(c)) - (endInclusive.code.mod(c))).mod(c)
                return endInclusive + m
            }
            throw IllegalArgumentException("Step is zero.")
        }

    override fun iterator(): Iterator<Char32> =
        object : Char32Iterator {
            private val finalElement: Char32 = last
            private var hasNext: Boolean = if (step > 0) first <= last else first >= last
            private var next: Char32 = if (hasNext) first else finalElement

            override fun hasNext(): Boolean = hasNext

            override fun nextChar32(): Char32 {
                IntProgression
                val value = next
                if (value == finalElement) {
                    if (!hasNext) throw NoSuchElementException()
                    hasNext = false
                } else {
                    next += step
                }
                return value
            }
        }

    open fun isEmpty(): Boolean = if (step > 0) first > last else first < last

    override fun equals(other: Any?): Boolean =
        other is Char32Progression && (isEmpty() && other.isEmpty() ||
                first == other.first && last == other.last && step == other.step)

    override fun hashCode(): Int =
        if (isEmpty()) -1 else (31 * (31 * first.code + last.code) + step)

    override fun toString(): String =
        if (step > 0) "$first..$last step $step"
        else "$first downTo $last step ${-step}"
}

/**
 * A range of values of type `Int`.
 */
public class Char32Range(start: Char32, endInclusive: Char32) : Char32Progression(start, endInclusive, 1),
    ClosedRange<Char32>, OpenEndRange<Char32> {
    companion object {
        /** An empty range of values of type Int. */
        public val EMPTY: Char32Range = Char32Range(Char32(1), Char32(0))
    }

    override val endExclusive: Char32
        get() {
            if (last.code == Int.MAX_VALUE)
                throw IllegalStateException("Cannot return the exclusive upper bound of a range that includes MAX_VALUE.")
            return last + 1
        }

    override fun contains(value: Char32): Boolean = value in first..last

    /**
     * Checks whether the range is empty.
     *
     * The range is empty if its start value is greater than the end value.
     */
    override fun isEmpty(): Boolean = first > last

    override fun equals(other: Any?): Boolean =
        other is Char32Progression && (isEmpty() && other.isEmpty() ||
                first == other.first && last == other.last)

    override fun hashCode(): Int =
        if (isEmpty()) -1 else (31 * first.code + last.code)

    override fun toString(): String = "$first..$last"
}

fun Byte.toChar32() = Char32(this.toInt())
fun Short.toChar32() = Char32(this.toInt())
fun Int.toChar32() = Char32(this)
fun Long.toChar32() = Char32(this.toInt())

package kleinert.edn.data

/**
 * A unicode char using a 32 bit code. This class is a value class, so it takes up no extra space on the stack or heap.
 *
 * @property code The char code Should be between 0 and 0x10ffff.
 * @constructor Creates a new [Char32].
 *
 * @author Armin Kleinert
 */
@JvmInline
value class Char32(val code: Int) : Comparable<Char32> {
    companion object {
        val MIN_VALUE = Char32(0)
        val MAX_VALUE = Char32(0x10ffff)

        /**
         * Creates a [Char32] from the input [code].
         * @return A new [Char32].
         */
        fun valueOf(code: Int) = Char32(code)

        /**
         * Creates a [Char32] from the input [code].
         * @return A new [Char32].
         */
        fun valueOf(code: UInt) = Char32(code.toInt())

        /**
         * Convert [Char] [chr] to [Char32].
         * @return A new [Char32].
         */
        fun valueOf(chr: Char) = Char32(chr.code)

        /**
         * Takes the first codepoint from the [String] [s] and creates a [Char32] from it.
         * The input must not be empty or have more than one codepoint.
         * @throws IllegalArgumentException if [s] was empty or has too many codepoints.
         * @return A new [Char32].
         */
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

    /**
     * Decreases [code] by 1, creating a new [Char32].
     * @return A new [Char32].
     */
    fun dec(): Char32 = Char32(code - 1)

    /**
     * Increases [code] by 1, creating a new [Char32].
     * @return A new [Char32].
     */
    fun inc(): Char32 = Char32(code + 1)

    operator fun minus(other: Char32): Char32 = Char32(code - other.code)
    operator fun minus(other: Char): Char32 = Char32(code - other.code)
    operator fun minus(other: Int): Char32 = Char32(code - other)
    operator fun plus(other: Int): Char32 = Char32(code + other)
    operator fun plus(other: Char): Char32 = Char32(code + other.code)
    operator fun plus(other: Char32): Char32 = Char32(code + other.code)

    /**
     * Converts the [Char32] to a [Char].
     * @throws IllegalArgumentException if [code] is bigger than [Char.MAX_VALUE].
     * @return the [Char32] as a [Char].
     */
    fun toChar(): Char {
        if (code > Char.MAX_VALUE.code)
            throw IllegalArgumentException("Cannot convert Char32 to Char because the char code $code is too big.")
        return Char(code)
    }

    /**
     * Converts the [Char32] to a [Byte].
     * @throws IllegalArgumentException if [code] is bigger than [Byte.MAX_VALUE].
     * @return the [Char32] as a [Byte].
     */
    fun toByte(): Byte {
        if (code > Byte.MAX_VALUE)
            throw IllegalArgumentException("Cannot convert Char32 to Byte because the char code $code is too big.")
        return code.toByte()
    }

    /**
     * Converts the [Char32] to a [Short].
     * @throws IllegalArgumentException if [code] is bigger than [Short.MAX_VALUE].
     * @return the [Char32] as a [Short].
     */
    fun toShort(): Short {
        if (code > Short.MAX_VALUE)
            throw IllegalArgumentException("Cannot convert Char32 to Short because the char code $code is too big.")
        return code.toShort()
    }

    /**
     * Converts the [Char32] to a [Int]. This is equivalent to accessing [code] with no additional logic.
     * @return the [Char32] as a [Int].
     */
    fun toInt(): Int = code

    /**
     * Converts the [Char32] to a [Long].
     * @return the [Char32] as a [Long].
     */
    fun toLong(): Long = code.toLong()

    /**
     * Converts the [Char32] to a [Float].
     * @return the [Char32] as a [Float].
     */
    fun toFloat(): Float = code.toFloat()

    /**
     * Converts the [Char32] to a [Double].
     * @return the [Char32] as a [Double].
     */
    fun toDouble(): Double = code.toDouble()

    /**
     * @return A [String] with this [Char32] as its only codepoint.
     */
    override fun toString(): String = String(intArrayOf(code), 0, 1)

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

//class Char32ProgressionIterator(first: Char32, last: Char32, private val step: Int) : Char32Iterator {
//    private val finalElement: Char32 = last
//    private var hasNext: Boolean = if (step > 0) first <= last else first >= last
//    private var next: Char32 = if (hasNext) first else finalElement
//
//    override fun hasNext(): Boolean = hasNext
//
//    override fun nextChar32(): Char32 {
//        val value = next
//        if (value == finalElement) {
//            if (!hasNext) throw NoSuchElementException()
//            hasNext = false
//        } else {
//            next += step
//        }
//        return value
//    }
//}

/**
 * A progression of values of type `Char32`.
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

    override fun iterator(): Char32Iterator =
        object : Char32Iterator {
            private val finalElement: Char32 = last
            private var hasNext: Boolean = if (step > 0) first <= last else first >= last
            private var next: Char32 = if (hasNext) first else finalElement

            override fun hasNext(): Boolean = hasNext

            override fun nextChar32(): Char32 {
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
 * A range of values of type [Char32].
 */
class Char32Range(start: Char32, endInclusive: Char32) : Char32Progression(start, endInclusive, 1),
    ClosedRange<Char32>, OpenEndRange<Char32> {
    companion object {
        /** An empty range of values of type Int. */
        val EMPTY: Char32Range = Char32Range(Char32(1), Char32(0))
    }

    override val endExclusive: Char32
        get() {
            if (last.code == Int.MAX_VALUE)
                throw IllegalStateException("Cannot return the exclusive upper bound of a range that includes MAX_VALUE.")
            return last + 1
        }

    override fun contains(value: Char32): Boolean = !(value < first || value > endInclusive)

    /**
     * Checks whether the range is empty.
     *
     * The range is empty if its start value is greater than the end value.
     * @return true if the range is empty, false otherwise.
     */
    override fun isEmpty(): Boolean = first > last

    override fun equals(other: Any?): Boolean =
        other is Char32Progression && (isEmpty() && other.isEmpty() ||
                first == other.first && last == other.last)

    override fun hashCode(): Int =
        if (isEmpty()) -1 else (31 * first.code + last.code)

    override fun toString(): String = "$first..$last"
}

fun Char.toChar32() = Char32(this.code)
fun Byte.toChar32() = Char32(this.toInt())
fun Short.toChar32() = Char32(this.toInt())
fun Int.toChar32() = Char32(this)
fun Long.toChar32() = Char32(this.toInt())

package kleinert.soap.data

/**
 * A generalized mutable sub-list. Supports traversing, reading, and setting elements, but not removal or addition of elements.
 * This implementation does not check for modifications on the source list.
 *
 * @property fromIndex Start index in the origin list, inclusive.
 * @property toIndexExclusive End index in the origin list, exclusive.
 * Constraint: 0 < [fromIndex] <= [toIndexExclusive]
 *
 * @author Armin Kleinert
 */
class MutableListView<T>(
    private val fromIndex: Int,
    private val toIndexExclusive: Int,
    private val origin: MutableList<T>
) :
    SimpleList<T> {

    override val size: Int = toIndexExclusive - fromIndex

    init {
        if (fromIndex > toIndexExclusive) throw IllegalArgumentException()
        if (fromIndex < 0 || toIndexExclusive > origin.size) throw IndexOutOfBoundsException()
    }

    override fun getUnchecked(index: Int): T {
        checkBounds(index)
        return origin[index + fromIndex]
    }

    override fun setUnchecked(index: Int, element: T): T {
        checkBounds(index)
        return origin.set(index + fromIndex, element)
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> {
        checkBoundsExclusiveRange(fromIndex, toIndex)
        if (fromIndex == 0 && toIndex == size) return this
        return MutableListView(fromIndex + this.fromIndex, toIndex + this.fromIndex, origin)
    }

    override fun toString(): String =
        joinToString(", ", prefix = "[", postfix = "]")

    override fun equals(other: Any?): Boolean =
        commonEquals(other)

    override fun hashCode(): Int {
        var result = fromIndex
        result = 31 * result + toIndexExclusive
        result = 31 * result + origin.hashCode()
        return result
    }


}
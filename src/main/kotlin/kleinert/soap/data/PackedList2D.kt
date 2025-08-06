package kleinert.soap.data

/**
 * A packed abstraction for a [List] of [List]s. All sublists have the same size.
 * Traversal, reading and setting of elements in the sublists is possible, but adding and removing elements is not.
 *
 * @property size
 * @property packedSize
 * @property subListSize
 * @property frozen
 *
 * @author Armin Kleinert
 */
class PackedList2D<T> : SimpleList<List<T>> {
    private var packed: MutableList<T>

    override val size: Int

    /**
     * Number of singular elements in the packed list.
     */
    val packedSize: Int
        get() = packed.size

    val subListSize: Int
        get() = if (size == 0) 0 else packedSize / size

    val frozen: Boolean

    /**
     * Constructs a [PackedList2D] from a prepacked list.
     *
     * @param m The size of each sublist.
     * @param packed The packed list.
     * @param frozen If true, the [PackedList2D] will be immutable.
     *
     * @throws IllegalArgumentException if [m] is below 0 or if the dimensions of [packed] are not divisible by [m].
     */
    constructor(m: Int, packed: List<T>, frozen: Boolean = true) {
        if (m < 0) throw IllegalArgumentException("Index $m is negative.")

        if (m == 0 && packed.isNotEmpty())
            throw IllegalArgumentException("With m=$m, the packed List must be empty.")
        else if (m != 0 && packed.size % m != 0)
            throw IllegalArgumentException("Invalid size of packed list. Must be divisible by $m (m) but is ${packed.size}.")

        size = m
        this.packed = packed.toMutableList()
        this.frozen = frozen
    }

    /**
     * Creates a [PackedList2D] from an unpacked list.
     *
     * @param unpacked The input list.
     * @param frozen If true, the result will be immutable.
     *
     * @throws IllegalArgumentException if not all sublists have the same size.
     */
    constructor(unpacked: List<List<T>>, frozen: Boolean = true) {
        val packed = mutableListOf<T>()
        var firstSize: Int = -1
        for (l in unpacked) {
            if (firstSize == -1)
                firstSize = l.size
            if (l.size != firstSize)
                throw IllegalArgumentException("All lists must have the same size ($firstSize).")
            packed.addAll(l)
        }

        size = if (packed.isEmpty()) 0 else unpacked.size
        this.packed = packed
        this.frozen = frozen
    }

    /**
     * @return An unpacked list of the sublists of this packed list.
     */
    fun unpack(): List<List<T>> =
        (0..<size).map { getUnchecked(it) }

    /**
     * Get a sub-list.
     */
    override fun getUnchecked(index: Int): List<T> =
        ArrayList(packed).subList(index * subListSize, index * subListSize + subListSize)

    /**
     * Set a sub-list.
     *
     * @throws UnsupportedOperationException if the [PackedList2D] is immutable.
     * @throws IllegalArgumentException if [element].size != [subListSize].
     */
    override fun setUnchecked(index: Int, element: List<T>): List<T> {
        if (frozen)
            throw UnsupportedOperationException()
        if (element.size != subListSize)
            throw IllegalArgumentException()

        val offset = index * subListSize
        val old = get(index)

        for ((i, item) in element.withIndex()) {
            packed[offset + i] = item
        }

        return old
    }

    override fun lastIndexOf(element: List<T>): Int {
        if (element.size != subListSize)
            return -1
        var lastIndex = -1
        for (i in IntProgression.fromClosedRange(0, size, subListSize)) {
            var found = true
            for (j in 0..<subListSize)
                if (element[j] != packed[i + j]) {
                    found = false
                    break
                }
            if (found)
                lastIndex = i
        }
        return lastIndex
    }

    override fun indexOf(element: List<T>): Int {
        if (element.size != subListSize)
            return -1
        for (i in IntProgression.fromClosedRange(0, size, subListSize)) {
            var found = true
            for (j in 0..<subListSize)
                if (element[j] != packed[i + j]) {
                    found = false
                    break
                }
            if (found)
                return i / subListSize
        }
        return -1
    }

    /**
     * Returns a copy of the underlying packed list.
     */
    fun flatten(): List<T> =
        packed.toList()

    /**
     * Get the element at index [j] in sub-list [i]. Both [i] and [j] are zero-based.
     *
     * @param i Index of the sub-list. Constraint: 0<=[i]<[size].
     * @param j The index within the sub-list. Constraint: 0<=[j]<[subListSize].
     *
     * @throws [IndexOutOfBoundsException] if [i] or [j] is invalid.
     */
    operator fun get(i: Int, j: Int): T {
        checkBounds(i, j)
        return packed[i * subListSize + j]
    }

    /**
     * Set the element at index [j] in sub-list [i]. Both [i] and [j] are zero-based.
     *
     * @param i Index of the sub-list. Constraint: 0<=[i]<[size].
     * @param j The index within the sub-list. Constraint: 0<=[j]<[subListSize].
     * @param element
     *
     * @throws [IndexOutOfBoundsException] if [i] or [j] is invalid.
     */
    operator fun set(i: Int, j: Int, element: T): T {
        checkBounds(i, j)
        val old = get(i, j)
        packed[i * subListSize + j] = element
        return old
    }

    private fun checkBounds(index: Int, innerIndex: Int) {
        if (isEmpty())
            throw IndexOutOfBoundsException("Index [$index, $innerIndex] is not in empty list.")
        if (index < 0 || innerIndex < 0 || index >= size || innerIndex >= subListSize)
            throw IndexOutOfBoundsException("Index [$index, $innerIndex] out of bounds [0, 0] to [$size, $subListSize] (both exclusive).")
    }

    override fun toString(): String = joinToString(", ", prefix="[", postfix = "]")

    override fun equals(other: Any?): Boolean = commonEquals(other)

    override fun hashCode(): Int {
        var result = packed.hashCode()
        result = 31 * result + size
        return result
    }
}
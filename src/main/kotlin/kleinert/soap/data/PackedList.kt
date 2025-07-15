package kleinert.soap.data

class PackedList<T> : SimpleList<List<T>> {
    private var packed: MutableList<T>

    override val size: Int

    val packedSize: Int
        get() = packed.size

    val subListSize: Int
        get() = if (size == 0) 0 else packedSize / size

    val frozen: Boolean

    constructor(m: Int, n: Int, packed: List<T>, frozen: Boolean = true) {
        if (m < 0) throw IllegalArgumentException("Index $m is negative.")
        if (n < 0) throw IllegalArgumentException("Inner index $n is negative.")
        if (packed.size != m * n) throw IllegalArgumentException("Invalid size of packed list. Must be ${m * n} ($m*$n) but is ${packed.size}.")
        size = m
        this.packed = packed.toMutableList()
        this.frozen = frozen
    }

    constructor(unpacked: List<List<T>>, frozen: Boolean = true) {
        val packed = mutableListOf<T>()
        var firstSize: Int = -1
        for (l in unpacked) {
            if (firstSize == -1) firstSize = l.size
            require(l.size == firstSize)
            if (l.size != firstSize) throw IllegalArgumentException("All lists must have the same size ($firstSize).")
            packed.addAll(l)
        }

        size = if (packed.isEmpty()) 0 else unpacked.size
        this.packed = packed
        this.frozen = frozen
    }

    constructor(m: Int, n: Int, packed: MutableList<T>, frozen: Boolean = true, unsafe: Boolean = false) {
        if (m < 0) throw IllegalArgumentException("Index $m is negative.")
        if (n < 0) throw IllegalArgumentException("Inner index $n is negative.")
        if (packed.size != m * n) throw IllegalArgumentException("Invalid size of packed list. Must be ${m * n} ($m*$n) but is ${packed.size}.")
        size = m
        this.packed = if (!unsafe || frozen) packed.toMutableList() else packed
        this.frozen = frozen
    }

    fun unpack(): List<List<T>> =
        (0..<size).map { getUnchecked(it) }

    override fun getUnchecked(index: Int): List<T> =
        packed.subList(index * subListSize, index * subListSize + subListSize)

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

    fun flatten(): List<T> =
        packed.toList()

    operator fun get(i: Int, j: Int): T {
        checkBounds(i, j)
        return packed[i * subListSize + j]
    }

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
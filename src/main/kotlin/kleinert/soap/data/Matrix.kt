package kleinert.soap.data

class PackedList<T> : List<List<T>> {
    private var packed: List<T>

    override val size: Int

    val packedSize: Int
        get() = packed.size

    val subListSize: Int
        get() = packedSize / size

    constructor(m: Int, n: Int, packed: List<T>, copy: Boolean = true) {
        if (m < 0) throw IllegalArgumentException("Index $m is negative.")
        if (n < 0) throw IllegalArgumentException("Inner index $n is negative.")
        if (packed.size != m * n) throw IllegalArgumentException("Invalid size of packed list. Must be ${m * n} ($m*$n) but is ${packed.size}.")
        size = m
        this.packed = if (copy) packed.toList() else packed
    }

    constructor(unpacked: List<List<T>>) {
        val packed = mutableListOf<T>()
        var firstSize: Int = -1
        for (l in unpacked) {
            if (firstSize == -1) firstSize = l.size
            require(l.size == firstSize)
            if (l.size != firstSize) throw IllegalArgumentException("All lists must have the same size ($firstSize).")
            packed.addAll(l)
        }

        size = unpacked.size
        this.packed = packed
    }

    fun unpack(): List<List<T>> =
        (0..<size).map { getUnchecked(it) }

    override fun get(index: Int): List<T> {
        checkBounds(index)
        return packed.subList(index * subListSize, index * subListSize + subListSize)
    }

    private fun getUnchecked(index: Int): List<T> =
        packed.subList(index * subListSize, index * subListSize + subListSize)

    override fun isEmpty(): Boolean =
        size == 0

    class PackedListIterator<T>(private val packedList: PackedList<T>, startIndex: Int = 0) : ListIterator<List<T>> {
        private var index: Int = startIndex

        init {
            packedList.checkBounds(startIndex)
        }

        override fun hasNext(): Boolean = index < packedList.size - 1
        override fun hasPrevious(): Boolean = index > 0
        override fun nextIndex(): Int = index
        override fun previousIndex(): Int = index - 1

        override fun next(): List<T> {
            val temp = packedList[index]
            index++
            return temp
        }

        override fun previous(): List<T> {
            index--
            return packedList[index]
        }
    }

    override fun iterator(): Iterator<List<T>> =
        PackedListIterator(this)

    override fun listIterator(): ListIterator<List<T>> =
        PackedListIterator(this)

    override fun listIterator(index: Int): ListIterator<List<T>> =
        PackedListIterator(this, index)

    override fun subList(fromIndex: Int, toIndex: Int): List<List<T>> {
        checkBoundsRange(fromIndex, toIndex)
        val subListSize = this.subListSize
        return PackedList(
            toIndex - fromIndex,
            subListSize,
            packed.subList(subListSize * fromIndex, subListSize * toIndex)
        )
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

    override fun containsAll(elements: Collection<List<T>>): Boolean {
        for (it in elements.toSet()) {
            if (it.size != subListSize) continue
            if (!contains(it)) return false
        }
        return true
    }

    override fun contains(element: List<T>): Boolean {
        if (element.size != subListSize) return false
        return indexOf(element) >= 0
    }

    fun flatten(): List<T> =
        packed.toList()

    operator fun get(i: Int, j: Int): T {
        checkBounds(i, j)
        return packed[i * subListSize + j]
    }

    fun getUnchecked(i: Int, j: Int): T =
        packed[i * subListSize + j]

    private fun checkBounds(index: Int) {
        if (isEmpty())
            throw IndexOutOfBoundsException("Index $index is not in empty list.")
        if (index < 0 || index >= size)
            throw IndexOutOfBoundsException("Index $index out of bounds 0 to $size (exclusive).")
    }

    private fun checkBounds(index: Int, innerIndex: Int) {
        if (isEmpty())
            throw IndexOutOfBoundsException("Index [$index, $innerIndex] is not in empty list.")
        if (index < 0 || innerIndex < 0 || index >= size || innerIndex >= subListSize)
            throw IndexOutOfBoundsException("Index [$index, $innerIndex] out of bounds [0, 0] to [$size, $subListSize] (both exclusive).")
    }

    private fun checkBoundsRange(index: Int, toIndex: Int) {
        if (toIndex < index)
            throw IllegalArgumentException("Index range $index to $toIndex (exclusive) is invalid.")
        if (isEmpty() && index != 0 && toIndex != 0)
            throw IndexOutOfBoundsException("Index range $index to $toIndex (exclusive) out of bounds on empty list.")
        if (index < 0 || /*toIndex < 0 ||*/ index >= size || toIndex >= size)
            throw IndexOutOfBoundsException("Index range $index to $toIndex (exclusive) out of bounds 0 to $size (exclusive).")
    }

    override fun toString(): String = unpack().toString()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is List<*>) return false

        for ((index, value) in withIndex().withIndex())
            if (value != other[index]) return false

        return true
    }

    override fun hashCode(): Int {
        var result = packed.hashCode()
        result = 31 * result + size
        return result
    }
}
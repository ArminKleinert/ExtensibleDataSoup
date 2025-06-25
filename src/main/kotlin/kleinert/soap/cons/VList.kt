package kleinert.soap.cons

class VList<T> private constructor(segment: Segment?, private val offset: Int) : PersistentList<T>, List<T> {
    private class Segment(val next: Segment?, val elements: Array<Any?>) {
        override fun toString(): String {
            return "Segment(next=$next, elements=${elements.contentToString()})"
        }

        override fun hashCode(): Int {
            var result = next?.hashCode() ?: 0
            result = 31 * result + elements.contentHashCode()
            return result
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Segment

            if (next != other.next) return false
            if (!elements.contentEquals(other.elements)) return false

            return true
        }
    }

    class VListIterator<T>(vList: VList<T>) : Iterator<T> {
        private var segment: Segment?
        private var offset: Int

        init {
            segment = vList.base
            offset = vList.offset
        }

        override fun hasNext(): Boolean = segment != null

        override fun next(): T {
            if (segment == null) throw NoSuchElementException()
            require(segment != null)
            val first = segment!!.elements[offset]
            offset++
            if (offset >= segment!!.elements.size) {
                offset = 0
                segment = segment!!.next
            }
            return first as T
        }
    }

    private val base: Segment? = segment

    override val size: Int
        get() = if (base == null) 0 else base.elements.size * 2 - 1 - offset

    override val car: T
        get() {
            if (base == null) throw NoSuchElementException("Empty list.")

            @Suppress("UNCHECKED_CAST")
            return base.elements[offset] as T
        }

    override val cdr: VList<T>
        get() {
            if (base == null) return this
            if (offset == base.elements.size - 1) return VList(base.next, 0)
            return VList(base, offset + 1)
        }

    fun split() = car to cdr

    companion object {
        private val EMPTY = VList<Any?>(null, 0)

        fun <T> of(vararg elements: T) =
            if (elements.isEmpty()) EMPTY as VList<T>
            else segmentsAndOffsetFromReversedList(elements.asIterable())

        fun <T> toVList(iterable: Iterable<T>) = when (iterable) {
            is VList<T> -> iterable
            else -> segmentsAndOffsetFromReversedList(iterable)
        }

        fun <T> toVList(iterable: Array<T>) =
            if (iterable.isEmpty()) of()
            else segmentsAndOffsetFromReversedList(iterable.asIterable())

        private fun <T> segmentsAndOffsetFromReversedList(inputList: Iterable<T>): VList<T> {
            var reversedList =
                if (inputList is List<*> && inputList is RandomAccess) inputList.asReversed()
                else inputList.reversed()
            var nextSegmentSize = 1
            var segment: Segment? = null
            var offset = 0

            while (reversedList.isNotEmpty()) {
                val segmentElements = reversedList.take(nextSegmentSize)
                reversedList = reversedList.drop(nextSegmentSize)

                val segmentElementArray = arrayOfNulls<Any?>(nextSegmentSize)
                if (segmentElements.size < nextSegmentSize) {
                    offset = nextSegmentSize - segmentElements.size
                }
                for ((index, elem) in segmentElements.withIndex()) {
                    segmentElementArray[nextSegmentSize - index - 1] = elem
                }

                segment = Segment(segment, segmentElementArray)

                nextSegmentSize *= 2
            }

            if (segment == null) return of()

            return VList(segment, offset)
        }
    }

    override fun cons(element: T): VList<T> {
        return prepend(listOf(element))
    }

    private fun prepend(elements: Iterable<T>): VList<T> {
        if (isEmpty())
            return toVList(elements)
        base!!

        if (elements is List<T> && elements.isEmpty())
            return this

        val reversedElementsIterator =
            when (elements) {
                is PersistentList<T> -> elements.toList()
                is List<T> -> elements
                else -> elements.toList()
            }.asReversed().iterator()

        var newBase = Segment(base.next, base.elements.copyOf())
        var newOffset = offset
        while (reversedElementsIterator.hasNext()) {
            if (newOffset > 0) {
                newBase.elements[newOffset - 1] = reversedElementsIterator.next()
                newOffset--
            } else {
                val nextSize = newBase.elements.size * 2
                newBase = Segment(newBase, arrayOfNulls(nextSize))
                newOffset = nextSize - 1
                newBase.elements[newOffset] = reversedElementsIterator.next()
            }
        }
        return VList(newBase, newOffset)
    }

    override fun cleared(): VList<T> = VList.of()

    override operator fun get(index: Int): T {
        if (index < 0 || index >= size)
            throw IndexOutOfBoundsException("Index $index is out of bounds.")

        if (index == 0)
            return car

        var i = index + offset
        var segment = base
        while (segment != null) {
            @Suppress("UNCHECKED_CAST")
            if (i < segment.elements.size) return segment.elements[i] as T
            i -= segment.elements.size
            segment = segment.next
        }

        throw IndexOutOfBoundsException("The code should not go here. But anyway, the index $index is out of bounds.")
    }

    override fun isEmpty(): Boolean = base == null

    override fun iterator(): Iterator<T> = VListIterator(this)

    override fun subList(fromIndex: Int, toIndex: Int): VList<T> = toVList(toList().subList(fromIndex, toIndex))

    override fun drop(n: Int): VList<T> {
        require(n >= 0) { "Requested element count $n is less than zero." }
        if (n == 0 || isEmpty()) return this
        if (n >= size) return of()

        val baseElementNum = base!!.elements.size - offset
        if (n < baseElementNum) return VList(base, offset + n)
        if (n == baseElementNum) return VList(base.next, 0)

        var segment = base.next
        var restN = n - baseElementNum

        while (segment != null) {
            val segmentSize = segment.elements.size
            if (restN == segmentSize) {
                return VList(segment.next, 0)
            }

            if (restN < segmentSize)
                return VList(segment, segmentSize - restN)

            segment = segment.next
            restN -= segmentSize
        }

        return VList.of()
    }

    override fun <R> sameTypeFromList(list: List<R>): VList<R> = toVList(list)

    override fun toMutableList(): MutableList<T> {
        val res = baseElementsAsMutableList()

        if (base == null) return res
        var segment = base.next

        while (segment != null) {
            res.addAll(segment.elements as Array<T>)
            segment = segment.next
        }

        return res
    }

    fun getSegments(): List<List<T?>> {
        if (base == null) return listOf()

        val res = mutableListOf<List<T>>()
        var segment = base
        while (segment != null) {
            res.add(segment.elements.map { it as T })
            segment = segment.next
        }
        return res
    }

    override fun asSequence() = sequence {
        var segment = base
        var offset = offset
        while (segment != null) {
            for (i in offset..<segment.elements.size)
                yield(segment.elements[i] as T)
            segment = segment.next
            offset = 0
        }
    }

    fun <R> mapSegments(f: (T) -> R): List<List<R>> {
        val res = mutableListOf<List<R>>()
        var segment = base
        var offset = offset
        while (segment != null) {
            val tempList = mutableListOf<R>()
            val elems = segment.elements as Array<T>
            for (i in offset..elems.lastIndex)
                tempList.add(f(elems[i]))
            res.add(tempList.toList())
            segment = segment.next
            offset = 0
        }
        return res
    }

    private fun baseElementsAsMutableList(): MutableList<T> {
        val res = mutableListOf<T>()
        if (base == null) return res
        for (i in offset..<base.elements.size)
            res.add(base.elements[i] as T)
        return res
    }

    override fun toString(): String = commonToString()
    override fun equals(other: Any?): Boolean = commonEqualityCheck(other)

    override fun hashCode(): Int {
        var result = base?.hashCode() ?: 0
        result = 31 * result + offset
        return result
    }
}

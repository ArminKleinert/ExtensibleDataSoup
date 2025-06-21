package kleinert.soap

class VList<T> : List<T> {
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

    private val base: Segment?

    private val offset: Int

    override val size: Int

    private constructor(segment: Segment?, offset: Int, size: Int = -1) {
        base = segment
        this.offset = offset
        this.size = if (size >= 0) size else calcSize()
    }

    constructor() {
        this.base = null
        this.offset = 0
        this.size = 0
    }

    constructor(coll: Iterable<T>) {
        val (baseSegment, offset) = segmentsAndOffsetFromReversedList(coll.reversed())
        this.base = baseSegment
        this.offset = offset
        this.size = calcSize()
    }

    constructor(coll: Array<T>) {
        val (baseSegment, offset) = segmentsAndOffsetFromReversedList(coll.reversed())
        this.base = baseSegment
        this.offset = offset
        this.size = calcSize()
    }

    constructor(vlist: VList<T>) {
        this.base = vlist.base
        this.offset = vlist.offset
        this.size = vlist.size
    }

    constructor(coll: List<T>) {
        val (baseSegment, offset) = segmentsAndOffsetFromReversedList(coll.asReversed())
        this.base = baseSegment
        this.offset = offset
        this.size = calcSize()
    }

    private fun calcSize(): Int {
        if (base == null) return 0
        var counter = -offset
        var segment = base
        while (segment != null) {
            counter += segment.elements.size
            segment = segment.next
        }
        return counter
    }

    companion object {
        fun <T> of(vararg elements: T) = VList<T>(elements.toList())

        private fun <T> segmentsAndOffsetFromReversedList(reversedList: List<T>): Pair<Segment?, Int> {
            var reversedList = reversedList
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

            return segment to offset
        }
    }

    fun cons(elem: T): VList<T> {
        require(offset >= 0)

        if (offset == 0 || base == null) {
            val nextSegmentSize =
                if (base == null) 1 else base.elements.size * 2
            val nextOffset = nextSegmentSize - 1
            val nextElements = arrayOfNulls<Any?>(nextSegmentSize)
            nextElements[nextOffset] = elem //as T
            val newSegment = Segment(base, nextElements)
            return VList(newSegment, nextOffset, size + 1)
        }

        val newElements = base.elements.copyOf()
        newElements[offset - 1] = elem
        val newSegment = Segment(base.next, newElements)
        return VList(newSegment, offset - 1, size + 1)
    }

    fun first() = car

    val car: T
        get() {
            if (base == null) throw NoSuchElementException("Empty list.")

            @Suppress("UNCHECKED_CAST")
            return base.elements[offset] as T
        }

    val cdr: VList<T>
        get() {
            if (base == null) return this
            if (offset == base.elements.size - 1) return VList(base.next, 0, size - 1)
            return VList(base, offset + 1, size - 1)
        }

    override fun contains(element: T): Boolean {
        val iterator = iterator()
        while (iterator.hasNext()) {
            val temp = iterator.next()
            if (temp == element) return true
        }
        return false
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        val iterator: Iterator<T> = elements.iterator()

        var element: T
        do {
            if (!iterator.hasNext())
                return true
            element = iterator.next()
        } while (contains(element))

        return false
    }

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

    override fun indexOf(element: T): Int {
        val iterator = iterator()
        var index = 0
        while (iterator.hasNext()) {
            val temp = iterator.next()
            if (temp == element) return index
            index++
        }
        return -1
    }

    override fun iterator(): Iterator<T> = VListIterator(this)

    override fun listIterator(): ListIterator<T> = toList().listIterator()

    override fun listIterator(index: Int): ListIterator<T> = toList().listIterator(index)

    override fun subList(fromIndex: Int, toIndex: Int): List<T> = VList(toList().subList(fromIndex, toIndex))

    override fun lastIndexOf(element: T): Int {
        val iterator = iterator()
        var counter = 0
        var index = -1
        while (iterator.hasNext()) {
            val temp = iterator.next()
            if (temp == element) index = counter
            counter++
        }
        return index
    }

    fun drop(n: Int): VList<T> {
        if (n > size) return of()

        var rest = this
        var restN = n

        while (rest.isNotEmpty()) {
            if (restN <= 0)
                return rest
            if (rest.base == null)
                return rest
            if (restN < rest.base!!.elements.size - offset)
                return VList(base, offset + restN, size - restN)

            val segmentSizeDiff = rest.base!!.elements.size - offset
            rest = VList(rest.base!!.next, 0, size - segmentSizeDiff)
            restN -= segmentSizeDiff
        }

        return rest
    }

    fun toMutableList(): MutableList<T> {
        val res = baseElementsAsMutableList()

        if (base == null) return res
        var segment = base.next

        while (segment != null) {
            res.addAll(segment.elements as Array<T>)
            segment = segment.next
        }

        return res
    }

    fun reversed(): VList<T> {
        if (base == null) return this

        var res = VList<T>(null, 0, 0)
        for (it in this) {
            res = res.cons(it)
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

    inline fun <R> map(f: (T) -> R): VList<R> = VList(asIterable().map(f))

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

    operator fun plus(other: List<T>): VList<T> {
        if (other is VList<T> && size <= other.offset) {
            var res: VList<T> = other
            for (e in this)
                res = res.cons(e)
            return res
        }
        return VList(toList() + other)
    }

    operator fun plus(other: Iterable<T>): VList<T> {
        return VList(toList() + other)
    }

    override fun toString(): String = StringBuilder()
        .append('[')
        .append(joinToString(", "))
        .append("]").toString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is List<Any?>) return false

        val iter = iterator()
        val otherIter = other.iterator()

        while (iter.hasNext()) {
            if (!otherIter.hasNext()) return false
            if (iter.next() != otherIter.next()) return false
        }

        return !otherIter.hasNext()
    }

    override fun hashCode(): Int {
        var result = base?.hashCode() ?: 0
        result = 31 * result + offset
        result = 31 * result + size
        return result
    }
}

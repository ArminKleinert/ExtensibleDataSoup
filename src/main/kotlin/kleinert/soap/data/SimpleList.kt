package kleinert.soap.data

/**
 * This interface implements all List methods except `size`. A good implementation of `get` should also be implemented separately.
 * The implementations does rely on `get` being O(1).
 */
interface SimpleList<T> : MutableList<T> {
    override val size: Int
    fun getUnchecked(index: Int): T
    fun setUnchecked(index: Int, element: T): T

    override fun set(index: Int, element: T): T {
        checkBounds(index)
        val temp = getUnchecked(index)
        setUnchecked(index, element)
        return temp
    }

    override fun get(index: Int): T {
        checkBounds(index)
        return getUnchecked(index)
    }

    override fun isEmpty(): Boolean =
        size == 0

    override fun lastIndexOf(element: T): Int {
        var lastIndex = -1
        for ((index, e) in withIndex()) {
            if (e == element)
                lastIndex = index
        }
        return lastIndex
    }

    override fun indexOf(element: T): Int {
        for ((index, e) in withIndex()) {
            if (e == element)
                return index
        }
        return -1
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        for (it in elements.toSet()) {
            if (!contains(it))
                return false
        }
        return true
    }

    override fun contains(element: T): Boolean =
        indexOf(element) >= 0

    override fun iterator(): MutableIterator<T> = listIterator(0)
    override fun listIterator(): MutableListIterator<T> = listIterator(0)
    override fun listIterator(index: Int): MutableListIterator<T> = object : MutableListIterator<T> {
        var cursor = index

        override fun hasNext(): Boolean = cursor < size
        override fun hasPrevious(): Boolean = cursor > 0
        override fun nextIndex(): Int = cursor
        override fun previousIndex(): Int = cursor - 1

        override fun next(): T {
            val temp = get(cursor)
            cursor++
            return temp
        }

        override fun previous(): T {
            cursor--
            return get(cursor)
        }

        override fun set(element: T) {
            set(cursor, element)
        }

        override fun add(element: T) {
            add(cursor, element)
        }

        override fun remove() {
            removeAt(cursor)
        }
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> {
        checkBoundsExclusiveRange(fromIndex, toIndex)
        if (fromIndex == 0 && toIndex == size) return this
        return MutableListView(fromIndex, toIndex, this)
    }

    override fun removeAt(index: Int): T = throw UnsupportedOperationException()
    override fun add(element: T): Boolean = throw UnsupportedOperationException()
    override fun add(index: Int, element: T) = throw UnsupportedOperationException()
    override fun addAll(index: Int, elements: Collection<T>): Boolean = throw UnsupportedOperationException()
    override fun addAll(elements: Collection<T>): Boolean = throw UnsupportedOperationException()
    override fun clear() = throw UnsupportedOperationException()
    override fun retainAll(elements: Collection<T>): Boolean = throw UnsupportedOperationException()
    override fun removeAll(elements: Collection<T>): Boolean = throw UnsupportedOperationException()
    override fun remove(element: T): Boolean = throw UnsupportedOperationException()

    fun checkBounds(index: Int) {
        if (isEmpty())
            throw IndexOutOfBoundsException("Index $index is not in empty list.")
        if (index < 0 || index >= size)
            throw IndexOutOfBoundsException("Index $index out of bounds 0 to $size (exclusive).")
    }

    fun checkBoundsExclusiveRange(index: Int, toIndex: Int) {
        if (toIndex < index)
            throw IllegalArgumentException("Index range $index to $toIndex (exclusive) is invalid.")
        if (isEmpty() && index != 0 && toIndex != 0)
            throw IndexOutOfBoundsException("Index range $index to $toIndex (exclusive) out of bounds on empty list.")
        if (index < 0 || /*toIndex < 0 ||*/ index >= size || toIndex > size)
            throw IndexOutOfBoundsException("Index range $index to $toIndex (exclusive) out of bounds 0 to $size (exclusive).")
    }

     fun commonEquals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is List<*>) return false

        for ((index, e) in this.withIndex()) {
            if (e != other[index])
                return false
        }

        return true
    }
}
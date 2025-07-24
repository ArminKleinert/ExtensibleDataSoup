package kleinert.soap.data

/**
 * This interface implements all List methods except `[size]`.
 * A good implementation of [get] should also be implemented separately.
 * The implementation does rely on [get] being O(1) for iteration.
 *
 * @author Armin Kleinert
 */
interface SimpleList<T> : MutableList<T> {
    override val size: Int

    /**
     * Returns the element at [index] without needing to check bounds.
     */
    fun getUnchecked(index: Int): T

    /**
     * Sets the element at [index] without needing to check bounds.
     *
     * Just throws [UnsupportedOperationException] by default.
     */
    fun setUnchecked(index: Int, element: T): T = throw UnsupportedOperationException()

    /**
     * Default implementation for this method. Checks bounds and then uses [setUnchecked].
     * Also uses [getUnchecked].
     * @throws IndexOutOfBoundsException if the indices are invalid.
     * @return The element which was at [index].
     */
    override fun set(index: Int, element: T): T {
        checkBounds(index)
        val temp = getUnchecked(index)
        setUnchecked(index, element)
        return temp
    }

    /**
     * Default implementation for this method. Checks bounds and then uses [getUnchecked].
     * @throws IndexOutOfBoundsException if the indices are invalid.
     * @return The element at [index].
     */
    override fun get(index: Int): T {
        checkBounds(index)
        return getUnchecked(index)
    }

    /**
     * Default implementation for this method.
     * Uses [size].
     */
    override fun isEmpty(): Boolean =
        size == 0

    /**
     * Default implementation for this method.
     * Iterates over the list, returning the last index of the element. The iteration order is start-to-end.
     * This is done because this interface may be used for implementing singly-linked lists.
     * Uses [iterator].
     */
    override fun lastIndexOf(element: T): Int {
        var lastIndex = -1
        for ((index, e) in withIndex()) {
            if (e == element)
                lastIndex = index
        }
        return lastIndex
    }

    /**
     * Default implementation for this method.
     * Iterates over the list, returning the first index of the element.
     * Uses [iterator].
     */
    override fun indexOf(element: T): Int {
        for ((index, e) in withIndex()) {
            if (e == element)
                return index
        }
        return -1
    }

    /**
     * Default implementation for this method.
     * Uses [contains].
     */
    override fun containsAll(elements: Collection<T>): Boolean {
        for (it in elements.toSet()) {
            if (!contains(it))
                return false
        }
        return true
    }

    /**
     * Default implementation for this method.
     * Uses [indexOf].
     */
    override fun contains(element: T): Boolean =
        indexOf(element) >= 0

    /**
     * @see listIterator
     */
    override fun iterator(): MutableIterator<T> = listIterator(0)

    /**
     * A default implementation of [MutableList.listIterator].
     */
    override fun listIterator(): MutableListIterator<T> = listIterator(0)

    /**
     * A default implementation of [MutableList.listIterator].
     */
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

        /**
         * Delegates to [SimpleList.set].
         */
        override fun set(element: T) {
            set(cursor, element)
        }

        /**
         * Delegates to [SimpleList.add].
         */
        override fun add(element: T) {
            add(cursor, element)
        }

        /**
         * Delegates to [SimpleList.remove].
         */
        override fun remove() {
            removeAt(cursor)
        }
    }

    /**
     * Creates a [MutableListView] for this list.
     *
     * @throws IndexOutOfBoundsException if the indices are invalid.
     */
    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> {
        checkBoundsExclusiveRange(fromIndex, toIndex)
        if (fromIndex == 0 && toIndex == size) return this
        return MutableListView(fromIndex, toIndex, this)
    }

    /**
     * Throws [UnsupportedOperationException].
     * @throws UnsupportedOperationException
     */
    override fun removeAt(index: Int): T = throw UnsupportedOperationException()

    /**
     * Throws [UnsupportedOperationException].
     * @throws UnsupportedOperationException
     */
    override fun add(element: T): Boolean = throw UnsupportedOperationException()

    /**
     * Throws [UnsupportedOperationException].
     * @throws UnsupportedOperationException
     */
    override fun add(index: Int, element: T) = throw UnsupportedOperationException()

    /**
     * Throws [UnsupportedOperationException].
     * @throws UnsupportedOperationException
     */
    override fun addAll(index: Int, elements: Collection<T>): Boolean = throw UnsupportedOperationException()

    /**
     * Throws [UnsupportedOperationException].
     * @throws UnsupportedOperationException
     */
    override fun addAll(elements: Collection<T>): Boolean = throw UnsupportedOperationException()

    /**
     * Throws [UnsupportedOperationException].
     * @throws UnsupportedOperationException
     */
    override fun clear() = throw UnsupportedOperationException()

    /**
     * Throws [UnsupportedOperationException].
     * @throws UnsupportedOperationException
     */
    override fun retainAll(elements: Collection<T>): Boolean = throw UnsupportedOperationException()

    /**
     * Throws [UnsupportedOperationException].
     * @throws UnsupportedOperationException
     */
    override fun removeAll(elements: Collection<T>): Boolean = throw UnsupportedOperationException()

    /**
     * Throws [UnsupportedOperationException].
     * @throws UnsupportedOperationException
     */
    override fun remove(element: T): Boolean = throw UnsupportedOperationException()

    /**
     * Checks if the index is within bounds.
     *
     * @throws IndexOutOfBoundsException if [index] is not in range or if the list is empty.
     */
    fun checkBounds(index: Int) {
        if (isEmpty())
            throw IndexOutOfBoundsException("Index $index is not in empty list.")
        if (index < 0 || index >= size)
            throw IndexOutOfBoundsException("Index $index out of bounds 0 to $size (exclusive).")
    }

    /**
     * Checks if the indices are within bounds.
     *
     * @throws IllegalArgumentException If [toIndex] is smaller than [index].
     * @throws IndexOutOfBoundsException If one of the indices is not in bounds.
     */
    fun checkBoundsExclusiveRange(index: Int, toIndex: Int) {
        if (toIndex < index)
            throw IllegalArgumentException("Index range $index to $toIndex (exclusive) is invalid.")
        if (isEmpty() && index != 0 && toIndex != 0)
            throw IndexOutOfBoundsException("Index range $index to $toIndex (exclusive) out of bounds on empty list.")
        if (index < 0 || /*toIndex < 0 ||*/ index >= size || toIndex > size)
            throw IndexOutOfBoundsException("Index range $index to $toIndex (exclusive) out of bounds 0 to $size (exclusive).")
    }

    /**
     * A default implementation for [Object.equals] for all classes which implement [SimpleList].
     * Kotlin does not allow direct overriding of [Object] methods from inside interfaces.
     */
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
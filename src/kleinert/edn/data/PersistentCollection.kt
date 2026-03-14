package kleinert.edn.data

/**
 * @author Armin Kleinert
 */
private class EdnIterator<T>(private val inner: ListIterator<T>) : MutableListIterator<T> {
    constructor(coll: Collection<T>) : this(if (coll is List<T>) coll.listIterator() else coll.toList().listIterator())

    override fun hasNext(): Boolean = inner.hasNext()
    override fun hasPrevious(): Boolean = inner.hasPrevious()
    override fun next(): T = inner.next()
    override fun nextIndex(): Int = inner.nextIndex()
    override fun previous(): T = inner.previous()
    override fun previousIndex(): Int = inner.previousIndex()

    /**
     * Throws [UnsupportedOperationException].
     * @throws UnsupportedOperationException
     */
    override fun add(element: T): Unit = throw UnsupportedOperationException("Not possible on persistent iterators.")

    /**
     * Throws [UnsupportedOperationException].
     * @throws UnsupportedOperationException
     */
    override fun remove() = throw UnsupportedOperationException("Not possible on persistent iterators.")

    /**
     * Throws [UnsupportedOperationException].
     * @throws UnsupportedOperationException
     */
    override fun set(element: T) = throw UnsupportedOperationException("Not possible on persistent iterators.")
}

/**
 * A wrapper around a [MutableSet].
 *
 * @param T The type of the elements.
 *
 * @property sorted
 * @property ordered
 *
 * @author Armin Kleinert
 */
class EdnSet<T> : Set<T> {
    companion object {
        fun <T> of(vararg elements: T): EdnSet<T> = EdnSet(LinkedHashSet(elements.toList()))
        fun <T> wrap(xs: Set<T>): EdnSet<T> = EdnSet(xs)
        fun <T> wrap(xs: List<T>): EdnSet<T> = EdnSet(xs)
    }

    private val inner: Set<T>

    constructor(xs: List<T>) {
        this.inner = LinkedHashSet(xs)
    }

    constructor(xs: Collection<T>) {
        this.inner = LinkedHashSet(xs)
    }

    constructor(xs: Set<T>) {
        this.inner = xs.minus(xs).plus(xs) // Copy the set without changing its type.
    }

    override val size: Int
        get() = inner.size

    /**
     * Checks whether the [inner] collection is empty.
     */
    override fun isEmpty(): Boolean = inner.isEmpty()

    /**
     * Checks whether the [inner] collection contains all elements from the input collection.
     */
    override fun containsAll(elements: Collection<T>): Boolean = inner.containsAll(elements)

    /**
     * Checks whether the [inner] collection contains the input.
     */
    override fun contains(element: T): Boolean = inner.contains(element)

    /**
     * Returns a [EdnIterator] for this collection.
     */
    override fun iterator(): Iterator<T> = EdnIterator(inner)

    override fun equals(other: Any?): Boolean {
        return inner == other
    }

    override fun hashCode(): Int {
        return inner.hashCode()
    }

    override fun toString(): String {
        return inner.toString()
    }
}

/**
 * A wrapper around a [Map].
 * @param K The type of the keys.
 * @param V The type of the values.
 *
 * @property sorted
 * @property ordered
 *
 * @author Armin Kleinert
 */
class EdnMap<K, V> : Map<K, V> {
    class Entry<K, V>(override val key: K, override val value: V) : Map.Entry<K, V> {
        override fun toString(): String {
            return "[$key=$value]"
        }

        override fun equals(other: Any?): Boolean {
            if (other === this) return true
            if (other !is Map.Entry<*, *>) return false
            return key == other.key && value == other.value
        }

        override fun hashCode(): Int {
            var result = key?.hashCode() ?: 0
            result = 31 * result + (value?.hashCode() ?: 0)
            return result
        }
    }

    private val inner: Map<K, V>

    companion object {
        fun <K, V> of(vararg entries: Pair<K, V>): EdnMap<K, V> = EdnMap(entries.toList())
        fun <K, V> wrap(xs: Map<K, V>): EdnMap<K, V> = EdnMap(xs)
        fun <K, V> wrap(xs: List<Pair<K, V>>): EdnMap<K, V> = EdnMap(xs)
    }

    constructor(xs: List<Pair<K, V>>) {
        inner = LinkedHashMap(xs.size * 2)
        for ((k, v) in xs)
            inner[k] = v
    }

    constructor(xs: Collection<Pair<K, V>>) {
        inner = LinkedHashMap(xs.size * 2)
        for ((k, v) in xs)
            inner[k] = v
    }

    constructor(xs: Map<K, V>) {
        this.inner = xs
    }

    override val entries: Set<Map.Entry<K, V>>
        get() = EdnSet(inner.entries.mapTo(mutableSetOf()) { Entry(it.key, it.value) })

    override val keys: Set<K>
        get() = EdnSet(inner.keys)

    override val size: Int
        get() = inner.size

    override val values: Collection<V>
        get() = EdnSet(inner.values.toSet())

    override fun get(key: K): V? = inner[key]
    override fun containsValue(value: V): Boolean = inner.containsValue(value)
    override fun containsKey(key: K): Boolean = inner.containsKey(key)
    override fun isEmpty(): Boolean = inner.isEmpty()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Map<*, *>) return false

        if (size != other.size) return false

        for ((k, v) in entries)
            if (other.containsKey(k) && other[k] != v)
                return false

        return true
    }

    override fun hashCode(): Int {
        return inner.hashCode()
    }

    override fun toString(): String {
        return inner.toString()
    }
}

/**
 * A wrapper around a [List]. This class is distinct from [EdnVector] only in name.
 * [kleinert.edn.EDN] makes a distinction between this and [EdnVector].
 *
 * @param T The type of the elements.
 *
 * @author Armin Kleinert
 */
class EdnList<T> : List<T> {
    companion object {
        fun <T> of(vararg elements: T): EdnList<T> = EdnList(elements.toList())

        fun <T> wrap(xs: List<T>): EdnList<T> = EdnList(xs, true)
    }

    private val inner: List<T>

    private constructor(xs: List<T>, wrapMarker: Boolean) {
        inner = xs
        !wrapMarker
    }

    constructor(xs: List<T>) {
        inner = xs.toList()
    }

    constructor(xs: Collection<T>) : this(xs.toList())

    override val size: Int
        get() = inner.size

    override fun get(index: Int): T = inner[index]
    override fun isEmpty(): Boolean = inner.isEmpty()
    override fun iterator(): Iterator<T> = EdnIterator(inner.toMutableList().listIterator())
    override fun listIterator(): ListIterator<T> = EdnIterator(inner.toMutableList().listIterator())
    override fun listIterator(index: Int): ListIterator<T> = EdnIterator(inner.toMutableList().listIterator(index))

    override fun subList(fromIndex: Int, toIndex: Int): EdnList<T> =
        EdnList(inner.toMutableList().subList(fromIndex, toIndex))

    override fun lastIndexOf(element: T): Int = inner.lastIndexOf(element)
    override fun indexOf(element: T): Int = inner.indexOf(element)
    override fun containsAll(elements: Collection<T>): Boolean = inner.containsAll(elements)
    override fun contains(element: T): Boolean = inner.contains(element)

    override fun equals(other: Any?): Boolean {
        return inner == other
    }

    override fun hashCode(): Int {
        return inner.hashCode()
    }

    override fun toString(): String {
        return inner.toString()
    }
}

/**
 * A wrapper around a [List]. This class is distinct from [EdnList] only in name.
 * [kleinert.edn.EDN] makes a distinction between this and [EdnList].
 *
 * @param T The type of the elements.
 *
 * @author Armin Kleinert
 */
class EdnVector<T> : List<T> {
    companion object {
        fun <T> of(vararg elements: T): EdnVector<T> = EdnVector(elements.toList())

        fun <T> wrap(xs: List<T>): EdnVector<T> = EdnVector(xs, true)
    }

    private val inner: List<T>

    private constructor(xs: List<T>, wrapMarker: Boolean) {
        inner = xs
        !wrapMarker
    }

    constructor(xs: List<T>) {
        inner = xs.toList()
    }

    constructor(xs: Collection<T>) {
        inner = xs.toList()
    }

    override val size: Int
        get() = inner.size

    override fun get(index: Int): T = inner[index]
    override fun isEmpty(): Boolean = inner.isEmpty()
    override fun iterator(): Iterator<T> = EdnIterator(inner.toMutableList().listIterator())
    override fun listIterator(): ListIterator<T> = EdnIterator(inner.toMutableList().listIterator())
    override fun listIterator(index: Int): ListIterator<T> = EdnIterator(inner.toMutableList().listIterator(index))

    override fun subList(fromIndex: Int, toIndex: Int): EdnVector<T> =
        EdnVector(inner.toMutableList().subList(fromIndex, toIndex))

    override fun lastIndexOf(element: T): Int = inner.lastIndexOf(element)
    override fun indexOf(element: T): Int = inner.indexOf(element)
    override fun containsAll(elements: Collection<T>): Boolean = inner.containsAll(elements)
    override fun contains(element: T): Boolean = inner.contains(element)

    override fun equals(other: Any?): Boolean {
        return inner == other
    }

    override fun hashCode(): Int {
        return inner.hashCode()
    }

    override fun toString(): String {
        return inner.toString()
    }
}

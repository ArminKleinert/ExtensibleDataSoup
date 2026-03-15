package kleinert.edn.data

import java.util.SequencedCollection
import java.util.SequencedMap
import java.util.SequencedSet

/**
 * @author Armin Kleinert
 */
private class EdnIterator<T>(private val inner: ListIterator<T>) : MutableListIterator<T>, MutableIterator<T> {
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
 * @author Armin Kleinert
 */
class EdnSet<T> : Set<T>, SequencedSet<T> {
    companion object {
        fun <T> of(vararg elements: T): EdnSet<T> = EdnSet(elements.toList())
        fun <T> create(xs: List<T>): EdnSet<T> = EdnSet(xs)
        fun <T> create(xs: SequencedCollection<T>): EdnSet<T> = EdnSet(xs)
        fun <T> create(xs: EdnSet<T>): EdnSet<T> = xs
    }

    private val inner: SequencedSet<T>

    private constructor(xs: List<T>) {
        this.inner = LinkedHashSet(xs)
    }

    private constructor(xs: SequencedCollection<T>) {
        this.inner = LinkedHashSet(xs)
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
    override fun iterator(): MutableIterator<T> = EdnIterator(inner)

    override fun equals(other: Any?): Boolean = inner == other
    override fun hashCode(): Int = inner.hashCode()
    override fun toString(): String = inner.toString()
    override fun reversed(): SequencedSet<T> = create(inner.reversed())

    override fun add(element: T) = throw UnsupportedOperationException()
    override fun remove(element: T) = throw UnsupportedOperationException()
    override fun addAll(elements: Collection<T>) = throw UnsupportedOperationException()
    override fun removeAll(elements: Collection<T>) = throw UnsupportedOperationException()
    override fun retainAll(elements: Collection<T>) = throw UnsupportedOperationException()
    override fun clear() = throw UnsupportedOperationException()
}

/**
 * A wrapper around a [Map].
 * @param K The type of the keys.
 * @param V The type of the values.
 *
 * @author Armin Kleinert
 */
class EdnMap<K, V> : Map<K, V>, SequencedMap<K, V> {
    class Entry<K, V>(override val key: K, override val value: V) : MutableMap.MutableEntry<K, V> {
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

        override fun setValue(newValue: V): V = throw UnsupportedOperationException()
    }

    private val inner: SequencedMap<K, V>

    companion object {
        fun <K, V> of(vararg entries: Pair<K, V>): EdnMap<K, V> = EdnMap(entries.toList())
        fun <K, V> create(xs: SequencedMap<K, V>): EdnMap<K, V> = EdnMap(xs)
        fun <K, V> create(xs: SequencedCollection<Pair<K, V>>): EdnMap<K, V> = EdnMap(xs)
        fun <K, V> create(xs: List<Pair<K, V>>): EdnMap<K, V> = EdnMap(xs)
        fun <K, V> create(xs: EdnMap<K, V>): EdnMap<K, V> = xs
    }

    private constructor(xs: List<Pair<K, V>>) {
        inner = LinkedHashMap(xs.size)
        for ((k, v) in xs)
            inner[k] = v
    }

    private constructor(xs: SequencedCollection<Pair<K, V>>) {
        inner = LinkedHashMap(xs.size)
        for ((k, v) in xs)
            inner[k] = v
    }

    private constructor(xs: SequencedMap<K, V>) {
        this.inner = LinkedHashMap(xs)
    }

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = EdnSet.create(inner.entries.map { Entry(it.key, it.value) })

    override fun put(key: K?, value: V?): V = throw UnsupportedOperationException()
    override fun remove(key: K?): V = throw UnsupportedOperationException()
    override fun putAll(from: Map<out K?, V?>) = throw UnsupportedOperationException()
    override fun clear() = throw UnsupportedOperationException()

    override val keys: MutableSet<K>
        get() = EdnSet.create(LinkedHashSet(inner.keys))

    override val size: Int
        get() = inner.size

    override val values: MutableCollection<V>
        get() = EdnSet.create(LinkedHashSet(inner.values))

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

    override fun reversed(): SequencedMap<K?, V?> = create(inner.reversed())
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

        fun <T> create(xs: List<T>): EdnList<T> = EdnList(xs)
        fun <T> create(xs: SequencedCollection<T>): EdnList<T> = EdnList(xs)
        fun <T> create(xs: EdnList<T>): EdnList<T> = xs
    }

    private val inner: List<T>

    private constructor(xs: List<T>) {
        inner = xs.toList()
    }

    private constructor(xs: SequencedCollection<T>) : this(xs.toList())

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

        fun <T> create(xs: List<T>): EdnVector<T> = EdnVector(xs)
        fun <T> create(xs: SequencedCollection<T>): EdnVector<T> = EdnVector(xs)
        fun <T> create(xs: EdnVector<T>): EdnVector<T> = xs
    }

    private val inner: List<T>

    private constructor(xs: List<T>) {
        inner = xs.toList()
    }

    private constructor(xs: SequencedCollection<T>) {
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

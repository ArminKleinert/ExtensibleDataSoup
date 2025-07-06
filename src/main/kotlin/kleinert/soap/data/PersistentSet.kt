package kleinert.soap.data

class PersistentIterator<T>(private val inner: ListIterator<T>) : MutableListIterator<T> {
    constructor(coll: Collection<T>) : this(if (coll is List<T>) coll.listIterator() else coll.toList().listIterator())

    override fun hasNext(): Boolean = inner.hasNext()
    override fun hasPrevious(): Boolean = inner.hasPrevious()
    override fun next(): T = inner.next()
    override fun nextIndex(): Int = inner.nextIndex()
    override fun previous(): T = inner.previous()
    override fun previousIndex(): Int = inner.previousIndex()

    override fun add(element: T): Unit = throw UnsupportedOperationException("Not possible on persistent iterators.")
    override fun remove() = throw UnsupportedOperationException("Not possible on persistent iterators.")
    override fun set(element: T) = throw UnsupportedOperationException("Not possible on persistent iterators.")
}

class PersistentSet<T>(private val inner: Set<T>, val sorted: Boolean = false, val ordered: Boolean = false) :
    MutableSet<T> {
    override val size: Int
        get() = inner.size

    override fun isEmpty(): Boolean = inner.isEmpty()
    override fun containsAll(elements: Collection<T>): Boolean = inner.containsAll(elements)
    override fun contains(element: T): Boolean = inner.contains(element)
    override fun iterator(): MutableIterator<T> = PersistentIterator(inner)

    override fun add(element: T): Boolean = throw UnsupportedOperationException("Not possible on persistent sets.")
    override fun addAll(elements: Collection<T>): Boolean =
        throw UnsupportedOperationException("Not possible on persistent sets.")

    override fun clear() = throw UnsupportedOperationException("Not possible on persistent sets.")
    override fun retainAll(elements: Collection<T>): Boolean =
        throw UnsupportedOperationException("Not possible on persistent sets.")

    override fun removeAll(elements: Collection<T>): Boolean =
        throw UnsupportedOperationException("Not possible on persistent sets.")

    override fun remove(element: T): Boolean = throw UnsupportedOperationException("Not possible on persistent sets.")

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


class PersistentMap<K, V>(
    private val inner: Map<K, V>,
    val sorted: Boolean = false,
    val ordered: Boolean = false
) :
    MutableMap<K, V> {
    class Entry<K, V>(override val key: K, override val value: V) : MutableMap.MutableEntry<K, V> {
        override fun setValue(newValue: V): V = throw UnsupportedOperationException("Not possible on persistent map entry.")

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

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = PersistentSet(inner.entries.mapTo(mutableSetOf()) { Entry(it.key, it.value) })
    override val keys: MutableSet<K>
        get() = PersistentSet(inner.keys)
    override val size: Int
        get() = inner.size
    override val values: MutableCollection<V>
        get() = PersistentSet(inner.values.toSet())

    override fun get(key: K): V? = inner[key]
    override fun containsValue(value: V): Boolean = inner.containsValue(value)
    override fun containsKey(key: K): Boolean = inner.containsKey(key)
    override fun isEmpty(): Boolean = inner.isEmpty()

    override fun clear() = throw UnsupportedOperationException("Not possible on persistent map.")
    override fun remove(key: K): V? = throw UnsupportedOperationException("Not possible on persistent map.")
    override fun putAll(from: Map<out K, V>) = throw UnsupportedOperationException("Not possible on persistent map.")
    override fun put(key: K, value: V): V? = throw UnsupportedOperationException("Not possible on persistent map.")

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Map<*, *>) return false

        other as Map<*, *>

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

class PersistentList<T>(private val inner: List<T>) : MutableList<T> {
    override val size: Int
        get() = inner.size

    override fun clear() = throw UnsupportedOperationException("Not possible on persistent map.")
    override fun addAll(elements: Collection<T>): Boolean =
        throw UnsupportedOperationException("Not possible on persistent map.")

    override fun addAll(index: Int, elements: Collection<T>): Boolean =
        throw UnsupportedOperationException("Not possible on persistent map.")

    override fun add(index: Int, element: T) = throw UnsupportedOperationException("Not possible on persistent map.")
    override fun add(element: T): Boolean = throw UnsupportedOperationException("Not possible on persistent map.")
    override fun get(index: Int): T = inner[index]
    override fun isEmpty(): Boolean = inner.isEmpty()
    override fun iterator(): MutableIterator<T> = inner.toMutableList().iterator()
    override fun listIterator(): MutableListIterator<T> = inner.toMutableList().listIterator()
    override fun listIterator(index: Int): MutableListIterator<T> = inner.toMutableList().listIterator(index)
    override fun removeAt(index: Int): T = throw UnsupportedOperationException("Not possible on persistent map.")
    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> =
        inner.toMutableList().subList(fromIndex, toIndex)

    override fun set(index: Int, element: T): T = throw UnsupportedOperationException("Not possible on persistent map.")
    override fun retainAll(elements: Collection<T>): Boolean =
        throw UnsupportedOperationException("Not possible on persistent map.")

    override fun removeAll(elements: Collection<T>): Boolean =
        throw UnsupportedOperationException("Not possible on persistent map.")

    override fun remove(element: T): Boolean = throw UnsupportedOperationException("Not possible on persistent map.")
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

class PersistentVector<T>(private val inner: List<T>) : MutableList<T> {
    override val size: Int
        get() = inner.size

    override fun clear() = throw UnsupportedOperationException("Not possible on persistent map.")
    override fun addAll(elements: Collection<T>): Boolean =
        throw UnsupportedOperationException("Not possible on persistent map.")

    override fun addAll(index: Int, elements: Collection<T>): Boolean =
        throw UnsupportedOperationException("Not possible on persistent map.")

    override fun add(index: Int, element: T) = throw UnsupportedOperationException("Not possible on persistent map.")
    override fun add(element: T): Boolean = throw UnsupportedOperationException("Not possible on persistent map.")
    override fun get(index: Int): T = inner[index]
    override fun isEmpty(): Boolean = inner.isEmpty()
    override fun iterator(): MutableIterator<T> = inner.toMutableList().iterator()
    override fun listIterator(): MutableListIterator<T> = inner.toMutableList().listIterator()
    override fun listIterator(index: Int): MutableListIterator<T> = inner.toMutableList().listIterator(index)
    override fun removeAt(index: Int): T = throw UnsupportedOperationException("Not possible on persistent map.")
    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> =
        inner.toMutableList().subList(fromIndex, toIndex)

    override fun set(index: Int, element: T): T = throw UnsupportedOperationException("Not possible on persistent map.")
    override fun retainAll(elements: Collection<T>): Boolean =
        throw UnsupportedOperationException("Not possible on persistent map.")

    override fun removeAll(elements: Collection<T>): Boolean =
        throw UnsupportedOperationException("Not possible on persistent map.")

    override fun remove(element: T): Boolean = throw UnsupportedOperationException("Not possible on persistent map.")
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

//    fun defaultAdd(a: Number, b: Number): Number {
//        return when (a) {
//            is Double -> {
//                when (b) {
//                    is Double -> BigDecimal.valueOf(a) + BigDecimal.valueOf(b)
//                    is BigDecimal -> BigDecimal.valueOf(a) + b
//                    is Ratio -> BigDecimal.valueOf(a) + b.toBigDecimal()
//                    is Long -> a + b
//                    is BigInteger -> BigDecimal.valueOf(a) + BigDecimal(b)
//                    else -> throw IllegalArgumentException()
//                }
//            }
//
//            is BigInteger -> {
//                when (b) {
//                    is Double -> BigDecimal(a) + BigDecimal.valueOf(b)
//                    is BigDecimal -> BigDecimal(a) + b
//                    is Ratio -> BigDecimal(a) + b.toBigDecimal()
//                    is Long -> a + BigInteger.valueOf(b)
//                    is BigInteger -> a + b
//                    else -> throw IllegalArgumentException()
//                }
//            }
//
//            is BigDecimal -> {
//                when (b) {
//                    is Double -> a + BigDecimal.valueOf(b)
//                    is BigDecimal -> a + b
//                    is Ratio -> a + b.toBigDecimal()
//                    is Long -> a + BigDecimal.valueOf(b)
//                    is BigInteger -> a + BigDecimal(b)
//                    else -> throw IllegalArgumentException()
//                }
//            }
//
//            is Ratio -> {
//                when (b) {
//                    is Double -> a.toBigDecimal() + BigDecimal.valueOf(b)
//                    is BigDecimal -> a.toBigDecimal() + b
//                    is Ratio -> a + b
//                    is Long -> a + Ratio.of(b)
//                    is BigInteger -> a + Ratio.of(b)
//                    else -> throw IllegalArgumentException()
//                }
//            }
//
//            else -> 2
//        }
//    }

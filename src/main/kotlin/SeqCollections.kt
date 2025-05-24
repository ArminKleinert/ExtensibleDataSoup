
class SeqMap<K, V>(
    entries : Iterable<Pair<K,V>>
) : Map<K, V> {
    private var inner: Array<Pair<K, V>?> = entries.toList().toTypedArray()
    private var used: Int = inner.size

    class Entry<K, V>(override val key: K, override var value: V) : Map.Entry<K, V> {
        override fun toString(): String = "($key, $value)"
    }

    private fun findIndex(key: K): Int {
        for (i in 0..<used) {
            if (inner[i]!!.first == key)
                return i
        }
        return -1
    }

    override val entries: MutableSet<Map.Entry<K, V>>
        get() = inner.mapNotNullTo(mutableSetOf()) {
            if (it != null) Entry(it.first, it.second) else null
        }

    override val keys: MutableSet<K>
        get() = mutableSetOf<K>().let {
            for (pair in inner) {
                if (pair == null) continue
                it.add(pair.first)
            }
            it
        }

    val keyList: MutableList<K>
        get() = mutableListOf<K>().let {
            for (pair in inner) {
                if (pair == null) continue
                it.add(pair.first)
            }
            it
        }

    override val size: Int
        get() = used

    override val values: MutableCollection<V>
        get() = mutableSetOf<V>().let {
            for (pair in inner) {
                if (pair == null) continue
                it.add(pair.second)
            }
            it
        }

    override fun isEmpty(): Boolean = used == 0

    override fun get(key: K): V? {
        val i = findIndex(key)
        if (i >= 0) return inner[i]!!.second
        return null
    }

    override fun containsValue(value: V): Boolean {
        for (i in 0..<used) {
            val atI = inner[i]?.second
            if (atI != null && atI == value) return true
        }
        return false
    }

    override fun containsKey(key: K): Boolean {
        val i = findIndex(key)
        return i >= 0
    }

    override fun toString(): String =
        "{" + inner.filterNotNull().joinToString(", ") { "${it.first}=${it.second}" } + "}"
}

class SeqSet<T>(values : Iterable<T>) : Set<T> {
    private val inner : SeqMap<T, Boolean> = SeqMap(values.map {it to true})

    override val size: Int
        get() = inner.size

    override fun isEmpty(): Boolean = inner.isEmpty()

    override fun iterator(): Iterator<T> = inner.keyList.iterator()

    override fun containsAll(elements: Collection<T>): Boolean =
        elements.all { inner.containsKey(it) }

    override fun contains(element: T): Boolean = inner.containsKey(element)

    override fun toString(): String =
        "[" + inner.keyList.joinToString(", ") + "]"
}

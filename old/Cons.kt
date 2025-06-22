package kleinert.soap

import kleinert.soap.EmptyList

interface Cons<T> : List<T>, Iterable<T> {
    companion object {
        fun <T> toCells(coll: Iterable<T>): Cons<T> {
            if (coll is Cons<T>) return coll
            if (!coll.iterator().hasNext()) return EmptyList()

            var res: Cons<T> = EmptyList()
            for (e in coll.reversed()) {
                res = ConsCell(e, res)
            }
            return res
        }

        fun <T> of(vararg elements: T): Cons<T> {
            if (elements.isEmpty())
                return EmptyList()

            var res: Cons<T> = EmptyList()
            for (e in elements.reversed()) {
                res = ConsCell(e, res)
            }
            return res
        }

        operator fun <T> invoke(collection: Collection<T>) = toCells(collection.asIterable())
        operator fun <T> invoke(collection: Iterable<T>) = toCells(collection)
        operator fun <T> invoke(arr: Array<T>) = toCells(arr.asIterable())
        operator fun <T> invoke() = EmptyList<T>()
    }

    val car: T

    val cdr: Cons<T>

    fun first(): T = car

    fun cons(element: T): Cons<T> = ConsCell(element, this)

    override val size: Int
        get() {
            var count = 0
            for (ignored in this) {
                count++
            }
            return count
        }

    override fun isEmpty(): Boolean

    override fun iterator(): Iterator<T>

    override fun get(index: Int): T = try {
        when {
            index == 0 -> car
            index == 1 -> cdr.first()
            index > 0 -> drop(index).first()
            else -> throw IndexOutOfBoundsException(index)
        }
    } catch (nsee: NoSuchElementException) {
        throw IndexOutOfBoundsException(index)
    }

    override fun listIterator(): ListIterator<T> = toList().listIterator()

    override fun listIterator(index: Int): ListIterator<T> = toList().listIterator(index)

    override fun subList(fromIndex: Int, toIndex: Int): List<T> = toList().subList(fromIndex, toIndex)

    override fun lastIndexOf(element: T): Int {
        var indexFound = -1
        for ((index, t) in withIndex()) {
            if (t == element) indexFound = index
        }
        return indexFound
    }

    override fun indexOf(element: T): Int {
        for ((index, t) in withIndex()) {
            if (t == element) return index
        }
        return -1
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        for (e in elements)
            if (!contains(e)) return false
        return true
    }

    override fun contains(element: T): Boolean = indexOf(element) != -1

    val cadr: T
        get() = cdr.car
    val cddr: List<T>
        get() = drop(2)
    val caddr: T
        get() = cdr.cdr.car
    val cdddr: List<T>
        get() = drop(3)
    val cadddr: T
        get() = cdr.cdr.cdr.car
    val cddddr: List<T>
        get() = drop(4)

    fun <R> map(f: (T) -> R) = if (isEmpty()) this else Cons(asIterable().map(f))
    fun filter(pred: (T) -> Boolean): Cons<T> = Cons(asIterable().filter(pred))
    fun filterNot(pred: (T) -> Boolean): Cons<T> = Cons(asIterable().filterNot(pred))
    fun <R> mapIndexed(f: (Int, T) -> R): Cons<R> = Cons(asIterable().mapIndexed(f))

    fun asSequence() = sequence {
        var cell = this@Cons.cdr
        while (cell.isEmpty()) {
            yield(cell.car)
            cell = cell.cdr
        }
    }

    fun drop(n: Int): Cons<T> {
        var res = this
        var counter = n
        while (counter > 0) {
            counter--
            res = res.cdr
        }
        return res
    }
}
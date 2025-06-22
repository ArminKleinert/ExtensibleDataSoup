package kleinert.soap.cons

import java.util.*
import kotlin.random.Random

sealed interface Cons<T> : List<T>, Iterable<T> {
    companion object {
        fun <T> of(vararg elements: T) = VList(elements.toList())

        operator fun <T> invoke(coll: Iterable<T>) = if (coll is Cons<T>) coll else VList(coll)
        operator fun <T> invoke(arr: Array<T>) = VList(arr)
        operator fun <T> invoke() = VList.of<T>()
    }

    val car: T

    val cdr: Cons<T>

    override val size: Int

    // Haskell style
    fun head(): T = car

    // Haskell style
    fun tail(): Cons<T> = cdr

    // Clojure & Logo language style
    fun first(): T = car

    // Logo language style
    fun butfirst(): Cons<T> = cdr

    // Clojure style
    fun next(): Cons<T>? {
        val r = cdr
        if (r.isEmpty()) return null
        return r
    }

    // Clojure style
    fun rest(): Cons<T> = cdr

    fun cons(element: T): Cons<T> = ConsHead(element, this)

    override fun isEmpty(): Boolean

    override fun iterator(): Iterator<T>

    fun cleared(): Cons<T>

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

    override fun containsAll(elements: Collection<T>): Boolean {
        for (e in elements)
            if (!contains(e)) return false
        return true
    }

    override fun contains(element: T): Boolean {
        val iterator = iterator()
        while (iterator.hasNext()) {
            val temp = iterator.next()
            if (temp == element) return true
        }
        return false
    }

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

    fun <R> map(f: (T) -> R): Cons<R> = sameTypeFromList(asIterable().map(f))
    fun filter(pred: (T) -> Boolean): Cons<T> = sameTypeFromList(asIterable().filter(pred))
    fun filterNot(pred: (T) -> Boolean): Cons<T> = sameTypeFromList(asIterable().filterNot(pred))
    fun <R> mapIndexed(f: (Int, T) -> R): Cons<R> = sameTypeFromList(asIterable().mapIndexed(f))
    fun <R> flatMap(f: (T) -> Iterable<R>): Cons<R> = sameTypeFromList(asIterable().flatMap(f))
    fun <T : Comparable<T>> Cons<T>.sorted(): Cons<T> = sameTypeFromList(asIterable().sorted())

    fun take(n: Int): Cons<T> = sameTypeFromList(asIterable().take(n))
    fun takeWhile(predicate: (T) -> Boolean): Cons<T> = sameTypeFromList(asIterable().takeWhile(predicate))
    fun dropWhile(predicate: (T) -> Boolean): Cons<T> = sameTypeFromList(asIterable().dropWhile(predicate))

    fun <R : Comparable<R>> sortedBy(selector: (T) -> R?): Cons<T> = sameTypeFromList(asIterable().sortedBy(selector))
    fun <R : Comparable<R>> sortedByDescending(selector: (T) -> R?): Cons<T> =
        sameTypeFromList(asIterable().sortedByDescending(selector))

    fun sortedWith(comparator: Comparator<in T>): Cons<T> = sameTypeFromList(asIterable().sortedWith(comparator))

    fun distinct(): Cons<T> = sameTypeFromList(asIterable().distinct())

    fun shuffled(random: Random = Random.Default): Cons<T> = sameTypeFromList(toList().shuffled(random))

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

    fun plus(element: T): Cons<T> = ConsSeq.concat(this, ConsHead(element, EmptyCons()))
    fun plus(other: Iterable<T>): Cons<T> = ConsSeq.concat(this, Cons(other))
    fun plus(other: List<T>): Cons<T> = ConsSeq.concat(this, CdrCodedList(other))
    fun plus(other: Cons<T>): Cons<T> = ConsSeq.concat(this, other)
    fun plus(other: VList<T>): Cons<T> = other.prepend(this)

    fun reversed(): Cons<T> {
        if (isEmpty())
            return this

        var res = cleared()
        for (it in this) {
            res = res.cons(it)
        }
        return res
    }

    fun commonToString(): String = StringBuilder()
        .append('[')
        .append(joinToString(", "))
        .append("]").toString()

    fun commonEqualityCheck(other: Any?): Boolean {
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

    fun <R> sameTypeFromList(list: List<R>): Cons<R>

    fun asIterable(): Iterable<T> = this

    fun toMutableList(): MutableList<T> = asIterable().toMutableList()

    fun toList(): List<T> = Collections.unmodifiableList(toMutableList())
}
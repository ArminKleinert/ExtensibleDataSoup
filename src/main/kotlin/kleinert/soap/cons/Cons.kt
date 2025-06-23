package kleinert.soap.cons

import java.util.*
import kotlin.random.Random

sealed interface Cons<T> : List<T>, Iterable<T> {
    companion object {
        fun <T> of(vararg elements: T): Cons<T> = if (elements.isEmpty()) nullCons() else VList(elements.toList())

        fun <T> fromIterable(coll: List<T>): Cons<T> = if (coll is Cons<T>) coll else CdrCodedList(coll)
        fun <T> fromIterable(coll: Iterable<T>): Cons<T> = if (coll is Cons<T>) coll else VList(coll)
        fun <T> fromIterable(arr: Array<T>): Cons<T> = VList(arr)

        fun <T> wrapList(list: List<T>): CdrCodedList<T> {
            return CdrCodedList(list, true)
        }

        fun <T> randomAccess(list: Iterable<T>): Cons<T> {
            return CdrCodedList(list.toList())
        }

        fun <T> log2Access(list: Iterable<T>): Cons<T> {
            return VList(list)
        }

        fun <T> singlyLinked(list: Iterable<T>): Cons<T> {
            var head: Cons<T> = nullCons()
            for (element in list.reversed()) {
                head = ConsCell(element, head)
            }
            return head
        }

        fun <T> cons(element: T, seq: Cons<T>): Cons<T> {
            return seq.cons(element)
        }
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

    fun cons(element: T): Cons<T> = ConsCell(element, this)

    override fun isEmpty(): Boolean

    override fun iterator(): Iterator<T> = asSequence().iterator()

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

    override fun subList(fromIndex: Int, toIndex: Int): Cons<T> = sameTypeFromList(toList().subList(fromIndex, toIndex))

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

    fun take(n: Int): Cons<T> = sameTypeFromList(asIterable().take(n))
    fun takeWhile(predicate: (T) -> Boolean): Cons<T> = sameTypeFromList(asIterable().takeWhile(predicate))

    fun drop(n: Int): Cons<T> {
        require(n >= 0) { "Requested element count $n is less than zero." }
        var countdown = n
        var rest: Cons<T> = this
        while (rest.isNotEmpty() && countdown > 0) {
            rest = rest.cdr
            countdown--
        }
        return rest
    }

    fun dropWhile(predicate: (T) -> Boolean): Cons<T> = sameTypeFromList(asIterable().dropWhile(predicate))

    fun <R : Comparable<R>> sortedBy(selector: (T) -> R?): Cons<T> = sameTypeFromList(asIterable().sortedBy(selector))

    fun <R : Comparable<R>> sortedByDescending(selector: (T) -> R?): Cons<T> =
        sameTypeFromList(asIterable().sortedByDescending(selector))

    fun sortedWith(comparator: Comparator<in T>): Cons<T> = sameTypeFromList(asIterable().sortedWith(comparator))

    fun distinct(): Cons<T> = sameTypeFromList(asIterable().distinct())

    fun shuffled(random: Random = Random.Default): Cons<T> = sameTypeFromList(toList().shuffled(random))

    fun asSequence(): Sequence<T> = sequence {
        var cell = this@Cons
        while (cell.isNotEmpty()) {
            yield(cell.car)
            cell = cell.cdr
        }
    }

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

    fun toMutableList(): MutableList<T> = asSequence().toMutableList()

    fun toList(): List<T> = Collections.unmodifiableList(toMutableList())

    fun isSingleton(): Boolean = isNotEmpty() && cdr.isEmpty()

    operator fun plus(element: T): Cons<T> = ConsPair.concat(this, ConsCell(element, nullCons()))

    operator fun plus(other: Iterable<T>): Cons<T> = ConsPair.concat(this, fromIterable(other))

    operator fun plus(other: List<T>): Cons<T> = ConsPair.concat(this, CdrCodedList(other))

    operator fun plus(other: Cons<T>): Cons<T> = ConsPair.concat(this, other)

    operator fun plus(other: VList<T>): VList<T> = other.prepend(this)
}

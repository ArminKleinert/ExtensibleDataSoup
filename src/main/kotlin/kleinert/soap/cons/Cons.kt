package kleinert.soap.cons

import java.util.*
import kotlin.random.Random

sealed interface Cons<T> : List<T>, Iterable<T> {
    companion object {
        fun <T> of(vararg elements: T): Cons<T> = if (elements.isEmpty()) nullCons() else VList(elements.toList())

        fun <T> fromIterable(coll: List<T>): Cons<T> = if (coll is Cons<T>) coll else CdrCodedList(coll)
        fun <T> fromIterable(coll: Iterable<T>): Cons<T> = if (coll is Cons<T>) coll else VList(coll)
        fun <T> fromIterable(arr: Array<T>): Cons<T> = VList(arr)
        fun <T> fromIterable(seq: Sequence<T>): Cons<T> = LazyCons.of(seq)

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

    fun count() = size

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

    fun shuffled(random: Random = Random.Default): Cons<T> =
        sameTypeFromList(toList().shuffled(random))

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

    fun isLazyType(): Boolean = false

    operator fun plus(element: T): Cons<T> = ConsPair.concat(this, ConsCell(element, nullCons()))

    operator fun plus(other: Iterable<T>): Cons<T> = ConsPair.concat(this, fromIterable(other))

    operator fun plus(other: List<T>): Cons<T> = ConsPair.concat(this, CdrCodedList(other))

    operator fun plus(other: Cons<T>): Cons<T> = ConsPair.concat(this, other)

    operator fun plus(other: VList<T>): Cons<T> =
        if (isLazyType()) ConsPair.concat(this, other)
        else other.prepend(this)

    fun <A : Appendable> joinTo( buffer: A, separator: CharSequence = ", ", prefix: CharSequence = "", postfix: CharSequence = "", limit: Int = -1, truncated: CharSequence = "...", transform: ((T) -> CharSequence)? = null): A =
        asSequence().joinTo(buffer, separator, prefix, postfix, limit, truncated, transform)

    fun joinToString( separator: CharSequence = ", ", prefix: CharSequence = "", postfix: CharSequence = "", limit: Int = -1, truncated: CharSequence = "...", transform: ((T) -> CharSequence)? = null): String =
        asSequence().joinToString(separator, prefix, postfix, limit, truncated, transform)

    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////

    fun chunked(size: Int): Cons<List<T>> = fromIterable(asIterable().chunked(size))
    fun <R> chunked(size: Int, transform: (List<T>) -> R): Cons<R> = fromIterable(asIterable().chunked(size, transform))
    fun distinct(): Cons<T> = fromIterable(asIterable().distinct())
    fun <K> distinctBy(selector: (T) -> K): Cons<T> = fromIterable(asIterable().distinctBy(selector))
    fun dropWhile(predicate: (T) -> Boolean): Cons<T> = fromIterable(asIterable().dropWhile(predicate))
    fun filter(predicate: (T) -> Boolean): Cons<T> = fromIterable(asIterable().filter(predicate))
    fun filterIndexed(predicate: (index: Int, T) -> Boolean): Cons<T> =
        fromIterable(asIterable().filterIndexed(predicate))

    fun filterNot(predicate: (T) -> Boolean): Cons<T> = fromIterable(asIterable().filterNot(predicate))
    fun filterNotNull(): Cons<T> = fromIterable(asIterable().filterNotNull())
    fun <R> flatMap(transform: (T) -> Iterable<R>): Cons<R> = fromIterable(asIterable().flatMap(transform))
    fun <R> flatMap(transform: (T) -> Sequence<R>): Cons<R> = fromIterable(asIterable().flatMap(transform))
    fun <R> flatMapIndexed(transform: (index: Int, T) -> Iterable<R>): Cons<R> =
        fromIterable(asIterable().flatMapIndexed(transform))

    fun <R> flatMapIndexed(transform: (index: Int, T) -> Sequence<R>): Cons<R> =
        fromIterable(asIterable().flatMapIndexed(transform))

    fun ifEmpty(defaultValue: () -> Cons<T>): Cons<T> =
        fromIterable(asSequence().ifEmpty { defaultValue().asSequence() })

    fun <R> map(transform: (T) -> R): Cons<R> = fromIterable(asIterable().map(transform))
    fun <R> mapIndexed(transform: (index: Int, T) -> R): Cons<R> = fromIterable(asIterable().mapIndexed(transform))
    fun <R : Any> mapIndexedNotNull(transform: (index: Int, T) -> R?): Cons<R> =
        fromIterable(asIterable().mapIndexedNotNull(transform))

    fun <R : Any> mapNotNull(transform: (T) -> R?): Cons<R> = fromIterable(asIterable().mapNotNull(transform))
    fun minus(element: T): Cons<T> = fromIterable(asIterable().minus(element))
    fun minus(elements: Set<T>): Cons<T> = fromIterable(asIterable().minus(elements.toSet()))
    fun minus(elements: Iterable<T>): Cons<T> = minus(elements.toSet())
    fun onEach(action: (T) -> Unit): Cons<T> = fromIterable(asIterable().onEach(action))
    fun onEachIndexed(action: (index: Int, T) -> Unit): Cons<T> = fromIterable(asIterable().onEachIndexed(action))
    fun requireNoNulls(): Cons<T> = fromIterable(asIterable().requireNoNulls())
    fun <R> runningFold(initial: R, operation: (acc: R, T) -> R): Cons<R> =
        fromIterable(asIterable().runningFold(initial, operation))

    fun <R> runningFoldIndexed(initial: R, operation: (index: Int, acc: R, T) -> R): Cons<R> =
        fromIterable(asIterable().runningFoldIndexed(initial, operation))

    fun runningReduce(operation: (acc: T, T) -> T): Cons<T> = fromIterable(asIterable().runningReduce(operation))
    fun runningReduceIndexed(operation: (index: Int, acc: T, T) -> T): Cons<T> =
        fromIterable(asIterable().runningReduceIndexed(operation))

    fun <R> scan(initial: R, operation: (acc: R, T) -> R): Cons<R> = fromIterable(asIterable().scan(initial, operation))
    fun <R> scanIndexed(initial: R, operation: (index: Int, acc: R, T) -> R): Cons<R> =
        fromIterable(asIterable().scanIndexed(initial, operation))

    fun <R : Comparable<R>> sortedBy(selector: (T) -> R?): Cons<T> = fromIterable(asIterable().sortedBy(selector))
    fun <R : Comparable<R>> sortedByDescending(selector: (T) -> R?): Cons<T> =
        fromIterable(asIterable().sortedByDescending(selector))

    fun sortedWith(comparator: Comparator<in T>): Cons<T> = fromIterable(asIterable().sortedWith(comparator))
    fun take(n: Int): Cons<T> = fromIterable(asIterable().take(n))
    fun takeWhile(predicate: (T) -> Boolean): Cons<T> = fromIterable(asIterable().takeWhile(predicate))
    fun windowed(size: Int, step: Int = 1, partialWindows: Boolean = false): Cons<List<T>> =
        fromIterable(asIterable().windowed(size, step, partialWindows))

    fun <R> windowed(size: Int, step: Int = 1, partialWindows: Boolean = false, transform: (List<T>) -> R): Cons<R> =
        fromIterable(asIterable().windowed(size, step, partialWindows, transform))

    fun withIndex(): Cons<IndexedValue<T>> = fromIterable(asIterable().withIndex())
    fun <R> zip(other: Iterable<R>): Cons<Pair<T, R>> = fromIterable(asIterable().zip(other.asIterable()))
    fun <R, V> zip(other: Iterable<R>, transform: (a: T, b: R) -> V): Cons<V> =
        fromIterable(asIterable().zip(other.asIterable(), transform))

    fun zipWithNext(): Cons<Pair<T, T>> = fromIterable(asIterable().zipWithNext())
    fun <R> zipWithNext(transform: (a: T, b: T) -> R): Cons<R> = fromIterable(asIterable().zipWithNext(transform))

}

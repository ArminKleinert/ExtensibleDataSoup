package kleinert.soap.cons

import kleinert.soap.EDNSoapWriter
import java.util.*
import kotlin.random.Random

sealed interface ImmutableLazyList<T> : List<T>, Iterable<T> {
    companion object {
        fun <T> of(vararg elements: T): ImmutableLazyList<T> = if (elements.isEmpty()) nullCons() else VList(elements.toList())

        fun <T> from(coll: List<T>): ImmutableLazyList<T> =
            if (coll is ImmutableLazyList<T>) coll
            else if (coll.isEmpty()) nullCons()
            else CdrCodedList(coll)
        fun <T> from(coll: Iterable<T>): ImmutableLazyList<T> = if (coll is ImmutableLazyList<T>) coll else VList(coll)
        fun <T> from(arr: Array<T>): ImmutableLazyList<T> = VList(arr)
        fun <T> from(seq: Sequence<T>): ImmutableLazyList<T> = LazyList.lazySeq(seq)

        fun <T> wrapList(list: List<T>): CdrCodedList<T> = CdrCodedList(list, true)

        fun <T> randomAccess(list: Iterable<T>): ImmutableLazyList<T> = CdrCodedList(list.toList())

        fun <T> log2Access(list: Iterable<T>): ImmutableLazyList<T> = VList(list)

        fun <T> lazy(list: Iterable<T>): ImmutableLazyList<T> = LazyList.fromIterator(list.iterator())
        fun <T> lazy(list: Sequence<T>): ImmutableLazyList<T> = LazyList.lazySeq(list)

        fun <T> singlyLinked(list: Iterable<T>): ImmutableLazyList<T> {
            var head: ImmutableLazyList<T> = nullCons()
            for (element in list.reversed()) {
                head = ConsCell(element, head)
            }
            return head
        }

        fun <T> cons(x: T, xs: ImmutableLazyList<T>): ImmutableLazyList<T> = xs.cons(x)
        fun <T> cons(x: T, xs: Iterable<T>): ImmutableLazyList<T> = from(xs).cons(x)
        fun <T> cons(x: T, xs: Sequence<T>): ImmutableLazyList<T> = from(xs).cons(x)
        fun <T> cons(x: T, xs: Array<T>): ImmutableLazyList<T> = from(xs).cons(x)
        fun <T> cons(x: T, xs: () -> ImmutableLazyList<T>): ImmutableLazyList<T> = LazyList(xs).cons(x)

        fun <T> concat(vararg conses: ImmutableLazyList<T>) =
            conses.foldRight(nullCons()) { ts: ImmutableLazyList<T>, acc: ImmutableLazyList<T> -> ListPair.concat(ts, acc) }

        fun <T> concat(cons1: ImmutableLazyList<T>, cons2: ImmutableLazyList<T>) = ListPair.concat(cons1, cons2)
    }

    val car: T

    val cdr: ImmutableLazyList<T>

    override val size: Int

    // Haskell style
    fun head(): T = car

    // Haskell style
    fun tail(): ImmutableLazyList<T> = cdr

    // Clojure & Logo language style
    fun first(): T = car

    // Logo language style
    fun butfirst(): ImmutableLazyList<T> = cdr

    // Clojure style
    fun next(): ImmutableLazyList<T>? {
        val r = cdr
        if (r.isEmpty()) return null
        return r
    }

    // Clojure style
    fun rest(): ImmutableLazyList<T> = cdr

    fun count() = size

    fun cons(element: T): ImmutableLazyList<T> = ConsCell(element, this)

    override fun isEmpty(): Boolean

    override fun iterator(): Iterator<T> = asSequence().iterator()

    fun cleared(): ImmutableLazyList<T> = nullCons()

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

    override fun subList(fromIndex: Int, toIndex: Int): ImmutableLazyList<T> = sameTypeFromList(toList().subList(fromIndex, toIndex))

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

    fun drop(n: Int): ImmutableLazyList<T> {
        require(n >= 0) { "Requested element count $n is less than zero." }
        var countdown = n
        var rest: ImmutableLazyList<T> = this
        while (rest.isNotEmpty() && countdown > 0) {
            rest = rest.cdr
            countdown--
        }
        return rest
    }

    fun shuffled(random: Random = Random.Default): ImmutableLazyList<T> =
        sameTypeFromList(toList().shuffled(random))

    fun asSequence(): Sequence<T> = sequence {
        var cell = this@ImmutableLazyList
        while (cell.isNotEmpty()) {
            yield(cell.car)
            cell = cell.cdr
        }
    }

    fun reversed(): ImmutableLazyList<T> {
        if (isEmpty())
            return this

        var res = cleared()
        for (it in this) {
            res = res.cons(it)
        }
        return res
    }

    fun commonToString(
        separator: CharSequence = ", ",
        prefix: CharSequence = "[",
        postfix: CharSequence = "]",
        limit: Int = -1,
        truncated: CharSequence = "...",
        prettyPrint: Boolean = false,
        buffer: Appendable = StringBuilder(),
    ): String {
        buffer.append(prefix)
        var count = 0
        for (element in this) {
            if (++count > 1) buffer.append(separator)
            if (!(limit < 0 || count <= limit)) break

            when {
                prettyPrint -> buffer.append(EDNSoapWriter.pprintS(element))
                element is CharSequence? -> buffer.append(element)
                element is Char -> buffer.append(element)
                else -> buffer.append(element.toString())
            }

        }
        if (limit in 0..<count) buffer.append(truncated)
        buffer.append(postfix)
        return buffer.toString()
    }

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

    fun <R> sameTypeFromList(list: List<R>): ImmutableLazyList<R>

    fun asIterable(): Iterable<T> = this

    fun toMutableList(): MutableList<T> = asSequence().toMutableList()

    fun toList(): List<T> = Collections.unmodifiableList(toMutableList())

    fun isSingleton(): Boolean = isNotEmpty() && cdr.isEmpty()

    fun isLazyType(): Boolean = false

    operator fun plus(element: T): ImmutableLazyList<T> = ListPair.concat(this, ConsCell(element, nullCons()))

    operator fun plus(other: Iterable<T>): ImmutableLazyList<T> = ListPair.concat(this, from(other))

    operator fun plus(other: List<T>): ImmutableLazyList<T> = ListPair.concat(this, CdrCodedList(other))

    operator fun plus(other: ImmutableLazyList<T>): ImmutableLazyList<T> = ListPair.concat(this, other)

    operator fun plus(other: VList<T>): ImmutableLazyList<T> =
        if (isLazyType()) ListPair.concat(this, other)
        else other.prepend(this)

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

    fun chunked(size: Int): ImmutableLazyList<List<T>> =
        if (isLazyType()) from(asSequence().chunked(size))
        else sameTypeFromList(asIterable().chunked(size))

    fun distinct(): ImmutableLazyList<T> =
        if (isLazyType()) from(asSequence().distinct())
        else sameTypeFromList(asIterable().distinct())

    fun dropWhile(predicate: (T) -> Boolean): ImmutableLazyList<T> =
        if (isLazyType()) from(asSequence().dropWhile(predicate))
        else sameTypeFromList(asIterable().dropWhile(predicate))

    fun filter(predicate: (T) -> Boolean): ImmutableLazyList<T> =
        if (isLazyType()) from(asSequence().filter(predicate))
        else sameTypeFromList(asIterable().filter(predicate))

    fun filterIndexed(predicate: (index: Int, T) -> Boolean): ImmutableLazyList<T> =
        if (isLazyType()) from(asSequence().filterIndexed(predicate))
        else sameTypeFromList(asIterable().filterIndexed(predicate))

    fun filterNot(predicate: (T) -> Boolean): ImmutableLazyList<T> =
        if (isLazyType()) from(asSequence().filterNot(predicate))
        else sameTypeFromList(asIterable().filterNot(predicate))

    fun filterNotNull(): ImmutableLazyList<T> =
        if (isLazyType()) from(asSequence().filterNotNull())
        else sameTypeFromList(asIterable().filterNotNull())

    fun <R> flatMap(transform: (T) -> Iterable<R>): ImmutableLazyList<R> =
        if (isLazyType()) from(asSequence().flatMap(transform))
        else sameTypeFromList(asIterable().flatMap(transform))

    fun <R> flatMapIndexed(transform: (index: Int, T) -> Iterable<R>): ImmutableLazyList<R> =
        if (isLazyType()) from(asSequence().flatMapIndexed(transform))
        else sameTypeFromList(asIterable().flatMapIndexed(transform))

    fun ifEmpty(defaultValue: () -> ImmutableLazyList<T>): ImmutableLazyList<T> =
        from(asSequence().ifEmpty { defaultValue().asSequence() })

    fun <R> map(transform: (T) -> R): ImmutableLazyList<R> =
        if (isLazyType()) from(asSequence().map(transform))
        else sameTypeFromList(asIterable().map(transform))

    fun <R> mapIndexed(transform: (index: Int, T) -> R): ImmutableLazyList<R> =
        if (isLazyType()) from(asSequence().mapIndexed(transform))
        else sameTypeFromList(asIterable().mapIndexed(transform))

    fun <R : Any> mapIndexedNotNull(transform: (index: Int, T) -> R?): ImmutableLazyList<R> =
        if (isLazyType()) from(asSequence().mapIndexedNotNull(transform))
        else sameTypeFromList(asIterable().mapIndexedNotNull(transform))

    fun <R : Any> mapNotNull(transform: (T) -> R?): ImmutableLazyList<R> =
        if (isLazyType()) from(asSequence().mapNotNull(transform))
        else sameTypeFromList(asIterable().mapNotNull(transform))

    fun minus(element: T): ImmutableLazyList<T> =
        if (isLazyType()) from(asSequence().minus(element))
        else sameTypeFromList(asIterable().minus(element))

    fun minus(elements: Set<T>): ImmutableLazyList<T> =
        if (isLazyType()) from(asSequence().minus(elements.toSet()))
        else sameTypeFromList(asIterable().minus(elements.toSet()))

    fun minus(elements: Iterable<T>): ImmutableLazyList<T> = minus(elements.toSet())
    fun onEach(action: (T) -> Unit): ImmutableLazyList<T> =
        if (isLazyType()) from(asSequence().onEach(action))
        else from(asIterable().onEach(action))

    fun onEachIndexed(action: (index: Int, T) -> Unit): ImmutableLazyList<T> =
        if (isLazyType()) from(asSequence().onEachIndexed(action))
        else from(asIterable().onEachIndexed(action))

    fun requireNoNulls(): ImmutableLazyList<T> =
        if (isLazyType()) from(asSequence().requireNoNulls())
        else from(asIterable().requireNoNulls())

    fun <R> runningFold(initial: R, operation: (acc: R, T) -> R): ImmutableLazyList<R> =
        if (isLazyType()) from(asSequence().runningFold(initial, operation))
        else sameTypeFromList(asIterable().runningFold(initial, operation))

    fun <R> runningFoldIndexed(initial: R, operation: (index: Int, acc: R, T) -> R): ImmutableLazyList<R> =
        if (isLazyType()) from(asSequence().runningFoldIndexed(initial, operation))
        else sameTypeFromList(asIterable().runningFoldIndexed(initial, operation))

    fun runningReduce(operation: (acc: T, T) -> T): ImmutableLazyList<T> =
        if (isLazyType()) from(asSequence().runningReduce(operation))
        else sameTypeFromList(asIterable().runningReduce(operation))

    fun runningReduceIndexed(operation: (index: Int, acc: T, T) -> T): ImmutableLazyList<T> =
        if (isLazyType()) from(asSequence().runningReduceIndexed(operation))
        else sameTypeFromList(asIterable().runningReduceIndexed(operation))

    fun <R> scan(initial: R, operation: (acc: R, T) -> R): ImmutableLazyList<R> =
        if (isLazyType()) from(asSequence().scan(initial, operation))
        else sameTypeFromList(asIterable().scan(initial, operation))

    fun <R> scanIndexed(initial: R, operation: (index: Int, acc: R, T) -> R): ImmutableLazyList<R> =
        if (isLazyType()) from(asSequence().scanIndexed(initial, operation))
        else sameTypeFromList(asIterable().scanIndexed(initial, operation))

    fun <R : Comparable<R>> sortedBy(selector: (T) -> R?): ImmutableLazyList<T> =
        if (isLazyType()) from(asSequence().sortedBy(selector))
        else sameTypeFromList(asIterable().sortedBy(selector))

    fun <R : Comparable<R>> sortedByDescending(selector: (T) -> R?): ImmutableLazyList<T> =
        if (isLazyType()) from(asSequence().sortedByDescending(selector))
        else sameTypeFromList(asIterable().sortedByDescending(selector))

    fun sortedWith(comparator: Comparator<in T>): ImmutableLazyList<T> =
        if (isLazyType()) from(asSequence().sortedWith(comparator))
        else sameTypeFromList(asIterable().sortedWith(comparator))

    fun take(n: Int): ImmutableLazyList<T> =
        if (isLazyType()) from(asSequence().take(n))
        else sameTypeFromList(asIterable().take(n))

    fun takeWhile(predicate: (T) -> Boolean): ImmutableLazyList<T> =
        if (isLazyType()) from(asSequence().takeWhile(predicate))
        else sameTypeFromList(asIterable().takeWhile(predicate))

    fun windowed(size: Int, step: Int = 1, partialWindows: Boolean = false): ImmutableLazyList<List<T>> =
        if (isLazyType()) from(asSequence().windowed(size, step, partialWindows))
        else sameTypeFromList(asIterable().windowed(size, step, partialWindows))

    fun <R> windowed(size: Int, step: Int = 1, partialWindows: Boolean = false, transform: (List<T>) -> R): ImmutableLazyList<R> =
        if (isLazyType()) from(asSequence().windowed(size, step, partialWindows, transform))
        else sameTypeFromList(asIterable().windowed(size, step, partialWindows, transform))

    fun withIndex(): ImmutableLazyList<IndexedValue<T>> =
        if (isLazyType()) from(asSequence().withIndex())
        else sameTypeFromList(asIterable().withIndex().toList())

    fun <R> zip(other: Iterable<R>): ImmutableLazyList<Pair<T, R>> =
        if (isLazyType()) from(asSequence().zip(other.asSequence()))
        else sameTypeFromList(asIterable().zip(other.asIterable()))

    fun <R, V> zip(other: Iterable<R>, transform: (a: T, b: R) -> V): ImmutableLazyList<V> =
        if (isLazyType()) from(asSequence().zip(other.asSequence(), transform))
        else sameTypeFromList(asIterable().zip(other.asIterable(), transform))

    fun zipWithNext(): ImmutableLazyList<Pair<T, T>> =
        if (isLazyType()) from(asSequence().zipWithNext())
        else sameTypeFromList(asIterable().zipWithNext())

    fun <R> zipWithNext(transform: (a: T, b: T) -> R): ImmutableLazyList<R> =
        if (isLazyType()) from(asSequence().zipWithNext(transform))
        else sameTypeFromList(asIterable().zipWithNext(transform))

}

package kleinert.soap.cons

import kleinert.soap.EDNSoapWriter
import java.util.*
import kotlin.random.Random

sealed interface PersistentList<T> : List<T>, Iterable<T> {
    companion object {
        fun <T> of(vararg elements: T): PersistentList<T> =
            if (elements.isEmpty()) nullCons() else VList.toVList(elements.toList())

        fun <T> from(coll: List<T>): PersistentList<T> =
            if (coll is PersistentList<T>) coll
            else if (coll.isEmpty()) nullCons()
            else PersistentWrapper(coll)

        fun <T> from(coll: Iterable<T>): PersistentList<T> =
            when (coll) {
                is PersistentList<T> -> coll
                is List<T> ->
                    if (coll.isEmpty()) nullCons()
                    else PersistentWrapper(coll)

                else -> VList.toVList(coll)
            }

        fun <T> from(arr: Array<T>): PersistentList<T> = VList.toVList(arr)

        fun <T> randomAccess(list: Iterable<T>): PersistentList<T> = PersistentWrapper(list.toList())

        fun <T> log2Access(list: Iterable<T>): PersistentList<T> = VList.toVList(list)

        fun <T> cons(x: T, xs: PersistentList<T>): PersistentList<T> = xs.cons(x)
        fun <T> cons(x: T, xs: Iterable<T>): PersistentList<T> = from(xs).cons(x)
        fun <T> cons(x: T, xs: Array<T>): PersistentList<T> = from(xs).cons(x)
        fun <T> cons(x: T, xs: () -> PersistentList<T>): PersistentList<T> = LazyList(xs).cons(x)

        fun <T> concat(lst1: PersistentList<T>, lst2: PersistentList<T>) = ListPair.concat(lst1, lst2)
    }

    val car: T

    val cdr: PersistentList<T>

    override val size: Int

    // Haskell style
    fun head(): T = car

    // Haskell style
    fun tail(): PersistentList<T> = cdr

    // Clojure & Logo language style
    fun first(): T = car

    // Logo language style
    fun butfirst(): PersistentList<T> = cdr

    // Clojure style
    fun next(): PersistentList<T>? {
        val r = cdr
        if (r.isEmpty()) return null
        return r
    }

    // Clojure style
    fun rest(): PersistentList<T> = cdr

    fun count() = size

    fun cons(element: T): PersistentList<T> = PersistentListHead(element, this)

    override fun isEmpty(): Boolean

    override fun iterator(): Iterator<T> = asSequence().iterator()

    fun cleared(): PersistentList<T> = nullCons()

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

    override fun subList(fromIndex: Int, toIndex: Int): PersistentList<T> =
        sameTypeFromList(toList().subList(fromIndex, toIndex))

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

    fun drop(n: Int): PersistentList<T> {
        require(n >= 0) { "Requested element count $n is less than zero." }
        var countdown = n
        var rest: PersistentList<T> = this
        while (rest.isNotEmpty() && countdown > 0) {
            rest = rest.cdr
            countdown--
        }
        return rest
    }

    fun shuffled(random: Random = Random.Default): PersistentList<T> =
        sameTypeFromList(toList().shuffled(random))

    fun asSequence(): Sequence<T> = sequence {
        var cell = this@PersistentList
        while (cell.isNotEmpty()) {
            yield(cell.car)
            cell = cell.cdr
        }
    }

    fun reversed(): PersistentList<T> {
        if (isEmpty())
            return this

        var res = cleared()
        for (it in this) {
            res = res.cons(it)
        }
        return res
    }

    fun <R> fold(initial: R, operation: (R, T) -> R): R {
        var accumulator = initial
        for (element in this) accumulator = operation(accumulator, element)
        return accumulator
    }

    fun <R> foldIndexed(initial: R, operation: (index: Int, acc: R, T) -> R): R {
        var index = 0
        var accumulator = initial
        for (element in this) accumulator = operation(index++, accumulator, element)
        return accumulator
    }

    fun <R> foldRight(initial: R, operation: (T, acc: R) -> R): R {
        var accumulator = initial
        if (!isEmpty()) {
            val iterator = listIterator(size)
            while (iterator.hasPrevious()) {
                accumulator = operation(iterator.previous(), accumulator)
            }
        }
        return accumulator
    }

    fun <R> foldRightIndexed(initial: R, operation: (index: Int, T, acc: R) -> R): R {
        var accumulator = initial
        if (isNotEmpty()) {
            val iterator = listIterator(size)
            while (iterator.hasPrevious()) {
                val index = iterator.previousIndex()
                accumulator = operation(index, iterator.previous(), accumulator)
            }
        }
        return accumulator
    }

    fun all(predicate: (T) -> Boolean): Boolean {
        for (e in this)
            if (!predicate(e)) return false
        return true
    }

    fun any(predicate: (T) -> Boolean): Boolean {
        for (e in this)
            if (predicate(e)) return true
        return false
    }

    fun none(predicate: (T) -> Boolean): Boolean {
        for (e in this)
            if (predicate(e)) return false
        return true
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

    fun <R> sameTypeFromList(list: List<R>): PersistentList<R> = PersistentList.from(list)

    fun asIterable(): Iterable<T> = this

    fun toMutableList(): MutableList<T> = asSequence().toMutableList()

    fun toList(): List<T> = Collections.unmodifiableList(toMutableList())

    fun isSingleton(): Boolean = isNotEmpty() && cdr.isEmpty()

    fun isLazyType(): Boolean = false

    ///////////////////////////////////////////////////////////////////////////////////

    operator fun plus(element: T): PersistentList<T> = ListPair.concat(this, PersistentListHead(element, nullCons()))

    operator fun plus(other: Iterable<T>): PersistentList<T> = ListPair.concat(this, from(other))

    fun chunked(size: Int): PersistentList<List<T>> =
        sameTypeFromList(asIterable().chunked(size))

    fun distinct(): PersistentList<T> =
       LazyList.distinct(this)

    fun dropWhile(predicate: (T) -> Boolean): PersistentList<T> =
        sameTypeFromList(asIterable().dropWhile(predicate))

    fun filter(predicate: (T) -> Boolean): PersistentList<T> =
        LazyList.filter(predicate, this)

    fun filterv(predicate: (T) -> Boolean): PersistentList<T> =
        sameTypeFromList(asIterable().filter(predicate))

    fun filterIndexed(predicate: (index: Int, T) -> Boolean): PersistentList<T> =
        LazyList.filterIndexed(predicate, this)

    fun filterNot(predicate: (T) -> Boolean): PersistentList<T> =
        LazyList.filterNot(predicate, this)

    fun filterNotNull(): PersistentList<T> =
        LazyList.filterNotNull(this)

    fun <R> flatMap(transform: (T) -> Iterable<R>): PersistentList<R> =
        LazyList.flatMap(transform, this)

    fun <R> flatMapIndexed(transform: (index: Int, T) -> Iterable<R>): PersistentList<R> =
        LazyList.flatMapIndexed(transform, this)

    fun ifEmpty(defaultValue: () -> PersistentList<T>): PersistentList<T> =
        if (isEmpty()) defaultValue()
        else this

    fun <R> map(transform: (T) -> R): PersistentList<R> =
        LazyList.map(transform, this)

    fun <R> mapv(transform: (T) -> R): PersistentList<R> =
        sameTypeFromList(asIterable().map(transform))

    fun <R> mapIndexed(transform: (index: Int, T) -> R): PersistentList<R> =
        sameTypeFromList(asIterable().mapIndexed(transform))

    fun <R : Any> mapIndexedNotNull(transform: (index: Int, T) -> R?): PersistentList<R> =
        sameTypeFromList(asIterable().mapIndexedNotNull(transform))

    fun <R : Any> mapNotNull(transform: (T) -> R?): PersistentList<R> =
        sameTypeFromList(asIterable().mapNotNull(transform))

    operator fun minus(element: T): PersistentList<T> =
        sameTypeFromList(asIterable().minus(element))

    operator fun minus(elements: Set<T>): PersistentList<T> =
        sameTypeFromList(asIterable().minus(elements.toSet()))

    operator fun minus(elements: Iterable<T>): PersistentList<T> =
        minus(elements.toSet())

    fun onEach(action: (T) -> Unit): PersistentList<T> {
        for (e in this)
            action(e)
        return this
    }

    fun onEachIndexed(action: (index: Int, T) -> Unit): PersistentList<T> {
        for ((index, e) in withIndex())
            action(index, e)
        return this
    }

    fun requireNoNulls(): PersistentList<T> =
        from(asIterable().requireNoNulls())

    fun <R> runningFold(initial: R, operation: (acc: R, T) -> R): PersistentList<R> =
        sameTypeFromList(asIterable().runningFold(initial, operation))

    fun <R> runningFoldIndexed(initial: R, operation: (index: Int, acc: R, T) -> R): PersistentList<R> =
        sameTypeFromList(asIterable().runningFoldIndexed(initial, operation))

    fun runningReduce(operation: (acc: T, T) -> T): PersistentList<T> =
        sameTypeFromList(asIterable().runningReduce(operation))

    fun runningReduceIndexed(operation: (index: Int, acc: T, T) -> T): PersistentList<T> =
        sameTypeFromList(asIterable().runningReduceIndexed(operation))

    fun <R> scan(initial: R, operation: (acc: R, T) -> R): PersistentList<R> =
        sameTypeFromList(asIterable().scan(initial, operation))

    fun <R> scanIndexed(initial: R, operation: (index: Int, acc: R, T) -> R): PersistentList<R> =
        sameTypeFromList(asIterable().scanIndexed(initial, operation))

    fun <R : Comparable<R>> sortedBy(selector: (T) -> R?): PersistentList<T> =
        sameTypeFromList(asIterable().sortedBy(selector))

    fun <R : Comparable<R>> sortedByDescending(selector: (T) -> R?): PersistentList<T> =
        sameTypeFromList(asIterable().sortedByDescending(selector))

    fun sortedWith(comparator: Comparator<in T>): PersistentList<T> =
        sameTypeFromList(asIterable().sortedWith(comparator))

    fun take(n: Int): PersistentList<T> =
        if (isLazyType()) LazyList.take(n, this)
        else sameTypeFromList(asIterable().take(n))

    fun takeWhile(predicate: (T) -> Boolean): PersistentList<T> =
        LazyList.takeWhile(predicate, this)

    fun <R> windowed(
        size: Int,
        step: Int = 1,
        partialWindows: Boolean = false,
        transform: (PersistentList<T>) -> R
    ): PersistentList<R> =
        LazyList.windowed(size, this, step, partialWindows, transform)

    fun windowed(
        size: Int,
        step: Int = 1,
        partialWindows: Boolean = false
    ): PersistentList<PersistentList<T>> =
        LazyList.windowed(size, this, step, partialWindows)

    fun withIndex(): PersistentList<IndexedValue<T>> =
        sameTypeFromList(asIterable().withIndex().toList())

    fun <R> zip(other: PersistentList<R>): PersistentList<Pair<T, R>> =
        sameTypeFromList(asIterable().zip(other.asIterable()))

    fun <R, V> zip(other: Iterable<R>, transform: (a: T, b: R) -> V): PersistentList<V> =
        sameTypeFromList(asIterable().zip(other.asIterable(), transform))

    fun zipWithNext(): PersistentList<Pair<T, T>> =
        sameTypeFromList(asIterable().zipWithNext())

    fun <R> zipWithNext(transform: (a: T, b: T) -> R): PersistentList<R> =
        sameTypeFromList(asIterable().zipWithNext(transform))

    fun cycle(): PersistentList<T> =
        LazyList.cycle(this)

    fun splitAt(n: Int): Pair<PersistentList<T>, PersistentList<T>> = LazyList.splitAt(n, this)

}

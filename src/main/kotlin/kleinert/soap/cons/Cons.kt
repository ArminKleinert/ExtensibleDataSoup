package kleinert.soap.cons

object NullCons : ImmutableLazyList<Any?> {
    @get:Throws(NoSuchElementException::class)
    override val car: Any
        get() = throw NoSuchElementException("")

    override val cdr: NullCons
        get() = this

    override val size: Int
        get() = 0

    override fun isEmpty(): Boolean = true

    override fun cleared(): NullCons = this

    override fun <R> sameTypeFromList(list: List<R>): ImmutableLazyList<R> {
        if (list.isEmpty()) return nullCons()
        return CdrCodedList(list)
    }

    override fun toString(): String = commonToString()
    override fun equals(other: Any?): Boolean = commonEqualityCheck(other)
    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

fun <T> nullCons(): ImmutableLazyList<T> = NullCons as ImmutableLazyList<T>

class ConsCell<T> : ImmutableLazyList<T> {
    companion object {
        fun <T> of(vararg elements: T) =
            if (elements.isEmpty()) nullCons()
            else ConsCell(elements.asIterable())
    }

    override val car: T

    override val cdr: ImmutableLazyList<T>

    private var lazySize: Int = -1

    override val size: Int
        get() {
            if (lazySize < 0) lazySize = cdr.size+1
            return lazySize
        }

    constructor(car: T, cdr: ImmutableLazyList<T>) {
        this.car = car
        this.cdr = cdr
    }

    @Throws(NoSuchElementException::class)
    constructor(iter: Iterable<T>) {
        val iterator = iter.iterator()
        this.car = iterator.next()

        val tail = mutableListOf<T>()
        for (it in iterator) {
            tail.add(it)
        }
        this.cdr = CdrCodedList(tail)
    }

    @Throws(NoSuchElementException::class)
    constructor(arr: Array<T>) : this(arr.asIterable())

    override fun isEmpty(): Boolean = false

    override fun asSequence(): Sequence<T> = sequence {
        var rest: ImmutableLazyList<T> = this@ConsCell
        while (rest is ConsCell<T>) {
            yield(rest.car)
            rest = rest.cdr
        }
        yieldAll(rest.asSequence())
    }

    override fun cleared(): ImmutableLazyList<T> = nullCons()

    override fun <R> sameTypeFromList(list: List<R>): ImmutableLazyList<R> = CdrCodedList(list)

    override fun toString(): String = commonToString()
    override fun equals(other: Any?): Boolean = commonEqualityCheck(other)
    override fun hashCode(): Int {
        var result = car?.hashCode() ?: 0
        result = 31 * result + cdr.hashCode()
        return result
    }
}
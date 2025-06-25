package kleinert.soap.cons

object EmptyList : PersistentList<Any?> {
    @get:Throws(NoSuchElementException::class)
    override val car: Any
        get() = throw NoSuchElementException("")

    override val cdr: EmptyList
        get() = this

    override val size: Int
        get() = 0

    override fun isEmpty(): Boolean = true

    override fun cleared(): EmptyList = this

    override fun <R> sameTypeFromList(list: List<R>): PersistentList<R> {
        if (list.isEmpty()) return nullCons()
        return PersistentWrapper(list)
    }

    override fun toString(): String = commonToString()
    override fun equals(other: Any?): Boolean = commonEqualityCheck(other)
    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

fun <T> nullCons(): PersistentList<T> = EmptyList as PersistentList<T>

class PersistentListHead<T> : PersistentList<T> {
    companion object {
        fun <T> of(vararg elements: T) =
            if (elements.isEmpty()) nullCons()
            else PersistentListHead(elements.asIterable())
    }

    override val car: T

    override val cdr: PersistentList<T>

    private var lazySize: Int = -1

    override val size: Int
        get() {
            if (lazySize < 0) lazySize = cdr.size+1
            return lazySize
        }

    constructor(car: T, cdr: PersistentList<T>) {
        this.car = car
        this.cdr = cdr
    }

    @Throws(NoSuchElementException::class)
    constructor(iter: Iterable<T>) {
        val iterator = iter.iterator()
        this.car = iterator.next()

        val tail = mutableListOf<T>()
        for (it in iterator)
            tail.add(it)
        this.cdr = PersistentWrapper(tail)
    }

    override fun isEmpty(): Boolean = false

    override fun toString(): String = commonToString()

    override fun equals(other: Any?): Boolean = commonEqualityCheck(other)
    override fun hashCode(): Int {
        var result = car?.hashCode() ?: 0
        result = 31 * result + cdr.hashCode()
        return result
    }
}
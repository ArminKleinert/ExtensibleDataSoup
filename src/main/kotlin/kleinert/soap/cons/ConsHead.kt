package kleinert.soap.cons


class EmptyCons<T> : Cons<T> {
    @get:Throws(NoSuchElementException::class)
    override val car: T
        get() = throw NoSuchElementException("")

    override val cdr: EmptyCons<T>
        get() = this

    override val size: Int
        get() = 0

    override fun isEmpty(): Boolean = true

    override fun cleared(): Cons<T> = this

    override fun <R> sameTypeFromList(list: List<R>): Cons<R> {
        if (list.isEmpty()) return EmptyCons()
        return CdrCodedList(list)
    }

    override fun toString(): String = commonToString()
    override fun equals(other: Any?): Boolean = commonEqualityCheck(other)
    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

class ConsHead<T> : Cons<T> {
    override val car: T

    override val cdr: Cons<T>

    override val size: Int

    constructor(car: T, cdr: Cons<T>) {
        this.car = car
        this.cdr = cdr
        this.size = cdr.size
    }

    @Throws(NoSuchElementException::class)
    constructor(coll: Cons<T>) {
        this.car = coll.car
        this.cdr = coll.cdr
        this.size = coll.size
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
        this.size = cdr.size + 1
    }

    override fun isEmpty(): Boolean = false

    override fun asSequence(): Sequence<T> = sequence {
        var rest: Cons<T> = this@ConsHead
        while (rest is ConsHead<T>) {
            yield(rest.car)
            rest = rest.cdr
        }
        yieldAll(rest.asSequence())
    }

    override fun cleared(): Cons<T> = EmptyCons()

    override fun <R> sameTypeFromList(list: List<R>): Cons<R> = CdrCodedList(list)

    override fun toString(): String = commonToString()
    override fun equals(other: Any?): Boolean = commonEqualityCheck(other)
    override fun hashCode(): Int {
        var result = car?.hashCode() ?: 0
        result = 31 * result + cdr.hashCode()
        return result
    }
}
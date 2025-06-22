package kleinert.soap

class ConsIterator<T>(private var cons: Cons<T>) : Iterator<T> {
    override fun hasNext(): Boolean =
        cons.isNotEmpty()

    override fun next(): T {
        val car = cons.car
        var cdr = cons.cdr

        if (cdr !is Cons<T>) {
            val iter = cdr.iterator()
            cdr = ConsCell(iter.next(), cdr)
        }

        cons = cdr as Cons<T>
        return car
    }
}

class EmptyList<T> : Cons<T> {
    override val car: T
        get() = throw NoSuchElementException("The empty list has no elements.")

    override val cdr: EmptyList<T>
        get() = this

    override fun isEmpty(): Boolean = true

    override fun iterator(): Iterator<T> = ConsIterator(this)

    override fun equals(other: Any?): Boolean {
        if (other is EmptyList<*>) return true
        if (other !is List<Any?>) return false
        return other.isEmpty()
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String = "[]"
}

class ConsCell<T> : Cons<T> {
    override val car: T
    override val cdr: Cons<T>

    constructor(car: T, cdr: Cons<T>) {
        this.car = car
        this.cdr = cdr
    }

    constructor(coll: Collection<T>) {
        if (coll is Cons<T>) {
            this.car = coll.car
            this.cdr = coll.cdr
        } else {
            var res: Cons<T> = EmptyList()
            for (e in coll.reversed()) {
                res = ConsCell(e, res)
            }
            this.car = res.car
            this.cdr = res.cdr
        }
    }

    override fun isEmpty(): Boolean = false

    override fun iterator(): Iterator<T> = ConsIterator(this)

    override fun equals(other: Any?): Boolean {
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

    override fun hashCode(): Int {
        var result = car?.hashCode() ?: 0
        result = 31 * result + cdr.hashCode()
        return result
    }

    override fun toString(): String = StringBuilder()
        .append('[')
        .append(joinToString(", "))
        .append("]").toString()
}
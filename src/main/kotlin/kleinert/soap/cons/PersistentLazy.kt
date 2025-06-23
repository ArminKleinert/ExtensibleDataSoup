package kleinert.soap.cons

import kotlin.experimental.ExperimentalTypeInference

class PersistentLazy<T> : Cons<T>, Sequence<T> {
    companion object {
        fun <T> from(coll: Iterable<T>) = when (coll) {
            is PersistentLazy<T> -> coll
            is Cons<T> -> PersistentLazy(coll)
            is Sequence<*> -> PersistentLazy(coll)
            else -> PersistentLazy(coll.asSequence())
        }

        @OptIn(ExperimentalTypeInference::class)
        fun <T> of(@BuilderInference block: suspend SequenceScope<T>.() -> Unit): PersistentLazy<T> =
            PersistentLazy(Sequence { iterator(block) })

        fun <T> of(vararg elements: T): PersistentLazy<T> =
            PersistentLazy(elements.asSequence())
    }

    private var memoized: Boolean = false

    private var memoCar: T? = null
    private var memoCdr: Cons<T>? = null
    private var memoSize: Int = -1

    private var seq: Sequence<T>

    override val car: T
        get() {
            if (!memoized) evalFirstAndRest()
            if (isEmpty()) throw NoSuchElementException()
            return memoCar!! as T
        }

    override val cdr: Cons<T>
        get() {
            if (!memoized) evalFirstAndRest()
            return memoCdr!!
        }

    override val size: Int
        get() {
            if (memoSize < 0) memoSize = seq.count()
            return memoSize
        }

    constructor(sequence: Sequence<T>) {
        this.seq = sequence
    }

    constructor(cons: Cons<T>) {
        if (cons is PersistentLazy<T>) {
            this.seq = cons.seq
        } else {
            this.seq = cons.asSequence()
        }
    }

    private fun evalFirstAndRest() {
        if (memoized)
            return

        memoized = true

        memoCar = try {
            seq.first()
        } catch (nsee: NoSuchElementException) {
            memoSize = 0
            null
        }
        memoCdr = PersistentLazy(seq.drop(1))
    }

    override fun isEmpty(): Boolean = size == 0

    override fun cleared(): Cons<T> = Cons.of()

    override fun <R> sameTypeFromList(list: List<R>): Cons<R> = PersistentLazy(list.asSequence())

    override fun iterator(): Iterator<T> = seq.iterator()

    override fun toString(): String = super.commonToString()
    override fun equals(other: Any?): Boolean = commonEqualityCheck(other)

    override fun hashCode(): Int {
        var result = javaClass.hashCode()
        result = 31 * result + seq.hashCode()
        return result
    }
}
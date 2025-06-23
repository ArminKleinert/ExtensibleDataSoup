package kleinert.soap.cons

import kotlin.experimental.ExperimentalTypeInference

class LazyCons1<T> : Cons<T>, Sequence<T> {
    companion object {
        fun <T> from(coll: Iterable<T>) = when (coll) {
            is LazyCons1<T> -> coll
            is Cons<T> -> LazyCons1(coll)
            is Sequence<*> -> LazyCons1(coll)
            else -> LazyCons1(coll.asSequence())
        }

        @OptIn(ExperimentalTypeInference::class)
        fun <T> of(@BuilderInference block: suspend SequenceScope<T>.() -> Unit): LazyCons1<T> =
            LazyCons1(Sequence { iterator(block) })

        fun <T> of(vararg elements: T): LazyCons1<T> =
            LazyCons1(elements.asSequence())
    }

    private var memoized: Boolean = false

    private var memoCar: T? = null
    private var memoCdr: Cons<T>? = null
    private var memoSize: Int = -1
    private var clearlyEmpty: Boolean = false

    private var seq: Sequence<T>

    override val car: T
        get() {
            if (!memoized) evalFirstAndRest()
            if (isEmpty()) throw NoSuchElementException()
            return memoCar!!
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
        if (cons is LazyCons1<T>) {
            this.seq = cons.seq
        } else {
            this.seq = cons.asSequence()
        }
    }

    private fun evalFirstAndRest() {
        if (memoized)
            return

        memoized = true

        val iter = seq.iterator()

        if (iter.hasNext()) {
            memoCar = seq.first() // May throw "NoSuchElementException", in which case the sequence must be empty.
            memoCdr = LazyCons1(seq.drop(1))
        } else {
            memoCar = null
            clearlyEmpty = true
            memoCdr = nullCons()
            memoSize = 0
            seq = sequenceOf()
        }

//        try {
//            memoCar = seq.first() // May throw "NoSuchElementException", in which case the sequence must be empty.
//            clearlyEmpty = false
//            memoCdr = PersistentLazy(seq.drop(1))
//        } catch (nsee: NoSuchElementException) {
//            memoCar = null
//            clearlyEmpty = true
//            memoCdr = nullCons()
//            memoSize = 0
//        }
    }

    override fun isEmpty(): Boolean {
        evalFirstAndRest()
        return clearlyEmpty
    }

    override fun cleared(): Cons<T> = Cons.of()

    override fun <R> sameTypeFromList(list: List<R>): Cons<R> = LazyCons1(list.asSequence())

    override fun iterator(): Iterator<T> = seq.iterator()

    override fun isLazyType(): Boolean = true

    override fun toString(): String = super.commonToString()
    override fun equals(other: Any?): Boolean = commonEqualityCheck(other)

    override fun hashCode(): Int {
        var result = javaClass.hashCode()
        result = 31 * result + seq.hashCode()
        return result
    }
}
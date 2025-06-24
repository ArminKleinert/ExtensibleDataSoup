package kleinert.soap.cons

class ListPair<T> private constructor(val left: Cons<T>, val right: Cons<T>) : Cons<T> {

    companion object {
        fun <T> concat(left: Cons<T>, right: Cons<T>): Cons<T> =
            when {
                left.isEmpty() -> right
                right.isEmpty() -> left
                left is ListPair<T> -> ListPair(left.left, ListPair(left.right, right))
                else -> ListPair(left, right)
            }
    }

    override val car: T
        get() = left.car
    override val cdr: Cons<T>
        get() = concat(left.cdr, right)
    override val size: Int
        get() = left.size + right.size

    override fun isEmpty(): Boolean = false

    override fun iterator(): Iterator<T> = ConsPairIterator(left.iterator(), right.iterator())

    class ConsPairIterator<T>(private val leftIterator: Iterator<T>, private val rightIterator: Iterator<T>) : Iterator<T> {

        override fun hasNext(): Boolean = leftIterator.hasNext() || rightIterator.hasNext()

        override fun next(): T {if (leftIterator.hasNext()) return leftIterator.next()
            return rightIterator.next()
        }
    }

    override fun cleared(): Cons<T> = VList()

    override fun <R> sameTypeFromList(list: List<R>): Cons<R> = CdrCodedList(list)

    override fun toString(): String = commonToString()
    override fun equals(other: Any?): Boolean = commonEqualityCheck(other)
    override fun hashCode(): Int {
        var result = left.hashCode()
        result = 31 * result + right.hashCode()
        return result
    }
}
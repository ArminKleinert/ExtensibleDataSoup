package kleinert.soap.cons

class ListPair<T> private constructor(val left: PersistentList<T>, val right: PersistentList<T>) : PersistentList<T> {

    companion object {
        fun <T> concat(left: PersistentList<T>, right: PersistentList<T>): PersistentList<T> =
            when {
                left.isEmpty() -> right
                right.isEmpty() -> left
                left is ListPair<T> -> ListPair(left.left, ListPair(left.right, right))
                else -> ListPair(left, right)
            }
    }

    override val car: T
        get() = left.car
    override val cdr: PersistentList<T>
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

    override fun cleared(): PersistentList<T> = PersistentList.of()

    override fun <R> sameTypeFromList(list: List<R>): PersistentList<R> = PersistentWrapper(list)

    override fun toString(): String = commonToString()
    override fun equals(other: Any?): Boolean = commonEqualityCheck(other)
    override fun hashCode(): Int {
        var result = left.hashCode()
        result = 31 * result + right.hashCode()
        return result
    }
}
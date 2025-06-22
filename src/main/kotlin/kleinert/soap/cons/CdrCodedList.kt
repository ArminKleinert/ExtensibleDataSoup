package kleinert.soap.cons

class CdrCodedList<T> : Cons<T>, RandomAccess {
    private val backingList: List<T>
    private val offset: Int

    private val actualList: List<T>
        get() = if (offset == 0) backingList else backingList.drop(offset)

    companion object {
        fun <T> of(vararg elements: T): CdrCodedList<T> = CdrCodedList(elements.toList())
    }

    constructor(lst: List<T>, isSafe: Boolean, offset: Int = 0) {
        val backing =
            if (isSafe) lst
            else lst.toList()

        when {
            offset >= backing.size -> {
                this.backingList = listOf()
                this.offset = 0
            }

            offset > backing.size / 2 -> {
                this.backingList = backing.drop(offset)
                this.offset = 0
            }

            else -> {
                this.backingList = backing
                this.offset = offset
            }
        }
    }

    constructor(lst: List<T>) : this(lst, false)

    constructor(lst: Array<T>) : this(lst.toList(), true)

    constructor(lst: Iterable<T>) : this(lst.toList(), true)

    constructor(lst: Sequence<T>) : this(lst.toList(), true)

    constructor() : this(listOf(), true)

    @get:Throws(NoSuchElementException::class)
    override val car: T
        get() = if (isEmpty()) throw NoSuchElementException() else backingList[offset]

    override val cdr: CdrCodedList<T>
        get() = if (isEmpty()) this else CdrCodedList(backingList, true, offset+1)

    override val size: Int
        get() = backingList.size - offset

    override fun isEmpty(): Boolean = size == 0

    override fun iterator(): Iterator<T> = actualList.iterator()

    override fun cleared(): Cons<T> = CdrCodedList()

    override fun <R> sameTypeFromList(list: List<R>): CdrCodedList<R> = CdrCodedList(list)

    override fun cons(element: T): Cons<T> = CdrCodedList(listOf(element) + actualList, true)

    override fun asIterable() = actualList

    override fun toList(): List<T> = actualList

    override fun asSequence() = actualList.asSequence()

    override fun reversed(): CdrCodedList<T> = CdrCodedList(actualList.asReversed())

    override fun toString(): String = commonToString()
    override fun equals(other: Any?): Boolean = commonEqualityCheck(other)
    override fun hashCode(): Int {
        return actualList.hashCode()
    }
}

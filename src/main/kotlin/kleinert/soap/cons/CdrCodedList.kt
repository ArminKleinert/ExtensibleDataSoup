package kleinert.soap.cons

class CdrCodedList<T> : Cons<T>, RandomAccess {
    private val backingList: List<T>
    private val offset: Int

    companion object {
        fun <T> of(vararg elements: T) = CdrCodedList(elements)
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
        get() = backingList[0]

    override val cdr: CdrCodedList<T>
        get() = CdrCodedList(backingList.subList(1, backingList.size))

    override val size: Int
        get() = backingList.size

    override fun isEmpty(): Boolean = size == 0

    override fun iterator(): Iterator<T> = backingList.iterator()

    override fun cleared(): Cons<T> = CdrCodedList()

    override fun <R> sameTypeFromList(list: List<R>): CdrCodedList<R> = CdrCodedList(list)

    override fun cons(element: T): Cons<T> = CdrCodedList(listOf(element) + backingList, true)

    override fun asIterable() = backingList

    override fun reversed(): CdrCodedList<T> = CdrCodedList(backingList.asReversed())

    override fun toString(): String = commonToString()
    override fun equals(other: Any?): Boolean = commonEqualityCheck(other)
    override fun hashCode(): Int {
        return backingList.hashCode()
    }
}

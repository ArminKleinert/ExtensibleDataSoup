package kleinert.soap.cons

class PersistentWrapper<T> : PersistentList<T>, List<T>, RandomAccess {
    private val backingList: List<T>
    private val offset: Int

    private val actualList: List<T>
        get() = if (offset == 0) backingList else backingList.drop(offset)

    companion object {
        fun <T> of(vararg elements: T): PersistentWrapper<T> = PersistentWrapper(elements.toList())
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

    constructor(lst: Iterable<T>) : this(lst.toList(), true)

    @get:Throws(NoSuchElementException::class)
    override val car: T
        get() = if (isEmpty()) throw NoSuchElementException() else backingList[offset]

    override val cdr: PersistentWrapper<T>
        get() = if (isEmpty()) this else PersistentWrapper(backingList, true, offset+1)

    override val size: Int
        get() = backingList.size - offset

    override fun isEmpty(): Boolean = size == 0

    override fun iterator(): Iterator<T> = actualList.iterator()

    override fun asIterable() = actualList

    override fun toList(): List<T> = actualList

    override fun asSequence() = actualList.asSequence()

    override fun reversed(): PersistentWrapper<T> = PersistentWrapper(actualList.asReversed())

    override fun drop(n: Int): PersistentList<T> {
        require(n >= 0) { "Requested element count $n is less than zero." }
        if (n == 0) return this
        if (offset + n >= backingList.size) return nullCons()
        return PersistentWrapper(backingList, true, offset + n)
    }

    override fun toString(): String = commonToString()
    override fun equals(other: Any?): Boolean = commonEqualityCheck(other)
    override fun hashCode(): Int {
        return actualList.hashCode()
    }
}

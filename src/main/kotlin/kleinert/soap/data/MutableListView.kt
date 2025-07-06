package kleinert.soap.data

class MutableListView<T>(val fromIndex: Int, val toIndexInclusive: Int, private val origin: MutableList<T>) :
    SimpleList<T> {

    override val size: Int = toIndexInclusive - fromIndex
    override fun getUnchecked(index: Int): T {
        if (index < 0||index > toIndexInclusive) throw IndexOutOfBoundsException()
        return origin[index + fromIndex]
    }
    override fun setUnchecked(index: Int, element: T): T{
        if (index < 0||index > toIndexInclusive) throw IndexOutOfBoundsException()
        return origin.set(index + fromIndex, element)
    }

    // TODO: Optimize subList to prevent O(z) complexity.
}
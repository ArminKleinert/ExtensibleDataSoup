package kleinert.soap.data


interface Matrix<T> {
    companion object {
        data class Position(val rowIndex: Int, val colIndex: Int)
    }

    val size: Int
        get() = m

    val dim: Pair<Int, Int>
        get() = m to n

    /*
    The matrix
        [[1 2 3]
         [4 5 6]]
     has dimensions m=2, n = 3;
     */
    val m: Int // Number of rows / height
    val n: Int // Number of columns / width

    fun packedRows(): List<T> =
        (0..<m).flatMap { getRow(it) }
    fun packedCols(): List<T> =
        (0..<n).flatMap { getCol(it) }

    fun subMatrix(rowIndices: IntArray, colIndices: IntArray): List<List<T>> {
        val res: MutableList<List<T>> = mutableListOf()
        for (row in 0..<m) {
            val rowValues = mutableListOf<T>()
            for (col in 0..<n)
                rowValues.add(get(row, col))
            res.add(rowValues.toList())
        }
        return res.toList()
    }

    operator fun get(index: Int): List<T> = getRow(index)

    fun getRow(rowIndex: Int): List<T> {
        val rowValues = mutableListOf<T>()
        for (colIndex in 0..<n)
            rowValues.add(get(rowIndex, colIndex))
        return rowValues.toList()
    }

    fun getCol(colIndex: Int): List<T> {
        val colValues = mutableListOf<T>()
        for (rowIndex in 0..<m)
            colValues.add(get(colIndex, rowIndex))
        return colValues.toList()
    }

    fun getRows(): List<List<T>> =
        (0..<m).map { getRow(it) }

    fun getRows(rowIndices: IntArray): List<List<T>> =
        rowIndices.map { getRow(it) }

    fun getCols(): List<List<T>> =
        (0..<n).map { getCol(it) }

    fun getCols(colIndices: IntArray): List<List<T>> =
        colIndices.map { getCol(it) }

    fun get(rowIndex:Int, colIndex: Int): T

    fun set(rowIndex:Int, colIndex: Int, element: T): Unit

    fun setRow(rowIndex:Int, row: List<T>) {
        if (rowIndex < 0 || rowIndex >= m) throw IndexOutOfBoundsException()
        if (row.size != m) throw IllegalArgumentException()

        for ((index, element) in row.withIndex())
            set(rowIndex, index, element)
    }

    fun setCol(colIndex:Int, row: List<T>) {
        if (row.size != m) throw IllegalArgumentException()

        for ((index, element) in row.withIndex())
            set(index, index, element)
    }

    fun isEmpty(): Boolean = m == 0 || n == 0

    fun valueIterator(): Iterator<T> = packedRows().iterator()
    fun rowIterator(): Iterator<List<T>> = getRows().iterator()
    fun colIterator(): Iterator<List<T>> = getCols().iterator()

    fun iteratePositions(f: (Int, Int, T) -> Unit) {
        for (row in 0..<m)
            for (col in 0..<n)
                f(row, col, get(row, col))
    }

    fun positionOf(element: T): Position {
        for (row in 0..<m)
            for (col in 0..<n)
                if (get(row,col) == element)
                    return Position(row,col)
        return Position(-1,-1)
    }

    fun positionsOf(element: T): List<Position> {
        val res = mutableListOf<Position>()
        for (row in 0..<m)
            for (col in 0..<n)
                if (get(row, col) == element)
                    res.add(Position(row, col))
        return res.toList()
    }

    fun contains(element: T): Boolean {
        for (row in 0..<m)
            for (col in 0..<n)
                if (get(row, col) == element)
                    return true
        return false
    }

    fun containsAll(elements: Collection<T>): Boolean =
        packedRows().containsAll(elements)

    fun requireColIndexInBounds(n: Int) {}

//     fun isEmpty(): Boolean = rows.isEmpty()
//     fun iterator(): Iterator<List<BigDecimal>> = rows.toMutableList().iterator()
//     fun listIterator(): ListIterator<List<BigDecimal>> = rows.toMutableList().listIterator()
//     fun listIterator(index: Int): ListIterator<List<BigDecimal>> = rows.toMutableList().listIterator(index)
//    override fun subList(fromIndex: Int, toIndex: Int): List<List<BigDecimal>> =
//        rows.toMutableList().subList(fromIndex, toIndex)
//
//    override fun lastIndexOf(element: List<BigDecimal>): Int = rows.lastIndexOf(element)
//    override fun indexOf(element: List<BigDecimal>): Int = rows.indexOf(element)
//    override fun containsAll(elements: Collection<List<BigDecimal>>): Boolean = rows.containsAll(elements)
//    override fun contains(element: List<BigDecimal>): Boolean = rows.contains(element)
}
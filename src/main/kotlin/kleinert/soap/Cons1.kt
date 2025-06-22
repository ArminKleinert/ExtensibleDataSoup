package kleinert.soap
//
//import kotlin.jvm.functions.FunctionN
//
//sealed interface Cons<T> : List<T>, Iterable<T> {
//    class ConsIterator<T>(private var cons: Cons<T>) : Iterator<T> {
//        override fun hasNext(): Boolean = !cons.isEmpty()
//
//        override fun next(): T {
//            val temp = cons.car
//            cons = cons.cdr
//            return temp
//        }
//    }
//
//    companion object {
//        fun <T> of(vararg elements: T) =
//            if (elements.isEmpty()) Empty<T>()
//            else Cell(elements.asIterable())
//    }
//
//    val car: T
//    val cdr: Cons<T>
//    override val size: Int
//
//    override fun isEmpty(): Boolean
//
//    override fun indexOf(element: T): Int
//
//    fun cons(element: T): Cons<T> = Cell(element, this)
//
//    override fun iterator(): Iterator<T> = ConsIterator(this)
//
//    override fun get(index: Int): T = when {
//        index < 0 -> throw IndexOutOfBoundsException("Index $index is less than 0.")
//        index > size -> throw IndexOutOfBoundsException("Index $index is out of bounds (size $size).")
//        else -> getUnchecked(this, index)
//    }
//
//    private tailrec fun getUnchecked(cons: Cons<T>, index: Int): T {
//        return if (index == 0) cons.car
//        else getUnchecked(cons.cdr, index - 1)
//    }
//
//
//    override fun containsAll(elements: Collection<T>): Boolean {
//        for (e in elements)
//            if (!contains(e)) return false
//        return true
//    }
//
//    override fun contains(element: T): Boolean =
//        indexOf(element) >= 0
//
//    override fun listIterator(): ListIterator<T> = toList().listIterator()
//
//    override fun listIterator(index: Int): ListIterator<T> = toList().listIterator(index)
//
//    override fun subList(fromIndex: Int, toIndex: Int): List<T> = toList().subList(fromIndex, toIndex)
//
//    override fun lastIndexOf(element: T): Int = countedLastIndexOf(this, element, 0, -1)
//
//    private tailrec fun countedLastIndexOf(cons: Cons<T>, element: T, counter: Int, lastIndex: Int): Int {
//        if (cons.isEmpty())
//            return lastIndex
//
//        return countedLastIndexOf(
//            cons.cdr, element, counter + 1,
//            if (cons.car == element) counter else lastIndex
//        )
//    }
//
//    fun take(n: Int) = splitAt(n).first
//
//    fun drop(n: Int) = splitAt(n).second
//
//    fun splitAt(n: Int): Pair<Cons<T>, Cons<T>> {
//        var front: Cons<T> = Empty()
//        var back = this
//        while (back.isNotEmpty()) {
//            front = front.cons(back.car)
//            back = back.cdr
//        }
//        return front.reversed() to back
//    }
//
//    fun reversed(): Cons<T> {
//        var result: Cons<T> = Empty<T>()
//        var rest = this
//        while (rest.isNotEmpty()) {
//            result = result.cons(rest.car)
//            rest = rest.cdr
//        }
//        return result
//    }
//
//    class Empty<T> : Cons<T> {
//        override val car: T
//            get() = throw NoSuchElementException()
//        override val cdr: Cons<T>
//            get() = this
//
//        override val size: Int = 0
//
//        override fun isEmpty(): Boolean = true
//        override fun containsAll(elements: Collection<T>): Boolean = false
//
//        override fun contains(element: T): Boolean = false
//
//        override fun equals(other: Any?): Boolean {
//            if (this === other) return true
//            if (other !is Iterable<*>) return false
//
//            if (other is Cons<*> && other.isEmpty()) return true
//            if (other is List<*> && other.isEmpty()) return true
//
//            return !other.iterator().hasNext()
//        }
//
//        override fun hashCode(): Int {
//            return size
//        }
//
//        override fun listIterator(): ListIterator<T> = listOf<T>().listIterator()
//
//        override fun listIterator(index: Int): ListIterator<T> = listOf<T>().listIterator(index)
//
//        override fun subList(fromIndex: Int, toIndex: Int): List<T> = listOf()
//
//        override fun lastIndexOf(element: T): Int = -1
//
//        override fun indexOf(element: T): Int = -1
//
//        override fun toString(): String = "()"
//    }
//
//    class Cell<T> : Cons<T> {
//        override val car: T
//        override val cdr: Cons<T>
//        override val size: Int
//
//        constructor(car: T, cdr: Cons<T>, size: Int = -1) {
//            this.car = car
//            this.cdr = cdr
//            this.size = if (size >= 1) size else 1 + cdr.size
//        }
//
//        constructor(iterable: Iterable<T>) {
//            val iter = iterable.iterator()
//            require(iter.hasNext())
//
//            val lst = iterable.reversed().fold(Empty<T>() as Cons<T>) { acc, t -> acc.cons(t) }
//            this.car = lst.car
//            this.cdr = lst.cdr
//            this.size = lst.size
//        }
//
//        constructor(cons: Cons<T>) {
//            require(cons.isNotEmpty())
//            this.car = cons.car
//            this.cdr = cons.cdr
//            this.size = cons.size
//        }
//
//        fun component1() = car
//        fun component2() = cdr
//
//        override fun isEmpty(): Boolean = false
//
//        override fun indexOf(element: T): Int {
//            if (car == element) return 0
//
//            var counter = 0
//            var rest = cdr
//            while (rest.isNotEmpty()) {
//                if (rest.car == element) return counter
//                rest = rest.cdr
//                counter++
//            }
//
//            return -1
//        }
//
//        override fun equals(other: Any?): Boolean {
//            if (this === other) return true
//            if (other !is Iterable<*>) return false
//
//            val iter = iterator()
//            val otherIter = other.iterator()
//
//            while (iter.hasNext()) {
//                if (!otherIter.hasNext()) return false
//                if (iter.next() != otherIter.next()) return false
//            }
//
//            return !otherIter.hasNext()
//        }
//
//        override fun hashCode(): Int {
//            var result = car?.hashCode() ?: 0
//            result = 31 * result + cdr.hashCode()
//            result = 31 * result + size
//            return result
//        }
//
//        override fun toString(): String = StringBuilder()
//            .append('(')
//            .append(joinToString(", "))
//            .append(")").toString()
//    }
//}

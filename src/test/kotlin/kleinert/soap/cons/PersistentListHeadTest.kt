package kleinert.soap.cons

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.random.Random

class PersistentListHeadTest {

    private val one = nullCons<Int>().cons(1)
    private val two = one.cons(2)
    private val three = two.cons(3)

    @Test
    fun testConstructor() {
        assertEquals(listOf(1), PersistentListHead(listOf(1)))
        assertEquals(listOf(1, 2), PersistentListHead(listOf(1, 2)))
        assertEquals(listOf(1, 2, 3, 4), PersistentListHead(listOf(1, 2, 3, 4)))
        assertEquals(PersistentListHead(1, nullCons()), PersistentListHead(1, nullCons()))
        assertEquals(PersistentListHead(1, PersistentListHead(2, nullCons())), PersistentListHead(1, PersistentListHead(2, nullCons())))
    }

    @Test
    fun getSize() {
        assertEquals(1, one.size)
        assertEquals(2, two.size)
        assertEquals(3, three.size)
    }

    @Test
    fun cons() {
        val list = nullCons<Int>()
        assertEquals(0, list.size)

        assertInstanceOf(PersistentList::class.java, one)
        assertEquals(PersistentWrapper(listOf(1)), one)
        assertEquals(PersistentWrapper(listOf(2, 1)), two)
        assertEquals(PersistentWrapper(listOf(3, 2, 1)), three)
    }

    @Test
    fun cdr() {
        val one = nullCons<Int>().cons(1)
        val two = one.cons(2)
        val three = two.cons(3)
        assertInstanceOf(PersistentList::class.java, one.cdr)
        assertInstanceOf(PersistentList::class.java, two.cdr)
        assertInstanceOf(PersistentList::class.java, three.cdr)

        assertSame(one.cdr, two.cdr.cdr)
        assertSame(one.cdr, three.cdr.cdr.cdr)
        assertSame(one, two.cdr)
        assertSame(two, three.cdr)
    }

    @Test
    fun car() {
        val one = nullCons<Int>().cons(1)
        val two = one.cons(2)
        val three = two.cons(3)

        assertThrows(NoSuchElementException::class.java) { one.cdr.car }
        assertThrows(NoSuchElementException::class.java) { two.cdr.cdr.car }
        assertThrows(NoSuchElementException::class.java) { three.cdr.cdr.cdr.car }

        assertEquals(1, one.car)
        assertEquals(2, two.car)
        assertEquals(3, three.car)
        assertEquals(1, two.cdr.car)
        assertEquals(2, three.cdr.car)
        assertEquals(1, three.cdr.cdr.car)

        assertSame(one.cdr, three.cdr.cdr.cdr)
        assertSame(one, two.cdr)
        assertSame(two, three.cdr)
    }

    @Test
    fun contains() {
        assertFalse(one.contains(0))
        assertTrue(one.contains(1))
        assertFalse(one.contains(2))
        assertFalse(one.contains(3))
        assertFalse(one.contains(4))

        assertFalse(two.contains(0))
        assertTrue(two.contains(1))
        assertTrue(two.contains(2))
        assertFalse(two.contains(3))
        assertFalse(two.contains(4))

        assertFalse(three.contains(0))
        assertTrue(three.contains(1))
        assertTrue(three.contains(2))
        assertTrue(three.contains(3))
        assertFalse(three.contains(4))
    }

    @Test
    fun containsAll() {
        assertTrue(one.containsAll(listOf(1)))
        assertFalse(one.containsAll(listOf(2)))
        assertFalse(one.containsAll(listOf(3)))
        assertFalse(one.containsAll(listOf(1, 2)))
        assertFalse(one.containsAll(listOf(2, 3)))
        assertFalse(one.containsAll(listOf(1, 2, 3)))

        assertTrue(two.containsAll(listOf(1)))
        assertTrue(two.containsAll(listOf(2)))
        assertFalse(two.containsAll(listOf(3)))
        assertTrue(two.containsAll(listOf(1, 2)))
        assertFalse(two.containsAll(listOf(2, 3)))
        assertFalse(two.containsAll(listOf(1, 2, 3)))

        assertTrue(three.containsAll(listOf(1)))
        assertTrue(three.containsAll(listOf(2)))
        assertTrue(three.containsAll(listOf(3)))
        assertTrue(three.containsAll(listOf(1, 2)))
        assertTrue(three.containsAll(listOf(2, 3)))
        assertTrue(three.containsAll(listOf(1, 2, 3)))
    }

    @Test
    fun get() {
        assertEquals(1, one[0])
        assertThrows(IndexOutOfBoundsException::class.java) { one[1] }
        assertThrows(IndexOutOfBoundsException::class.java) { one[2] }
        assertThrows(IndexOutOfBoundsException::class.java) { one[3] }

        assertEquals(2, two[0])
        assertEquals(1, two[1])
        assertThrows(IndexOutOfBoundsException::class.java) { two[2] }
        assertThrows(IndexOutOfBoundsException::class.java) { two[3] }

        assertEquals(3, three[0])
        assertEquals(2, three[1])
        assertEquals(1, three[2])
        assertThrows(IndexOutOfBoundsException::class.java) { three[3] }
    }

    @Test
    fun isEmpty() {
        assertFalse(one.isEmpty())
        assertTrue(one.cdr.isEmpty())

        assertFalse(two.isEmpty())
        assertFalse(two.cdr.isEmpty())
        assertTrue(two.cdr.cdr.isEmpty())

        assertFalse(three.isEmpty())
        assertFalse(three.cdr.isEmpty())
        assertFalse(three.cdr.cdr.isEmpty())
        assertTrue(three.cdr.cdr.cdr.isEmpty())
    }

    @Test
    fun indexOf() {
        assertEquals(-1, one.indexOf(0))
        assertEquals(0, one.indexOf(1))
        assertEquals(-1, one.indexOf(2))
        assertEquals(-1, one.indexOf(3))
        assertEquals(-1, one.indexOf(4))

        assertEquals(-1, two.indexOf(0))
        assertEquals(1, two.indexOf(1))
        assertEquals(0, two.indexOf(2))
        assertEquals(-1, two.indexOf(3))
        assertEquals(-1, two.indexOf(4))

        assertEquals(-1, three.indexOf(0))
        assertEquals(2, three.indexOf(1))
        assertEquals(1, three.indexOf(2))
        assertEquals(0, three.indexOf(3))
        assertEquals(-1, three.indexOf(4))

        val threeConsOne = three.cons(1)
        assertEquals(-1, threeConsOne.indexOf(0))
        assertEquals(0, threeConsOne.indexOf(1))
        assertEquals(2, threeConsOne.indexOf(2))
        assertEquals(1, threeConsOne.indexOf(3))
        assertEquals(-1, threeConsOne.indexOf(4))
    }

    @Test
    fun lastIndexOf() {
        assertEquals(-1, one.lastIndexOf(0))
        assertEquals(0, one.lastIndexOf(1))
        assertEquals(-1, one.lastIndexOf(2))
        assertEquals(-1, one.lastIndexOf(3))
        assertEquals(-1, one.lastIndexOf(4))

        assertEquals(-1, two.lastIndexOf(0))
        assertEquals(1, two.lastIndexOf(1))
        assertEquals(0, two.lastIndexOf(2))
        assertEquals(-1, two.lastIndexOf(3))
        assertEquals(-1, two.lastIndexOf(4))

        assertEquals(-1, three.lastIndexOf(0))
        assertEquals(2, three.lastIndexOf(1))
        assertEquals(1, three.lastIndexOf(2))
        assertEquals(0, three.lastIndexOf(3))
        assertEquals(-1, three.lastIndexOf(4))

        val threeConsOne = three.cons(1)
        assertEquals(-1, threeConsOne.lastIndexOf(0))
        assertEquals(3, threeConsOne.lastIndexOf(1))
        assertEquals(2, threeConsOne.lastIndexOf(2))
        assertEquals(1, threeConsOne.lastIndexOf(3))
        assertEquals(-1, threeConsOne.lastIndexOf(4))
    }

    @Test
    fun iterator() {
        run {
            var counter = 0
            for (e in one.cdr) {
                counter++
            }
            assertEquals(0, counter)
        }
        run {
            var counter = 0
            for (e in three) {
                counter++
            }
            assertEquals(3, counter)
        }
        run {
            var counter = 0
            PersistentWrapper(three).forEach { _ -> counter++ }
            assertEquals(3, counter)
        }

        run {
            val iterator = three.iterator()
            var sum = 0
            while (iterator.hasNext()) {
                sum += iterator.next()
            }
            assertEquals(6, sum)
        }

        assertEquals(6, three.sum())
        assertEquals(6, three.fold(0, Int::plus))
        assertEquals(6, three.foldRight(0, Int::plus))
        assertEquals(6, three.reduce(Int::plus))
    }

    @Test
    fun listIterator() {
        run {
            var counter = 0
            for (e in one.cdr.listIterator()) {
                counter++
            }
            assertEquals(0, counter)
        }
        run {
            var counter = 0
            for (e in three.listIterator()) {
                counter++
            }
            assertEquals(3, counter)
        }
        run {
            var counter = 0
            three.listIterator().forEach { _ -> counter++ }
            assertEquals(3, counter)
        }

        run {
            val iterator = three.listIterator()
            var sum = 0
            while (iterator.hasNext()) {
                sum += iterator.next()
            }
            assertEquals(6, sum)
        }
    }

    @Test
    fun subList() {
        assertThrows(IndexOutOfBoundsException::class.java) { three.subList(0, 5) }
        assertInstanceOf(PersistentList::class.java, three.subList(0, 2))
        assertEquals(three, three.subList(0, 3))
        assertEquals(two, three.subList(1, 3))
        assertEquals(one, three.subList(2, 3))
    }

    @Test
    fun toMutableList() {
        assertEquals(mutableListOf(1), one.toMutableList())
        assertEquals(mutableListOf(2, 1), two.toMutableList())
        assertEquals(mutableListOf(3, 2, 1), three.toMutableList())
    }

    @Test
    fun reversed() {
        assertInstanceOf(PersistentList::class.java, one.reversed())
        assertInstanceOf(PersistentList::class.java, two.reversed())
        assertInstanceOf(PersistentList::class.java, three.reversed())

        assertEquals(three, three.reversed().reversed())

        assertEquals(PersistentList.of(1), one.reversed())
        assertEquals(PersistentList.of(1, 2), two.reversed())
        assertEquals(PersistentList.of(1, 2, 3), three.reversed())
    }

    @Test
    fun map() {
        assertInstanceOf(PersistentList::class.java, one.map { it })
        assertInstanceOf(PersistentList::class.java, two.map { it })
        assertInstanceOf(PersistentList::class.java, three.map { it })

        assertEquals(PersistentList.of(1), one.map { it })
        assertEquals(PersistentList.of(3, 2), two.map { it + 1 })
    }

    @Test
    fun mapIndexed() {
        assertInstanceOf(PersistentList::class.java, one.mapIndexed { i, e -> e + i })
        assertInstanceOf(PersistentList::class.java, two.mapIndexed { i, e -> e + i })
        assertInstanceOf(PersistentList::class.java, three.mapIndexed { i, e -> e + i })

        assertEquals(PersistentList.of(1), one.mapIndexed { i, e -> e + i })
        assertEquals(PersistentList.of(2, 2), two.mapIndexed { i, e -> e + i })
        assertEquals(PersistentList.of(3, 3, 3), three.mapIndexed { i, e -> e + i })
        assertEquals(PersistentList.of(4, 3, 2), three.mapIndexed { _, e -> e + 1 })
    }

    @Test
    fun filter() {
        assertInstanceOf(PersistentList::class.java, one.filter { true })
        assertInstanceOf(PersistentList::class.java, three.filter { it % 2 != 0 })

        assertEquals(PersistentList.of<Int>(), one.filter { it % 2 == 0 })
        assertEquals(PersistentList.of(1), one.filter { it % 2 != 0 })
        assertEquals(PersistentList.of(1), two.filter { it % 2 != 0 })
        assertEquals(PersistentList.of(3, 1), three.filter { it % 2 != 0 })
    }

    @Test
    fun filterNot() {
        assertInstanceOf(PersistentList::class.java, one.filterNot { true })
        assertInstanceOf(PersistentList::class.java, three.filterNot { it % 2 != 0 })

        assertEquals(PersistentList.of<Int>(), one.filterNot { it % 2 != 0 })
        assertEquals(PersistentList.of(1), one.filterNot { it % 2 == 0 })
        assertEquals(PersistentList.of(1), two.filterNot { it % 2 == 0 })
        assertEquals(PersistentList.of(3, 1), three.filterNot { it % 2 == 0 })
    }

    @Test
    fun cadr() {
        assertThrows(NoSuchElementException::class.java) { one.cadr }
        assertEquals(1, two.cadr)
        assertEquals(2, three.cadr)
        assertEquals(3, three.cons(4).cadr)
    }

    @Test
    fun caddr() {
        assertThrows(NoSuchElementException::class.java) { one.caddr }
        assertThrows(NoSuchElementException::class.java) { two.caddr }
        assertEquals(1, three.caddr)
        assertEquals(2, three.cons(4).caddr)
    }

    @Test
    fun cadddr() {
        assertThrows(NoSuchElementException::class.java) { one.cadddr }
        assertThrows(NoSuchElementException::class.java) { two.cadddr }
        assertThrows(NoSuchElementException::class.java) { three.cadddr }
        assertEquals(1, three.cons(4).cadddr)
    }

    @Test
    fun cddr() {
        assertEquals(nullCons<Int>(), one.cddr)
        assertEquals(nullCons<Int>(), two.cddr)
        assertEquals(PersistentList.of(1), three.cddr)
        assertEquals(PersistentList.of(2, 1), three.cons(4).cddr)
        assertEquals(PersistentList.of(3, 2, 1), three.cons(4).cons(5).cddr)
    }

    @Test
    fun cdddr() {
        assertEquals(nullCons<Int>(), one.cdddr)
        assertEquals(nullCons<Int>(), two.cdddr)
        assertEquals(nullCons<Int>(), three.cdddr)
        assertEquals(PersistentList.of(1), three.cons(4).cdddr)
        assertEquals(PersistentList.of(2, 1), three.cons(4).cons(5).cdddr)
    }

    @Test
    fun cddddr() {
        assertEquals(nullCons<Int>(), one.cddddr)
        assertEquals(nullCons<Int>(), two.cddddr)
        assertEquals(nullCons<Int>(), three.cddddr)
        assertEquals(nullCons<Int>(), three.cons(4).cddddr)
        assertEquals(PersistentList.of(1), three.cons(4).cons(5).cddddr)
    }

    @Test
    fun flatMap() {
        assertInstanceOf(PersistentList::class.java, three.flatMap { listOf(it) })
        assertEquals(three, three.flatMap { listOf(it) })
        assertEquals(PersistentList.of(3, 3, 2, 2, 1, 1), three.flatMap { listOf(it, it) })
    }

    @Test
    fun take() {
        assertInstanceOf(PersistentList::class.java, three.take(0))
        assertInstanceOf(PersistentList::class.java, three.take(1))
        assertInstanceOf(PersistentList::class.java, three.take(2))
        assertInstanceOf(PersistentList::class.java, three.take(22))

        assertEquals(PersistentList.of<Int>(), three.take(0))
        assertEquals(PersistentList.of(3), three.take(1))
        assertEquals(PersistentList.of(3, 2), three.take(2))
        assertEquals(PersistentList.of(3, 2, 1), three.take(3))
        assertEquals(PersistentList.of(3, 2, 1), three.take(4))
    }

    @Test
    fun drop() {
        assertInstanceOf(PersistentList::class.java, three.drop(0))
        assertInstanceOf(PersistentList::class.java, three.drop(1))
        assertInstanceOf(PersistentList::class.java, three.drop(2))
        assertInstanceOf(PersistentList::class.java, three.drop(22))

        assertEquals(three, three.drop(0))
        assertEquals(two, three.drop(1))
        assertEquals(one, three.drop(2))
        assertEquals(PersistentList.of<Int>(), three.drop(3))
        assertEquals(PersistentList.of<Int>(), three.drop(4))
    }

    @Test
    fun takeWhile() {
        assertInstanceOf(PersistentList::class.java, three.takeWhile { true })
        assertInstanceOf(PersistentList::class.java, three.takeWhile { false })
        assertInstanceOf(PersistentList::class.java, three.takeWhile { it % 2 == 1 })

        assertEquals(three, three.takeWhile { true })
        assertEquals(PersistentList.of(3), three.takeWhile { it % 2 == 1 })
        assertEquals(PersistentList.of<Int>(), three.takeWhile { false })
    }

    @Test
    fun dropWhile() {
        assertInstanceOf(PersistentList::class.java, three.dropWhile { true })
        assertInstanceOf(PersistentList::class.java, three.dropWhile { false })
        assertInstanceOf(PersistentList::class.java, three.dropWhile { it % 2 == 1 })

        assertEquals(three, three.dropWhile { false })
        assertEquals(two, three.dropWhile { it % 2 == 1 })
        assertEquals(PersistentList.of<Int>(), three.dropWhile { true })
    }

//    @Test
//    fun sorted() {
//        assertInstanceOf(Cons::class.java, (CdrCodedList.of<Int>() as Cons<Int>).sorted())
//        assertInstanceOf(Cons::class.java, CdrCodedList.of<Int>().sorted())
//        assertEquals(CdrCodedList.of<Int>(), CdrCodedList.of<Int>().sorted())
//        assertInstanceOf(Cons::class.java, CdrCodedList.of(1, 2, 3).sorted())
//        assertEquals(CdrCodedList.of(1, 2, 3), CdrCodedList.of(1, 2, 3).sorted())
//        assertEquals(CdrCodedList.of(1, 2, 3), CdrCodedList.of(3, 2, 1).sorted())
//        assertEquals(CdrCodedList.of(1, 2, 3, 3), CdrCodedList.of(3, 2, 3, 1).sorted())
//    }

//    @Test
//    fun sortedDescending() {
//        assertInstanceOf(Cons::class.java, CdrCodedList.of<Int>().sortedDescending())
//        assertEquals(CdrCodedList.of<Int>(), CdrCodedList.of<Int>().sortedDescending())
//        assertInstanceOf(Cons::class.java, CdrCodedList.of(1, 2, 3).sortedDescending())
//        assertEquals(CdrCodedList.of(3, 2, 1), CdrCodedList.of(1, 2, 3).sortedDescending())
//        assertEquals(CdrCodedList.of(3, 2, 1), CdrCodedList.of(3, 2, 1).sortedDescending())
//        assertEquals(CdrCodedList.of(3, 3, 2, 1), CdrCodedList.of(3, 2, 3, 1).sortedDescending())
//    }

    @Test
    fun sortedBy() {
        assertInstanceOf(PersistentList::class.java, one.sortedBy { it })
        assertInstanceOf(PersistentList::class.java, three.sortedBy { it })

        assertEquals(PersistentList.of(1), one.sortedBy { it })
        assertEquals(PersistentList.of(1, 2, 3), three.sortedBy { it })
        assertEquals(PersistentList.of(1, 2, 3), three.reversed().sortedBy { it })
    }

    @Test
    fun sortedByDescending() {
        assertInstanceOf(PersistentList::class.java, one.sortedByDescending { it })
        assertInstanceOf(PersistentList::class.java, three.sortedByDescending { it })

        assertEquals(PersistentList.of(1), one.sortedByDescending { it })
        assertEquals(PersistentList.of(3, 2, 1), three.sortedByDescending { it })
        assertEquals(PersistentList.of(3, 2, 1), three.reversed().sortedByDescending { it })
    }

    @Test
    fun sortedWith() {
        assertInstanceOf(PersistentList::class.java, one.sortedWith { n, m -> n.compareTo(m) })
        assertInstanceOf(PersistentList::class.java, three.sortedWith { n, m -> n.compareTo(m) })
        assertInstanceOf(PersistentList::class.java, one.sortedWith { n, m -> -n.compareTo(m) })
        assertInstanceOf(PersistentList::class.java, three.sortedWith { n, m -> -n.compareTo(m) })

        assertEquals(PersistentList.of(1), one.sortedWith { n, m -> n.compareTo(m) })
        assertEquals(PersistentList.of(1, 2, 3), three.sortedWith { n, m -> n.compareTo(m) })
        assertEquals(PersistentList.of(1, 2, 3), three.reversed().sortedWith { n, m -> n.compareTo(m) })
        assertEquals(PersistentList.of(1), one.sortedWith { n, m -> -n.compareTo(m) })
        assertEquals(PersistentList.of(3, 2, 1), three.sortedWith { n, m -> -n.compareTo(m) })
        assertEquals(PersistentList.of(3, 2, 1), three.reversed().sortedWith { n, m -> -n.compareTo(m) })
    }

    @Test
    fun distinct() {
        assertInstanceOf(PersistentList::class.java, three.distinct())
        assertInstanceOf(PersistentList::class.java, two.cons(1).distinct())

        assertEquals(three, three.distinct())
        assertEquals(nullCons<Int>().cons(2).cons(1), two.cons(1).distinct())
    }

    @Test
    fun shuffled() {
        assertInstanceOf(PersistentList::class.java, three.shuffled())

        val seed = 0xDEADBEEF
        val rand = Random(seed)

        assertEquals(one, one.shuffled(rand))

        val oneOneOne = nullCons<Int>().cons(1).cons(1).cons(1)
        assertEquals(oneOneOne, oneOneOne.shuffled(rand))
    }

    @Test
    fun asSequence() {
        assertInstanceOf(Sequence::class.java, one.asSequence())
        assertInstanceOf(Sequence::class.java, two.asSequence())
        assertInstanceOf(Sequence::class.java, three.asSequence())
        assertInstanceOf(Sequence::class.java, three.asSequence().map { it + 1 })

        assertEquals(listOf(1), one.asSequence().toList())
        assertEquals(listOf(2, 1), two.asSequence().toList())
        assertEquals(listOf(4, 3, 2), three.asSequence().map { it + 1 }.toList())
    }

    @Test
    fun plusElement() {
        assertInstanceOf(PersistentList::class.java, one + 1)
        assertInstanceOf(PersistentList::class.java, two + 2)

        assertEquals(PersistentList.of(1, 1), one + 1)
        assertEquals(PersistentList.of(3, 2, 1, 2), three + 2)
    }

    @Test
    fun plusIterable() {
        assertInstanceOf(PersistentList::class.java, one + listOf(2, 3))

        assertEquals(three, three + listOf())

        assertEquals(PersistentList.of(2, 1), nullCons<Int>().cons(2) + listOf(1))
        assertEquals(PersistentList.of(1, 2, 3), nullCons<Int>().cons(1) + listOf(2, 3))

        assertEquals(PersistentList.of(1, 2, 3, 4, 5), one + (2..5))
        assertEquals(PersistentList.of(1, 2, 3, 4, 5), one + (2..<6))
        assertEquals(PersistentList.of(1, 2, 3, 4, 5), one + sequenceOf(2, 3, 4, 5))

        assertEquals(PersistentList.of(1, 2, 3, 4, 5), one + PersistentList.of(2, 3, 4, 5))
        assertEquals(PersistentList.of(3, 2, 1, 3, 4), three + PersistentWrapper(listOf(3, 4)))
    }

    @Test
    fun plusVList() {
        assertInstanceOf(VList::class.java, three + VList.of())
        assertEquals(VList.of(3, 2, 1), three + VList.of())

        assertInstanceOf(VList::class.java, three + VList.of(1))
        assertEquals(VList.of(3, 2, 1, 1), three + VList.of(1))

        assertInstanceOf(VList::class.java, three + VList.of(1, 2))
        assertEquals(VList.of(3, 2, 1, 1, 2), three + VList.of(1, 2))

        assertInstanceOf(VList::class.java, three + VList.toVList(0..5))
        assertEquals(VList.of(3, 2, 1, 0, 1, 2, 3, 4, 5), three + VList.toVList(0..5))
    }

    @Test
    fun isSingleton() {
        assertFalse(one.cdr.isSingleton())
        assertTrue(one.isSingleton())
        assertFalse(two.isSingleton())
        assertFalse(three.isSingleton())
    }
}
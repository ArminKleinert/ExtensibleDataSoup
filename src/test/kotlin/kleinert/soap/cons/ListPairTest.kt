package kleinert.soap.cons

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.random.Random

class ListPairTest {
    private val oneTwoThree = PersistentList.of(1, 2, 3)
    private val oneTwoThreeOneTwoThree = ListPair.concat(oneTwoThree, oneTwoThree)

    @Test
    fun testConstructor() {
        run {
            assertTrue(ListPair.concat(nullCons<Int>(), nullCons()).isEmpty())
            assertEquals(nullCons<Int>(), ListPair.concat(nullCons<Int>(), nullCons()))
        }
        run {
            val lst = nullCons<Int>().cons(1).cons(2).cons(3)
            assertSame(lst, ListPair.concat(lst, nullCons()))
            assertSame(lst, ListPair.concat(nullCons(), lst))
            assertSame(lst, ListPair.concat(lst, VList.of()))
            assertSame(lst, ListPair.concat(VList.of(), lst))
            assertEquals(lst + lst, ListPair.concat(lst, lst))
        }
        run {
            val lst = PersistentList.of(1, 2, 3)
            assertSame(lst, ListPair.concat(lst, nullCons()))
            assertSame(lst, ListPair.concat(nullCons(), lst))
            assertSame(lst, ListPair.concat(lst, VList.of()))
            assertSame(lst, ListPair.concat(VList.of(), lst))
            assertEquals(lst + lst, ListPair.concat(lst, lst))
        }
        run {
            val lst = PersistentWrapper.of(1, 2, 3)
            assertSame(lst, ListPair.concat(lst, nullCons()))
            assertSame(lst, ListPair.concat(nullCons(), lst))
            assertSame(lst, ListPair.concat(lst, VList.of()))
            assertSame(lst, ListPair.concat(VList.of(), lst))
            assertEquals(lst + lst, ListPair.concat(lst, lst))
        }
        run {
            val lst = VList.of(1, 2, 3)
            assertSame(lst, ListPair.concat(lst, nullCons()))
            assertSame(lst, ListPair.concat(nullCons(), lst))
            assertSame(lst, ListPair.concat(lst, VList.of()))
            assertSame(lst, ListPair.concat(VList.of(), lst))
            assertEquals(lst + lst, ListPair.concat(lst, lst))
        }
    }

    @Test
    fun getSize() {
        val lst = oneTwoThreeOneTwoThree
        assertEquals(0, ListPair.concat(nullCons<Int>(), nullCons()).size)
        assertEquals(oneTwoThree.size * 2, lst.size)
        assertEquals(oneTwoThree.size * 3, ListPair.concat(oneTwoThree, lst).size)
    }

    @Test
    fun cons() {
        assertEquals(PersistentList.of(1, 1, 2, 3, 1, 2, 3), oneTwoThreeOneTwoThree.cons(1))
    }

    @Test
    fun cdr() {
        val lst = oneTwoThreeOneTwoThree
        assertEquals(PersistentList.of(1, 2, 3, 1, 2, 3), lst)
        assertEquals(PersistentList.of(2, 3, 1, 2, 3), lst.cdr)
        assertEquals(PersistentList.of(3, 1, 2, 3), lst.cdr.cdr)
        assertEquals(PersistentList.of(1, 2, 3), lst.cdr.cdr.cdr)
        assertSame(oneTwoThree, lst.cdr.cdr.cdr)
        assertEquals(oneTwoThree.cdr, lst.cdr.cdr.cdr.cdr)
    }

    @Test
    fun car() {
        assertEquals(1, oneTwoThreeOneTwoThree.car)
        assertEquals(2, ListPair.concat(PersistentList.of(2, 3), oneTwoThree).car)
        assertEquals(3, ListPair.concat(PersistentList.of(3), oneTwoThree).car)
        assertEquals(oneTwoThree.car, ListPair.concat(PersistentList.of(), oneTwoThree).car)

    }

    @Test
    fun contains() {
        assertFalse(oneTwoThreeOneTwoThree.contains(0))
        assertTrue(oneTwoThreeOneTwoThree.contains(1))
        assertTrue(oneTwoThreeOneTwoThree.contains(2))
        assertTrue(oneTwoThreeOneTwoThree.contains(3))
        assertFalse(oneTwoThreeOneTwoThree.contains(4))

        assertTrue(ListPair.concat(oneTwoThree, PersistentList.of(4)).contains(4))
        assertTrue(ListPair.concat(PersistentList.of(0), oneTwoThree).contains(0))
    }

    @Test
    fun containsAll() {
        assertFalse(oneTwoThreeOneTwoThree.containsAll(listOf(0)))
        assertTrue(oneTwoThreeOneTwoThree.containsAll(listOf(1)))
        assertTrue(oneTwoThreeOneTwoThree.containsAll(listOf(2, 3)))
        assertTrue(oneTwoThreeOneTwoThree.containsAll(listOf(1, 2, 3)))
        assertFalse(oneTwoThreeOneTwoThree.containsAll(listOf(4)))

        assertFalse(oneTwoThreeOneTwoThree.containsAll(listOf(1, 4)))
        assertFalse(oneTwoThreeOneTwoThree.containsAll(listOf(1, 2, 3, 4)))

        assertTrue(ListPair.concat(oneTwoThree, PersistentList.of(4)).containsAll(listOf(1, 2, 3, 4)))
        assertTrue(ListPair.concat(PersistentList.of(0), oneTwoThree).containsAll(listOf(1, 2, 3, 0)))
    }

    @Test
    fun get() {
        assertEquals(oneTwoThree.car, ListPair.concat(oneTwoThree, PersistentList.of(0, 8, 9))[0])
        assertEquals(0, ListPair.concat(oneTwoThree, PersistentList.of(0, 8, 9))[3])
        assertThrows(IndexOutOfBoundsException::class.java) { oneTwoThreeOneTwoThree[6] }
    }

    @Test
    fun isEmpty() {
        assertFalse(oneTwoThreeOneTwoThree.isEmpty())
        assertFalse(ListPair.concat(PersistentList.of(0), PersistentList.of(9)).cdr.isEmpty())
        assertTrue(ListPair.concat(PersistentList.of(0), PersistentList.of(9)).cdr.cdr.isEmpty())
    }

    @Test
    fun indexOf() {
        assertEquals(0, ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(1, 2, 3)).indexOf(1))
        assertEquals(1, ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(1, 2, 3)).indexOf(2))
        assertEquals(2, ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(1, 2, 3)).indexOf(3))
        assertEquals(-1, ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(1, 2, 3)).indexOf(4))
    }

    @Test
    fun lastIndexOf() {
        assertEquals(3, ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(1, 2, 3)).lastIndexOf(1))
        assertEquals(4, ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(1, 2, 3)).lastIndexOf(2))
        assertEquals(5, ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(1, 2, 3)).lastIndexOf(3))
        assertEquals(-1, ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(1, 2, 3)).lastIndexOf(4))
    }

    @Test
    fun iterator() {
        run {
            var counter = 0
            for (e in oneTwoThreeOneTwoThree) {
                counter++
            }
            assertEquals(6, counter)
        }
        run {
            var counter = 0
            oneTwoThreeOneTwoThree.forEach { _ -> counter++ }
            assertEquals(6, counter)
        }

        run {
            val iterator = oneTwoThreeOneTwoThree.iterator()
            var sum = 0
            while (iterator.hasNext()) {
                sum += iterator.next()
            }
            assertEquals(12, sum)
        }

        assertEquals(12, oneTwoThreeOneTwoThree.sum())
        assertEquals(12, oneTwoThreeOneTwoThree.fold(0, Int::plus))
        assertEquals(12, oneTwoThreeOneTwoThree.foldRight(0, Int::plus))
        assertEquals(12, oneTwoThreeOneTwoThree.reduce(Int::plus))
    }

    @Test
    fun listIterator() {
        run {
            var counter = 0
            for (e in oneTwoThreeOneTwoThree.listIterator()) {
                counter++
            }
            assertEquals(6, counter)
        }
        run {
            var counter = 0
            oneTwoThreeOneTwoThree.listIterator().forEach { _ -> counter++ }
            assertEquals(6, counter)
        }

        run {
            val iterator = oneTwoThreeOneTwoThree.listIterator()
            var sum = 0
            while (iterator.hasNext()) {
                sum += iterator.next()
            }
            assertEquals(12, sum)
        }
    }

    @Test
    fun subList() {
        assertThrows(IndexOutOfBoundsException::class.java) {
            oneTwoThreeOneTwoThree.subList(
                0,
                oneTwoThreeOneTwoThree.size + 1
            )
        }
        assertEquals(oneTwoThreeOneTwoThree, oneTwoThreeOneTwoThree.subList(0, oneTwoThreeOneTwoThree.size))
        assertEquals(oneTwoThree, oneTwoThreeOneTwoThree.subList(0, oneTwoThree.size))
        assertEquals(PersistentList.of(2, 3, 4, 5), ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(4, 5, 6)).subList(1, 5))
    }

    @Test
    fun toMutableList() {
        assertEquals(
            (oneTwoThree.toMutableList() + oneTwoThree.toMutableList()).toMutableList(),
            oneTwoThreeOneTwoThree.toMutableList()
        )
    }

    @Test
    fun reversed() {
        assertInstanceOf(PersistentList::class.java, oneTwoThreeOneTwoThree.reversed())
        assertEquals(oneTwoThreeOneTwoThree, oneTwoThreeOneTwoThree.reversed().reversed())
        assertEquals(PersistentList.of(6, 5, 4, 3, 2, 1), ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(4, 5, 6)).reversed())
    }

    @Test
    fun map() {
        assertInstanceOf(PersistentList::class.java, oneTwoThreeOneTwoThree.map { it })
        assertEquals(oneTwoThreeOneTwoThree, oneTwoThreeOneTwoThree.map { it })
        assertEquals(oneTwoThreeOneTwoThree.toList().map { it + 1 }, oneTwoThreeOneTwoThree.map { it + 1 })
    }

    @Test
    fun mapIndexed() {
        assertInstanceOf(PersistentList::class.java, oneTwoThreeOneTwoThree.mapIndexed { i, e -> e + i })
        assertEquals(oneTwoThreeOneTwoThree, oneTwoThreeOneTwoThree.mapIndexed { _, e -> e })
        assertEquals(
            oneTwoThreeOneTwoThree.toList().mapIndexed { i, e -> e + i },
            oneTwoThreeOneTwoThree.mapIndexed { i, e -> e + i })
    }

    @Test
    fun filter() {
        assertInstanceOf(PersistentList::class.java, oneTwoThreeOneTwoThree.filter { true })
        assertInstanceOf(PersistentList::class.java, oneTwoThreeOneTwoThree.filter { false })

        assertEquals(oneTwoThreeOneTwoThree, oneTwoThreeOneTwoThree.filter { true })
        assertEquals(PersistentList.of<Int>(), oneTwoThreeOneTwoThree.filter { false })
        assertEquals(
            ListPair.concat(oneTwoThree.filter { it % 2 == 1 }, oneTwoThree.filter { it % 2 == 1 }),
            oneTwoThreeOneTwoThree.filter { it % 2 == 1 })
        assertEquals(
            ListPair.concat(oneTwoThree.filter { it % 2 == 0 }, oneTwoThree.filter { it % 2 == 0 }),
            oneTwoThreeOneTwoThree.filter { it % 2 == 0 })
    }

    @Test
    fun filterNot() {
        assertInstanceOf(PersistentList::class.java, oneTwoThreeOneTwoThree.filterNot { true })
        assertInstanceOf(PersistentList::class.java, oneTwoThreeOneTwoThree.filterNot { false })

        assertEquals(oneTwoThreeOneTwoThree, oneTwoThreeOneTwoThree.filterNot { false })
        assertEquals(PersistentList.of<Int>(), oneTwoThreeOneTwoThree.filterNot { true })
        assertEquals(
            ListPair.concat(oneTwoThree.filterNot { it % 2 == 1 }, oneTwoThree.filterNot { it % 2 == 1 }),
            oneTwoThreeOneTwoThree.filterNot { it % 2 == 1 })
        assertEquals(
            ListPair.concat(oneTwoThree.filterNot { it % 2 == 0 }, oneTwoThree.filterNot { it % 2 == 0 }),
            oneTwoThreeOneTwoThree.filterNot { it % 2 == 0 })
    }

    @Test
    fun flatMap() {
        assertInstanceOf(PersistentList::class.java, oneTwoThreeOneTwoThree.flatMap { listOf(it) })
        assertEquals(oneTwoThreeOneTwoThree, oneTwoThreeOneTwoThree.flatMap { listOf(it) })
        assertEquals(oneTwoThreeOneTwoThree.map { it + 1 }, oneTwoThreeOneTwoThree.flatMap { listOf(it + 1) })
        assertEquals(
            ListPair.concat(oneTwoThree.flatMap { listOf(it, it) }, oneTwoThree.flatMap { listOf(it, it) }),
            ListPair.concat(oneTwoThree, oneTwoThree).flatMap { listOf(it, it) })
        assertEquals(
            ListPair.concat(oneTwoThree.flatMap { listOf<Int>() }, oneTwoThree.flatMap { listOf() }),
            ListPair.concat(oneTwoThree, oneTwoThree).flatMap { listOf<Int>() })
    }

    @Test
    fun take() {
        assertInstanceOf(PersistentList::class.java, oneTwoThreeOneTwoThree.take(0))
        assertInstanceOf(PersistentList::class.java, oneTwoThreeOneTwoThree.take(oneTwoThree.size))

        assertEquals(PersistentList.of<Int>(), oneTwoThreeOneTwoThree.take(0))
        assertEquals(oneTwoThree, oneTwoThreeOneTwoThree.take(oneTwoThree.size))

        assertEquals(PersistentList.of(1, 2), ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(4, 5, 6)).take(2))
        assertEquals(PersistentList.of(1, 2, 3), ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(4, 5, 6)).take(3))
        assertEquals(PersistentList.of(1, 2, 3, 4), ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(4, 5, 6)).take(4))

        assertEquals(oneTwoThreeOneTwoThree, oneTwoThreeOneTwoThree.take(oneTwoThreeOneTwoThree.size))
        assertEquals(oneTwoThreeOneTwoThree, oneTwoThreeOneTwoThree.take(999))
    }

    @Test
    fun drop() {
        assertInstanceOf(PersistentList::class.java, oneTwoThreeOneTwoThree.drop(0))
        assertInstanceOf(PersistentList::class.java, oneTwoThreeOneTwoThree.drop(oneTwoThree.size))

        assertEquals(oneTwoThreeOneTwoThree, oneTwoThreeOneTwoThree.drop(0))
        assertEquals(oneTwoThree, oneTwoThreeOneTwoThree.drop(oneTwoThree.size))

        assertEquals(PersistentList.of(3, 4, 5, 6), ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(4, 5, 6)).drop(2))
        assertEquals(PersistentList.of(4, 5, 6), ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(4, 5, 6)).drop(3))
        assertEquals(PersistentList.of(5, 6), ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(4, 5, 6)).drop(4))

        assertEquals(PersistentList.of<Int>(), oneTwoThreeOneTwoThree.drop(oneTwoThreeOneTwoThree.size))
        assertEquals(PersistentList.of<Int>(), oneTwoThreeOneTwoThree.drop(999))
    }

    @Test
    fun takeWhile() {
        assertInstanceOf(PersistentList::class.java, oneTwoThreeOneTwoThree.takeWhile { true })
        assertInstanceOf(PersistentList::class.java, oneTwoThreeOneTwoThree.takeWhile { false })
        assertInstanceOf(
            PersistentList::class.java,
            ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(4, 5, 6)).takeWhile { it % 2 == 1 })

        assertEquals(oneTwoThreeOneTwoThree, oneTwoThreeOneTwoThree.takeWhile { true })
        assertEquals(PersistentList.of<Int>(), oneTwoThreeOneTwoThree.takeWhile { false })
        assertEquals(PersistentList.of<Int>(), ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(4, 5, 6)).takeWhile { it < 0 })
        assertEquals(PersistentList.of(1, 2), ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(4, 5, 6)).takeWhile { it < 3 })
        assertEquals(PersistentList.of(1, 2, 3), ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(4, 5, 6)).takeWhile { it < 4 })
        assertEquals(PersistentList.of(1, 2, 3, 4), ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(4, 5, 6)).takeWhile { it < 5 })
        assertEquals(
            PersistentList.of(1, 2, 3, 4, 5, 6),
            ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(4, 5, 6)).takeWhile { it < 99 })
    }

    @Test
    fun dropWhile() {
        assertInstanceOf(PersistentList::class.java, oneTwoThreeOneTwoThree.dropWhile { true })
        assertInstanceOf(PersistentList::class.java, oneTwoThreeOneTwoThree.dropWhile { false })
        assertInstanceOf(
            PersistentList::class.java,
            ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(4, 5, 6)).dropWhile { it % 2 == 1 })

        assertEquals(PersistentList.of<Int>(), oneTwoThreeOneTwoThree.dropWhile { true })
        assertEquals(oneTwoThreeOneTwoThree, oneTwoThreeOneTwoThree.dropWhile { false })
        assertEquals(
            PersistentList.of(1, 2, 3, 4, 5, 6),
            ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(4, 5, 6)).dropWhile { it < 0 })
        assertEquals(PersistentList.of(3, 4, 5, 6), ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(4, 5, 6)).dropWhile { it < 3 })
        assertEquals(PersistentList.of(4, 5, 6), ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(4, 5, 6)).dropWhile { it < 4 })
        assertEquals(PersistentList.of(5, 6), ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(4, 5, 6)).dropWhile { it < 5 })
        assertEquals(PersistentList.of<Int>(), ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(4, 5, 6)).dropWhile { it < 99 })
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
        assertInstanceOf(PersistentList::class.java, oneTwoThreeOneTwoThree.sortedBy { it })
        assertEquals(PersistentList.of(1, 2, 3, 4, 5, 6), ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(4, 5, 6)).sortedBy { it })
        assertEquals(PersistentList.of(1, 2, 3, 4, 5, 6), ListPair.concat(PersistentList.of(4, 5, 6), PersistentList.of(1, 2, 3)).sortedBy { it })
        assertEquals(PersistentList.of(6, 5, 4, 3, 2, 1), ListPair.concat(PersistentList.of(4, 5, 6), PersistentList.of(1, 2, 3)).sortedBy { -it })
    }

    @Test
    fun sortedByDescending() {
        assertInstanceOf(PersistentList::class.java, oneTwoThreeOneTwoThree.sortedByDescending { it })
        assertEquals(
            PersistentList.of(6, 5, 4, 3, 2, 1),
            ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(4, 5, 6)).sortedByDescending { it })
        assertEquals(
            PersistentList.of(6, 5, 4, 3, 2, 1),
            ListPair.concat(PersistentList.of(4, 5, 6), PersistentList.of(1, 2, 3)).sortedByDescending { it })
        assertEquals(
            PersistentList.of(1, 2, 3, 4, 5, 6),
            ListPair.concat(PersistentList.of(4, 5, 6), PersistentList.of(1, 2, 3)).sortedByDescending { -it })
    }

    @Test
    fun sortedWith() {
        assertInstanceOf(PersistentList::class.java, oneTwoThreeOneTwoThree.sortedWith { n, m -> n.compareTo(m) })
        assertEquals(
            PersistentList.of(1, 2, 3, 4, 5, 6),
            ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(4, 5, 6)).sortedWith { n, m -> n.compareTo(m) })
        assertEquals(
            PersistentList.of(1, 2, 3, 4, 5, 6),
            ListPair.concat(PersistentList.of(4, 5, 6), PersistentList.of(1, 2, 3)).sortedWith { n, m -> n.compareTo(m) })
        assertEquals(
            PersistentList.of(6, 5, 4, 3, 2, 1),
            ListPair.concat(PersistentList.of(4, 5, 6), PersistentList.of(1, 2, 3)).sortedWith { n, m -> -n.compareTo(m) })
    }

    @Test
    fun distinct() {
        assertInstanceOf(PersistentList::class.java, oneTwoThreeOneTwoThree.distinct())
        assertInstanceOf(PersistentList::class.java, ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(1, 2, 3)).distinct())
        assertEquals(PersistentList.of(1, 2, 3), ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(3, 2, 1)).distinct())
        assertEquals(PersistentList.of(3, 2, 1), ListPair.concat(PersistentList.of(3, 2, 1), PersistentList.of(1, 2, 3)).distinct())
    }

    @Test
    fun shuffled() {
        assertInstanceOf(PersistentList::class.java, oneTwoThreeOneTwoThree.shuffled())

        val seed = 0xDEADBEEF
        val rand = Random(seed)

        val oneOneOne = PersistentList.of(1, 1, 1, 1, 1)
        val oneOneOneTimesTwo = ListPair.concat(oneOneOne, oneOneOne)
        assertEquals(oneOneOneTimesTwo, oneOneOneTimesTwo.shuffled(rand))
    }

    @Test
    fun asSequence() {
        assertInstanceOf(Sequence::class.java, oneTwoThreeOneTwoThree.asSequence())
        assertInstanceOf(Sequence::class.java, oneTwoThreeOneTwoThree.asSequence().map { it + 1 })

        assertEquals(oneTwoThreeOneTwoThree.toList(), oneTwoThreeOneTwoThree.asSequence().toList())

        assertEquals(
            listOf(1, 2, 3, 1, 2, 3),
            ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(1, 2, 3)).asSequence().toList()
        )
        assertEquals(
            listOf(2, 3, 4, 2, 3, 4),
            ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(1, 2, 3)).asSequence().map { it + 1 }.toList()
        )
    }

    @Test
    fun plusElement() {
        assertInstanceOf(PersistentList::class.java, ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(1, 2, 3)) + 1)

        assertEquals(
            ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(1, 2, 3, 1)),
            ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(1, 2, 3)) + 1
        )
    }

    @Test
    fun plusIterable() {
        assertInstanceOf(PersistentList::class.java, ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(1, 2, 3)) + listOf(1))

        assertEquals(
            ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(1, 2, 3, 1)),
            ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(1, 2, 3)) + listOf(1)
        )
        assertEquals(
            ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(1, 2, 3, 1, 2, 3)),
            ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(1, 2, 3)) + listOf(1, 2, 3)
        )
        assertEquals(
            ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(1, 2, 3, 1, 2, 3)),
            ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(1, 2, 3)) + (1..3)
        )
        assertEquals(
            ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(1, 2, 3, 1, 2, 3)),
            ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(1, 2, 3)) + sequenceOf(1,2,3)
        )
        assertEquals(
            ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(1, 2, 3, 1, 2, 3)),
            ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(1, 2, 3)) + PersistentList.of(1,2,3)
        )
    }

    @Test
    fun isSingleton() {
        assertFalse(ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(1, 2, 3)).isSingleton())
        assertFalse(ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(1, 2, 3)).drop(3).isSingleton())
        assertTrue(ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(1, 2, 3)).drop(5).isSingleton())
        assertFalse(ListPair.concat(PersistentList.of(1, 2, 3), PersistentList.of(1, 2, 3)).drop(6).isSingleton())
    }
}
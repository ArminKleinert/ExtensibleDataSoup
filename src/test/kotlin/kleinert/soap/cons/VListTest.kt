package kleinert.soap.cons

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.random.Random

class VListTest {

    @Test
    fun testConstructor() {
        assertTrue(VList.of<Int>() == VList.toVList(listOf<Int>()))
        assertTrue(VList.toVList<Int>(arrayOf()) == VList.toVList(listOf<Int>()))
        assertTrue(VList.toVList(listOf<Int>().asIterable()) == VList.toVList(listOf<Int>()))
        assertTrue(VList.toVList(VList.of<Int>()) == VList.toVList(listOf<Int>()))

        assertTrue(VList.toVList(arrayOf(1)) == VList.toVList(listOf(1)))
        assertTrue(VList.toVList(listOf(1).asIterable()) == VList.toVList(listOf(1)))

        assertTrue(VList.toVList(arrayOf(1, 2, 3, 4, 5)) == VList.toVList(listOf(1, 2, 3, 4, 5)))
        assertTrue(VList.toVList(listOf(1, 2, 3, 4, 5).asIterable()) == VList.toVList(listOf(1, 2, 3, 4, 5)))
    }

    @Test
    fun getSize() {
        assertEquals(0, VList.of<Int>().size)
        assertEquals(0, VList.of<Int>().size)
        assertEquals(1, VList.of(99).size)
        assertEquals(5, VList.of(1, 2, 3, 4, 5).size)
        assertEquals(55, VList.toVList((1..55).toList()).size)
    }

    @Test
    fun cons() {
        val vlist = VList.of<Int>()
        assertEquals(0, vlist.size)

        assertInstanceOf(VList::class.java, vlist.cons(1))
        assertEquals(VList.toVList(arrayOf(1)), vlist.cons(1))
        assertEquals(VList.toVList(arrayOf(2, 1)), vlist.cons(1).cons(2))
        assertEquals(VList.toVList(arrayOf(3, 2, 1)), vlist.cons(1).cons(2).cons(3))
    }

    @Test
    fun cdr() {
        assertInstanceOf(VList::class.java, VList.of<Int>().cdr)
        assertInstanceOf(VList::class.java, VList.of(2, 1).cdr)
        assertEquals(VList.of<Int>(), VList.of<Int>().cdr)
        assertEquals(VList.of(1), VList.of(2, 1).cdr)
        assertEquals(VList.toVList(2..5), VList.toVList(1..5).cdr)
    }

    @Test
    fun car() {
        assertThrows(NoSuchElementException::class.java) { VList.of<Int>().car }
        assertEquals(1, VList.of(1).car)
        assertEquals(2, VList.of(2, 1).car)
        assertEquals((0..99).first(), VList.toVList(0..99).car)
        assertEquals(5, VList.of(1, 2, 3, 4).cons(5).car)

        assertThrows(NoSuchElementException::class.java) { VList.of<Int>().first() }
        assertEquals(1, VList.of(1).first())
        assertEquals(2, VList.of(2, 1).first())
        assertEquals((0..99).first(), VList.toVList(0..99).first())
        assertEquals(5, VList.of(1, 2, 3, 4).cons(5).first())
    }

    @Test
    fun contains() {
        assertFalse(VList.of<Int>().contains(1))
        assertFalse(VList.of<String>().contains(""))
        assertFalse(VList.toVList<Int>(listOf()).contains(1))
        assertFalse(VList.toVList(listOf(2, 1, 3)).contains(5))

        assertTrue(VList.toVList(listOf(1)).contains(1))
        assertTrue(VList.toVList(listOf(2, 1, 3)).contains(1))
    }

    @Test
    fun containsAll() {
        assertTrue(VList.of<Int>().containsAll(listOf()))

        assertFalse(VList.of<Int>().containsAll(listOf(1)))
        assertFalse(VList.of<String>().containsAll(listOf("")))
        assertFalse(VList.toVList<Int>(listOf()).containsAll(listOf(1)))
        assertFalse(VList.toVList(listOf(2, 1, 3)).containsAll(listOf(5)))

        assertTrue(VList.toVList(listOf(1)).containsAll(listOf(1)))
        assertTrue(VList.toVList(listOf(2, 1, 3)).containsAll(listOf(1)))

        assertTrue(VList.toVList(0..99).containsAll(listOf(10, 20, 30, 50, 60, 70, 80, 90)))
        assertTrue(VList.toVList(0..99).containsAll(setOf(10, 20, 30, 50, 60, 70, 80, 90)))
        assertTrue(VList.toVList(0..99).containsAll(VList.of(10, 20, 30, 50, 60, 70, 80, 90)))
    }

    @Test
    fun get() {
        assertThrows(IndexOutOfBoundsException::class.java) { VList.of<Int>()[0] }
        assertThrows(IndexOutOfBoundsException::class.java) { VList.of(1)[1] }
        assertThrows(IndexOutOfBoundsException::class.java) { VList.of(1, 2, 3, 4, 5)[10] }

        assertEquals(1, VList.of(1)[0])
        assertEquals(2, VList.of(2, 1)[0])
        assertEquals((0..99).first(), VList.toVList(0..99)[0])
        assertEquals(5, VList.of(1, 2, 3, 4).cons(5)[0])

        assertEquals(2, VList.of(3, 2, 1)[1])
        assertEquals(1, VList.of(3, 2, 1)[2])
        assertEquals(5, VList.of(1, 2, 3, 4).cons(5)[0])
        assertEquals(50, VList.toVList(0..99)[50])
    }

    @Test
    fun isEmpty() {
        assertTrue(VList.of<Int>().isEmpty())
        assertTrue(VList.of<String>().isEmpty())
        assertTrue(VList.toVList<Int>(listOf()).isEmpty())
        assertTrue(VList.toVList(listOf(1)).cdr.isEmpty())

        assertFalse(VList.of<Int>().cons(1).isEmpty())
        assertFalse(VList.of<Int>().cons(1).cons(2).isEmpty())
        assertFalse(VList.of<Int>().cons(1).cons(2).cons(3).isEmpty())
        assertFalse(VList.toVList(listOf(2, 1, 3)).isEmpty())

        assertFalse(VList.of<Int>().isNotEmpty())
        assertFalse(VList.of<String>().isNotEmpty())
        assertFalse(VList.toVList<Int>(listOf()).isNotEmpty())
        assertFalse(VList.toVList(listOf(1)).cdr.isNotEmpty())

        assertTrue(VList.of<Int>().cons(1).isNotEmpty())
        assertTrue(VList.of<Int>().cons(1).cons(2).isNotEmpty())
        assertTrue(VList.of<Int>().cons(1).cons(2).cons(3).isNotEmpty())
        assertTrue(VList.toVList(listOf(2, 1, 3)).isNotEmpty())
    }

    @Test
    fun indexOf() {
        assertEquals(-1, VList.of(1).indexOf(0))
        assertEquals(-1, VList.toVList(listOf(2, 1, 3, 2, 1, 3)).indexOf(4))
        assertEquals(0, VList.of(1).indexOf(1))
        assertEquals(0, VList.of<Int>().cons(1).indexOf(1))
        assertEquals(2, VList.toVList(listOf(2, 1, 3)).indexOf(3))
        assertEquals(2, VList.toVList(listOf(2, 1, 3, 2, 1, 3)).indexOf(3))
        assertEquals(2, VList.toVList(listOf(2, 1, 3, 2, 1, 3, 15)).indexOf(3))
        assertEquals(0, VList.toVList(listOf(1, 1, 1, 1, 1, 1)).indexOf(1))
    }

    @Test
    fun lastIndexOf() {
        assertEquals(-1, VList.of(1).lastIndexOf(0))
        assertEquals(-1, VList.toVList(listOf(2, 1, 3, 2, 1, 3)).lastIndexOf(4))
        assertEquals(0, VList.of(1).lastIndexOf(1))
        assertEquals(0, VList.of<Int>().cons(1).lastIndexOf(1))
        assertEquals(2, VList.toVList(listOf(2, 1, 3)).lastIndexOf(3))
        assertEquals(5, VList.toVList(listOf(2, 1, 3, 2, 1, 3)).lastIndexOf(3))
        assertEquals(5, VList.toVList(listOf(2, 1, 3, 2, 1, 3, 15)).lastIndexOf(3))
        assertEquals(5, VList.toVList(listOf(1, 1, 1, 1, 1, 1)).lastIndexOf(1))
    }

    @Test
    fun iterator() {
        run {
            var counter = 0
            for (e in VList.of<Int>()) {
                counter++
            }
            assertEquals(0, counter)
        }
        run {
            var counter = 0
            for (e in VList.toVList(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))) {
                counter++
            }
            assertEquals(10, counter)
        }
        run {
            var counter = 0
            VList.toVList(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).forEach { _ -> counter++ }
            assertEquals(10, counter)
        }

        run {
            val iterator = VList.of(1, 2, 3, 4, 5).iterator()
            var sum = 0
            while (iterator.hasNext()) {
                sum += iterator.next()
            }
            assertEquals(15, sum)
        }

        assertEquals(15, VList.of(1, 2, 3, 4, 5).sum())
        assertEquals(15, VList.of(1, 2, 3, 4, 5).fold(0, Int::plus))
        assertEquals(15, VList.of(1, 2, 3, 4, 5).foldRight(0, Int::plus))
        assertEquals(15, VList.of(1, 2, 3, 4, 5).reduce(Int::plus))
    }

    @Test
    fun listIterator() {
        run {
            var counter = 0
            for (e in VList.of<Int>().listIterator()) {
                counter++
            }
            assertEquals(0, counter)
        }
        run {
            var counter = 0
            for (e in VList.toVList(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).listIterator()) {
                counter++
            }
            assertEquals(10, counter)
        }
        run {
            var counter = 0
            VList.toVList(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).listIterator().forEach { _ -> counter++ }
            assertEquals(10, counter)
        }

        run {
            val iterator = VList.of(1, 2, 3, 4, 5).listIterator()
            var sum = 0
            while (iterator.hasNext()) {
                sum += iterator.next()
            }
            assertEquals(15, sum)
        }
    }

    @Test
    fun subList() {
        assertThrows(IndexOutOfBoundsException::class.java) { VList.of<Int>().subList(3, 6).size }
        assertInstanceOf(PersistentList::class.java, VList.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).subList(3, 6))
        assertEquals(listOf<Int>(), VList.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).subList(0, 0))
        assertEquals(listOf(4, 5, 6), VList.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).subList(3, 6))
        assertEquals(3, VList.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).subList(3, 6).size)
    }

    @Test
    fun toMutableList() {
        assertTrue(VList.of<Int>().toMutableList().isEmpty())
        assertEquals(5, VList.of(1, 2, 3, 4, 5).toMutableList().size)
    }

    @Test
    fun reversed() {
        assertInstanceOf(VList::class.java, VList.of<Int>().reversed())
        assertInstanceOf(VList::class.java, VList.of(3, 2, 1).reversed())

        assertEquals(VList.of<Int>(), VList.of<Int>().reversed())
        assertEquals(VList.of(1, 2, 3), VList.of(3, 2, 1).reversed())
        assertEquals(VList.of(1, 2, 3), VList.of<Int>().cons(1).cons(2).cons(3).reversed())
    }

    @Test
    fun map() {
        assertInstanceOf(VList::class.java, VList.of<Int>().map { it })
        assertInstanceOf(VList::class.java, VList.of(1).map { it })

        assertEquals(VList.of(1), VList.of(1).map { it })
        assertEquals(VList.of(1, 2), VList.of(1, 2).map { it })
        assertEquals(VList.of(2, 3), VList.of(1, 2).map { it + 1 })
        assertEquals(VList.of(1, 2, 3), VList.of(1, 2, 3).map { it })
        assertEquals(VList.of(2, 3, 4), VList.of(1, 2, 3).map { it + 1 })
    }

    @Test
    fun mapIndexed() {
        assertInstanceOf(VList::class.java, VList.of<Int>().mapIndexed { _, elem -> elem })
        assertInstanceOf(VList::class.java, VList.of(1).mapIndexed { _, elem -> elem })

        assertEquals(VList.of(1), VList.of(1).mapIndexed { _, elem -> elem })
        assertEquals(VList.of(1, 2), VList.of(1, 2).mapIndexed { _, elem -> elem })
        assertEquals(VList.of(0, 1), VList.of(1, 2).mapIndexed { i, _ -> i })
        assertEquals(VList.of(1, 2, 3), VList.of(1, 2, 3).mapIndexed { _, elem -> elem })
        assertEquals(VList.of(2, 3, 4), VList.of(1, 2, 3).mapIndexed { _, elem -> elem + 1 })
        assertEquals(VList.of(1, 3, 5), VList.of(1, 2, 3).mapIndexed { i, elem -> elem + i })
        assertEquals(VList.of(1, 2, 3, 4), VList.of(1, 2, 3, 4).mapIndexed { _, elem -> elem })
        assertEquals(VList.of(2, 3, 4, 5), VList.of(1, 2, 3, 4).mapIndexed { _, elem -> elem + 1 })
        assertEquals(VList.of(1, 3, 5, 7), VList.of(1, 2, 3, 4).mapIndexed { i, elem -> elem + i })
    }

    @Test
    fun filter() {
        assertInstanceOf(VList::class.java, VList.of<Int>().filter { true })
        assertInstanceOf(VList::class.java, VList.of(1).filter { true })

        assertEquals(VList.of(1, 2), VList.of(1, 2).filter { true })
        assertEquals(VList.of<Int>(), VList.of(1, 2).filter { false })
        assertEquals(VList.of(2), VList.of(1, 2, 3).filter { it % 2 == 0 })
        assertEquals(VList.of(2, 4), VList.of(1, 2, 3, 4).filter { it % 2 == 0 })
    }

    @Test
    fun filterNot() {
        assertInstanceOf(VList::class.java, VList.of<Int>().filterNot { true })
        assertInstanceOf(VList::class.java, VList.of(1).filterNot { true })

        assertEquals(VList.of(1, 2), VList.of(1, 2).filterNot { false })
        assertEquals(VList.of<Int>(), VList.of(1, 2).filterNot { true })
        assertEquals(VList.of(2), VList.of(1, 2, 3).filterNot { it % 2 != 0 })
        assertEquals(VList.of(2, 4), VList.of(1, 2, 3, 4).filterNot { it % 2 != 0 })
    }

    @Test
    fun split() {
        assertThrows(NoSuchElementException::class.java) { VList.of<Int>().split() }
        assertEquals(1 to VList.of<Int>(), VList.of(1).split())
        assertEquals(1 to VList.of(2), VList.of(1, 2).split())
    }

    @Test
    fun cadr() {
        assertThrows(NoSuchElementException::class.java) { VList.of<Int>().cadr }
        assertThrows(NoSuchElementException::class.java) { VList.of(1).cadr }
        assertEquals(2, VList.of(1, 2, 3, 4, 5).cadr)
        assertEquals(VList.of(1, 2, 3, 4, 5).cdr.car, VList.of(1, 2, 3, 4, 5).cadr)
    }

    @Test
    fun caddr() {
        assertThrows(NoSuchElementException::class.java) { VList.of<Int>().caddr }
        assertThrows(NoSuchElementException::class.java) { VList.of(1).caddr }
        assertEquals(3, VList.of(1, 2, 3, 4, 5).caddr)
        assertEquals(VList.of(1, 2, 3, 4, 5).cdr.cdr.car, VList.of(1, 2, 3, 4, 5).caddr)
    }

    @Test
    fun cadddr() {
        assertThrows(NoSuchElementException::class.java) { VList.of<Int>().cadddr }
        assertThrows(NoSuchElementException::class.java) { VList.of(1).cadddr }
        assertEquals(4, VList.of(1, 2, 3, 4, 5).cadddr)
        assertEquals(VList.of(1, 2, 3, 4, 5).cdr.cdr.cdr.car, VList.of(1, 2, 3, 4, 5).cadddr)
    }

    @Test
    fun cddr() {
        assertEquals(nullCons<Int>(), VList.of<Int>().cddr)
        assertEquals(nullCons<Int>(), VList.of(1).cddr)
        assertEquals(VList.of(3, 4, 5), VList.of(1, 2, 3, 4, 5).cddr)
        assertEquals(VList.of(1, 2, 3, 4, 5).cdr.cdr, VList.of(1, 2, 3, 4, 5).cddr)
    }

    @Test
    fun cdddr() {
        assertEquals(nullCons<Int>(), VList.of<Int>().cdddr)
        assertEquals(nullCons<Int>(), VList.of(1).cdddr)
        assertEquals(VList.of(4, 5), VList.of(1, 2, 3, 4, 5).cdddr)
        assertEquals(VList.of(1, 2, 3, 4, 5).cdr.cdr.cdr, VList.of(1, 2, 3, 4, 5).cdddr)
    }

    @Test
    fun cddddr() {
        assertEquals(nullCons<Int>(), VList.of<Int>().cddddr)
        assertEquals(nullCons<Int>(), VList.of(1).cddddr)
        assertEquals(VList.of(5), VList.of(1, 2, 3, 4, 5).cddddr)
        assertEquals(VList.of(1, 2, 3, 4, 5).cdr.cdr.cdr.cdr, VList.of(1, 2, 3, 4, 5).cddddr)
    }

    @Test
    fun flatMap() {
        assertInstanceOf(VList::class.java, VList.of<Int>().flatMap { listOf(it) })
        assertEquals(VList.of<Int>(), VList.of<Int>().flatMap { listOf(it) })
        assertInstanceOf(VList::class.java, VList.of(1, 2, 3).flatMap { listOf(it) })
        assertEquals(VList.of(1, 2, 3), VList.of(1, 2, 3).flatMap { listOf(it) })
        assertEquals(VList.of(1, 2, 3), VList.of(listOf(1, 2), listOf(3)).flatMap { it })
    }

    @Test
    fun take() {
        assertInstanceOf(VList::class.java, VList.of<Int>().take(10))
        assertInstanceOf(VList::class.java, VList.of(1, 2, 3).take(10))
        assertInstanceOf(VList::class.java, VList.of(1, 2, 3).take(2))

        assertEquals(VList.of<Int>(), VList.of<Int>().drop(0))
        assertEquals(VList.of<Int>(), VList.of<Int>().drop(10))

        assertEquals(VList.of<Int>(), VList.of(1, 2, 3, 4, 5, 6).take(0))
        assertEquals(VList.of(1), VList.of(1, 2, 3, 4, 5, 6).take(1))
        assertEquals(VList.of(1, 2), VList.of(1, 2, 3, 4, 5, 6).take(2))
        assertEquals(VList.of(1, 2, 3), VList.of(1, 2, 3, 4, 5, 6).take(3))
        assertEquals(VList.of(1, 2, 3, 4), VList.of(1, 2, 3, 4, 5, 6).take(4))
        assertEquals(VList.of(1, 2, 3, 4, 5), VList.of(1, 2, 3, 4, 5, 6).take(5))
        assertEquals(VList.of(1, 2, 3, 4, 5, 6), VList.of(1, 2, 3, 4, 5, 6).take(6))
    }

    @Test
    fun drop() {
        assertInstanceOf(VList::class.java, VList.of<Int>().drop(10))
        assertInstanceOf(VList::class.java, VList.of(1, 2, 3).drop(10))
        assertInstanceOf(VList::class.java, VList.of(1, 2, 3).drop(2))

        assertEquals(VList.of<Int>(), VList.of<Int>().drop(0))
        assertEquals(VList.of<Int>(), VList.of<Int>().drop(10))

        assertEquals(VList.of(1, 2, 3, 4, 5, 6), VList.of(1, 2, 3, 4, 5, 6).drop(0))
        assertEquals(VList.of(2, 3, 4, 5, 6), VList.of(1, 2, 3, 4, 5, 6).drop(1))
        assertEquals(VList.of(3, 4, 5, 6), VList.of(1, 2, 3, 4, 5, 6).drop(2))
        assertEquals(VList.of(4, 5, 6), VList.of(1, 2, 3, 4, 5, 6).drop(3))
        assertEquals(VList.of(5, 6), VList.of(1, 2, 3, 4, 5, 6).drop(4))
        assertEquals(VList.of(6), VList.of(1, 2, 3, 4, 5, 6).drop(5))
        assertEquals(VList.of<Int>(), VList.of(1, 2, 3, 4, 5, 6).drop(6))
    }

    @Test
    fun takeWhile() {
        assertInstanceOf(VList::class.java, VList.of<Int>().takeWhile { true })
        assertInstanceOf(VList::class.java, VList.of(1, 2, 3).takeWhile { true })
        assertInstanceOf(VList::class.java, VList.of(1, 2, 3).takeWhile { false })
        assertInstanceOf(VList::class.java, VList.of(1, 2, 3).takeWhile { it < 3 })

        assertEquals(VList.of<Int>(), VList.of<Int>().takeWhile { it < 3 })

        assertEquals(VList.of(1, 2), VList.of(1, 2, 3, 2, 1).takeWhile { it < 3 })

        assertEquals(VList.of<Int>(), VList.of(1, 2, 3, 4, 5, 6).takeWhile { it < 1 })
        assertEquals(VList.of(1), VList.of(1, 2, 3, 4, 5, 6).takeWhile { it < 2 })
        assertEquals(VList.of(1, 2), VList.of(1, 2, 3, 4, 5, 6).takeWhile { it < 3 })
        assertEquals(VList.of(1, 2, 3), VList.of(1, 2, 3, 4, 5, 6).takeWhile { it < 4 })
        assertEquals(VList.of(1, 2, 3, 4), VList.of(1, 2, 3, 4, 5, 6).takeWhile { it < 5 })
        assertEquals(VList.of(1, 2, 3, 4, 5), VList.of(1, 2, 3, 4, 5, 6).takeWhile { it < 6 })
        assertEquals(VList.of(1, 2, 3, 4, 5, 6), VList.of(1, 2, 3, 4, 5, 6).takeWhile { it < 7 })
    }

    @Test
    fun dropWhile() {
        assertInstanceOf(VList::class.java, VList.of<Int>().dropWhile { true })
        assertInstanceOf(VList::class.java, VList.of(1, 2, 3).dropWhile { true })
        assertInstanceOf(VList::class.java, VList.of(1, 2, 3).dropWhile { false })
        assertInstanceOf(VList::class.java, VList.of(1, 2, 3).dropWhile { it < 3 })

        assertEquals(VList.of<Int>(), VList.of<Int>().dropWhile { it < 3 })

        assertEquals(VList.of(3, 2, 1), VList.of(1, 2, 3, 2, 1).dropWhile { it < 3 })

        assertEquals(VList.of(1, 2, 3, 4, 5, 6), VList.of(1, 2, 3, 4, 5, 6).dropWhile { it < 1 })
        assertEquals(VList.of(2, 3, 4, 5, 6), VList.of(1, 2, 3, 4, 5, 6).dropWhile { it < 2 })
        assertEquals(VList.of(3, 4, 5, 6), VList.of(1, 2, 3, 4, 5, 6).dropWhile { it < 3 })
        assertEquals(VList.of(4, 5, 6), VList.of(1, 2, 3, 4, 5, 6).dropWhile { it < 4 })
        assertEquals(VList.of(5, 6), VList.of(1, 2, 3, 4, 5, 6).dropWhile { it < 5 })
        assertEquals(VList.of(6), VList.of(1, 2, 3, 4, 5, 6).dropWhile { it < 6 })
        assertEquals(VList.of<Int>(), VList.of(1, 2, 3, 4, 5, 6).dropWhile { it < 8 })
    }

//    @Test
//    fun sorted() {
//        assertInstanceOf(Cons::class.java, (VList.of<Int>() as Cons<Int>).sorted())
//        assertInstanceOf(Cons::class.java, VList.of<Int>().sorted())
//        assertEquals(VList.of<Int>(), VList.of<Int>().sorted())
//        assertInstanceOf(Cons::class.java, VList.of(1, 2, 3).sorted())
//        assertEquals(VList.of(1, 2, 3), VList.of(1, 2, 3).sorted())
//        assertEquals(VList.of(1, 2, 3), VList.of(3, 2, 1).sorted())
//        assertEquals(VList.of(1, 2, 3, 3), VList.of(3, 2, 3, 1).sorted())
//    }

//    @Test
//    fun sortedDescending() {
//        assertInstanceOf(Cons::class.java, VList.of<Int>().sortedDescending())
//        assertEquals(VList.of<Int>(), VList.of<Int>().sortedDescending())
//        assertInstanceOf(Cons::class.java, VList.of(1, 2, 3).sortedDescending())
//        assertEquals(VList.of(3, 2, 1), VList.of(1, 2, 3).sortedDescending())
//        assertEquals(VList.of(3, 2, 1), VList.of(3, 2, 1).sortedDescending())
//        assertEquals(VList.of(3, 3, 2, 1), VList.of(3, 2, 3, 1).sortedDescending())
//    }

    @Test
    fun sortedBy() {
        assertInstanceOf(VList::class.java, VList.of<Int>().sortedBy { it })
        assertInstanceOf(VList::class.java, VList.of(1, 2, 3).sortedBy { it })

        assertEquals(VList.of<Int>(), VList.of<Int>().sortedBy { it })
        assertEquals(VList.of(1), VList.of(1).sortedBy { it })

        assertEquals(VList.of(1, 2, 3), VList.of(1, 2, 3).sortedBy { it })
        assertEquals(VList.of(1, 2, 3), VList.of(3, 1, 2).sortedBy { it })

        assertEquals(VList.of(1, 2, 2, 3), VList.of(1, 2, 2, 3).sortedBy { it })
        assertEquals(VList.of(1, 2, 2, 3), VList.of(3, 1, 2, 2).sortedBy { it })
    }

    @Test
    fun sortedByDescending() {
        assertInstanceOf(VList::class.java, VList.of<Int>().sortedByDescending { it })
        assertInstanceOf(VList::class.java, VList.of(1, 2, 3).sortedByDescending { it })

        assertEquals(VList.of<Int>(), VList.of<Int>().sortedByDescending { it })
        assertEquals(VList.of(1), VList.of(1).sortedByDescending { it })

        assertEquals(VList.of(3, 2, 1), VList.of(1, 2, 3).sortedByDescending { it })
        assertEquals(VList.of(3, 2, 1), VList.of(3, 1, 2).sortedByDescending { it })

        assertEquals(VList.of(3, 2, 2, 1), VList.of(1, 2, 2, 3).sortedByDescending { it })
        assertEquals(VList.of(3, 2, 2, 1), VList.of(3, 1, 2, 2).sortedByDescending { it })
    }

    @Test
    fun sortedWith() {
        assertInstanceOf(VList::class.java, VList.of<Int>().sortedWith { n, m -> n.compareTo(m) })
        assertInstanceOf(VList::class.java, VList.of(1, 2, 3).sortedWith { n, m -> n.compareTo(m) })
        assertInstanceOf(VList::class.java, VList.of(1, 2, 3).sortedWith { n, m -> -n.compareTo(m) })

        assertEquals(VList.of<Int>(), VList.of<Int>().sortedWith { n, m -> n.compareTo(m) })
        assertEquals(VList.of(1), VList.of(1).sortedWith { n, m -> n.compareTo(m) })
        assertEquals(VList.of(1), VList.of(1).sortedWith { n, m -> -n.compareTo(m) })

        assertEquals(VList.of(3, 1, 2), VList.of(3, 1, 2).sortedWith { _, _ -> 0 })

        assertEquals(VList.of(1, 2, 3), VList.of(1, 2, 3).sortedWith { n, m -> n.compareTo(m) })
        assertEquals(VList.of(1, 2, 3), VList.of(3, 1, 2).sortedWith { n, m -> n.compareTo(m) })

        assertEquals(VList.of(1, 2, 2, 3), VList.of(1, 2, 2, 3).sortedWith { n, m -> n.compareTo(m) })
        assertEquals(VList.of(1, 2, 2, 3), VList.of(3, 1, 2, 2).sortedWith { n, m -> n.compareTo(m) })

        assertEquals(VList.of(3, 2, 1), VList.of(1, 2, 3).sortedWith { n, m -> -n.compareTo(m) })
        assertEquals(VList.of(3, 2, 1), VList.of(3, 1, 2).sortedWith { n, m -> -n.compareTo(m) })

        assertEquals(VList.of(3, 2, 2, 1), VList.of(1, 2, 2, 3).sortedWith { n, m -> -n.compareTo(m) })
        assertEquals(VList.of(3, 2, 2, 1), VList.of(3, 1, 2, 2).sortedWith { n, m -> -n.compareTo(m) })
    }

    @Test
    fun distinct() {
        assertInstanceOf(VList::class.java, VList.of<Int>().distinct())
        assertInstanceOf(VList::class.java, VList.of(1, 2, 3).distinct())

        assertEquals(VList.of<Int>(), VList.of<Int>().distinct())
        assertEquals(VList.of(1), VList.of(1).distinct())
        assertEquals(VList.of(1, 2, 3, 4, 5), VList.of(1, 2, 3, 4, 5).distinct())
        assertEquals(VList.of(1), VList.of(1, 1, 1, 1, 1, 1).distinct())

        assertEquals(VList.of(1, 2, 3), VList.of(1, 1, 2, 2, 3, 3).distinct())
        assertEquals(VList.of(2, 1, 3), VList.of(2, 1, 1, 2, 3, 3).distinct())
    }

    @Test
    fun shuffled() {
        assertInstanceOf(VList::class.java, VList.of<Int>().shuffled())
        assertInstanceOf(VList::class.java, VList.of(1, 2, 3).shuffled())

        val seed = 0xDEADBEEF
        val rand = Random(seed)
        val temp = VList.toVList(1..50)
        val tempShuffled = temp.shuffled(rand)
        assertNotSame(temp, tempShuffled)

        assertEquals(VList.of(1), VList.of(1).shuffled(rand))
        assertEquals(VList.of(1, 1, 1), VList.of(1, 1, 1).shuffled(rand))
        assertEquals(VList.toVList(1..55).shuffled(Random(seed)), VList.toVList(1..55).shuffled(Random(seed)))
    }

    @Test
    fun asSequence() {
        assertInstanceOf(Sequence::class.java, VList.of<Int>().asSequence())
        assertInstanceOf(Sequence::class.java, VList.of(1, 2).asSequence())
        assertInstanceOf(Sequence::class.java, VList.of(1, 2, 3).asSequence())
        assertInstanceOf(Sequence::class.java, VList.of(1, 2, 3).asSequence().map { it + 1 })

        assertEquals(listOf(1), VList.of(1).asSequence().toList())
        assertEquals(listOf(1, 2), VList.of(1, 2).asSequence().toList())
        assertEquals(listOf(2, 3, 4), VList.of(1, 2, 3).asSequence().map { it + 1 }.toList())
    }

    @Test
    fun plusElement() {
        assertInstanceOf(PersistentList::class.java, VList.of<Int>() + 1)
        assertInstanceOf(PersistentList::class.java, VList.of(1) + 2)

        assertEquals(PersistentList.of(1), VList.of<Int>() + 1)
        assertEquals(PersistentList.of(1, 2), VList.of(1) + 2)
    }

    @Test
    fun plusIterable() {
        assertInstanceOf(PersistentList::class.java, VList.of<Int>() + listOf(1))
        assertInstanceOf(PersistentList::class.java, VList.of(1) + listOf(2, 3))

        assertEquals(PersistentList.of<Int>(), VList.of<Int>() + listOf())
        assertEquals(PersistentList.of(1, 2, 3), VList.of(1, 2, 3) + listOf())

        assertEquals(PersistentList.of(1), VList.of<Int>() + listOf(1))
        assertEquals(PersistentList.of(1, 2, 3), VList.of(1) + listOf(2, 3))

        assertEquals(PersistentList.of(1, 2, 3, 4, 5), VList.of<Int>() + (1..5))
        assertEquals(PersistentList.of(1, 2, 3, 4, 5), VList.of<Int>() + (1..<6))
        assertEquals(PersistentList.of(1, 2, 3, 4, 5), VList.of(1) + (2..5))
        assertEquals(PersistentList.of(1, 2, 3, 4, 5), VList.of(1) + sequenceOf(2, 3, 4, 5))

        assertEquals(PersistentList.of<Int>(), VList.of<Int>() + PersistentList.of())
        assertEquals(PersistentList.of(1, 2, 3, 4, 5), VList.of(1) + PersistentList.of(2, 3, 4, 5))
        assertEquals(PersistentList.of(1, 2, 3, 4, 5), VList.of(1) + PersistentWrapper(listOf(2, 3, 4, 5)))
    }

    @Test
    fun getSegments() {
        assertEquals(listOf<List<Int>>(), VList.of<Int>().getSegments())
        assertEquals(listOf(listOf(1)), VList.of(1).getSegments())
        assertEquals(listOf(listOf(1, 2), listOf(3)), VList.of(1, 2, 3).getSegments())
        assertEquals(listOf(listOf(null, 1), listOf(2)), VList.of(1, 2).getSegments())
        assertEquals(listOf(listOf(1)), VList.of<Int>().cons(1).getSegments())
        assertEquals(listOf(listOf(null, 1), listOf(2)), VList.of<Int>().cons(2).cons(1).getSegments())
        assertEquals(listOf(listOf(1, 2), listOf(3)), VList.of<Int>().cons(3).cons(2).cons(1).getSegments())
    }

    @Test
    fun mapSegments() {
        assertEquals(listOf<List<Int>>(), VList.of<Int>().mapSegments { it })

        assertEquals(
            listOf(listOf(2), listOf(3)),
            VList.of(1, 2).mapSegments { it + 1 }
        )
        assertEquals(
            listOf(listOf(2, 3), listOf(4)),
            VList.of(1, 2, 3).mapSegments { it + 1 }
        )
        assertEquals(
            listOf(listOf(2, 3, 4, 5), listOf(6, 7), listOf(8)),
            VList.of(1, 2, 3, 4, 5, 6, 7).mapSegments { it + 1 }
        )
        assertEquals(
            listOf(listOf(1), listOf(2, 3, 4, 5), listOf(6, 7), listOf(8)),
            VList.of(0, 1, 2, 3, 4, 5, 6, 7).mapSegments { it + 1 }
        )
    }

    @Test
    fun isSingleton() {
        assertFalse(VList.of<Int>().isSingleton())
        assertTrue(VList.of(1).isSingleton())
        assertFalse(VList.of(1, 2).isSingleton())
    }
}

package kleinert.soap

import kleinert.soap.cons.CdrCodedList
import kleinert.soap.cons.Cons
import kleinert.soap.cons.EmptyCons
import kleinert.soap.cons.VList
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.random.Random

class CdrCodedListTest {
    @Test
    fun testConstructor() {
        assertTrue(CdrCodedList<Int>() == CdrCodedList(listOf<Int>()))
        assertTrue(CdrCodedList<Int>(arrayOf()) == CdrCodedList(listOf<Int>()))
        assertTrue(CdrCodedList(listOf<Int>().asIterable()) == CdrCodedList(listOf<Int>()))
        assertTrue(CdrCodedList(CdrCodedList<Int>()) == CdrCodedList(listOf<Int>()))

        assertTrue(CdrCodedList(arrayOf(1)) == CdrCodedList(listOf(1)))
        assertTrue(CdrCodedList(listOf(1).asIterable()) == CdrCodedList(listOf(1)))

        assertTrue(CdrCodedList(arrayOf(1, 2, 3, 4, 5)) == CdrCodedList(listOf(1, 2, 3, 4, 5)))
        assertTrue(CdrCodedList(listOf(1, 2, 3, 4, 5).asIterable()) == CdrCodedList(listOf(1, 2, 3, 4, 5)))
    }

    @Test
    fun getSize() {
        assertEquals(0, CdrCodedList<Int>().size)
        assertEquals(0, CdrCodedList.of<Int>().size)
        assertEquals(1, CdrCodedList.of(99).size)
        assertEquals(5, CdrCodedList.of(1, 2, 3, 4, 5).size)
        assertEquals(55, CdrCodedList((1..55).toList()).size)
    }

    @Test
    fun cons() {
        val vlist = CdrCodedList<Int>()
        assertEquals(0, vlist.size)

        assertInstanceOf(Cons::class.java, vlist.cons(1))
        assertEquals(CdrCodedList(arrayOf(1)), vlist.cons(1))
        assertEquals(CdrCodedList(arrayOf(2, 1)), vlist.cons(1).cons(2))
        assertEquals(CdrCodedList(arrayOf(3, 2, 1)), vlist.cons(1).cons(2).cons(3))
    }

    @Test
    fun cdr() {
        assertInstanceOf(CdrCodedList::class.java, CdrCodedList<Int>().cdr)
        assertInstanceOf(CdrCodedList::class.java, CdrCodedList.of(2, 1).cdr)
        assertEquals(CdrCodedList<Int>(), CdrCodedList<Int>().cdr)
        assertEquals(CdrCodedList.of(1), CdrCodedList.of(2, 1).cdr)
        assertEquals(CdrCodedList(2..5), CdrCodedList(1..5).cdr)
    }

    @Test
    fun car() {
        assertThrows(NoSuchElementException::class.java) { CdrCodedList.of<Int>().car }
        assertEquals(1, CdrCodedList.of(1).car)
        assertEquals(2, CdrCodedList.of(2, 1).car)
        assertEquals((0..99).first(), CdrCodedList(0..99).car)
        assertEquals(5, CdrCodedList.of(1, 2, 3, 4).cons(5).car)

        assertThrows(NoSuchElementException::class.java) { CdrCodedList.of<Int>().first() }
        assertEquals(1, CdrCodedList.of(1).first())
        assertEquals(2, CdrCodedList.of(2, 1).first())
        assertEquals((0..99).first(), CdrCodedList(0..99).first())
        assertEquals(5, CdrCodedList.of(1, 2, 3, 4).cons(5).first())
    }

    @Test
    fun contains() {
        assertFalse(CdrCodedList<Int>().contains(1))
        assertFalse(CdrCodedList<String>().contains(""))
        assertFalse(CdrCodedList<Int>(listOf()).contains(1))
        assertFalse(CdrCodedList(listOf(2, 1, 3)).contains(5))

        assertTrue(CdrCodedList(listOf(1)).contains(1))
        assertTrue(CdrCodedList(listOf(2, 1, 3)).contains(1))
    }

    @Test
    fun containsAll() {
        assertTrue(CdrCodedList<Int>().containsAll(listOf()))

        assertFalse(CdrCodedList<Int>().containsAll(listOf(1)))
        assertFalse(CdrCodedList<String>().containsAll(listOf("")))
        assertFalse(CdrCodedList<Int>(listOf()).containsAll(listOf(1)))
        assertFalse(CdrCodedList(listOf(2, 1, 3)).containsAll(listOf(5)))

        assertTrue(CdrCodedList(listOf(1)).containsAll(listOf(1)))
        assertTrue(CdrCodedList(listOf(2, 1, 3)).containsAll(listOf(1)))

        assertTrue(CdrCodedList(0..99).containsAll(listOf(10, 20, 30, 50, 60, 70, 80, 90)))
        assertTrue(CdrCodedList(0..99).containsAll(setOf(10, 20, 30, 50, 60, 70, 80, 90)))
        assertTrue(CdrCodedList(0..99).containsAll(CdrCodedList.of(10, 20, 30, 50, 60, 70, 80, 90)))
    }

    @Test
    fun get() {
        assertThrows(IndexOutOfBoundsException::class.java) { CdrCodedList.of<Int>()[0] }
        assertThrows(IndexOutOfBoundsException::class.java) { CdrCodedList.of(1)[1] }
        assertThrows(IndexOutOfBoundsException::class.java) { CdrCodedList.of(1, 2, 3, 4, 5)[10] }

        assertEquals(1, CdrCodedList.of(1)[0])
        assertEquals(2, CdrCodedList.of(2, 1)[0])
        assertEquals((0..99).first(), CdrCodedList(0..99)[0])
        assertEquals(5, CdrCodedList.of(1, 2, 3, 4).cons(5)[0])

        assertEquals(2, CdrCodedList.of(3, 2, 1)[1])
        assertEquals(1, CdrCodedList.of(3, 2, 1)[2])
        assertEquals(5, CdrCodedList.of(1, 2, 3, 4).cons(5)[0])
        assertEquals(50, CdrCodedList(0..99)[50])
    }

    @Test
    fun isEmpty() {
        assertTrue(CdrCodedList<Int>().isEmpty())
        assertTrue(CdrCodedList<String>().isEmpty())
        assertTrue(CdrCodedList<Int>(listOf()).isEmpty())
        assertTrue(CdrCodedList(listOf(1)).cdr.isEmpty())

        assertFalse(CdrCodedList<Int>().cons(1).isEmpty())
        assertFalse(CdrCodedList<Int>().cons(1).cons(2).isEmpty())
        assertFalse(CdrCodedList<Int>().cons(1).cons(2).cons(3).isEmpty())
        assertFalse(CdrCodedList(listOf(2, 1, 3)).isEmpty())

        assertFalse(CdrCodedList<Int>().isNotEmpty())
        assertFalse(CdrCodedList<String>().isNotEmpty())
        assertFalse(CdrCodedList<Int>(listOf()).isNotEmpty())
        assertFalse(CdrCodedList(listOf(1)).cdr.isNotEmpty())

        assertTrue(CdrCodedList<Int>().cons(1).isNotEmpty())
        assertTrue(CdrCodedList<Int>().cons(1).cons(2).isNotEmpty())
        assertTrue(CdrCodedList<Int>().cons(1).cons(2).cons(3).isNotEmpty())
        assertTrue(CdrCodedList(listOf(2, 1, 3)).isNotEmpty())
    }

    @Test
    fun indexOf() {
        assertEquals(-1, CdrCodedList.of(1).indexOf(0))
        assertEquals(-1, CdrCodedList(listOf(2, 1, 3, 2, 1, 3)).indexOf(4))
        assertEquals(0, CdrCodedList.of(1).indexOf(1))
        assertEquals(0, CdrCodedList.of<Int>().cons(1).indexOf(1))
        assertEquals(2, CdrCodedList(listOf(2, 1, 3)).indexOf(3))
        assertEquals(2, CdrCodedList(listOf(2, 1, 3, 2, 1, 3)).indexOf(3))
        assertEquals(2, CdrCodedList(listOf(2, 1, 3, 2, 1, 3, 15)).indexOf(3))
        assertEquals(0, CdrCodedList(listOf(1, 1, 1, 1, 1, 1)).indexOf(1))
    }

    @Test
    fun lastIndexOf() {
        assertEquals(-1, CdrCodedList.of(1).lastIndexOf(0))
        assertEquals(-1, CdrCodedList(listOf(2, 1, 3, 2, 1, 3)).lastIndexOf(4))
        assertEquals(0, CdrCodedList.of(1).lastIndexOf(1))
        assertEquals(0, CdrCodedList.of<Int>().cons(1).lastIndexOf(1))
        assertEquals(2, CdrCodedList(listOf(2, 1, 3)).lastIndexOf(3))
        assertEquals(5, CdrCodedList(listOf(2, 1, 3, 2, 1, 3)).lastIndexOf(3))
        assertEquals(5, CdrCodedList(listOf(2, 1, 3, 2, 1, 3, 15)).lastIndexOf(3))
        assertEquals(5, CdrCodedList(listOf(1, 1, 1, 1, 1, 1)).lastIndexOf(1))
    }

    @Test
    fun iterator() {
        run {
            var counter = 0
            for (e in CdrCodedList<Int>()) {
                counter++
            }
            assertEquals(0, counter)
        }
        run {
            var counter = 0
            for (e in CdrCodedList(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))) {
                counter++
            }
            assertEquals(10, counter)
        }
        run {
            var counter = 0
            CdrCodedList(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).forEach { _ -> counter++ }
            assertEquals(10, counter)
        }

        run {
            val iterator = CdrCodedList.of(1, 2, 3, 4, 5).iterator()
            var sum = 0
            while (iterator.hasNext()) {
                sum += iterator.next()
            }
            assertEquals(15, sum)
        }

        assertEquals(15, CdrCodedList.of(1, 2, 3, 4, 5).sum())
        assertEquals(15, CdrCodedList.of(1, 2, 3, 4, 5).fold(0, Int::plus))
        assertEquals(15, CdrCodedList.of(1, 2, 3, 4, 5).foldRight(0, Int::plus))
        assertEquals(15, CdrCodedList.of(1, 2, 3, 4, 5).reduce(Int::plus))
    }

    @Test
    fun listIterator() {
        run {
            var counter = 0
            for (e in CdrCodedList<Int>().listIterator()) {
                counter++
            }
            assertEquals(0, counter)
        }
        run {
            var counter = 0
            for (e in CdrCodedList(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).listIterator()) {
                counter++
            }
            assertEquals(10, counter)
        }
        run {
            var counter = 0
            CdrCodedList(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).listIterator().forEach { _ -> counter++ }
            assertEquals(10, counter)
        }

        run {
            val iterator = CdrCodedList.of(1, 2, 3, 4, 5).listIterator()
            var sum = 0
            while (iterator.hasNext()) {
                sum += iterator.next()
            }
            assertEquals(15, sum)
        }
    }

    @Test
    fun subList() {
        assertThrows(IndexOutOfBoundsException::class.java) { CdrCodedList.of<Int>().subList(3, 6).size }
        assertInstanceOf(Cons::class.java, CdrCodedList.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).subList(3, 6))
        assertEquals(listOf(4, 5, 6), CdrCodedList.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).subList(3, 6))
        assertEquals(3, CdrCodedList.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).subList(3, 6).size)
    }

    @Test
    fun toMutableList() {
        assertTrue(CdrCodedList<Int>().toMutableList().isEmpty())
        assertEquals(5, CdrCodedList.of(1, 2, 3, 4, 5).toMutableList().size)
    }

    @Test
    fun reversed() {
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Int>().reversed())
        assertInstanceOf(Cons::class.java, CdrCodedList.of(3, 2, 1).reversed())

        assertEquals(CdrCodedList.of<Int>(), CdrCodedList.of<Int>().reversed())
        assertEquals(CdrCodedList.of(1, 2, 3), CdrCodedList.of(3, 2, 1).reversed())
        assertEquals(CdrCodedList.of(1, 2, 3), CdrCodedList.of<Int>().cons(1).cons(2).cons(3).reversed())
    }

    @Test
    fun map() {
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Int>().map { it })
        assertInstanceOf(Cons::class.java, CdrCodedList.of(1).map { it })

        assertEquals(CdrCodedList.of(1), CdrCodedList.of(1).map { it })
        assertEquals(CdrCodedList.of(1, 2), CdrCodedList.of(1, 2).map { it })
        assertEquals(CdrCodedList.of(2, 3), CdrCodedList.of(1, 2).map { it + 1 })
        assertEquals(CdrCodedList.of(1, 2, 3), CdrCodedList.of(1, 2, 3).map { it })
        assertEquals(CdrCodedList.of(2, 3, 4), CdrCodedList.of(1, 2, 3).map { it + 1 })
    }

    @Test
    fun mapIndexed() {
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Int>().mapIndexed { _, elem -> elem })
        assertInstanceOf(Cons::class.java, CdrCodedList.of(1).mapIndexed { _, elem -> elem })

        assertEquals(CdrCodedList.of(1), CdrCodedList.of(1).mapIndexed { _, elem -> elem })
        assertEquals(CdrCodedList.of(1, 2), CdrCodedList.of(1, 2).mapIndexed { _, elem -> elem })
        assertEquals(CdrCodedList.of(0, 1), CdrCodedList.of(1, 2).mapIndexed { i, _ -> i })
        assertEquals(CdrCodedList.of(1, 2, 3), CdrCodedList.of(1, 2, 3).mapIndexed { _, elem -> elem })
        assertEquals(CdrCodedList.of(2, 3, 4), CdrCodedList.of(1, 2, 3).mapIndexed { _, elem -> elem + 1 })
        assertEquals(CdrCodedList.of(1, 3, 5), CdrCodedList.of(1, 2, 3).mapIndexed { i, elem -> elem + i })
        assertEquals(CdrCodedList.of(1, 2, 3, 4), CdrCodedList.of(1, 2, 3, 4).mapIndexed { _, elem -> elem })
        assertEquals(CdrCodedList.of(2, 3, 4, 5), CdrCodedList.of(1, 2, 3, 4).mapIndexed { _, elem -> elem + 1 })
        assertEquals(CdrCodedList.of(1, 3, 5, 7), CdrCodedList.of(1, 2, 3, 4).mapIndexed { i, elem -> elem + i })
    }

    @Test
    fun filter() {
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Int>().filter { true })
        assertInstanceOf(Cons::class.java, CdrCodedList.of(1).filter { true })

        assertEquals(CdrCodedList.of(1, 2), CdrCodedList.of(1, 2).filter { true })
        assertEquals(CdrCodedList.of<Int>(), CdrCodedList.of(1, 2).filter { false })
        assertEquals(CdrCodedList.of(2), CdrCodedList.of(1, 2, 3).filter { it % 2 == 0 })
        assertEquals(CdrCodedList.of(2, 4), CdrCodedList.of(1, 2, 3, 4).filter { it % 2 == 0 })
    }

    @Test
    fun filterNot() {
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Int>().filterNot { true })
        assertInstanceOf(Cons::class.java, CdrCodedList.of(1).filterNot { true })

        assertEquals(CdrCodedList.of(1, 2), CdrCodedList.of(1, 2).filterNot { false })
        assertEquals(CdrCodedList.of<Int>(), CdrCodedList.of(1, 2).filterNot { true })
        assertEquals(CdrCodedList.of(2), CdrCodedList.of(1, 2, 3).filterNot { it % 2 != 0 })
        assertEquals(CdrCodedList.of(2, 4), CdrCodedList.of(1, 2, 3, 4).filterNot { it % 2 != 0 })
    }

    @Test
    fun cadr() {
        assertThrows(NoSuchElementException::class.java) { CdrCodedList.of<Int>().cadr }
        assertThrows(NoSuchElementException::class.java) { CdrCodedList.of(1).cadr }
        assertEquals(2, CdrCodedList.of(1, 2, 3, 4, 5).cadr)
        assertEquals(CdrCodedList.of(1, 2, 3, 4, 5).cdr.car, CdrCodedList.of(1, 2, 3, 4, 5).cadr)
    }

    @Test
    fun caddr() {
        assertThrows(NoSuchElementException::class.java) { CdrCodedList.of<Int>().caddr }
        assertThrows(NoSuchElementException::class.java) { CdrCodedList.of(1).caddr }
        assertEquals(3, CdrCodedList.of(1, 2, 3, 4, 5).caddr)
        assertEquals(CdrCodedList.of(1, 2, 3, 4, 5).cdr.cdr.car, CdrCodedList.of(1, 2, 3, 4, 5).caddr)
    }

    @Test
    fun cadddr() {
        assertThrows(NoSuchElementException::class.java) { CdrCodedList.of<Int>().cadddr }
        assertThrows(NoSuchElementException::class.java) { CdrCodedList.of(1).cadddr }
        assertEquals(4, CdrCodedList.of(1, 2, 3, 4, 5).cadddr)
        assertEquals(CdrCodedList.of(1, 2, 3, 4, 5).cdr.cdr.cdr.car, CdrCodedList.of(1, 2, 3, 4, 5).cadddr)
    }

    @Test
    fun cddr() {
        assertEquals(EmptyCons<Int>(), CdrCodedList.of<Int>().cddr)
        assertEquals(EmptyCons<Int>(), CdrCodedList.of(1).cddr)
        assertEquals(CdrCodedList.of(3, 4, 5), CdrCodedList.of(1, 2, 3, 4, 5).cddr)
        assertEquals(CdrCodedList.of(1, 2, 3, 4, 5).cdr.cdr, CdrCodedList.of(1, 2, 3, 4, 5).cddr)
    }

    @Test
    fun cdddr() {
        assertEquals(EmptyCons<Int>(), CdrCodedList.of<Int>().cdddr)
        assertEquals(EmptyCons<Int>(), CdrCodedList.of(1).cdddr)
        assertEquals(CdrCodedList.of(4, 5), CdrCodedList.of(1, 2, 3, 4, 5).cdddr)
        assertEquals(CdrCodedList.of(1, 2, 3, 4, 5).cdr.cdr.cdr, CdrCodedList.of(1, 2, 3, 4, 5).cdddr)
    }

    @Test
    fun cddddr() {
        assertEquals(EmptyCons<Int>(), CdrCodedList.of<Int>().cddddr)
        assertEquals(EmptyCons<Int>(), CdrCodedList.of(1).cddddr)
        assertEquals(CdrCodedList.of(5), CdrCodedList.of(1, 2, 3, 4, 5).cddddr)
        assertEquals(CdrCodedList.of(1, 2, 3, 4, 5).cdr.cdr.cdr.cdr, CdrCodedList.of(1, 2, 3, 4, 5).cddddr)
    }

    @Test
    fun flatMap() {
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Int>().flatMap { listOf(it) })
        assertEquals(CdrCodedList.of<Int>(), CdrCodedList.of<Int>().flatMap { listOf(it) })
        assertInstanceOf(Cons::class.java, CdrCodedList.of(1, 2, 3).flatMap { listOf(it) })
        assertEquals(CdrCodedList.of(1, 2, 3), CdrCodedList.of(1, 2, 3).flatMap { listOf(it) })
        assertEquals(CdrCodedList.of(1, 2, 3), CdrCodedList.of(listOf(1, 2), listOf(3)).flatMap { it })
    }

    @Test
    fun take() {
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Int>().take(10))
        assertInstanceOf(Cons::class.java, CdrCodedList.of(1, 2, 3).take(10))
        assertInstanceOf(Cons::class.java, CdrCodedList.of(1, 2, 3).take(2))

        assertEquals(CdrCodedList.of<Int>(), CdrCodedList.of<Int>().drop(0))
        assertEquals(CdrCodedList.of<Int>(), CdrCodedList.of<Int>().drop(10))

        assertEquals(CdrCodedList.of<Int>(), CdrCodedList.of(1, 2, 3, 4, 5, 6).take(0))
        assertEquals(CdrCodedList.of(1), CdrCodedList.of(1, 2, 3, 4, 5, 6).take(1))
        assertEquals(CdrCodedList.of(1, 2), CdrCodedList.of(1, 2, 3, 4, 5, 6).take(2))
        assertEquals(CdrCodedList.of(1, 2, 3), CdrCodedList.of(1, 2, 3, 4, 5, 6).take(3))
        assertEquals(CdrCodedList.of(1, 2, 3, 4), CdrCodedList.of(1, 2, 3, 4, 5, 6).take(4))
        assertEquals(CdrCodedList.of(1, 2, 3, 4, 5), CdrCodedList.of(1, 2, 3, 4, 5, 6).take(5))
        assertEquals(CdrCodedList.of(1, 2, 3, 4, 5, 6), CdrCodedList.of(1, 2, 3, 4, 5, 6).take(6))
    }

    @Test
    fun drop() {
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Int>().drop(10))
        assertInstanceOf(Cons::class.java, CdrCodedList.of(1, 2, 3).drop(10))
        assertInstanceOf(Cons::class.java, CdrCodedList.of(1, 2, 3).drop(2))

        assertEquals(CdrCodedList.of<Int>(), CdrCodedList.of<Int>().drop(0))
        assertEquals(CdrCodedList.of<Int>(), CdrCodedList.of<Int>().drop(10))

        assertEquals(CdrCodedList.of(1, 2, 3, 4, 5, 6), CdrCodedList.of(1, 2, 3, 4, 5, 6).drop(0))
        assertEquals(CdrCodedList.of(2, 3, 4, 5, 6), CdrCodedList.of(1, 2, 3, 4, 5, 6).drop(1))
        assertEquals(CdrCodedList.of(3, 4, 5, 6), CdrCodedList.of(1, 2, 3, 4, 5, 6).drop(2))
        assertEquals(CdrCodedList.of(4, 5, 6), CdrCodedList.of(1, 2, 3, 4, 5, 6).drop(3))
        assertEquals(CdrCodedList.of(5, 6), CdrCodedList.of(1, 2, 3, 4, 5, 6).drop(4))
        assertEquals(CdrCodedList.of(6), CdrCodedList.of(1, 2, 3, 4, 5, 6).drop(5))
        assertEquals(CdrCodedList.of<Int>(), CdrCodedList.of(1, 2, 3, 4, 5, 6).drop(6))
    }

    @Test
    fun takeWhile() {
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Int>().takeWhile { true })
        assertInstanceOf(Cons::class.java, CdrCodedList.of(1, 2, 3).takeWhile { true })
        assertInstanceOf(Cons::class.java, CdrCodedList.of(1, 2, 3).takeWhile { false })
        assertInstanceOf(Cons::class.java, CdrCodedList.of(1, 2, 3).takeWhile { it < 3 })

        assertEquals(CdrCodedList.of<Int>(), CdrCodedList.of<Int>().takeWhile { it < 3 })

        assertEquals(CdrCodedList.of(1, 2), CdrCodedList.of(1, 2, 3, 2, 1).takeWhile { it < 3 })

        assertEquals(CdrCodedList.of<Int>(), CdrCodedList.of(1, 2, 3, 4, 5, 6).takeWhile { it < 1 })
        assertEquals(CdrCodedList.of(1), CdrCodedList.of(1, 2, 3, 4, 5, 6).takeWhile { it < 2 })
        assertEquals(CdrCodedList.of(1, 2), CdrCodedList.of(1, 2, 3, 4, 5, 6).takeWhile { it < 3 })
        assertEquals(CdrCodedList.of(1, 2, 3), CdrCodedList.of(1, 2, 3, 4, 5, 6).takeWhile { it < 4 })
        assertEquals(CdrCodedList.of(1, 2, 3, 4), CdrCodedList.of(1, 2, 3, 4, 5, 6).takeWhile { it < 5 })
        assertEquals(CdrCodedList.of(1, 2, 3, 4, 5), CdrCodedList.of(1, 2, 3, 4, 5, 6).takeWhile { it < 6 })
        assertEquals(CdrCodedList.of(1, 2, 3, 4, 5, 6), CdrCodedList.of(1, 2, 3, 4, 5, 6).takeWhile { it < 7 })
    }

    @Test
    fun dropWhile() {
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Int>().dropWhile { true })
        assertInstanceOf(Cons::class.java, CdrCodedList.of(1, 2, 3).dropWhile { true })
        assertInstanceOf(Cons::class.java, CdrCodedList.of(1, 2, 3).dropWhile { false })
        assertInstanceOf(Cons::class.java, CdrCodedList.of(1, 2, 3).dropWhile { it < 3 })

        assertEquals(CdrCodedList.of<Int>(), CdrCodedList.of<Int>().dropWhile { it < 3 })

        assertEquals(CdrCodedList.of(3, 2, 1), CdrCodedList.of(1, 2, 3, 2, 1).dropWhile { it < 3 })

        assertEquals(CdrCodedList.of(1, 2, 3, 4, 5, 6), CdrCodedList.of(1, 2, 3, 4, 5, 6).dropWhile { it < 1 })
        assertEquals(CdrCodedList.of(2, 3, 4, 5, 6), CdrCodedList.of(1, 2, 3, 4, 5, 6).dropWhile { it < 2 })
        assertEquals(CdrCodedList.of(3, 4, 5, 6), CdrCodedList.of(1, 2, 3, 4, 5, 6).dropWhile { it < 3 })
        assertEquals(CdrCodedList.of(4, 5, 6), CdrCodedList.of(1, 2, 3, 4, 5, 6).dropWhile { it < 4 })
        assertEquals(CdrCodedList.of(5, 6), CdrCodedList.of(1, 2, 3, 4, 5, 6).dropWhile { it < 5 })
        assertEquals(CdrCodedList.of(6), CdrCodedList.of(1, 2, 3, 4, 5, 6).dropWhile { it < 6 })
        assertEquals(CdrCodedList.of<Int>(), CdrCodedList.of(1, 2, 3, 4, 5, 6).dropWhile { it < 8 })
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
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Int>().sortedBy { it })
        assertInstanceOf(Cons::class.java, CdrCodedList.of(1, 2, 3).sortedBy { it })

        assertEquals(CdrCodedList.of<Int>(), CdrCodedList.of<Int>().sortedBy { it })
        assertEquals(CdrCodedList.of(1), CdrCodedList.of(1).sortedBy { it })

        assertEquals(CdrCodedList.of(1, 2, 3), CdrCodedList.of(1, 2, 3).sortedBy { it })
        assertEquals(CdrCodedList.of(1, 2, 3), CdrCodedList.of(3, 1, 2).sortedBy { it })

        assertEquals(CdrCodedList.of(1, 2, 2, 3), CdrCodedList.of(1, 2, 2, 3).sortedBy { it })
        assertEquals(CdrCodedList.of(1, 2, 2, 3), CdrCodedList.of(3, 1, 2, 2).sortedBy { it })
    }

    @Test
    fun sortedByDescending() {
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Int>().sortedByDescending { it })
        assertInstanceOf(Cons::class.java, CdrCodedList.of(1, 2, 3).sortedByDescending { it })

        assertEquals(CdrCodedList.of<Int>(), CdrCodedList.of<Int>().sortedByDescending { it })
        assertEquals(CdrCodedList.of(1), CdrCodedList.of(1).sortedByDescending { it })

        assertEquals(CdrCodedList.of(3, 2, 1), CdrCodedList.of(1, 2, 3).sortedByDescending { it })
        assertEquals(CdrCodedList.of(3, 2, 1), CdrCodedList.of(3, 1, 2).sortedByDescending { it })

        assertEquals(CdrCodedList.of(3, 2, 2, 1), CdrCodedList.of(1, 2, 2, 3).sortedByDescending { it })
        assertEquals(CdrCodedList.of(3, 2, 2, 1), CdrCodedList.of(3, 1, 2, 2).sortedByDescending { it })
    }

    @Test
    fun sortedWith() {
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Int>().sortedWith { n, m -> n.compareTo(m) })
        assertInstanceOf(Cons::class.java, CdrCodedList.of(1, 2, 3).sortedWith { n, m -> n.compareTo(m) })
        assertInstanceOf(Cons::class.java, CdrCodedList.of(1, 2, 3).sortedWith { n, m -> -n.compareTo(m) })

        assertEquals(CdrCodedList.of<Int>(), CdrCodedList.of<Int>().sortedWith { n, m -> n.compareTo(m) })
        assertEquals(CdrCodedList.of(1), CdrCodedList.of(1).sortedWith { n, m -> n.compareTo(m) })
        assertEquals(CdrCodedList.of(1), CdrCodedList.of(1).sortedWith { n, m -> -n.compareTo(m) })

        assertEquals(CdrCodedList.of(3, 1, 2), CdrCodedList.of(3, 1, 2).sortedWith { _, _ -> 0 })

        assertEquals(CdrCodedList.of(1, 2, 3), CdrCodedList.of(1, 2, 3).sortedWith { n, m -> n.compareTo(m) })
        assertEquals(CdrCodedList.of(1, 2, 3), CdrCodedList.of(3, 1, 2).sortedWith { n, m -> n.compareTo(m) })

        assertEquals(CdrCodedList.of(1, 2, 2, 3), CdrCodedList.of(1, 2, 2, 3).sortedWith { n, m -> n.compareTo(m) })
        assertEquals(CdrCodedList.of(1, 2, 2, 3), CdrCodedList.of(3, 1, 2, 2).sortedWith { n, m -> n.compareTo(m) })

        assertEquals(CdrCodedList.of(3, 2, 1), CdrCodedList.of(1, 2, 3).sortedWith { n, m -> -n.compareTo(m) })
        assertEquals(CdrCodedList.of(3, 2, 1), CdrCodedList.of(3, 1, 2).sortedWith { n, m -> -n.compareTo(m) })

        assertEquals(CdrCodedList.of(3, 2, 2, 1), CdrCodedList.of(1, 2, 2, 3).sortedWith { n, m -> -n.compareTo(m) })
        assertEquals(CdrCodedList.of(3, 2, 2, 1), CdrCodedList.of(3, 1, 2, 2).sortedWith { n, m -> -n.compareTo(m) })
    }

    @Test
    fun distinct() {
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Int>().distinct())
        assertInstanceOf(Cons::class.java, CdrCodedList.of(1, 2, 3).distinct())

        assertEquals(CdrCodedList.of<Int>(), CdrCodedList.of<Int>().distinct())
        assertEquals(CdrCodedList.of(1), CdrCodedList.of(1).distinct())
        assertEquals(CdrCodedList.of(1, 2, 3, 4, 5), CdrCodedList.of(1, 2, 3, 4, 5).distinct())
        assertEquals(CdrCodedList.of(1), CdrCodedList.of(1, 1, 1, 1, 1, 1).distinct())

        assertEquals(CdrCodedList.of(1, 2, 3), CdrCodedList.of(1, 1, 2, 2, 3, 3).distinct())
        assertEquals(CdrCodedList.of(2, 1, 3), CdrCodedList.of(2, 1, 1, 2, 3, 3).distinct())
    }

    @Test
    fun shuffled() {
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Int>().shuffled())
        assertInstanceOf(Cons::class.java, CdrCodedList.of(1, 2, 3).shuffled())

        val seed = 0xDEADBEEF
        val rand = Random(seed)
        val temp = CdrCodedList(1..50)
        val tempShuffled = temp.shuffled(rand)
        assertNotSame(temp, tempShuffled)

        assertEquals(CdrCodedList.of(1), CdrCodedList.of(1).shuffled(rand))
        assertEquals(CdrCodedList.of(1, 1, 1), CdrCodedList.of(1, 1, 1).shuffled(rand))
        assertEquals(CdrCodedList(1..55).shuffled(Random(seed)), CdrCodedList(1..55).shuffled(Random(seed)))
    }

    @Test
    fun asSequence() {
        assertInstanceOf(Sequence::class.java, CdrCodedList.of<Int>().asSequence())
        assertInstanceOf(Sequence::class.java, CdrCodedList.of(1, 2).asSequence())
        assertInstanceOf(Sequence::class.java, CdrCodedList.of(1, 2, 3).asSequence())
        assertInstanceOf(Sequence::class.java, CdrCodedList.of(1, 2, 3).asSequence().map { it + 1 })

        assertEquals(listOf(1), CdrCodedList.of(1).asSequence().toList())
        assertEquals(listOf(1, 2), CdrCodedList.of(1, 2).asSequence().toList())
        assertEquals(listOf(2, 3, 4), CdrCodedList.of(1, 2, 3).asSequence().map { it + 1 }.toList())
    }

    @Test
    fun plusElement() {
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Int>() + 1)
        assertInstanceOf(Cons::class.java, CdrCodedList.of(1) + 2)

        assertEquals(Cons.of(1), CdrCodedList.of<Int>() + 1)
        assertEquals(Cons.of(1, 2), CdrCodedList.of(1) + 2)
    }

    @Test
    fun plusIterable() {
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Int>() + listOf(1))
        assertInstanceOf(Cons::class.java, CdrCodedList.of(1) + listOf(2, 3))

        assertEquals(Cons.of<Int>(), CdrCodedList.of<Int>() + listOf())
        assertEquals(Cons.of(1, 2, 3), CdrCodedList.of(1,2,3) + listOf())

        assertEquals(Cons.of(1), CdrCodedList.of<Int>() + listOf(1))
        assertEquals(Cons.of(1, 2, 3), CdrCodedList.of(1) + listOf(2, 3))

        assertEquals(Cons.of(1, 2, 3, 4, 5), CdrCodedList.of<Int>() + (1..5))
        assertEquals(Cons.of(1, 2, 3, 4, 5), CdrCodedList.of<Int>() + (1..<6))
        assertEquals(Cons.of(1, 2, 3, 4, 5), CdrCodedList.of(1) + (2..5))
        assertEquals(Cons.of(1, 2, 3, 4, 5), CdrCodedList.of(1) + sequenceOf(2, 3, 4, 5))

        assertEquals(Cons.of<Int>(), CdrCodedList.of<Int>() + Cons.of())
        assertEquals(Cons.of(1, 2, 3, 4, 5), CdrCodedList.of(1) + Cons.of(2, 3, 4, 5))
        assertEquals(Cons.of(1, 2, 3, 4, 5), CdrCodedList.of(1) + CdrCodedList(listOf(2, 3, 4, 5)))
    }

    @Test
    fun plusVList() {
        assertInstanceOf(VList::class.java, CdrCodedList.of<Int>() + VList.of())
        assertEquals(VList.of<Int>(), CdrCodedList.of<Int>() + VList.of())

        assertInstanceOf(VList::class.java, CdrCodedList.of<Int>() + VList.of(1))
        assertEquals(VList.of(1), CdrCodedList.of<Int>() + VList.of(1))

        assertInstanceOf(VList::class.java, CdrCodedList.of(1, 2) + VList.of(3))
        assertEquals(VList.of(1, 2, 3), CdrCodedList.of(1, 2) + VList.of(3))

        assertInstanceOf(VList::class.java, CdrCodedList.of(1, 2) + VList.of(3, 4))
        assertEquals(VList.of(1, 2, 3, 4), CdrCodedList.of(1, 2) + VList.of(3, 4))
    }
}
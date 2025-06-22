package kleinert.soap

import kleinert.soap.cons.EmptyCons
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class VListTest {

    @Test
    fun testConstructor() {
        assertTrue(VList<Int>() == VList(listOf<Int>()))
        assertTrue(VList<Int>(arrayOf()) == VList(listOf<Int>()))
        assertTrue(VList(listOf<Int>().asIterable()) == VList(listOf<Int>()))
        assertTrue(VList(VList<Int>()) == VList(listOf<Int>()))

        assertTrue(VList(arrayOf(1)) == VList(listOf(1)))
        assertTrue(VList(listOf(1).asIterable()) == VList(listOf(1)))

        assertTrue(VList(arrayOf(1, 2, 3, 4, 5)) == VList(listOf(1, 2, 3, 4, 5)))
        assertTrue(VList(listOf(1, 2, 3, 4, 5).asIterable()) == VList(listOf(1, 2, 3, 4, 5)))
    }

    @Test
    fun getSize() {
        assertEquals(0, VList<Int>().size)
        assertEquals(0, VList.of<Int>().size)
        assertEquals(1, VList.of(99).size)
        assertEquals(5, VList.of(1, 2, 3, 4, 5).size)
        assertEquals(55, VList((1..55).toList()).size)
    }

    @Test
    fun cons() {
        val vlist = VList<Int>()
        assertEquals(0, vlist.size)

        assertEquals(VList(arrayOf(1)), vlist.cons(1))
        assertEquals(VList(arrayOf(2, 1)), vlist.cons(1).cons(2))
        assertEquals(VList(arrayOf(3, 2, 1)), vlist.cons(1).cons(2).cons(3))
    }

    @Test
    fun cdr() {
        assertEquals(VList<Int>(), VList<Int>().cdr)
        assertEquals(VList.of(1), VList.of(2, 1).cdr)
        assertEquals(VList(2..5), VList(1..5).cdr)
    }

    @Test
    fun car() {
        assertThrows(NoSuchElementException::class.java) { VList.of<Int>().car }
        assertEquals(1, VList.of(1).car)
        assertEquals(2, VList.of(2, 1).car)
        assertEquals((0..99).first(), VList(0..99).car)
        assertEquals(5, VList.of(1, 2, 3, 4).cons(5).car)

        assertThrows(NoSuchElementException::class.java) { VList.of<Int>().first() }
        assertEquals(1, VList.of(1).first())
        assertEquals(2, VList.of(2, 1).first())
        assertEquals((0..99).first(), VList(0..99).first())
        assertEquals(5, VList.of(1, 2, 3, 4).cons(5).first())
    }

    @Test
    fun contains() {
        assertFalse(VList<Int>().contains(1))
        assertFalse(VList<String>().contains(""))
        assertFalse(VList<Int>(listOf()).contains(1))
        assertFalse(VList(listOf(2, 1, 3)).contains(5))

        assertTrue(VList(listOf(1)).contains(1))
        assertTrue(VList(listOf(2, 1, 3)).contains(1))
    }

    @Test
    fun containsAll() {
        assertTrue(VList<Int>().containsAll(listOf()))

        assertFalse(VList<Int>().containsAll(listOf(1)))
        assertFalse(VList<String>().containsAll(listOf("")))
        assertFalse(VList<Int>(listOf()).containsAll(listOf(1)))
        assertFalse(VList(listOf(2, 1, 3)).containsAll(listOf(5)))

        assertTrue(VList(listOf(1)).containsAll(listOf(1)))
        assertTrue(VList(listOf(2, 1, 3)).containsAll(listOf(1)))

        assertTrue(VList(0..99).containsAll(listOf(10, 20, 30, 50, 60, 70, 80, 90)))
        assertTrue(VList(0..99).containsAll(setOf(10, 20, 30, 50, 60, 70, 80, 90)))
        assertTrue(VList(0..99).containsAll(VList.of(10, 20, 30, 50, 60, 70, 80, 90)))
    }

    @Test
    fun get() {
        assertThrows(IndexOutOfBoundsException::class.java) { VList.of<Int>()[0] }
        assertThrows(IndexOutOfBoundsException::class.java) { VList.of(1)[1] }
        assertThrows(IndexOutOfBoundsException::class.java) { VList.of(1, 2, 3, 4, 5)[10] }

        assertEquals(1, VList.of(1)[0])
        assertEquals(2, VList.of(2, 1)[0])
        assertEquals((0..99).first(), VList(0..99)[0])
        assertEquals(5, VList.of(1, 2, 3, 4).cons(5)[0])

        assertEquals(2, VList.of(3, 2, 1)[1])
        assertEquals(1, VList.of(3, 2, 1)[2])
        assertEquals(5, VList.of(1, 2, 3, 4).cons(5)[0])
        assertEquals(50, VList(0..99)[50])
    }

    @Test
    fun isEmpty() {
        assertTrue(VList<Int>().isEmpty())
        assertTrue(VList<String>().isEmpty())
        assertTrue(VList<Int>(listOf()).isEmpty())
        assertTrue(VList(listOf(1)).cdr.isEmpty())

        assertFalse(VList<Int>().cons(1).isEmpty())
        assertFalse(VList<Int>().cons(1).cons(2).isEmpty())
        assertFalse(VList<Int>().cons(1).cons(2).cons(3).isEmpty())
        assertFalse(VList(listOf(2, 1, 3)).isEmpty())

        assertFalse(VList<Int>().isNotEmpty())
        assertFalse(VList<String>().isNotEmpty())
        assertFalse(VList<Int>(listOf()).isNotEmpty())
        assertFalse(VList(listOf(1)).cdr.isNotEmpty())

        assertTrue(VList<Int>().cons(1).isNotEmpty())
        assertTrue(VList<Int>().cons(1).cons(2).isNotEmpty())
        assertTrue(VList<Int>().cons(1).cons(2).cons(3).isNotEmpty())
        assertTrue(VList(listOf(2, 1, 3)).isNotEmpty())
    }

    @Test
    fun indexOf() {
        assertEquals(-1, VList.of(1).indexOf(0))
        assertEquals(-1, VList(listOf(2, 1, 3, 2, 1, 3)).indexOf(4))
        assertEquals(0, VList.of(1).indexOf(1))
        assertEquals(0, VList.of<Int>().cons(1).indexOf(1))
        assertEquals(2, VList(listOf(2, 1, 3)).indexOf(3))
        assertEquals(2, VList(listOf(2, 1, 3, 2, 1, 3)).indexOf(3))
        assertEquals(2, VList(listOf(2, 1, 3, 2, 1, 3, 15)).indexOf(3))
        assertEquals(0, VList(listOf(1, 1, 1, 1, 1, 1)).indexOf(1))
    }

    @Test
    fun lastIndexOf() {
        assertEquals(-1, VList.of(1).lastIndexOf(0))
        assertEquals(-1, VList(listOf(2, 1, 3, 2, 1, 3)).lastIndexOf(4))
        assertEquals(0, VList.of(1).lastIndexOf(1))
        assertEquals(0, VList.of<Int>().cons(1).lastIndexOf(1))
        assertEquals(2, VList(listOf(2, 1, 3)).lastIndexOf(3))
        assertEquals(5, VList(listOf(2, 1, 3, 2, 1, 3)).lastIndexOf(3))
        assertEquals(5, VList(listOf(2, 1, 3, 2, 1, 3, 15)).lastIndexOf(3))
        assertEquals(5, VList(listOf(1, 1, 1, 1, 1, 1)).lastIndexOf(1))
    }

    @Test
    fun iterator() {
        run {
            var counter = 0
            for (e in VList<Int>()) {
                counter++
            }
            assertEquals(0, counter)
        }
        run {
            var counter = 0
            for (e in VList(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))) {
                counter++
            }
            assertEquals(10, counter)
        }
        run {
            var counter = 0
            VList(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).forEach { _ -> counter++ }
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
            for (e in VList<Int>().listIterator()) {
                counter++
            }
            assertEquals(0, counter)
        }
        run {
            var counter = 0
            for (e in VList(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).listIterator()) {
                counter++
            }
            assertEquals(10, counter)
        }
        run {
            var counter = 0
            VList(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).listIterator().forEach { _ -> counter++ }
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
        assertEquals(3, VList.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).subList(3, 6).size)
    }

    @Test
    fun toMutableList() {
        assertTrue(VList<Int>().toMutableList() is MutableList<Int>)
        assertTrue(VList<Int>().toMutableList().isEmpty())
        assertEquals(5, VList.of(1, 2, 3, 4, 5).toMutableList().size)
    }

    @Test
    fun reversed() {
        assertEquals(VList.of<Int>(), VList.of<Int>().reversed())
        assertEquals(VList.of(1, 2, 3), VList.of(3, 2, 1).reversed())
        assertEquals(VList.of(1, 2, 3), VList.of<Int>().cons(1).cons(2).cons(3).reversed())
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
    fun map() {
        assertEquals(VList<Int>().javaClass, VList.of<Int>().map { it }.javaClass)
        assertEquals(VList.of(1).javaClass, VList.of<Int>(1).map { it }.javaClass)
        assertEquals(VList.of(1), VList.of(1).map { it })
        assertEquals(VList.of(1, 2), VList.of(1, 2).map { it })
        assertEquals(VList.of(2, 3), VList.of(1, 2).map { it+1 })
        assertEquals(VList.of(1, 2, 3), VList.of(1, 2, 3).map { it })
        assertEquals(VList.of(2, 3, 4), VList.of(1, 2, 3).map { it + 1 })
    }

    @Test
    fun mapIndexed() {
        assertEquals(VList<Int>().javaClass, VList.of<Int>().mapIndexed { _, elem -> elem }.javaClass)
        assertEquals(VList.of(1).javaClass, VList.of(1).mapIndexed { _, elem -> elem }.javaClass)
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
        assertEquals(VList<Int>().javaClass, VList.of<Int>().filter { true }.javaClass)
        assertEquals(VList.of(1).javaClass, VList.of<Int>(1).filter { true }.javaClass)
        assertEquals(VList.of(1, 2), VList.of(1, 2).filter { true })
        assertEquals(VList.of<Int>(), VList.of(1, 2).filter { false })
        assertEquals(VList.of(2), VList.of(1, 2, 3).filter { it % 2 == 0 })
        assertEquals(VList.of(2, 4), VList.of(1, 2, 3, 4).filter { it % 2 == 0 })
    }

    @Test
    fun filterNot() {
        assertEquals(VList<Int>().javaClass, VList.of<Int>().filterNot { true }.javaClass)
        assertEquals(VList.of(1).javaClass, VList.of<Int>(1).filter { false }.javaClass)
        assertEquals(VList.of(1, 2), VList.of(1, 2).filterNot { false })
        assertEquals(VList.of<Int>(), VList.of(1, 2).filterNot { true })
        assertEquals(VList.of(2), VList.of(1, 2, 3).filterNot { it % 2 != 0 })
        assertEquals(VList.of(2, 4), VList.of(1, 2, 3, 4).filterNot { it % 2 != 0 })
    }

    @Test
    fun drop() {
        assertEquals(VList.of<Int>(), VList.of<Int>().drop(-10))
        assertEquals(VList.of<Int>(), VList.of<Int>().drop(0))
        assertEquals(VList.of<Int>(), VList.of<Int>().drop(10))

        assertEquals(VList.of(1, 2, 3), VList.of(1, 2, 3).drop(-10))
        assertEquals(VList.of(1, 2, 3), VList.of(1, 2, 3).drop(0))
        assertEquals(VList.of<Int>(), VList.of(1, 2, 3).drop(10))

        assertEquals(VList.of(1, 2, 3), VList.of(1, 2, 3).drop(-10))
        assertEquals(VList.of(1, 2, 3), VList.of(1, 2, 3).drop(0))
        assertEquals(VList.of<Int>(), VList.of(1, 2, 3).drop(10))

        assertEquals(VList.of(1, 2, 3, 4, 5, 6), VList.of(1, 2, 3, 4, 5, 6).drop(0))
        assertEquals(VList.of(2, 3, 4, 5, 6), VList.of(1, 2, 3, 4, 5, 6).drop(1))
        assertEquals(VList.of(3, 4, 5, 6), VList.of(1, 2, 3, 4, 5, 6).drop(2))
        assertEquals(VList.of(4, 5, 6), VList.of(1, 2, 3, 4, 5, 6).drop(3))
        assertEquals(VList.of(5, 6), VList.of(1, 2, 3, 4, 5, 6).drop(4))
        assertEquals(VList.of(6), VList.of(1, 2, 3, 4, 5, 6).drop(5))
        assertEquals(VList.of<Int>(), VList.of(1, 2, 3, 4, 5, 6).drop(6))
    }

    @Test
    fun mapSegments() {
        assertEquals(listOf<List<Int>>(), VList.of<Int>().mapSegments { it })

        assertEquals(
            listOf(listOf(2), listOf(3)),
            VList.of(1, 2).mapSegments { it+1 }
        )
        assertEquals(
            listOf(listOf(2, 3), listOf(4)),
            VList.of(1, 2, 3).mapSegments { it+1 }
        )
        assertEquals(
            listOf(listOf(2, 3, 4, 5), listOf(6, 7), listOf(8)),
            VList.of(1, 2, 3, 4, 5, 6, 7).mapSegments { it+1 }
        )
        assertEquals(
            listOf(listOf(1), listOf(2, 3, 4, 5), listOf(6, 7), listOf(8)),
            VList.of(0, 1, 2, 3, 4, 5, 6, 7).mapSegments { it+1 }
        )
    }

    @Test
    fun split() {
        assertThrows(NoSuchElementException::class.java) { VList.of<Int>().split() }
        assertEquals(1 to VList.of<Int>(), VList.of(1).split())
        assertEquals(1 to VList.of<Int>(2), VList.of(1, 2).split())
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
        assertEquals(VList.of(1, 2, 3, 4, 5).cdr.cdr.car, VList.of(1, 2, 3, 4, 5).caddr)}

    @Test
    fun cadddr() {
        assertThrows(NoSuchElementException::class.java) { VList.of<Int>().cadddr }
        assertThrows(NoSuchElementException::class.java) { VList.of(1).cadddr }
        assertEquals(4, VList.of(1, 2, 3, 4, 5).cadddr)
        assertEquals(VList.of(1, 2, 3, 4, 5).cdr.cdr.cdr.car, VList.of(1, 2, 3, 4, 5).cadddr)}

    @Test
    fun cddr() {
        assertEquals(EmptyCons<Int>(), VList.of<Int>().cddr)
        assertEquals(EmptyCons<Int>(), VList.of(1).cddr)
        assertEquals(VList.of(3, 4, 5), VList.of(1, 2, 3, 4, 5).cddr)
        assertEquals(VList.of(1, 2, 3, 4, 5).cdr.cdr, VList.of(1, 2, 3, 4, 5).cddr)
    }

    @Test
    fun cdddr() {
        assertEquals(EmptyCons<Int>(), VList.of<Int>().cdddr)
        assertEquals(EmptyCons<Int>(), VList.of(1).cdddr)
        //assertEquals(VList.of(4, 5), VList.of(1, 2, 3, 4, 5).cdddr)
        assertEquals(VList.of(1, 2, 3, 4, 5).cdr.cdr.cdr, VList.of(1, 2, 3, 4, 5).cdddr)}

    @Test
    fun cddddr() {
        assertEquals(EmptyCons<Int>(), VList.of<Int>().cddddr)
        assertEquals(EmptyCons<Int>(), VList.of(1).cddddr)
        //assertEquals(VList.of(5), VList.of(1, 2, 3, 4, 5).cddddr)
        assertEquals(VList.of(1, 2, 3, 4, 5).cdr.cdr.cdr.cdr, VList.of(1, 2, 3, 4, 5).cddddr)}
}

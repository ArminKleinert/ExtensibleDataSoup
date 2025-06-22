package kleinert.soap

import kleinert.soap.cons.Cons
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ConsTest {

    @Test
    fun testConstructor() {
        //assertTrue(Cons<Int>() is EmptyList<Int>)
        assertTrue(Cons.of<Int>() == Cons.fromIterable(listOf<Int>()))
        assertTrue(Cons.fromIterable<Int>(arrayOf()) == Cons.fromIterable(listOf<Int>()))
        assertTrue(Cons.fromIterable(listOf<Int>().asIterable()) == Cons.fromIterable(listOf<Int>()))
        assertTrue(Cons.fromIterable(Cons.of<Int>()) == Cons.fromIterable(listOf<Int>()))

        assertTrue(Cons.fromIterable(arrayOf(1)) == Cons.fromIterable(listOf(1)))
        assertTrue(Cons.fromIterable(listOf(1).asIterable()) == Cons.fromIterable(listOf(1)))

        assertTrue(Cons.fromIterable(arrayOf(1, 2, 3, 4, 5)) == Cons.fromIterable(listOf(1, 2, 3, 4, 5)))
        assertTrue(Cons.fromIterable(listOf(1, 2, 3, 4, 5).asIterable()) == Cons.fromIterable(listOf(1, 2, 3, 4, 5)))
    }

    @Test
    fun getSize() {
        assertEquals(0, Cons.of<Int>().size)
        assertEquals(0, Cons.of<Int>().size)
        assertEquals(1, Cons.of(99).size)
        assertEquals(5, Cons.of(1, 2, 3, 4, 5).size)
        assertEquals(55, Cons.fromIterable((1..55).toList()).size)
    }

    @Test
    fun cons() {
        val cons = Cons.of<Int>()
        assertEquals(0, cons.size)

        assertEquals(Cons.fromIterable(arrayOf(1)), cons.cons(1))
        assertEquals(Cons.fromIterable(arrayOf(2, 1)), cons.cons(1).cons(2))
        assertEquals(Cons.fromIterable(arrayOf(3, 2, 1)), cons.cons(1).cons(2).cons(3))
    }

    @Test
    fun cdr() {
        assertEquals(Cons.of<Int>(), Cons.of<Int>().cdr)
        assertEquals(Cons.of(1), Cons.of(2, 1).cdr)
        assertEquals(Cons.fromIterable(2..5), Cons.fromIterable(1..5).cdr)
    }

    @Test
    fun car() {
        assertThrows(NoSuchElementException::class.java) { Cons.of<Int>().car }
        assertEquals(1, Cons.of(1).car)
        assertEquals(2, Cons.of(2, 1).car)
        assertEquals((0..99).first(), Cons.fromIterable(0..99).car)
        assertEquals(5, Cons.of(1, 2, 3, 4).cons(5).car)

        assertThrows(NoSuchElementException::class.java) { Cons.of<Int>().first() }
        assertEquals(1, Cons.of(1).first())
        assertEquals(2, Cons.of(2, 1).first())
        assertEquals((0..99).first(), Cons.fromIterable(0..99).first())
        assertEquals(5, Cons.of(1, 2, 3, 4).cons(5).first())
    }

    @Test
    fun contains() {
        assertFalse(Cons.of<Int>().contains(1))
        assertFalse(Cons.of<String>().contains(""))
        assertFalse(Cons.fromIterable<Int>(listOf()).contains(1))
        assertFalse(Cons.fromIterable(listOf(2, 1, 3)).contains(5))

        assertTrue(Cons.fromIterable(listOf(1)).contains(1))
        assertTrue(Cons.fromIterable(listOf(2, 1, 3)).contains(1))
    }

    @Test
    fun containsAll() {
        assertTrue(Cons.of<Int>().containsAll(listOf()))

        assertFalse(Cons.of<Int>().containsAll(listOf(1)))
        assertFalse(Cons.of<String>().containsAll(listOf("")))
        assertFalse(Cons.fromIterable<Int>(listOf()).containsAll(listOf(1)))
        assertFalse(Cons.fromIterable(listOf(2, 1, 3)).containsAll(listOf(5)))

        assertTrue(Cons.fromIterable(listOf(1)).containsAll(listOf(1)))
        assertTrue(Cons.fromIterable(listOf(2, 1, 3)).containsAll(listOf(1)))

        assertTrue(Cons.fromIterable(0..99).containsAll(listOf(10, 20, 30, 50, 60, 70, 80, 90)))
        assertTrue(Cons.fromIterable(0..99).containsAll(setOf(10, 20, 30, 50, 60, 70, 80, 90)))
        assertTrue(Cons.fromIterable(0..99).containsAll(Cons.of(10, 20, 30, 50, 60, 70, 80, 90)))
    }

    @Test
    fun get() {
        assertThrows(IndexOutOfBoundsException::class.java) { Cons.of<Int>()[0] }
        assertThrows(IndexOutOfBoundsException::class.java) { Cons.of(1)[1] }
        assertThrows(IndexOutOfBoundsException::class.java) { Cons.of(1, 2, 3, 4, 5)[10] }

        assertEquals(1, Cons.of(1)[0])
        assertEquals(2, Cons.of(2, 1)[0])
        assertEquals((0..99).first(), Cons.fromIterable(0..99)[0])
        assertEquals(5, Cons.of(1, 2, 3, 4).cons(5)[0])

        assertEquals(2, Cons.of(3, 2, 1)[1])
        assertEquals(1, Cons.of(3, 2, 1)[2])
        assertEquals(5, Cons.of(1, 2, 3, 4).cons(5)[0])
        assertEquals(50, Cons.fromIterable(0..99)[50])
    }

    @Test
    fun isEmpty() {
        assertTrue(Cons.of<Int>().isEmpty())
        assertTrue(Cons.of<String>().isEmpty())
        assertTrue(Cons.fromIterable<Int>(listOf()).isEmpty())
        assertTrue(!Cons.fromIterable(listOf(1)).cdr.iterator().hasNext())

        assertFalse(Cons.of<Int>().cons(1).isEmpty())
        assertFalse(Cons.of<Int>().cons(1).cons(2).isEmpty())
        assertFalse(Cons.of<Int>().cons(1).cons(2).cons(3).isEmpty())
        assertFalse(Cons.fromIterable(listOf(2, 1, 3)).isEmpty())

        assertFalse(Cons.of<Int>().isNotEmpty())
        assertFalse(Cons.of<String>().isNotEmpty())
        assertFalse(Cons.fromIterable<Int>(listOf()).isNotEmpty())
        assertFalse(!Cons.fromIterable(listOf(1)).iterator().hasNext())

        assertTrue(Cons.of<Int>().cons(1).isNotEmpty())
        assertTrue(Cons.of<Int>().cons(1).cons(2).isNotEmpty())
        assertTrue(Cons.of<Int>().cons(1).cons(2).cons(3).isNotEmpty())
        assertTrue(Cons.fromIterable(listOf(2, 1, 3)).isNotEmpty())
    }

    @Test
    fun indexOf() {
        assertEquals(-1, Cons.of(1).indexOf(0))
        assertEquals(-1, Cons.fromIterable(listOf(2, 1, 3, 2, 1, 3)).indexOf(4))
        assertEquals(0, Cons.of(1).indexOf(1))
        assertEquals(0, Cons.of<Int>().cons(1).indexOf(1))
        assertEquals(2, Cons.fromIterable(listOf(2, 1, 3)).indexOf(3))
        assertEquals(2, Cons.fromIterable(listOf(2, 1, 3, 2, 1, 3)).indexOf(3))
        assertEquals(2, Cons.fromIterable(listOf(2, 1, 3, 2, 1, 3, 15)).indexOf(3))
        assertEquals(0, Cons.fromIterable(listOf(1, 1, 1, 1, 1, 1)).indexOf(1))
    }

    @Test
    fun lastIndexOf() {
        assertEquals(-1, Cons.of(1).lastIndexOf(0))
        assertEquals(-1, Cons.fromIterable(listOf(2, 1, 3, 2, 1, 3)).lastIndexOf(4))
        assertEquals(0, Cons.of(1).lastIndexOf(1))
        assertEquals(0, Cons.of<Int>().cons(1).lastIndexOf(1))
        assertEquals(2, Cons.fromIterable(listOf(2, 1, 3)).lastIndexOf(3))
        assertEquals(5, Cons.fromIterable(listOf(2, 1, 3, 2, 1, 3)).lastIndexOf(3))
        assertEquals(5, Cons.fromIterable(listOf(2, 1, 3, 2, 1, 3, 15)).lastIndexOf(3))
        assertEquals(5, Cons.fromIterable(listOf(1, 1, 1, 1, 1, 1)).lastIndexOf(1))
    }

    @Test
    fun iterator() {
        run {
            var counter = 0
            for (e in Cons.of<Int>()) {
                counter++
            }
            assertEquals(0, counter)
        }
        run {
            var counter = 0
            for (e in Cons.fromIterable(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))) {
                counter++
            }
            assertEquals(10, counter)
        }
        run {
            var counter = 0
            Cons.fromIterable(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).forEach { _ -> counter++ }
            assertEquals(10, counter)
        }

        run {
            val iterator = Cons.of(1, 2, 3, 4, 5).iterator()
            var sum = 0
            while (iterator.hasNext()) {
                sum += iterator.next()
            }
            assertEquals(15, sum)
        }

        assertEquals(15, Cons.of(1, 2, 3, 4, 5).sum())
        assertEquals(15, Cons.of(1, 2, 3, 4, 5).fold(0, Int::plus))
        assertEquals(15, Cons.of(1, 2, 3, 4, 5).foldRight(0, Int::plus))
        assertEquals(15, Cons.of(1, 2, 3, 4, 5).reduce(Int::plus))
    }

    @Test
    fun listIterator() {
        run {
            var counter = 0
            for (e in Cons.of<Int>().listIterator()) {
                counter++
            }
            assertEquals(0, counter)
        }
        run {
            var counter = 0
            for (e in Cons.fromIterable(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).listIterator()) {
                counter++
            }
            assertEquals(10, counter)
        }
        run {
            var counter = 0
            Cons.fromIterable(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).listIterator().forEach { _ -> counter++ }
            assertEquals(10, counter)
        }

        run {
            val iterator = Cons.of(1, 2, 3, 4, 5).listIterator()
            var sum = 0
            while (iterator.hasNext()) {
                sum += iterator.next()
            }
            assertEquals(15, sum)
        }
    }

    @Test
    fun subList() {
        assertThrows(IndexOutOfBoundsException::class.java) { Cons.of<Int>().subList(3, 6).size }
        assertEquals(3, Cons.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).subList(3, 6).size)
    }

    @Test
    fun toMutableList() {
        assertEquals(mutableListOf<Int>(), Cons.of<Int>().toMutableList())
        assertTrue(Cons.of<Int>().toMutableList().isEmpty())
        assertEquals(5, Cons.of(1, 2, 3, 4, 5).toMutableList().size)
    }

    @Test
    fun reversed() {
        assertEquals(Cons.of<Int>(), Cons.of<Int>().reversed())
        assertEquals(Cons.of(1, 2, 3), Cons.of(3, 2, 1).reversed())
        assertEquals(Cons.of(1, 2, 3), Cons.of<Int>().cons(1).cons(2).cons(3).reversed())
    }

    @Test
    fun map() {
        assertEquals(Cons.of<Int>(), Cons.of<Int>().map { it })
        assertEquals(Cons.of(1, 2, 3), Cons.of(1, 2, 3).map { it })
        assertEquals(Cons.of(2, 3, 4), Cons.of(1, 2, 3).map { it + 1 })
    }

    @Test
    fun drop() {
        assertTrue(Cons.of<Int>().drop(2) is Cons<Int>)
        assertTrue(Cons.of(1, 2, 3).drop(2) is Cons<Int>)

        assertEquals(Cons.of<Int>(), Cons.of<Int>().drop(-10))
        assertEquals(Cons.of<Int>(), Cons.of<Int>().drop(0))
        assertEquals(Cons.of<Int>(), Cons.of<Int>().drop(10))

        assertEquals(Cons.of(1, 2, 3), Cons.of(1, 2, 3).drop(-10))
        assertEquals(Cons.of(1, 2, 3), Cons.of(1, 2, 3).drop(0))
        assertEquals(Cons.of<Int>(), Cons.of(1, 2, 3).drop(10))

        assertEquals(Cons.of(1, 2, 3), Cons.of(1, 2, 3).drop(-10))
        assertEquals(Cons.of(1, 2, 3), Cons.of(1, 2, 3).drop(0))
        assertEquals(Cons.of<Int>(), Cons.of(1, 2, 3).drop(10))

        assertEquals(Cons.of(1, 2, 3, 4, 5), Cons.of(1, 2, 3, 4, 5).drop(0))
        assertEquals(Cons.of(2, 3, 4, 5), Cons.of(1, 2, 3, 4, 5).drop(1))
        assertEquals(Cons.of(3, 4, 5), Cons.of(1, 2, 3, 4, 5).drop(2))
        assertEquals(Cons.of(4, 5), Cons.of(1, 2, 3, 4, 5).drop(3))
        assertEquals(Cons.of(5), Cons.of(1, 2, 3, 4, 5).drop(4))
        assertEquals(Cons.of<Int>(), Cons.of(1, 2, 3, 4, 5).drop(5))
    }

    @Test
    fun cadr() {
        assertThrows(NoSuchElementException::class.java) { Cons.of<Int>().cadr }
        assertThrows(NoSuchElementException::class.java) { Cons.of(1).cadr }
        assertEquals(2, Cons.of(1, 2, 3, 4, 5).cadr)
        assertEquals(Cons.of(1, 2, 3, 4, 5).cdr.car, Cons.of(1, 2, 3, 4, 5).cadr)
    }

    @Test
    fun caddr() {
        assertThrows(NoSuchElementException::class.java) { Cons.of<Int>().caddr }
        assertThrows(NoSuchElementException::class.java) { Cons.of(1).caddr }
        assertEquals(3, Cons.of(1, 2, 3, 4, 5).caddr)
    assertEquals(Cons.of(1, 2, 3, 4, 5).cdr.cdr.car, Cons.of(1, 2, 3, 4, 5).caddr)}

    @Test
    fun cadddr() {
        assertThrows(NoSuchElementException::class.java) { Cons.of<Int>().cadddr }
        assertThrows(NoSuchElementException::class.java) { Cons.of(1).cadddr }
        assertEquals(4, Cons.of(1, 2, 3, 4, 5).cadddr)
    assertEquals(Cons.of(1, 2, 3, 4, 5).cdr.cdr.cdr.car, Cons.of(1, 2, 3, 4, 5).cadddr)}

//    @Test
//    fun cddr() {
//        assertEquals(EmptyList<Int>(), Cons.of<Int>().cddr)
//        assertEquals(EmptyList<Int>(), Cons.of(1).cddr)
//        assertEquals(Cons.of(3, 4, 5), Cons.of(1, 2, 3, 4, 5).cddr)
//        assertEquals(Cons.of(1, 2, 3, 4, 5).cdr.cdr, Cons.of(1, 2, 3, 4, 5).cddr)
//    }
//
//    @Test
//    fun cdddr() {
//        assertEquals(EmptyList<Int>(), Cons.of<Int>().cdddr)
//        assertEquals(EmptyList<Int>(), Cons.of(1).cdddr)
//        assertEquals(Cons.of(4, 5), Cons.of(1, 2, 3, 4, 5).cdddr)
//        assertEquals(Cons.of(1, 2, 3, 4, 5).cdr.cdr.cdr, Cons.of(1, 2, 3, 4, 5).cdddr)
//    }
//
//    @Test
//    fun cddddr() {
//        assertEquals(EmptyList<Int>(), Cons.of<Int>().cddddr)
//        assertEquals(EmptyList<Int>(), Cons.of(1).cddddr)
//        assertEquals(Cons.of(5), Cons.of(1, 2, 3, 4, 5).cddddr)
//        assertEquals(Cons.of(1, 2, 3, 4, 5).cdr.cdr.cdr.cdr, Cons.of(1, 2, 3, 4, 5).cddddr)
//    }
}

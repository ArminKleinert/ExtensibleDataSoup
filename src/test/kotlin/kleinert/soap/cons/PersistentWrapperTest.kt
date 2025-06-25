package kleinert.soap.cons
//
//import org.junit.jupiter.api.Assertions.*
//import org.junit.jupiter.api.Test
//import kotlin.random.Random
//
//class PersistentWrapperTest {
//    @Test
//    fun testConstructor() {
//        assertTrue(PersistentWrapper<Int>() == PersistentWrapper(listOf<Int>()))
//        assertTrue(PersistentWrapper<Int>(arrayOf()) == PersistentWrapper(listOf<Int>()))
//        assertTrue(PersistentWrapper(listOf<Int>().asIterable()) == PersistentWrapper(listOf<Int>()))
//        assertTrue(PersistentWrapper(PersistentWrapper<Int>()) == PersistentWrapper(listOf<Int>()))
//
//        assertTrue(PersistentWrapper(arrayOf(1)) == PersistentWrapper(listOf(1)))
//        assertTrue(PersistentWrapper(listOf(1).asIterable()) == PersistentWrapper(listOf(1)))
//
//        assertTrue(PersistentWrapper(arrayOf(1, 2, 3, 4, 5)) == PersistentWrapper(listOf(1, 2, 3, 4, 5)))
//        assertTrue(PersistentWrapper(listOf(1, 2, 3, 4, 5).asIterable()) == PersistentWrapper(listOf(1, 2, 3, 4, 5)))
//    }
//
//    @Test
//    fun getSize() {
//        assertEquals(0, PersistentWrapper<Int>().size)
//        assertEquals(0, PersistentWrapper.of<Int>().size)
//        assertEquals(1, PersistentWrapper.of(99).size)
//        assertEquals(5, PersistentWrapper.of(1, 2, 3, 4, 5).size)
//        assertEquals(55, PersistentWrapper((1..55).toList()).size)
//    }
//
//    @Test
//    fun cons() {
//        val list = PersistentWrapper<Int>()
//        assertEquals(0, list.size)
//
//        assertInstanceOf(PersistentList::class.java, list.cons(1))
//        assertEquals(PersistentWrapper(arrayOf(1)), list.cons(1))
//        assertEquals(PersistentWrapper(arrayOf(2, 1)), list.cons(1).cons(2))
//        assertEquals(PersistentWrapper(arrayOf(3, 2, 1)), list.cons(1).cons(2).cons(3))
//    }
//
//    @Test
//    fun cdr() {
//        assertInstanceOf(PersistentWrapper::class.java, PersistentWrapper<Int>().cdr)
//        assertInstanceOf(PersistentWrapper::class.java, PersistentWrapper.of(2, 1).cdr)
//        assertEquals(PersistentWrapper<Int>(), PersistentWrapper<Int>().cdr)
//        assertEquals(PersistentWrapper.of(1), PersistentWrapper.of(2, 1).cdr)
//        assertEquals(PersistentWrapper(2..5), PersistentWrapper(1..5).cdr)
//    }
//
//    @Test
//    fun car() {
//        assertThrows(NoSuchElementException::class.java) { PersistentWrapper.of<Int>().car }
//        assertEquals(1, PersistentWrapper.of(1).car)
//        assertEquals(2, PersistentWrapper.of(2, 1).car)
//        assertEquals((0..99).first(), PersistentWrapper(0..99).car)
//        assertEquals(5, PersistentWrapper.of(1, 2, 3, 4).cons(5).car)
//
//        assertThrows(NoSuchElementException::class.java) { PersistentWrapper.of<Int>().first() }
//        assertEquals(1, PersistentWrapper.of(1).first())
//        assertEquals(2, PersistentWrapper.of(2, 1).first())
//        assertEquals((0..99).first(), PersistentWrapper(0..99).first())
//        assertEquals(5, PersistentWrapper.of(1, 2, 3, 4).cons(5).first())
//    }
//
//    @Test
//    fun contains() {
//        assertFalse(PersistentWrapper<Int>().contains(1))
//        assertFalse(PersistentWrapper<String>().contains(""))
//        assertFalse(PersistentWrapper<Int>(listOf()).contains(1))
//        assertFalse(PersistentWrapper(listOf(2, 1, 3)).contains(5))
//
//        assertTrue(PersistentWrapper(listOf(1)).contains(1))
//        assertTrue(PersistentWrapper(listOf(2, 1, 3)).contains(1))
//    }
//
//    @Test
//    fun containsAll() {
//        assertTrue(PersistentWrapper<Int>().containsAll(listOf()))
//
//        assertFalse(PersistentWrapper<Int>().containsAll(listOf(1)))
//        assertFalse(PersistentWrapper<String>().containsAll(listOf("")))
//        assertFalse(PersistentWrapper<Int>(listOf()).containsAll(listOf(1)))
//        assertFalse(PersistentWrapper(listOf(2, 1, 3)).containsAll(listOf(5)))
//
//        assertTrue(PersistentWrapper(listOf(1)).containsAll(listOf(1)))
//        assertTrue(PersistentWrapper(listOf(2, 1, 3)).containsAll(listOf(1)))
//
//        assertTrue(PersistentWrapper(0..99).containsAll(listOf(10, 20, 30, 50, 60, 70, 80, 90)))
//        assertTrue(PersistentWrapper(0..99).containsAll(setOf(10, 20, 30, 50, 60, 70, 80, 90)))
//        assertTrue(PersistentWrapper(0..99).containsAll(PersistentWrapper.of(10, 20, 30, 50, 60, 70, 80, 90)))
//    }
//
//    @Test
//    fun get() {
//        assertThrows(IndexOutOfBoundsException::class.java) { PersistentWrapper.of<Int>()[0] }
//        assertThrows(IndexOutOfBoundsException::class.java) { PersistentWrapper.of(1)[1] }
//        assertThrows(IndexOutOfBoundsException::class.java) { PersistentWrapper.of(1, 2, 3, 4, 5)[10] }
//
//        assertEquals(1, PersistentWrapper.of(1)[0])
//        assertEquals(2, PersistentWrapper.of(2, 1)[0])
//        assertEquals((0..99).first(), PersistentWrapper(0..99)[0])
//        assertEquals(5, PersistentWrapper.of(1, 2, 3, 4).cons(5)[0])
//
//        assertEquals(2, PersistentWrapper.of(3, 2, 1)[1])
//        assertEquals(1, PersistentWrapper.of(3, 2, 1)[2])
//        assertEquals(5, PersistentWrapper.of(1, 2, 3, 4).cons(5)[0])
//        assertEquals(50, PersistentWrapper(0..99)[50])
//    }
//
//    @Test
//    fun isEmpty() {
//        assertTrue(PersistentWrapper<Int>().isEmpty())
//        assertTrue(PersistentWrapper<String>().isEmpty())
//        assertTrue(PersistentWrapper<Int>(listOf()).isEmpty())
//        assertTrue(PersistentWrapper(listOf(1)).cdr.isEmpty())
//
//        assertFalse(PersistentWrapper<Int>().cons(1).isEmpty())
//        assertFalse(PersistentWrapper<Int>().cons(1).cons(2).isEmpty())
//        assertFalse(PersistentWrapper<Int>().cons(1).cons(2).cons(3).isEmpty())
//        assertFalse(PersistentWrapper(listOf(2, 1, 3)).isEmpty())
//
//        assertFalse(PersistentWrapper<Int>().isNotEmpty())
//        assertFalse(PersistentWrapper<String>().isNotEmpty())
//        assertFalse(PersistentWrapper<Int>(listOf()).isNotEmpty())
//        assertFalse(PersistentWrapper(listOf(1)).cdr.isNotEmpty())
//
//        assertTrue(PersistentWrapper<Int>().cons(1).isNotEmpty())
//        assertTrue(PersistentWrapper<Int>().cons(1).cons(2).isNotEmpty())
//        assertTrue(PersistentWrapper<Int>().cons(1).cons(2).cons(3).isNotEmpty())
//        assertTrue(PersistentWrapper(listOf(2, 1, 3)).isNotEmpty())
//    }
//
//    @Test
//    fun indexOf() {
//        assertEquals(-1, PersistentWrapper.of(1).indexOf(0))
//        assertEquals(-1, PersistentWrapper(listOf(2, 1, 3, 2, 1, 3)).indexOf(4))
//        assertEquals(0, PersistentWrapper.of(1).indexOf(1))
//        assertEquals(0, PersistentWrapper.of<Int>().cons(1).indexOf(1))
//        assertEquals(2, PersistentWrapper(listOf(2, 1, 3)).indexOf(3))
//        assertEquals(2, PersistentWrapper(listOf(2, 1, 3, 2, 1, 3)).indexOf(3))
//        assertEquals(2, PersistentWrapper(listOf(2, 1, 3, 2, 1, 3, 15)).indexOf(3))
//        assertEquals(0, PersistentWrapper(listOf(1, 1, 1, 1, 1, 1)).indexOf(1))
//    }
//
//    @Test
//    fun lastIndexOf() {
//        assertEquals(-1, PersistentWrapper.of(1).lastIndexOf(0))
//        assertEquals(-1, PersistentWrapper(listOf(2, 1, 3, 2, 1, 3)).lastIndexOf(4))
//        assertEquals(0, PersistentWrapper.of(1).lastIndexOf(1))
//        assertEquals(0, PersistentWrapper.of<Int>().cons(1).lastIndexOf(1))
//        assertEquals(2, PersistentWrapper(listOf(2, 1, 3)).lastIndexOf(3))
//        assertEquals(5, PersistentWrapper(listOf(2, 1, 3, 2, 1, 3)).lastIndexOf(3))
//        assertEquals(5, PersistentWrapper(listOf(2, 1, 3, 2, 1, 3, 15)).lastIndexOf(3))
//        assertEquals(5, PersistentWrapper(listOf(1, 1, 1, 1, 1, 1)).lastIndexOf(1))
//    }
//
//    @Test
//    fun iterator() {
//        run {
//            var counter = 0
//            for (e in PersistentWrapper<Int>()) {
//                counter++
//            }
//            assertEquals(0, counter)
//        }
//        run {
//            var counter = 0
//            for (e in PersistentWrapper(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))) {
//                counter++
//            }
//            assertEquals(10, counter)
//        }
//        run {
//            var counter = 0
//            PersistentWrapper(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).forEach { _ -> counter++ }
//            assertEquals(10, counter)
//        }
//
//        run {
//            val iterator = PersistentWrapper.of(1, 2, 3, 4, 5).iterator()
//            var sum = 0
//            while (iterator.hasNext()) {
//                sum += iterator.next()
//            }
//            assertEquals(15, sum)
//        }
//
//        assertEquals(15, PersistentWrapper.of(1, 2, 3, 4, 5).sum())
//        assertEquals(15, PersistentWrapper.of(1, 2, 3, 4, 5).fold(0, Int::plus))
//        assertEquals(15, PersistentWrapper.of(1, 2, 3, 4, 5).foldRight(0, Int::plus))
//        assertEquals(15, PersistentWrapper.of(1, 2, 3, 4, 5).reduce(Int::plus))
//    }
//
//    @Test
//    fun listIterator() {
//        run {
//            var counter = 0
//            for (e in PersistentWrapper<Int>().listIterator()) {
//                counter++
//            }
//            assertEquals(0, counter)
//        }
//        run {
//            var counter = 0
//            for (e in PersistentWrapper(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).listIterator()) {
//                counter++
//            }
//            assertEquals(10, counter)
//        }
//        run {
//            var counter = 0
//            PersistentWrapper(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).listIterator().forEach { _ -> counter++ }
//            assertEquals(10, counter)
//        }
//
//        run {
//            val iterator = PersistentWrapper.of(1, 2, 3, 4, 5).listIterator()
//            var sum = 0
//            while (iterator.hasNext()) {
//                sum += iterator.next()
//            }
//            assertEquals(15, sum)
//        }
//    }
//
//    @Test
//    fun subList() {
//        assertThrows(IndexOutOfBoundsException::class.java) { PersistentWrapper.of<Int>().subList(3, 6).size }
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).subList(3, 6))
//        assertEquals(listOf<Int>(), PersistentWrapper.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).subList(0, 0))
//        assertEquals(listOf(4, 5, 6), PersistentWrapper.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).subList(3, 6))
//        assertEquals(3, PersistentWrapper.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).subList(3, 6).size)
//    }
//
//    @Test
//    fun toMutableList() {
//        assertTrue(PersistentWrapper<Int>().toMutableList().isEmpty())
//        assertEquals(5, PersistentWrapper.of(1, 2, 3, 4, 5).toMutableList().size)
//    }
//
//    @Test
//    fun reversed() {
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of<Int>().reversed())
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of(3, 2, 1).reversed())
//
//        assertEquals(PersistentWrapper.of<Int>(), PersistentWrapper.of<Int>().reversed())
//        assertEquals(PersistentWrapper.of(1, 2, 3), PersistentWrapper.of(3, 2, 1).reversed())
//        assertEquals(PersistentWrapper.of(1, 2, 3), PersistentWrapper.of<Int>().cons(1).cons(2).cons(3).reversed())
//    }
//
//    @Test
//    fun map() {
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of<Int>().map { it })
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of(1).map { it })
//
//        assertEquals(PersistentWrapper.of(1), PersistentWrapper.of(1).map { it })
//        assertEquals(PersistentWrapper.of(1, 2), PersistentWrapper.of(1, 2).map { it })
//        assertEquals(PersistentWrapper.of(2, 3), PersistentWrapper.of(1, 2).map { it + 1 })
//        assertEquals(PersistentWrapper.of(1, 2, 3), PersistentWrapper.of(1, 2, 3).map { it })
//        assertEquals(PersistentWrapper.of(2, 3, 4), PersistentWrapper.of(1, 2, 3).map { it + 1 })
//    }
//
//    @Test
//    fun mapIndexed() {
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of<Int>().mapIndexed { _, elem -> elem })
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of(1).mapIndexed { _, elem -> elem })
//
//        assertEquals(PersistentWrapper.of(1), PersistentWrapper.of(1).mapIndexed { _, elem -> elem })
//        assertEquals(PersistentWrapper.of(1, 2), PersistentWrapper.of(1, 2).mapIndexed { _, elem -> elem })
//        assertEquals(PersistentWrapper.of(0, 1), PersistentWrapper.of(1, 2).mapIndexed { i, _ -> i })
//        assertEquals(PersistentWrapper.of(1, 2, 3), PersistentWrapper.of(1, 2, 3).mapIndexed { _, elem -> elem })
//        assertEquals(PersistentWrapper.of(2, 3, 4), PersistentWrapper.of(1, 2, 3).mapIndexed { _, elem -> elem + 1 })
//        assertEquals(PersistentWrapper.of(1, 3, 5), PersistentWrapper.of(1, 2, 3).mapIndexed { i, elem -> elem + i })
//        assertEquals(PersistentWrapper.of(1, 2, 3, 4), PersistentWrapper.of(1, 2, 3, 4).mapIndexed { _, elem -> elem })
//        assertEquals(PersistentWrapper.of(2, 3, 4, 5), PersistentWrapper.of(1, 2, 3, 4).mapIndexed { _, elem -> elem + 1 })
//        assertEquals(PersistentWrapper.of(1, 3, 5, 7), PersistentWrapper.of(1, 2, 3, 4).mapIndexed { i, elem -> elem + i })
//    }
//
//    @Test
//    fun filter() {
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of<Int>().filter { true })
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of(1).filter { true })
//
//        assertEquals(PersistentWrapper.of(1, 2), PersistentWrapper.of(1, 2).filter { true })
//        assertEquals(PersistentWrapper.of<Int>(), PersistentWrapper.of(1, 2).filter { false })
//        assertEquals(PersistentWrapper.of(2), PersistentWrapper.of(1, 2, 3).filter { it % 2 == 0 })
//        assertEquals(PersistentWrapper.of(2, 4), PersistentWrapper.of(1, 2, 3, 4).filter { it % 2 == 0 })
//    }
//
//    @Test
//    fun filterNot() {
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of<Int>().filterNot { true })
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of(1).filterNot { true })
//
//        assertEquals(PersistentWrapper.of(1, 2), PersistentWrapper.of(1, 2).filterNot { false })
//        assertEquals(PersistentWrapper.of<Int>(), PersistentWrapper.of(1, 2).filterNot { true })
//        assertEquals(PersistentWrapper.of(2), PersistentWrapper.of(1, 2, 3).filterNot { it % 2 != 0 })
//        assertEquals(PersistentWrapper.of(2, 4), PersistentWrapper.of(1, 2, 3, 4).filterNot { it % 2 != 0 })
//    }
//
//    @Test
//    fun cadr() {
//        assertThrows(NoSuchElementException::class.java) { PersistentWrapper.of<Int>().cadr }
//        assertThrows(NoSuchElementException::class.java) { PersistentWrapper.of(1).cadr }
//        assertEquals(2, PersistentWrapper.of(1, 2, 3, 4, 5).cadr)
//        assertEquals(PersistentWrapper.of(1, 2, 3, 4, 5).cdr.car, PersistentWrapper.of(1, 2, 3, 4, 5).cadr)
//    }
//
//    @Test
//    fun caddr() {
//        assertThrows(NoSuchElementException::class.java) { PersistentWrapper.of<Int>().caddr }
//        assertThrows(NoSuchElementException::class.java) { PersistentWrapper.of(1).caddr }
//        assertEquals(3, PersistentWrapper.of(1, 2, 3, 4, 5).caddr)
//        assertEquals(PersistentWrapper.of(1, 2, 3, 4, 5).cdr.cdr.car, PersistentWrapper.of(1, 2, 3, 4, 5).caddr)
//    }
//
//    @Test
//    fun cadddr() {
//        assertThrows(NoSuchElementException::class.java) { PersistentWrapper.of<Int>().cadddr }
//        assertThrows(NoSuchElementException::class.java) { PersistentWrapper.of(1).cadddr }
//        assertEquals(4, PersistentWrapper.of(1, 2, 3, 4, 5).cadddr)
//        assertEquals(PersistentWrapper.of(1, 2, 3, 4, 5).cdr.cdr.cdr.car, PersistentWrapper.of(1, 2, 3, 4, 5).cadddr)
//    }
//
//    @Test
//    fun cddr() {
//        assertEquals(nullCons<Int>(), PersistentWrapper.of<Int>().cddr)
//        assertEquals(nullCons<Int>(), PersistentWrapper.of(1).cddr)
//        assertEquals(PersistentWrapper.of(3, 4, 5), PersistentWrapper.of(1, 2, 3, 4, 5).cddr)
//        assertEquals(PersistentWrapper.of(1, 2, 3, 4, 5).cdr.cdr, PersistentWrapper.of(1, 2, 3, 4, 5).cddr)
//    }
//
//    @Test
//    fun cdddr() {
//        assertEquals(nullCons<Int>(), PersistentWrapper.of<Int>().cdddr)
//        assertEquals(nullCons<Int>(), PersistentWrapper.of(1).cdddr)
//        assertEquals(PersistentWrapper.of(4, 5), PersistentWrapper.of(1, 2, 3, 4, 5).cdddr)
//        assertEquals(PersistentWrapper.of(1, 2, 3, 4, 5).cdr.cdr.cdr, PersistentWrapper.of(1, 2, 3, 4, 5).cdddr)
//    }
//
//    @Test
//    fun cddddr() {
//        assertEquals(nullCons<Int>(), PersistentWrapper.of<Int>().cddddr)
//        assertEquals(nullCons<Int>(), PersistentWrapper.of(1).cddddr)
//        assertEquals(PersistentWrapper.of(5), PersistentWrapper.of(1, 2, 3, 4, 5).cddddr)
//        assertEquals(PersistentWrapper.of(1, 2, 3, 4, 5).cdr.cdr.cdr.cdr, PersistentWrapper.of(1, 2, 3, 4, 5).cddddr)
//    }
//
//    @Test
//    fun flatMap() {
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of<Int>().flatMap { listOf(it) })
//        assertEquals(PersistentWrapper.of<Int>(), PersistentWrapper.of<Int>().flatMap { listOf(it) })
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of(1, 2, 3).flatMap { listOf(it) })
//        assertEquals(PersistentWrapper.of(1, 2, 3), PersistentWrapper.of(1, 2, 3).flatMap { listOf(it) })
//        assertEquals(PersistentWrapper.of(1, 2, 3), PersistentWrapper.of(listOf(1, 2), listOf(3)).flatMap { it })
//    }
//
//    @Test
//    fun take() {
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of<Int>().take(10))
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of(1, 2, 3).take(10))
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of(1, 2, 3).take(2))
//
//        assertEquals(PersistentWrapper.of<Int>(), PersistentWrapper.of<Int>().drop(0))
//        assertEquals(PersistentWrapper.of<Int>(), PersistentWrapper.of<Int>().drop(10))
//
//        assertEquals(PersistentWrapper.of<Int>(), PersistentWrapper.of(1, 2, 3, 4, 5, 6).take(0))
//        assertEquals(PersistentWrapper.of(1), PersistentWrapper.of(1, 2, 3, 4, 5, 6).take(1))
//        assertEquals(PersistentWrapper.of(1, 2), PersistentWrapper.of(1, 2, 3, 4, 5, 6).take(2))
//        assertEquals(PersistentWrapper.of(1, 2, 3), PersistentWrapper.of(1, 2, 3, 4, 5, 6).take(3))
//        assertEquals(PersistentWrapper.of(1, 2, 3, 4), PersistentWrapper.of(1, 2, 3, 4, 5, 6).take(4))
//        assertEquals(PersistentWrapper.of(1, 2, 3, 4, 5), PersistentWrapper.of(1, 2, 3, 4, 5, 6).take(5))
//        assertEquals(PersistentWrapper.of(1, 2, 3, 4, 5, 6), PersistentWrapper.of(1, 2, 3, 4, 5, 6).take(6))
//    }
//
//    @Test
//    fun drop() {
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of<Int>().drop(10))
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of(1, 2, 3).drop(10))
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of(1, 2, 3).drop(2))
//
//        assertEquals(PersistentWrapper.of<Int>(), PersistentWrapper.of<Int>().drop(0))
//        assertEquals(PersistentWrapper.of<Int>(), PersistentWrapper.of<Int>().drop(10))
//
//        assertEquals(PersistentWrapper.of(1, 2, 3, 4, 5, 6), PersistentWrapper.of(1, 2, 3, 4, 5, 6).drop(0))
//        assertEquals(PersistentWrapper.of(2, 3, 4, 5, 6), PersistentWrapper.of(1, 2, 3, 4, 5, 6).drop(1))
//        assertEquals(PersistentWrapper.of(3, 4, 5, 6), PersistentWrapper.of(1, 2, 3, 4, 5, 6).drop(2))
//        assertEquals(PersistentWrapper.of(4, 5, 6), PersistentWrapper.of(1, 2, 3, 4, 5, 6).drop(3))
//        assertEquals(PersistentWrapper.of(5, 6), PersistentWrapper.of(1, 2, 3, 4, 5, 6).drop(4))
//        assertEquals(PersistentWrapper.of(6), PersistentWrapper.of(1, 2, 3, 4, 5, 6).drop(5))
//        assertEquals(PersistentWrapper.of<Int>(), PersistentWrapper.of(1, 2, 3, 4, 5, 6).drop(6))
//    }
//
//    @Test
//    fun takeWhile() {
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of<Int>().takeWhile { true })
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of(1, 2, 3).takeWhile { true })
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of(1, 2, 3).takeWhile { false })
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of(1, 2, 3).takeWhile { it < 3 })
//
//        assertEquals(PersistentWrapper.of<Int>(), PersistentWrapper.of<Int>().takeWhile { it < 3 })
//
//        assertEquals(PersistentWrapper.of(1, 2), PersistentWrapper.of(1, 2, 3, 2, 1).takeWhile { it < 3 })
//
//        assertEquals(PersistentWrapper.of<Int>(), PersistentWrapper.of(1, 2, 3, 4, 5, 6).takeWhile { it < 1 })
//        assertEquals(PersistentWrapper.of(1), PersistentWrapper.of(1, 2, 3, 4, 5, 6).takeWhile { it < 2 })
//        assertEquals(PersistentWrapper.of(1, 2), PersistentWrapper.of(1, 2, 3, 4, 5, 6).takeWhile { it < 3 })
//        assertEquals(PersistentWrapper.of(1, 2, 3), PersistentWrapper.of(1, 2, 3, 4, 5, 6).takeWhile { it < 4 })
//        assertEquals(PersistentWrapper.of(1, 2, 3, 4), PersistentWrapper.of(1, 2, 3, 4, 5, 6).takeWhile { it < 5 })
//        assertEquals(PersistentWrapper.of(1, 2, 3, 4, 5), PersistentWrapper.of(1, 2, 3, 4, 5, 6).takeWhile { it < 6 })
//        assertEquals(PersistentWrapper.of(1, 2, 3, 4, 5, 6), PersistentWrapper.of(1, 2, 3, 4, 5, 6).takeWhile { it < 7 })
//    }
//
//    @Test
//    fun dropWhile() {
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of<Int>().dropWhile { true })
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of(1, 2, 3).dropWhile { true })
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of(1, 2, 3).dropWhile { false })
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of(1, 2, 3).dropWhile { it < 3 })
//
//        assertEquals(PersistentWrapper.of<Int>(), PersistentWrapper.of<Int>().dropWhile { it < 3 })
//
//        assertEquals(PersistentWrapper.of(3, 2, 1), PersistentWrapper.of(1, 2, 3, 2, 1).dropWhile { it < 3 })
//
//        assertEquals(PersistentWrapper.of(1, 2, 3, 4, 5, 6), PersistentWrapper.of(1, 2, 3, 4, 5, 6).dropWhile { it < 1 })
//        assertEquals(PersistentWrapper.of(2, 3, 4, 5, 6), PersistentWrapper.of(1, 2, 3, 4, 5, 6).dropWhile { it < 2 })
//        assertEquals(PersistentWrapper.of(3, 4, 5, 6), PersistentWrapper.of(1, 2, 3, 4, 5, 6).dropWhile { it < 3 })
//        assertEquals(PersistentWrapper.of(4, 5, 6), PersistentWrapper.of(1, 2, 3, 4, 5, 6).dropWhile { it < 4 })
//        assertEquals(PersistentWrapper.of(5, 6), PersistentWrapper.of(1, 2, 3, 4, 5, 6).dropWhile { it < 5 })
//        assertEquals(PersistentWrapper.of(6), PersistentWrapper.of(1, 2, 3, 4, 5, 6).dropWhile { it < 6 })
//        assertEquals(PersistentWrapper.of<Int>(), PersistentWrapper.of(1, 2, 3, 4, 5, 6).dropWhile { it < 8 })
//    }
//
////    @Test
////    fun sorted() {
////        assertInstanceOf(Cons::class.java, (CdrCodedList.of<Int>() as Cons<Int>).sorted())
////        assertInstanceOf(Cons::class.java, CdrCodedList.of<Int>().sorted())
////        assertEquals(CdrCodedList.of<Int>(), CdrCodedList.of<Int>().sorted())
////        assertInstanceOf(Cons::class.java, CdrCodedList.of(1, 2, 3).sorted())
////        assertEquals(CdrCodedList.of(1, 2, 3), CdrCodedList.of(1, 2, 3).sorted())
////        assertEquals(CdrCodedList.of(1, 2, 3), CdrCodedList.of(3, 2, 1).sorted())
////        assertEquals(CdrCodedList.of(1, 2, 3, 3), CdrCodedList.of(3, 2, 3, 1).sorted())
////    }
//
////    @Test
////    fun sortedDescending() {
////        assertInstanceOf(Cons::class.java, CdrCodedList.of<Int>().sortedDescending())
////        assertEquals(CdrCodedList.of<Int>(), CdrCodedList.of<Int>().sortedDescending())
////        assertInstanceOf(Cons::class.java, CdrCodedList.of(1, 2, 3).sortedDescending())
////        assertEquals(CdrCodedList.of(3, 2, 1), CdrCodedList.of(1, 2, 3).sortedDescending())
////        assertEquals(CdrCodedList.of(3, 2, 1), CdrCodedList.of(3, 2, 1).sortedDescending())
////        assertEquals(CdrCodedList.of(3, 3, 2, 1), CdrCodedList.of(3, 2, 3, 1).sortedDescending())
////    }
//
//    @Test
//    fun sortedBy() {
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of<Int>().sortedBy { it })
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of(1, 2, 3).sortedBy { it })
//
//        assertEquals(PersistentWrapper.of<Int>(), PersistentWrapper.of<Int>().sortedBy { it })
//        assertEquals(PersistentWrapper.of(1), PersistentWrapper.of(1).sortedBy { it })
//
//        assertEquals(PersistentWrapper.of(1, 2, 3), PersistentWrapper.of(1, 2, 3).sortedBy { it })
//        assertEquals(PersistentWrapper.of(1, 2, 3), PersistentWrapper.of(3, 1, 2).sortedBy { it })
//
//        assertEquals(PersistentWrapper.of(1, 2, 2, 3), PersistentWrapper.of(1, 2, 2, 3).sortedBy { it })
//        assertEquals(PersistentWrapper.of(1, 2, 2, 3), PersistentWrapper.of(3, 1, 2, 2).sortedBy { it })
//    }
//
//    @Test
//    fun sortedByDescending() {
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of<Int>().sortedByDescending { it })
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of(1, 2, 3).sortedByDescending { it })
//
//        assertEquals(PersistentWrapper.of<Int>(), PersistentWrapper.of<Int>().sortedByDescending { it })
//        assertEquals(PersistentWrapper.of(1), PersistentWrapper.of(1).sortedByDescending { it })
//
//        assertEquals(PersistentWrapper.of(3, 2, 1), PersistentWrapper.of(1, 2, 3).sortedByDescending { it })
//        assertEquals(PersistentWrapper.of(3, 2, 1), PersistentWrapper.of(3, 1, 2).sortedByDescending { it })
//
//        assertEquals(PersistentWrapper.of(3, 2, 2, 1), PersistentWrapper.of(1, 2, 2, 3).sortedByDescending { it })
//        assertEquals(PersistentWrapper.of(3, 2, 2, 1), PersistentWrapper.of(3, 1, 2, 2).sortedByDescending { it })
//    }
//
//    @Test
//    fun sortedWith() {
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of<Int>().sortedWith { n, m -> n.compareTo(m) })
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of(1, 2, 3).sortedWith { n, m -> n.compareTo(m) })
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of(1, 2, 3).sortedWith { n, m -> -n.compareTo(m) })
//
//        assertEquals(PersistentWrapper.of<Int>(), PersistentWrapper.of<Int>().sortedWith { n, m -> n.compareTo(m) })
//        assertEquals(PersistentWrapper.of(1), PersistentWrapper.of(1).sortedWith { n, m -> n.compareTo(m) })
//        assertEquals(PersistentWrapper.of(1), PersistentWrapper.of(1).sortedWith { n, m -> -n.compareTo(m) })
//
//        assertEquals(PersistentWrapper.of(3, 1, 2), PersistentWrapper.of(3, 1, 2).sortedWith { _, _ -> 0 })
//
//        assertEquals(PersistentWrapper.of(1, 2, 3), PersistentWrapper.of(1, 2, 3).sortedWith { n, m -> n.compareTo(m) })
//        assertEquals(PersistentWrapper.of(1, 2, 3), PersistentWrapper.of(3, 1, 2).sortedWith { n, m -> n.compareTo(m) })
//
//        assertEquals(PersistentWrapper.of(1, 2, 2, 3), PersistentWrapper.of(1, 2, 2, 3).sortedWith { n, m -> n.compareTo(m) })
//        assertEquals(PersistentWrapper.of(1, 2, 2, 3), PersistentWrapper.of(3, 1, 2, 2).sortedWith { n, m -> n.compareTo(m) })
//
//        assertEquals(PersistentWrapper.of(3, 2, 1), PersistentWrapper.of(1, 2, 3).sortedWith { n, m -> -n.compareTo(m) })
//        assertEquals(PersistentWrapper.of(3, 2, 1), PersistentWrapper.of(3, 1, 2).sortedWith { n, m -> -n.compareTo(m) })
//
//        assertEquals(PersistentWrapper.of(3, 2, 2, 1), PersistentWrapper.of(1, 2, 2, 3).sortedWith { n, m -> -n.compareTo(m) })
//        assertEquals(PersistentWrapper.of(3, 2, 2, 1), PersistentWrapper.of(3, 1, 2, 2).sortedWith { n, m -> -n.compareTo(m) })
//    }
//
//    @Test
//    fun distinct() {
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of<Int>().distinct())
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of(1, 2, 3).distinct())
//
//        assertEquals(PersistentWrapper.of<Int>(), PersistentWrapper.of<Int>().distinct())
//        assertEquals(PersistentWrapper.of(1), PersistentWrapper.of(1).distinct())
//        assertEquals(PersistentWrapper.of(1, 2, 3, 4, 5), PersistentWrapper.of(1, 2, 3, 4, 5).distinct())
//        assertEquals(PersistentWrapper.of(1), PersistentWrapper.of(1, 1, 1, 1, 1, 1).distinct())
//
//        assertEquals(PersistentWrapper.of(1, 2, 3), PersistentWrapper.of(1, 1, 2, 2, 3, 3).distinct())
//        assertEquals(PersistentWrapper.of(2, 1, 3), PersistentWrapper.of(2, 1, 1, 2, 3, 3).distinct())
//    }
//
//    @Test
//    fun shuffled() {
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of<Int>().shuffled())
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of(1, 2, 3).shuffled())
//
//        val seed = 0xDEADBEEF
//        val rand = Random(seed)
//        val temp = PersistentWrapper(1..50)
//        val tempShuffled = temp.shuffled(rand)
//        assertNotSame(temp, tempShuffled)
//
//        assertEquals(PersistentWrapper.of(1), PersistentWrapper.of(1).shuffled(rand))
//        assertEquals(PersistentWrapper.of(1, 1, 1), PersistentWrapper.of(1, 1, 1).shuffled(rand))
//        assertEquals(PersistentWrapper(1..55).shuffled(Random(seed)), PersistentWrapper(1..55).shuffled(Random(seed)))
//    }
//
//    @Test
//    fun asSequence() {
//        assertInstanceOf(Sequence::class.java, PersistentWrapper.of<Int>().asSequence())
//        assertInstanceOf(Sequence::class.java, PersistentWrapper.of(1, 2).asSequence())
//        assertInstanceOf(Sequence::class.java, PersistentWrapper.of(1, 2, 3).asSequence())
//        assertInstanceOf(Sequence::class.java, PersistentWrapper.of(1, 2, 3).asSequence().map { it + 1 })
//
//        assertEquals(listOf(1), PersistentWrapper.of(1).asSequence().toList())
//        assertEquals(listOf(1, 2), PersistentWrapper.of(1, 2).asSequence().toList())
//        assertEquals(listOf(2, 3, 4), PersistentWrapper.of(1, 2, 3).asSequence().map { it + 1 }.toList())
//    }
//
//    @Test
//    fun plusElement() {
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of<Int>() + 1)
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of(1) + 2)
//
//        assertEquals(PersistentList.of(1), PersistentWrapper.of<Int>() + 1)
//        assertEquals(PersistentList.of(1, 2), PersistentWrapper.of(1) + 2)
//    }
//
//    @Test
//    fun plusIterable() {
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of<Int>() + listOf(1))
//        assertInstanceOf(PersistentList::class.java, PersistentWrapper.of(1) + listOf(2, 3))
//
//        assertEquals(PersistentList.of<Int>(), PersistentWrapper.of<Int>() + listOf())
//        assertEquals(PersistentList.of(1, 2, 3), PersistentWrapper.of(1, 2, 3) + listOf())
//
//        assertEquals(PersistentList.of(1), PersistentWrapper.of<Int>() + listOf(1))
//        assertEquals(PersistentList.of(1, 2, 3), PersistentWrapper.of(1) + listOf(2, 3))
//
//        assertEquals(PersistentList.of(1, 2, 3, 4, 5), PersistentWrapper.of<Int>() + (1..5))
//        assertEquals(PersistentList.of(1, 2, 3, 4, 5), PersistentWrapper.of<Int>() + (1..<6))
//        assertEquals(PersistentList.of(1, 2, 3, 4, 5), PersistentWrapper.of(1) + (2..5))
//        assertEquals(PersistentList.of(1, 2, 3, 4, 5), PersistentWrapper.of(1) + sequenceOf(2, 3, 4, 5))
//
//        assertEquals(PersistentList.of<Int>(), PersistentWrapper.of<Int>() + PersistentList.of())
//        assertEquals(PersistentList.of(1, 2, 3, 4, 5), PersistentWrapper.of(1) + PersistentList.of(2, 3, 4, 5))
//        assertEquals(PersistentList.of(1, 2, 3, 4, 5), PersistentWrapper.of(1) + PersistentWrapper(listOf(2, 3, 4, 5)))
//    }
//
//    @Test
//    fun plusVList() {
//        assertInstanceOf(VList::class.java, PersistentWrapper.of<Int>() + VList.of())
//        assertEquals(VList.of<Int>(), PersistentWrapper.of<Int>() + VList.of())
//
//        assertInstanceOf(VList::class.java, PersistentWrapper.of<Int>() + VList.of(1))
//        assertEquals(VList.of(1), PersistentWrapper.of<Int>() + VList.of(1))
//
//        assertInstanceOf(VList::class.java, PersistentWrapper.of(1, 2) + VList.of(3))
//        assertEquals(VList.of(1, 2, 3), PersistentWrapper.of(1, 2) + VList.of(3))
//
//        assertInstanceOf(VList::class.java, PersistentWrapper.of(1, 2) + VList.of(3, 4))
//        assertEquals(VList.of(1, 2, 3, 4), PersistentWrapper.of(1, 2) + VList.of(3, 4))
//    }
//
//    @Test
//    fun isSingleton() {
//        assertFalse(PersistentWrapper.of<Int>().isSingleton())
//        assertTrue(PersistentWrapper.of(1).isSingleton())
//        assertFalse(PersistentWrapper.of(1, 2).isSingleton())
//    }
//}
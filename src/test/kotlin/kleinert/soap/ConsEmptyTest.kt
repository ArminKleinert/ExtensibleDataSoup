package kleinert.soap

import kleinert.soap.cons.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.random.Random

class ConsEmptyTest {

    private val instance = EmptyCons<Boolean>()
    
    @Test
    fun testConstructor() {
        assertEquals(instance, EmptyCons<Boolean>())
    }

    @Test
    fun getSize() {
        assertEquals(0, instance.size)
    }

    @Test
    fun cons() {
        assertInstanceOf(Cons::class.java, instance.cons(true))
        assertEquals(Cons.of(true), instance.cons(true))
        assertEquals(Cons.of(false, true), instance.cons(true).cons(false))
        assertEquals(Cons.of(true, false, true), instance.cons(true).cons(false).cons(true))
    }

    @Test
    fun cdr() {
        assertInstanceOf(EmptyCons::class.java, instance.cdr)
        assertTrue(instance.isEmpty())
        assertTrue(instance.cdr.isEmpty())
        assertSame(instance, instance.cdr)
    }

    @Test
    fun car() {
        assertThrows(NoSuchElementException::class.java) { instance.car }
    }

    @Test
    fun contains() {
        assertFalse(instance.contains(true))
        assertFalse(EmptyCons<String>().contains(""))
    }

    @Test
    fun containsAll() {
        assertFalse(instance.containsAll(listOf(true)))
        assertFalse(EmptyCons<String>().containsAll(listOf("")))
    }

    @Test
    fun get() {
        assertThrows(IndexOutOfBoundsException::class.java) { instance[0] }
        assertThrows(IndexOutOfBoundsException::class.java) { instance[1] }
        assertThrows(IndexOutOfBoundsException::class.java) { instance[2] }
    }

    @Test
    fun isEmpty() {
        assertTrue(instance.isEmpty())
        assertTrue(EmptyCons<String>().isEmpty())
    }

    @Test
    fun indexOf() {
        assertEquals(-1, instance.indexOf(true))
        assertEquals(-1, instance.indexOf(false))
    }

    @Test
    fun lastIndexOf() {
        assertEquals(-1, instance.lastIndexOf(true))
        assertEquals(-1, instance.lastIndexOf(false))
    }

    @Test
    fun iterator() {
        run {
            var counter = 0
            for (e in instance) {
                counter++
            }
            assertEquals(0, counter)
        }
        assertEquals(false, instance.fold(false, Boolean::or))
        assertEquals(true, instance.fold(true, Boolean::or))
        assertEquals(false, instance.foldRight(false, Boolean::or))
        assertEquals(true, instance.foldRight(true, Boolean::or))
    }

    @Test
    fun listIterator() {
        run {
            var counter = 0
            for (e in instance.listIterator()) {
                counter++
            }
            assertEquals(0, counter)
        }
    }

    @Test
    fun subList() {
        assertThrows(IndexOutOfBoundsException::class.java) { instance.subList(0, 1).size }
        assertThrows(IndexOutOfBoundsException::class.java) { instance.subList(1, 2).size }
        assertEquals(instance, instance.subList(0, 0))
    }

    @Test
    fun toMutableList() {
        assertTrue(instance.toMutableList().isEmpty())
        assertEquals(0, instance.toMutableList().size)
    }

    @Test
    fun reversed() {
        assertInstanceOf(EmptyCons::class.java,instance.reversed())
        assertEquals(instance, instance.reversed())
    }

    @Test
    fun map() {
        assertInstanceOf(Cons::class.java,instance.map { it })

        assertEquals(instance, instance.map { it })
        assertEquals(instance, instance.map { false })
    }

    @Test
    fun mapIndexed() {
        assertInstanceOf(Cons::class.java, instance.mapIndexed { _, elem -> elem })
        assertInstanceOf(Cons::class.java, CdrCodedList.of(true).mapIndexed { _, elem -> elem })

        assertEquals(instance, instance.mapIndexed { _, elem -> elem })
        assertEquals(EmptyCons<Int>(), instance.mapIndexed { i, _ -> i })
    }

    @Test
    fun filter() {
        assertInstanceOf(EmptyCons::class.java, instance.filter { true })
        assertInstanceOf(EmptyCons::class.java, instance.filter { false })

        assertEquals(instance, instance.filter { true })
        assertEquals(instance, instance.filter { it })
    }

    @Test
    fun filterNot() {
        assertInstanceOf(EmptyCons::class.java, instance.filterNot { true })
        assertInstanceOf(EmptyCons::class.java, instance.filterNot { false })

        assertEquals(instance, instance.filterNot { true })
        assertEquals(instance, instance.filterNot { it })
    }

    @Test
    fun cadr() {
        assertThrows(NoSuchElementException::class.java) { instance.cadr }
    }

    @Test
    fun caddr() {
        assertThrows(NoSuchElementException::class.java) { instance.caddr }
    }

    @Test
    fun cadddr() {
        assertThrows(NoSuchElementException::class.java) { instance.cadddr }
    }

    @Test
    fun cddr() {
        assertSame(instance, instance.cddr)
    }

    @Test
    fun cdddr() {
        assertSame(instance, instance.cdddr)
    }

    @Test
    fun cddddr() {
        assertSame(instance, instance.cddddr)
    }

    @Test
    fun flatMap() {
        assertInstanceOf(Cons::class.java, instance.flatMap { listOf(it) })
        assertEquals(instance, instance.flatMap { listOf(it) })
        assertEquals(instance, instance.flatMap { listOf(false, true) })
    }

    @Test
    fun take() {
        assertInstanceOf(Cons::class.java, instance.take(0))
        assertInstanceOf(Cons::class.java, instance.take(10))

        assertEquals(instance, instance.take(0))
        assertEquals(instance, instance.take(10))
    }

    @Test
    fun drop() {
        assertInstanceOf(Cons::class.java, instance.drop(0))
        assertInstanceOf(Cons::class.java, instance.drop(10))

        assertEquals(instance, instance.drop(0))
        assertEquals(instance, instance.drop(10))
    }

    @Test
    fun takeWhile() {
        assertInstanceOf(Cons::class.java, instance.takeWhile { true })
        assertInstanceOf(Cons::class.java, instance.takeWhile { false })
        assertInstanceOf(Cons::class.java, instance.takeWhile { it })

        assertEquals(instance, instance.takeWhile { true })
        assertEquals(instance, instance.takeWhile { false })
    }

    @Test
    fun dropWhile() {
        assertInstanceOf(Cons::class.java, instance.dropWhile { true })
        assertInstanceOf(Cons::class.java, instance.dropWhile { false })
        assertInstanceOf(Cons::class.java, instance.dropWhile { it })

        assertEquals(instance, instance.dropWhile { true })
        assertEquals(instance, instance.dropWhile { false })
    }

//    @Test
//    fun sorted() {
//        assertInstanceOf(Cons::class.java, (CdrCodedList.of<Boolean>() as Cons<Boolean>).sorted())
//        assertInstanceOf(Cons::class.java, CdrCodedList.of<Boolean>().sorted())
//        assertEquals(CdrCodedList.of<Boolean>(), CdrCodedList.of<Boolean>().sorted())
//        assertInstanceOf(Cons::class.java, CdrCodedList.of(true, false, true).sorted())
//        assertEquals(CdrCodedList.of(true, false, true), CdrCodedList.of(true, false, true).sorted())
//        assertEquals(CdrCodedList.of(true, false, true), CdrCodedList.of(true, false, true).sorted())
//        assertEquals(CdrCodedList.of(true, false, true, true), CdrCodedList.of(true, false, true, true).sorted())
//    }

//    @Test
//    fun sortedDescending() {
//        assertInstanceOf(Cons::class.java, CdrCodedList.of<Boolean>().sortedDescending())
//        assertEquals(CdrCodedList.of<Boolean>(), CdrCodedList.of<Boolean>().sortedDescending())
//        assertInstanceOf(Cons::class.java, CdrCodedList.of(true, false, true).sortedDescending())
//        assertEquals(CdrCodedList.of(true, false, true), CdrCodedList.of(true, false, true).sortedDescending())
//        assertEquals(CdrCodedList.of(true, false, true), CdrCodedList.of(true, false, true).sortedDescending())
//        assertEquals(CdrCodedList.of(true, true, false, true), CdrCodedList.of(true, false, true, true).sortedDescending())
//    }

    @Test
    fun sortedBy() {
        assertInstanceOf(Cons::class.java, EmptyCons<Int>().sortedBy { it })
        assertEquals(EmptyCons<Int>(), EmptyCons<Int>().sortedBy { it })
    }

    @Test
    fun sortedByDescending() {
        assertInstanceOf(Cons::class.java, EmptyCons<Int>().sortedByDescending { it })
        assertEquals(EmptyCons<Int>(), EmptyCons<Int>().sortedByDescending { it })
    }

    @Test
    fun sortedWith() {
        assertInstanceOf(Cons::class.java,  EmptyCons<Int>().sortedWith { n, m -> n.compareTo(m) })
        assertEquals( EmptyCons<Int>(),  EmptyCons<Int>().sortedWith { n, m -> n.compareTo(m) })
    }

    @Test
    fun distinct() {
        assertInstanceOf(Cons::class.java, instance.distinct())
        assertEquals(instance, instance.distinct())
    }

    @Test
    fun shuffled() {
        assertInstanceOf(Cons::class.java, instance.shuffled())

        val seed = 0xDEADBEEF
        val rand = Random(seed)
        assertEquals(instance, instance.shuffled(rand))
        assertEquals(instance.shuffled(rand), instance.shuffled(rand))
    }

    @Test
    fun asSequence() {
        assertInstanceOf(Sequence::class.java, instance.asSequence())
        assertEquals(listOf<Boolean>(), instance.asSequence().toList())
        assertEquals(listOf<Boolean>(), instance.asSequence().map{!it}.toList())
    }

    @Test
    fun plusElement() {
        assertInstanceOf(Cons::class.java, instance + true)
        assertEquals(Cons.of(true), instance + true)
    }

    @Test
    fun plusIterable() {
        assertInstanceOf(Cons::class.java, instance + listOf(true))

        assertEquals(instance, instance + listOf())

        assertEquals(Cons.of(true), instance + listOf(true))
        assertEquals(Cons.of(1,2,3,4,5), EmptyCons<Int>() + (1..5))

        assertEquals(instance, instance + Cons.of())
        assertEquals(Cons.of(false, true, false, true), instance + Cons.of(false, true, false, true))
        assertEquals(Cons.of(false, true, false, true), instance + CdrCodedList(listOf(false, true, false, true)))
    }

    @Test
    fun plusVList() {
        assertInstanceOf(VList::class.java, instance + VList.of())
        assertEquals(VList.of<Boolean>(), instance + VList.of())

        assertInstanceOf(VList::class.java, instance + VList.of(true))
        assertEquals(VList.of(true), instance + VList.of(true))

        assertInstanceOf(VList::class.java, instance+ VList.of(true, false, true, false))
        assertEquals(VList.of(true, false, true, false), instance + VList.of(true, false, true, false))
    }
}
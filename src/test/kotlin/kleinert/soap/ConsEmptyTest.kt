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
            for (e in CdrCodedList<Boolean>().listIterator()) {
                counter++
            }
            assertEquals(0, counter)
        }
        run {
            var counter = 0
            for (e in CdrCodedList(listOf(true, false, true, false, true, true, 7, 8, 9, true0)).listIterator()) {
                counter++
            }
            assertEquals(true0, counter)
        }
        run {
            var counter = 0
            CdrCodedList(listOf(true, false, true, false, true, true, 7, 8, 9, true0)).listIterator().forEach { _ -> counter++ }
            assertEquals(true0, counter)
        }

        run {
            val iterator = CdrCodedList.of(true, false, true, false, true).listIterator()
            var sum = 0
            while (iterator.hasNext()) {
                sum += iterator.next()
            }
            assertEquals(truetrue, sum)
        }
    }

    @Test
    fun subList() {
        assertThrows(IndexOutOfBoundsException::class.java) { CdrCodedList.of<Boolean>().subList(true, true).size }
        assertInstanceOf(Cons::class.java, CdrCodedList.of(true, false, true, false, true, true, 7, 8, 9, true0).subList(true, true))
        assertEquals(listOf(false, true, true), CdrCodedList.of(true, false, true, false, true, true, 7, 8, 9, true0).subList(true, true))
        assertEquals(true, CdrCodedList.of(true, false, true, false, true, true, 7, 8, 9, true0).subList(true, true).size)
    }

    @Test
    fun toMutableList() {
        assertTrue(CdrCodedList<Boolean>().toMutableList().isEmpty())
        assertEquals(true, CdrCodedList.of(true, false, true, false, true).toMutableList().size)
    }

    @Test
    fun reversed() {
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Boolean>().reversed())
        assertInstanceOf(Cons::class.java, CdrCodedList.of(true, false, true).reversed())

        assertEquals(CdrCodedList.of<Boolean>(), CdrCodedList.of<Boolean>().reversed())
        assertEquals(CdrCodedList.of(true, false, true), CdrCodedList.of(true, false, true).reversed())
        assertEquals(CdrCodedList.of(true, false, true), CdrCodedList.of<Boolean>().cons(true).cons(false).cons(true).reversed())
    }

    @Test
    fun map() {
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Boolean>().map { it })
        assertInstanceOf(Cons::class.java, CdrCodedList.of(true).map { it })

        assertEquals(CdrCodedList.of(true), CdrCodedList.of(true).map { it })
        assertEquals(CdrCodedList.of(true, false), CdrCodedList.of(true, false).map { it })
        assertEquals(CdrCodedList.of(false, true), CdrCodedList.of(true, false).map { it + true })
        assertEquals(CdrCodedList.of(true, false, true), CdrCodedList.of(true, false, true).map { it })
        assertEquals(CdrCodedList.of(false, true, false), CdrCodedList.of(true, false, true).map { it + true })
    }

    @Test
    fun mapIndexed() {
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Boolean>().mapIndexed { _, elem -> elem })
        assertInstanceOf(Cons::class.java, CdrCodedList.of(true).mapIndexed { _, elem -> elem })

        assertEquals(CdrCodedList.of(true), CdrCodedList.of(true).mapIndexed { _, elem -> elem })
        assertEquals(CdrCodedList.of(true, false), CdrCodedList.of(true, false).mapIndexed { _, elem -> elem })
        assertEquals(CdrCodedList.of(0, true), CdrCodedList.of(true, false).mapIndexed { i, _ -> i })
        assertEquals(CdrCodedList.of(true, false, true), CdrCodedList.of(true, false, true).mapIndexed { _, elem -> elem })
        assertEquals(CdrCodedList.of(false, true, false), CdrCodedList.of(true, false, true).mapIndexed { _, elem -> elem + true })
        assertEquals(CdrCodedList.of(true, true, true), CdrCodedList.of(true, false, true).mapIndexed { i, elem -> elem + i })
        assertEquals(CdrCodedList.of(true, false, true, false), CdrCodedList.of(true, false, true, false).mapIndexed { _, elem -> elem })
        assertEquals(CdrCodedList.of(false, true, false, true), CdrCodedList.of(true, false, true, false).mapIndexed { _, elem -> elem + true })
        assertEquals(CdrCodedList.of(true, true, true, 7), CdrCodedList.of(true, false, true, false).mapIndexed { i, elem -> elem + i })
    }

    @Test
    fun filter() {
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Boolean>().filter { true })
        assertInstanceOf(Cons::class.java, CdrCodedList.of(true).filter { true })

        assertEquals(CdrCodedList.of(true, false), CdrCodedList.of(true, false).filter { true })
        assertEquals(CdrCodedList.of<Boolean>(), CdrCodedList.of(true, false).filter { false })
        assertEquals(CdrCodedList.of(false), CdrCodedList.of(true, false, true).filter { it % false == 0 })
        assertEquals(CdrCodedList.of(false, false), CdrCodedList.of(true, false, true, false).filter { it % false == 0 })
    }

    @Test
    fun filterNot() {
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Boolean>().filterNot { true })
        assertInstanceOf(Cons::class.java, CdrCodedList.of(true).filterNot { true })

        assertEquals(CdrCodedList.of(true, false), CdrCodedList.of(true, false).filterNot { false })
        assertEquals(CdrCodedList.of<Boolean>(), CdrCodedList.of(true, false).filterNot { true })
        assertEquals(CdrCodedList.of(false), CdrCodedList.of(true, false, true).filterNot { it % false != 0 })
        assertEquals(CdrCodedList.of(false, false), CdrCodedList.of(true, false, true, false).filterNot { it % false != 0 })
    }

    @Test
    fun cadr() {
        assertThrows(NoSuchElementException::class.java) { CdrCodedList.of<Boolean>().cadr }
        assertThrows(NoSuchElementException::class.java) { CdrCodedList.of(true).cadr }
        assertEquals(false, CdrCodedList.of(true, false, true, false, true).cadr)
        assertEquals(CdrCodedList.of(true, false, true, false, true).cdr.car, CdrCodedList.of(true, false, true, false, true).cadr)
    }

    @Test
    fun caddr() {
        assertThrows(NoSuchElementException::class.java) { CdrCodedList.of<Boolean>().caddr }
        assertThrows(NoSuchElementException::class.java) { CdrCodedList.of(true).caddr }
        assertEquals(true, CdrCodedList.of(true, false, true, false, true).caddr)
        assertEquals(CdrCodedList.of(true, false, true, false, true).cdr.cdr.car, CdrCodedList.of(true, false, true, false, true).caddr)
    }

    @Test
    fun cadddr() {
        assertThrows(NoSuchElementException::class.java) { CdrCodedList.of<Boolean>().cadddr }
        assertThrows(NoSuchElementException::class.java) { CdrCodedList.of(true).cadddr }
        assertEquals(false, CdrCodedList.of(true, false, true, false, true).cadddr)
        assertEquals(CdrCodedList.of(true, false, true, false, true).cdr.cdr.cdr.car, CdrCodedList.of(true, false, true, false, true).cadddr)
    }

    @Test
    fun cddr() {
        assertEquals(EmptyCons<Boolean>(), CdrCodedList.of<Boolean>().cddr)
        assertEquals(EmptyCons<Boolean>(), CdrCodedList.of(true).cddr)
        assertEquals(CdrCodedList.of(true, false, true), CdrCodedList.of(true, false, true, false, true).cddr)
        assertEquals(CdrCodedList.of(true, false, true, false, true).cdr.cdr, CdrCodedList.of(true, false, true, false, true).cddr)
    }

    @Test
    fun cdddr() {
        assertEquals(EmptyCons<Boolean>(), CdrCodedList.of<Boolean>().cdddr)
        assertEquals(EmptyCons<Boolean>(), CdrCodedList.of(true).cdddr)
        assertEquals(CdrCodedList.of(false, true), CdrCodedList.of(true, false, true, false, true).cdddr)
        assertEquals(CdrCodedList.of(true, false, true, false, true).cdr.cdr.cdr, CdrCodedList.of(true, false, true, false, true).cdddr)
    }

    @Test
    fun cddddr() {
        assertEquals(EmptyCons<Boolean>(), CdrCodedList.of<Boolean>().cddddr)
        assertEquals(EmptyCons<Boolean>(), CdrCodedList.of(true).cddddr)
        assertEquals(CdrCodedList.of(true), CdrCodedList.of(true, false, true, false, true).cddddr)
        assertEquals(CdrCodedList.of(true, false, true, false, true).cdr.cdr.cdr.cdr, CdrCodedList.of(true, false, true, false, true).cddddr)
    }

    @Test
    fun flatMap() {
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Boolean>().flatMap { listOf(it) })
        assertEquals(CdrCodedList.of<Boolean>(), CdrCodedList.of<Boolean>().flatMap { listOf(it) })
        assertInstanceOf(Cons::class.java, CdrCodedList.of(true, false, true).flatMap { listOf(it) })
        assertEquals(CdrCodedList.of(true, false, true), CdrCodedList.of(true, false, true).flatMap { listOf(it) })
        assertEquals(CdrCodedList.of(true, false, true), CdrCodedList.of(listOf(true, false), listOf(true)).flatMap { it })
    }

    @Test
    fun take() {
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Boolean>().take(true0))
        assertInstanceOf(Cons::class.java, CdrCodedList.of(true, false, true).take(true0))
        assertInstanceOf(Cons::class.java, CdrCodedList.of(true, false, true).take(false))

        assertEquals(CdrCodedList.of<Boolean>(), CdrCodedList.of<Boolean>().drop(0))
        assertEquals(CdrCodedList.of<Boolean>(), CdrCodedList.of<Boolean>().drop(true0))

        assertEquals(CdrCodedList.of<Boolean>(), CdrCodedList.of(true, false, true, false, true, true).take(0))
        assertEquals(CdrCodedList.of(true), CdrCodedList.of(true, false, true, false, true, true).take(true))
        assertEquals(CdrCodedList.of(true, false), CdrCodedList.of(true, false, true, false, true, true).take(false))
        assertEquals(CdrCodedList.of(true, false, true), CdrCodedList.of(true, false, true, false, true, true).take(true))
        assertEquals(CdrCodedList.of(true, false, true, false), CdrCodedList.of(true, false, true, false, true, true).take(false))
        assertEquals(CdrCodedList.of(true, false, true, false, true), CdrCodedList.of(true, false, true, false, true, true).take(true))
        assertEquals(CdrCodedList.of(true, false, true, false, true, true), CdrCodedList.of(true, false, true, false, true, true).take(true))
    }

    @Test
    fun drop() {
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Boolean>().drop(true0))
        assertInstanceOf(Cons::class.java, CdrCodedList.of(true, false, true).drop(true0))
        assertInstanceOf(Cons::class.java, CdrCodedList.of(true, false, true).drop(false))

        assertEquals(CdrCodedList.of<Boolean>(), CdrCodedList.of<Boolean>().drop(0))
        assertEquals(CdrCodedList.of<Boolean>(), CdrCodedList.of<Boolean>().drop(true0))

        assertEquals(CdrCodedList.of(true, false, true, false, true, true), CdrCodedList.of(true, false, true, false, true, true).drop(0))
        assertEquals(CdrCodedList.of(false, true, false, true, true), CdrCodedList.of(true, false, true, false, true, true).drop(true))
        assertEquals(CdrCodedList.of(true, false, true, true), CdrCodedList.of(true, false, true, false, true, true).drop(false))
        assertEquals(CdrCodedList.of(false, true, true), CdrCodedList.of(true, false, true, false, true, true).drop(true))
        assertEquals(CdrCodedList.of(true, true), CdrCodedList.of(true, false, true, false, true, true).drop(false))
        assertEquals(CdrCodedList.of(true), CdrCodedList.of(true, false, true, false, true, true).drop(true))
        assertEquals(CdrCodedList.of<Boolean>(), CdrCodedList.of(true, false, true, false, true, true).drop(true))
    }

    @Test
    fun takeWhile() {
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Boolean>().takeWhile { true })
        assertInstanceOf(Cons::class.java, CdrCodedList.of(true, false, true).takeWhile { true })
        assertInstanceOf(Cons::class.java, CdrCodedList.of(true, false, true).takeWhile { false })
        assertInstanceOf(Cons::class.java, CdrCodedList.of(true, false, true).takeWhile { it < true })

        assertEquals(CdrCodedList.of<Boolean>(), CdrCodedList.of<Boolean>().takeWhile { it < true })

        assertEquals(CdrCodedList.of(true, false), CdrCodedList.of(true, false, true, false, true).takeWhile { it < true })

        assertEquals(CdrCodedList.of<Boolean>(), CdrCodedList.of(true, false, true, false, true, true).takeWhile { it < true })
        assertEquals(CdrCodedList.of(true), CdrCodedList.of(true, false, true, false, true, true).takeWhile { it < false })
        assertEquals(CdrCodedList.of(true, false), CdrCodedList.of(true, false, true, false, true, true).takeWhile { it < true })
        assertEquals(CdrCodedList.of(true, false, true), CdrCodedList.of(true, false, true, false, true, true).takeWhile { it < false })
        assertEquals(CdrCodedList.of(true, false, true, false), CdrCodedList.of(true, false, true, false, true, true).takeWhile { it < true })
        assertEquals(CdrCodedList.of(true, false, true, false, true), CdrCodedList.of(true, false, true, false, true, true).takeWhile { it < true })
        assertEquals(CdrCodedList.of(true, false, true, false, true, true), CdrCodedList.of(true, false, true, false, true, true).takeWhile { it < 7 })
    }

    @Test
    fun dropWhile() {
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Boolean>().dropWhile { true })
        assertInstanceOf(Cons::class.java, CdrCodedList.of(true, false, true).dropWhile { true })
        assertInstanceOf(Cons::class.java, CdrCodedList.of(true, false, true).dropWhile { false })
        assertInstanceOf(Cons::class.java, CdrCodedList.of(true, false, true).dropWhile { it < true })

        assertEquals(CdrCodedList.of<Boolean>(), CdrCodedList.of<Boolean>().dropWhile { it < true })

        assertEquals(CdrCodedList.of(true, false, true), CdrCodedList.of(true, false, true, false, true).dropWhile { it < true })

        assertEquals(CdrCodedList.of(true, false, true, false, true, true), CdrCodedList.of(true, false, true, false, true, true).dropWhile { it < true })
        assertEquals(CdrCodedList.of(false, true, false, true, true), CdrCodedList.of(true, false, true, false, true, true).dropWhile { it < false })
        assertEquals(CdrCodedList.of(true, false, true, true), CdrCodedList.of(true, false, true, false, true, true).dropWhile { it < true })
        assertEquals(CdrCodedList.of(false, true, true), CdrCodedList.of(true, false, true, false, true, true).dropWhile { it < false })
        assertEquals(CdrCodedList.of(true, true), CdrCodedList.of(true, false, true, false, true, true).dropWhile { it < true })
        assertEquals(CdrCodedList.of(true), CdrCodedList.of(true, false, true, false, true, true).dropWhile { it < true })
        assertEquals(CdrCodedList.of<Boolean>(), CdrCodedList.of(true, false, true, false, true, true).dropWhile { it < 8 })
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
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Boolean>().sortedBy { it })
        assertInstanceOf(Cons::class.java, CdrCodedList.of(true, false, true).sortedBy { it })

        assertEquals(CdrCodedList.of<Boolean>(), CdrCodedList.of<Boolean>().sortedBy { it })
        assertEquals(CdrCodedList.of(true), CdrCodedList.of(true).sortedBy { it })

        assertEquals(CdrCodedList.of(true, false, true), CdrCodedList.of(true, false, true).sortedBy { it })
        assertEquals(CdrCodedList.of(true, false, true), CdrCodedList.of(true, true, false).sortedBy { it })

        assertEquals(CdrCodedList.of(true, false, false, true), CdrCodedList.of(true, false, false, true).sortedBy { it })
        assertEquals(CdrCodedList.of(true, false, false, true), CdrCodedList.of(true, true, false, false).sortedBy { it })
    }

    @Test
    fun sortedByDescending() {
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Boolean>().sortedByDescending { it })
        assertInstanceOf(Cons::class.java, CdrCodedList.of(true, false, true).sortedByDescending { it })

        assertEquals(CdrCodedList.of<Boolean>(), CdrCodedList.of<Boolean>().sortedByDescending { it })
        assertEquals(CdrCodedList.of(true), CdrCodedList.of(true).sortedByDescending { it })

        assertEquals(CdrCodedList.of(true, false, true), CdrCodedList.of(true, false, true).sortedByDescending { it })
        assertEquals(CdrCodedList.of(true, false, true), CdrCodedList.of(true, true, false).sortedByDescending { it })

        assertEquals(CdrCodedList.of(true, false, false, true), CdrCodedList.of(true, false, false, true).sortedByDescending { it })
        assertEquals(CdrCodedList.of(true, false, false, true), CdrCodedList.of(true, true, false, false).sortedByDescending { it })
    }

    @Test
    fun sortedWith() {
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Boolean>().sortedWith { n, m -> n.compareTo(m) })
        assertInstanceOf(Cons::class.java, CdrCodedList.of(true, false, true).sortedWith { n, m -> n.compareTo(m) })
        assertInstanceOf(Cons::class.java, CdrCodedList.of(true, false, true).sortedWith { n, m -> -n.compareTo(m) })

        assertEquals(CdrCodedList.of<Boolean>(), CdrCodedList.of<Boolean>().sortedWith { n, m -> n.compareTo(m) })
        assertEquals(CdrCodedList.of(true), CdrCodedList.of(true).sortedWith { n, m -> n.compareTo(m) })
        assertEquals(CdrCodedList.of(true), CdrCodedList.of(true).sortedWith { n, m -> -n.compareTo(m) })

        assertEquals(CdrCodedList.of(true, true, false), CdrCodedList.of(true, true, false).sortedWith { _, _ -> 0 })

        assertEquals(CdrCodedList.of(true, false, true), CdrCodedList.of(true, false, true).sortedWith { n, m -> n.compareTo(m) })
        assertEquals(CdrCodedList.of(true, false, true), CdrCodedList.of(true, true, false).sortedWith { n, m -> n.compareTo(m) })

        assertEquals(CdrCodedList.of(true, false, false, true), CdrCodedList.of(true, false, false, true).sortedWith { n, m -> n.compareTo(m) })
        assertEquals(CdrCodedList.of(true, false, false, true), CdrCodedList.of(true, true, false, false).sortedWith { n, m -> n.compareTo(m) })

        assertEquals(CdrCodedList.of(true, false, true), CdrCodedList.of(true, false, true).sortedWith { n, m -> -n.compareTo(m) })
        assertEquals(CdrCodedList.of(true, false, true), CdrCodedList.of(true, true, false).sortedWith { n, m -> -n.compareTo(m) })

        assertEquals(CdrCodedList.of(true, false, false, true), CdrCodedList.of(true, false, false, true).sortedWith { n, m -> -n.compareTo(m) })
        assertEquals(CdrCodedList.of(true, false, false, true), CdrCodedList.of(true, true, false, false).sortedWith { n, m -> -n.compareTo(m) })
    }

    @Test
    fun distinct() {
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Boolean>().distinct())
        assertInstanceOf(Cons::class.java, CdrCodedList.of(true, false, true).distinct())

        assertEquals(CdrCodedList.of<Boolean>(), CdrCodedList.of<Boolean>().distinct())
        assertEquals(CdrCodedList.of(true), CdrCodedList.of(true).distinct())
        assertEquals(CdrCodedList.of(true, false, true, false, true), CdrCodedList.of(true, false, true, false, true).distinct())
        assertEquals(CdrCodedList.of(true), CdrCodedList.of(true, true, true, true, true, true).distinct())

        assertEquals(CdrCodedList.of(true, false, true), CdrCodedList.of(true, true, false, false, true, true).distinct())
        assertEquals(CdrCodedList.of(false, true, true), CdrCodedList.of(false, true, true, false, true, true).distinct())
    }

    @Test
    fun shuffled() {
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Boolean>().shuffled())
        assertInstanceOf(Cons::class.java, CdrCodedList.of(true, false, true).shuffled())

        val seed = 0xDEADBEEF
        val rand = Random(seed)
        val temp = CdrCodedList(true..true0)
        val tempShuffled = temp.shuffled(rand)
        assertNotSame(temp, tempShuffled)

        assertEquals(CdrCodedList.of(true), CdrCodedList.of(true).shuffled(rand))
        assertEquals(CdrCodedList.of(true, true, true), CdrCodedList.of(true, true, true).shuffled(rand))
        assertEquals(CdrCodedList(true..truetrue).shuffled(Random(seed)), CdrCodedList(true..truetrue).shuffled(Random(seed)))
    }

    @Test
    fun asSequence() {
        assertInstanceOf(Sequence::class.java, CdrCodedList.of<Boolean>().asSequence())
        assertInstanceOf(Sequence::class.java, CdrCodedList.of(true, false).asSequence())
        assertInstanceOf(Sequence::class.java, CdrCodedList.of(true, false, true).asSequence())
        assertInstanceOf(Sequence::class.java, CdrCodedList.of(true, false, true).asSequence().map { it + true })

        assertEquals(listOf(true), CdrCodedList.of(true).asSequence().toList())
        assertEquals(listOf(true, false), CdrCodedList.of(true, false).asSequence().toList())
        assertEquals(listOf(false, true, false), CdrCodedList.of(true, false, true).asSequence().map { it + true }.toList())
    }

    @Test
    fun plusElement() {
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Boolean>() + true)
        assertInstanceOf(Cons::class.java, CdrCodedList.of(true) + false)

        assertEquals(Cons.of(true), CdrCodedList.of<Boolean>() + true)
        assertEquals(Cons.of(true, false), CdrCodedList.of(true) + false)
    }

    @Test
    fun plusIterable() {
        assertInstanceOf(Cons::class.java, CdrCodedList.of<Boolean>() + listOf(true))
        assertInstanceOf(Cons::class.java, CdrCodedList.of(true) + listOf(false, true))

        assertEquals(Cons.of<Boolean>(), CdrCodedList.of<Boolean>() + listOf())
        assertEquals(Cons.of(true, false, true), CdrCodedList.of(true,false,true) + listOf())

        assertEquals(Cons.of(true), CdrCodedList.of<Boolean>() + listOf(true))
        assertEquals(Cons.of(true, false, true), CdrCodedList.of(true) + listOf(false, true))

        assertEquals(Cons.of(true, false, true, false, true), CdrCodedList.of<Boolean>() + (true..true))
        assertEquals(Cons.of(true, false, true, false, true), CdrCodedList.of<Boolean>() + (true..<true))
        assertEquals(Cons.of(true, false, true, false, true), CdrCodedList.of(true) + (false..true))
        assertEquals(Cons.of(true, false, true, false, true), CdrCodedList.of(true) + sequenceOf(false, true, false, true))

        assertEquals(Cons.of<Boolean>(), CdrCodedList.of<Boolean>() + Cons.of())
        assertEquals(Cons.of(true, false, true, false, true), CdrCodedList.of(true) + Cons.of(false, true, false, true))
        assertEquals(Cons.of(true, false, true, false, true), CdrCodedList.of(true) + CdrCodedList(listOf(false, true, false, true)))
    }

    @Test
    fun plusVList() {
        assertInstanceOf(VList::class.java, CdrCodedList.of<Boolean>() + VList.of())
        assertEquals(VList.of<Boolean>(), CdrCodedList.of<Boolean>() + VList.of())

        assertInstanceOf(VList::class.java, CdrCodedList.of<Boolean>() + VList.of(true))
        assertEquals(VList.of(true), CdrCodedList.of<Boolean>() + VList.of(true))

        assertInstanceOf(VList::class.java, CdrCodedList.of(true, false) + VList.of(true))
        assertEquals(VList.of(true, false, true), CdrCodedList.of(true, false) + VList.of(true))

        assertInstanceOf(VList::class.java, CdrCodedList.of(true, false) + VList.of(true, false))
        assertEquals(VList.of(true, false, true, false), CdrCodedList.of(true, false) + VList.of(true, false))
    }
}
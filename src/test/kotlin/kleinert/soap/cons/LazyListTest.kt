package kleinert.soap.cons

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.random.nextInt

class LazyListTest {

    @Test
    fun testConstructor() {
        run {
            val ll = LazyList.of<Int>()
            assertTrue(ll.isEmpty())
            assertTrue(ll.isLazyType())
        }
        run {
            val ll = LazyList.of(1)
            assertTrue(ll.isSingleton())
        }
        run {
            val ll = LazyList.of(1, 2, 3, 2, 1, 4)
            assertEquals(listOf(1, 2, 3, 2, 1, 4), ll)
            assertEquals(LazyList.of(1, 2, 3, 2, 1, 4), ll)
        }
        run {
            val ll = LazyList.of(1, 2, 3, 2, 1, 4)
            assertEquals(listOf(1, 2, 3, 2, 1, 4), ll)
            val ll2 = ll
            assertEquals(LazyList.of(1, 2, 3, 2, 1, 4), ll2)
        }
    }

    @Test
    fun take() {
        run {
            val ll = LazyList.of<Int>().take(15)
            assertTrue(ll.isEmpty())
        }
        run {
            val ll = LazyList.of(1, 2, 3, 2, 1, 4)
            assertEquals(listOf<Int>(), ll.take(0))
            assertEquals(listOf<Int>(1,2,3), ll.take(3))
            assertEquals(listOf<Int>(1,2,3,2,1,4), ll.take(6))
            assertEquals(listOf<Int>(1,2,3,2,1,4), ll.take(128))
        }
    }

    @Test
    fun drop() {
        run {
            val ll = LazyList.of<Int>().drop(15)
            assertTrue(ll.isEmpty())
        }
        run {
            val ll = LazyList.of(1, 2, 3, 2, 1, 4)
            assertEquals(listOf<Int>(1,2,3,2,1,4), ll.drop(0))
            assertEquals(listOf<Int>(2,1,4), ll.drop(3))
            assertEquals(listOf<Int>(), ll.drop(6))
            assertEquals(listOf<Int>(), ll.drop(128))
        }
    }

    @Test
    fun map() {
        run {
            val ll = LazyList.of<Int>()
            val ll2 = ll.map { it + 1 }
            assertNotSame(ll, ll2)
            assertTrue(ll.isEmpty())
            assertTrue(ll.isLazyType())
        }
        run {
            val ll = LazyList.of(1, 2, 3, 2, 1, 4)
            assertEquals(listOf(2, 3, 4, 3, 2, 5), ll.map{it+1})
        }
    }

    @Test
    fun filter() {
        run {
            val ll = LazyList.of<Int>()
            val ll2 = ll.filter { it > 1 }
            assertEquals(ll, ll2)
            assertTrue(ll.isEmpty())
            assertTrue(ll.isLazyType())
        }
        run {
            val ll = LazyList.of(1,2,3,2,1,4)
            val ll2 = ll.filter { it > 1 }
            assertNotSame(ll, ll2)
            assertEquals(listOf(2,3,2,4),ll2)
        }
        run {
            val ll = LazyList.of(1,2,3,2,1,4)
            val ll2 = ll.filter {false}
            assertNotSame(ll, ll2)
            assertTrue(ll2.isEmpty())
            assertEquals(listOf<Int>(),ll2)
        }
    }

    @Test
    fun repeat() {
        run {
            val ll = LazyList.repeat(1)
            assertTrue(ll.isLazyType()) // Must not hang
            assertTrue(ll.isNotEmpty()) // Must not hang
            assertEquals(listOf(1,1,1,1,1,1), ll.take(6))
            assertEquals(listOf(1,1,1,1,1,1,1,1,1,1,1,1), ll.take(12))
        }
    }

    @Test
    fun repeatedly() {
        run {
            val ll = LazyList.repeatedly { 1 }
            assertTrue(ll.isLazyType()) // Must not hang
            assertTrue(ll.isNotEmpty()) // Must not hang
            assertEquals(listOf(1,1,1,1,1,1), ll.take(6))
        }
        run {
            val rand = Random(0xDEADBEEF)
                val ll = LazyList.repeatedly { rand.nextInt() }
            assertTrue(ll.isLazyType()) // Must not hang
            assertTrue(ll.isNotEmpty()) // Must not hang
            assertEquals(6, ll.take(6).size)
        }
    }

    @Test
    fun splitAt() {
        run { val ll = LazyList.of<Int>()
            require(ll is LazyList<Int>)
            val (head, tail) = ll.splitAt(5)
            assertEquals(listOf<Int>(), head)
            assertEquals(listOf<Int>(), head.take(8))
        }
        run { val ll = LazyList.repeat(1)
            require(ll is LazyList<Int>)
            val (head, tail) = ll.splitAt(5)
            assertEquals(listOf(1,1,1,1,1), head)
            assertEquals(listOf(1,1,1,1,1,1,1,1), tail.take(8))
        }
        run { val ll = LazyList.of<Int>()
            require(ll is LazyList<Int>)
            val (head, tail) = ll.splitAt(5)
            assertTrue(head.isEmpty())
            assertEquals(listOf<Int>(), head)
            assertTrue(tail.isEmpty())
            assertEquals(listOf<Int>(), tail.take(8))
        }
        run { val ll = LazyList.of(1,2,3)
            require(ll is LazyList<Int>)
            val (head, tail) = ll.splitAt(0)
            assertEquals(listOf<Int>(), head)
            assertEquals(listOf(1,2,3), tail)
        }
    }

    @Test
    fun cycle() {
        run { val ll = LazyList.of<Int>()
            assertTrue(ll.cycle().isEmpty()) // Tests constraint: list.cycle() == [] <=> list == []
        }
        run { val ll = LazyList.of(1,2,3)
            val cycled = ll.cycle()
            assertEquals(listOf(1,2,3,1,2,3), cycled.take(6))
        }
        run { val ll = LazyList.iterate({it+1}, 1)
            val cycled = ll.cycle() // If the list is infinite and never repeats, then list.cycle() still never repeats.
            assertEquals(listOf(1,2,3,4,5,6,7,8,9,10), cycled.take(10))
        }
    }
}
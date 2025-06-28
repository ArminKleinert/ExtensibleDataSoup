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
            assertEquals(listOf<Int>(1, 2, 3), ll.take(3))
            assertEquals(listOf<Int>(1, 2, 3, 2, 1, 4), ll.take(6))
            assertEquals(listOf<Int>(1, 2, 3, 2, 1, 4), ll.take(128))
        }
    }

    @Test
    fun takeWhile() {
        run {
            val ll = LazyList.of<Int>().takeWhile { false }
            assertTrue(ll.isEmpty())
        }
        run {
            val ll = LazyList.of(1, 2, 3, 2, 1, 4, 4)
            assertEquals(listOf<Int>(), ll.takeWhile { false })
            assertEquals(listOf(1, 2), ll.takeWhile { it < 3 })
            assertEquals(listOf(1, 2, 3, 2, 1, 4, 4), ll.takeWhile { it < 5 })
            assertEquals(listOf(1, 2, 3, 2, 1, 4, 4), ll.takeWhile { true })
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
            assertEquals(listOf<Int>(1, 2, 3, 2, 1, 4), ll.drop(0))
            assertEquals(listOf<Int>(2, 1, 4), ll.drop(3))
            assertEquals(listOf<Int>(), ll.drop(6))
            assertEquals(listOf<Int>(), ll.drop(128))
        }
    }

    @Test
    fun dropWhile() {
        run {
            val ll = LazyList.of<Int>().dropWhile { false }
            assertTrue(ll.isEmpty())
        }
        run {
            val ll = LazyList.of(1, 2, 3, 2, 1, 4)
            assertEquals(listOf<Int>(1, 2, 3, 2, 1, 4), ll.dropWhile { false })
            assertEquals(listOf<Int>(4), ll.dropWhile { it <= 3 })
            assertEquals(listOf<Int>(), ll.dropWhile { true })
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
            assertEquals(listOf(2, 3, 4, 3, 2, 5), ll.map { it + 1 })
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
            val ll = LazyList.of(1, 2, 3, 2, 1, 4)
            val ll2 = ll.filter { it > 1 }
            assertNotSame(ll, ll2)
            assertEquals(listOf(2, 3, 2, 4), ll2)
        }
        run {
            val ll = LazyList.of(1, 2, 3, 2, 1, 4)
            val ll2 = ll.filter { false }
            assertNotSame(ll, ll2)
            assertTrue(ll2.isEmpty())
            assertEquals(listOf<Int>(), ll2)
        }
    }

    @Test
    fun filterNot() {
        run {
            val ll = LazyList.of<Int>()
            val ll2 = ll.filterNot { it > 1 }
            assertEquals(ll, ll2)
            assertTrue(ll.isEmpty())
            assertTrue(ll.isLazyType())
        }
        run {
            val ll = LazyList.of(1, 2, 3, 2, 1, 4)
            assertInstanceOf(LazyList::class.java, ll)
            val ll2 = ll.filterNot { it <= 1 }
            assertNotSame(ll, ll2)
            assertEquals(listOf(2, 3, 2, 4), ll2)
        }
        run {
            val ll = LazyList.of(1, 2, 3, 2, 1, 4)
            val ll2 = ll.filterNot { true }
            assertNotSame(ll, ll2)
            assertTrue(ll2.isEmpty())
            assertEquals(listOf<Int>(), ll2)
        }
    }

    @Test
    fun filterNotNull() {
        run {
            val ll = LazyList.of<Int?>()
            assertEquals(ll, ll.filterNotNull())
        }
        run {
            val ll = LazyList.of<Int?>(1, 2, 3)
            assertEquals(ll, ll.filterNotNull())
        }
        run {
            val ll = LazyList.of<Int?>(null)
            assertEquals(listOf<Any?>(), ll.filterNotNull())
        }
        run {
            val ll = LazyList.of<Int?>(1, null, 2)
            assertEquals(listOf<Int>(1, 2), ll.filterNotNull())
        }
    }

    @Test
    fun repeat() {
        run {
            val ll = LazyList.repeat(1)
            assertTrue(ll.isLazyType()) // Must not hang
            assertTrue(ll.isNotEmpty()) // Must not hang
            assertEquals(listOf(1, 1, 1, 1, 1, 1), ll.take(6))
            assertEquals(listOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), ll.take(12))
        }
    }

    @Test
    fun repeatedly() {
        run {
            val ll = LazyList.repeatedly { 1 }
            assertTrue(ll.isLazyType()) // Must not hang
            assertTrue(ll.isNotEmpty()) // Must not hang
            assertEquals(listOf(1, 1, 1, 1, 1, 1), ll.take(6))
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
        run {
            val ll = LazyList.of<Int>()
            require(ll is LazyList<Int>)
            val (head, tail) = ll.splitAt(5)
            assertEquals(listOf<Int>(), head)
            assertEquals(listOf<Int>(), head.take(8))
        }
        run {
            val ll = LazyList.repeat(1)
            require(ll is LazyList<Int>)
            val (head, tail) = ll.splitAt(5)
            assertEquals(listOf(1, 1, 1, 1, 1), head)
            assertEquals(listOf(1, 1, 1, 1, 1, 1, 1, 1), tail.take(8))
        }
        run {
            val ll = LazyList.of<Int>()
            require(ll is LazyList<Int>)
            val (head, tail) = ll.splitAt(5)
            assertTrue(head.isEmpty())
            assertEquals(listOf<Int>(), head)
            assertTrue(tail.isEmpty())
            assertEquals(listOf<Int>(), tail.take(8))
        }
        run {
            val ll = LazyList.of(1, 2, 3)
            require(ll is LazyList<Int>)
            val (head, tail) = ll.splitAt(0)
            assertEquals(listOf<Int>(), head)
            assertEquals(listOf(1, 2, 3), tail)
        }
    }

    @Test
    fun cycle() {
        run {
            val ll = LazyList.of<Int>()
            assertTrue(ll.cycle().isEmpty()) // Tests constraint: list.cycle() == [] <=> list == []
        }
        run {
            val ll = LazyList.of(1, 2, 3)
            val cycled = ll.cycle()
            assertEquals(listOf(1, 2, 3, 1, 2, 3), cycled.take(6))
        }
        run {
            val ll = LazyList.iterate({ it + 1 }, 1)
            val cycled = ll.cycle() // If the list is infinite and never repeats, then list.cycle() still never repeats.
            assertEquals(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), cycled.take(10))
        }
    }

    @Test
    fun distinct() {
        run {
            val ll = LazyList.of<Int>().distinct()
            assertTrue(ll.isEmpty())
        }
        run {
            val ll = LazyList.of(1).distinct()
            assertEquals(listOf(1), ll)
        }
        run {
            val ll = LazyList.of(1, 2, 3)
            assertEquals(ll, ll.distinct())
        }
        run {
            val ll = LazyList.of(1, 2, 3, 2, 1, 4)
            assertEquals(listOf(1, 2, 3, 4), ll.distinct())
        }
    }

    @Test
    fun flatMap() {
        assertEquals(LazyList.of<Int>(), LazyList.of<Int>().flatMap { listOf(it) })
        assertEquals(LazyList.of(1, 2, 3), LazyList.of(1, 2, 3).flatMap { listOf(it) })
        assertEquals(LazyList.of(1, 2, 3), LazyList.of(listOf(1, 2), listOf(3)).flatMap { it })
    }

    @Test
    fun flatMapIndexed() {
        assertEquals(LazyList.of<Int>(), LazyList.of<Int>().flatMapIndexed { _, it -> listOf(it) })
        assertEquals(LazyList.of(1, 2, 3), LazyList.of(1, 2, 3).flatMapIndexed { _, it -> listOf(it) })
        assertEquals(LazyList.of(1, 3, 5), LazyList.of(1, 2, 3).flatMapIndexed { i, it -> listOf(i + it) })
        assertEquals(LazyList.of(1, 2, 3), LazyList.of(listOf(1, 2), listOf(3)).flatMapIndexed { _, it -> it })
    }

    @Test
    fun mapNotNull() {
        assertEquals(LazyList.of<Int>(), LazyList.of<Int>().mapNotNull { it })
        assertEquals(LazyList.of(2), LazyList.of(1, 2, 5).mapNotNull { if (it % 2 == 0) it else null })
        assertEquals(LazyList.of(1,5), LazyList.of(1, 2, 5).mapNotNull { if (it % 2 != 0) it else null })

        val seq = LazyList.iterate({it+1}, 0).mapNotNull { if (it % 2 != 0) it else null }.take(5)
        assertEquals(LazyList.of(1,3, 5, 7, 9), seq)
    }

    @Test
    fun minus() {
        // Minus with xs empty
        assertEquals(LazyList.of<Int>(), LazyList.of<Int>().minus(1))
        assertEquals(LazyList.of<Int>(), LazyList.of<Int>().minus(listOf(1, 2, 3)))
        assertEquals(LazyList.of<Int>(), LazyList.of<Int>().minus(setOf(1, 2, 3)))

        // Minus nothing
        assertEquals(LazyList.of(1, 2, 3), LazyList.of(1, 2, 3).minus(listOf()))
        assertEquals(LazyList.of(1, 2, 3), LazyList.of(1, 2, 3).minus(setOf()))

        // Minus but none of the elements match
        assertEquals(LazyList.of(1, 2, 3), LazyList.of(1, 2, 3).minus(4))
        assertEquals(LazyList.of(1, 2, 3), LazyList.of(1, 2, 3).minus(listOf(4, 5, 6)))
        assertEquals(LazyList.of(1, 2, 3), LazyList.of(1, 2, 3).minus(setOf(4, 5, 6)))

        // Minus all
        assertEquals(LazyList.of<Int>(), LazyList.of(1, 2, 3).minus(1).minus(2).minus(3))
        assertEquals(LazyList.of<Int>(), LazyList.of(1, 2, 3).minus(listOf(1, 2, 3)))
        assertEquals(LazyList.of<Int>(), LazyList.of(1, 2, 3).minus(setOf(1, 2, 3)))

        // Minus partial match
        assertEquals(LazyList.of(1, 3), LazyList.of(1, 2, 3).minus(2))
        assertEquals(LazyList.of(3), LazyList.of(1, 2, 3).minus(listOf(1, 2, 4)))
        assertEquals(LazyList.of(3), LazyList.of(1, 2, 3).minus(setOf(1, 2, 4)))

        // Works on infinite lists
        assertEquals(LazyList.of(1, 2, 1, 2), LazyList.cycle(LazyList.of(1, 2, 3)).minus(3).take(4))
        assertEquals(LazyList.of(1, 2, 1, 2), LazyList.cycle(LazyList.of(1, 2, 3)).minus(listOf(3)).take(4))
        assertEquals(LazyList.of(1, 2, 1, 2), LazyList.cycle(LazyList.of(1, 2, 3)).minus(setOf(3)).take(4))
    }

    @Test
    fun windowed() {
        run { //Tests on empty inputs
            assertEquals(PersistentList.of<PersistentList<Int>>(), PersistentList.of<Int>().windowed(2))

            assertEquals(
                PersistentList.of<PersistentList<Int>>(),
                PersistentList.of<Int>().windowed(2, partialWindows = true)
            )
        }

        run { // Tests on basic, finite inputs
            val seq = PersistentList.of(1, 2, 3, 4)
            assertEquals(listOf(listOf(1, 2), listOf(2, 3), listOf(3, 4)), seq.windowed(2))
            assertEquals(listOf(listOf(1, 2), listOf(3, 4)), seq.windowed(2, step = 2))
            assertEquals(listOf(listOf(1, 2, 3)), seq.windowed(3, step = 3))
            assertEquals(listOf(listOf(1, 2, 3), listOf(4)), seq.windowed(3, step = 3, partialWindows = true))

            assertEquals(listOf(3), seq.windowed(3, step = 3, transform = {it.size}))
            assertEquals(listOf(3, 1), seq.windowed(3, step = 3, partialWindows = true, transform = {it.size}))
        }
        run { // Test on infinite input
            val seq = PersistentList.of(1, 2, 3).cycle()
            assertEquals(listOf(listOf(1, 2, 3, 1), listOf(2, 3, 1, 2), listOf(3, 1, 2, 3)), seq.windowed(4).take(3))

            val windowsXP = seq.windowed(4, transform={ (it as Iterable<Int>).sum()})
            assertEquals(listOf(7, 8, 9), windowsXP.take(3))
        }
    }
}

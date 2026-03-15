package kleinert.edn.data

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions
import java.util.*
import kotlin.collections.LinkedHashSet

class EdnSetTest {
    private fun <T> sequencedSetOf(vararg xs: T): SequencedSet<T> =
        LinkedHashSet(xs.toList())

    @Test
    fun fromSequencedCollection() {
        Assertions.assertEquals(setOf(1, 2, 3), EdnSet.create(sequencedSetOf(1, 2, 3)))
        Assertions.assertEquals(setOf(2, 3, 1), EdnSet.create(sequencedSetOf(2, 3, 1)))

        Assertions.assertEquals(listOf(1, 2, 3), EdnSet.create(sequencedSetOf(1, 2, 3)).toList())
        Assertions.assertEquals(listOf(2, 3, 1), EdnSet.create(sequencedSetOf(2, 3, 1)).toList())

        val lhs = LinkedHashSet<Int>()
        val list = (0..19990).filter { it.mod(2) == 0 }.shuffled()
        lhs.addAll(list)
        Assertions.assertEquals(list, EdnSet.create(lhs).toList())
        Assertions.assertEquals(list, EdnSet.create(list).toList())
    }

    @Test
    fun fromSelf() {
        val l = EdnSet.of(1, 2, 3, 4, 5)
        Assertions.assertSame(l, EdnSet.create(l))
    }

    @Test
    fun getSize() {
        Assertions.assertEquals(0, EdnSet.of<Int>().size)
        Assertions.assertEquals(5, EdnSet.of(1, 2, 3, 4, 5).size)
        Assertions.assertEquals(1, EdnSet.of(1, 1, 1, 1, 1).size)

        Assertions.assertEquals(0, EdnSet.create<Int>(sequencedSetOf()).size)
        Assertions.assertEquals(5, EdnSet.create(sequencedSetOf(1, 2, 3, 4, 5)).size)
        Assertions.assertEquals(1, EdnSet.create(sequencedSetOf(1, 1, 1, 1, 1)).size)
    }

    @Test
    fun isEmpty() {
        Assertions.assertTrue(EdnSet.of<Int>().isEmpty())
        Assertions.assertFalse(EdnSet.of(1, 2, 3, 4, 5).isEmpty())
    }

    @Test
    fun containsAll() {
        Assertions.assertTrue(EdnSet.of(1, 2, 3).containsAll(listOf()))
        Assertions.assertTrue(EdnSet.of(1, 2, 3).containsAll(listOf(1, 3, 2)))
        Assertions.assertFalse(EdnSet.of(1, 2, 3).containsAll(listOf(1, 5)))
    }

    @Test
    fun contains() {
        Assertions.assertTrue(EdnSet.of(1, 2, 3).contains(1))
        Assertions.assertTrue(EdnSet.of(1, 2, 3).contains(3))
        Assertions.assertFalse(EdnSet.of(1, 2, 3).contains(4))
    }

    @Test
    operator fun iterator() {
        Assertions.assertFalse(EdnSet.of<Int>().iterator().hasNext())
        Assertions.assertTrue(EdnSet.of(1, 2).iterator().hasNext())
    }

    @Test
    fun testEquals() {
        val testSet = EdnSet.of(1, 2, 3)
        Assertions.assertEquals(testSet, testSet)
        Assertions.assertEquals(testSet, EdnSet.of(1, 2, 3))
        Assertions.assertEquals(testSet, setOf(1, 2, 3))
        Assertions.assertEquals(testSet, EdnSet.of(2, 1, 3))
        Assertions.assertEquals(testSet, setOf(2, 1, 3))
        Assertions.assertFalse(testSet == listOf<Int>())
        Assertions.assertNotEquals(testSet, setOf<Int>())
    }
}
package kleinert.soap.data

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions
import java.util.*
import kotlin.collections.HashSet
import kotlin.collections.LinkedHashSet

class PersistentSetTest {

    @Test
    fun isSorted() {
        Assertions.assertFalse(PersistentSet.of<Int>().sorted)
        Assertions.assertFalse(PersistentSet.wrap(LinkedHashSet<Int>(), ordered = true).sorted)
        Assertions.assertFalse(PersistentSet.wrap(HashSet<Int>(), ordered = false).sorted)
        Assertions.assertTrue(PersistentSet.wrap(TreeSet<Int>(), sorted = true).sorted)
    }

    @Test
    fun isOrdered() {
        Assertions.assertTrue(PersistentSet.of<Int>().ordered)
        Assertions.assertTrue(PersistentSet.wrap(LinkedHashSet<Int>(), ordered = true).ordered)
        Assertions.assertFalse(PersistentSet.wrap(HashSet<Int>(), ordered = false).ordered)
        Assertions.assertFalse(PersistentSet.wrap(TreeSet<Int>(), sorted = true).ordered)
    }

    @Test
    fun getSize() {
        Assertions.assertEquals(0, PersistentSet.of<Int>().size)
        Assertions.assertEquals(5, PersistentSet.of(1, 2, 3, 4, 5).size)
        Assertions.assertEquals(1, PersistentSet.of(1, 1, 1, 1, 1).size)
    }

    @Test
    fun isEmpty() {
        Assertions.assertTrue(PersistentSet.of<Int>().isEmpty())
        Assertions.assertFalse(PersistentSet.of(1, 2, 3, 4, 5).isEmpty())
    }

    @Test
    fun containsAll() {
        Assertions.assertTrue(PersistentSet.of(1, 2, 3).containsAll(listOf()))
        Assertions.assertTrue(PersistentSet.of(1, 2, 3).containsAll(listOf(1, 3, 2)))
        Assertions.assertFalse(PersistentSet.of(1, 2, 3).containsAll(listOf(1, 5)))
    }

    @Test
    fun contains() {
        Assertions.assertTrue(PersistentSet.of(1, 2, 3).contains(1))
        Assertions.assertTrue(PersistentSet.of(1, 2, 3).contains(3))
        Assertions.assertFalse(PersistentSet.of(1, 2, 3).contains(4))
    }

    @Test
    operator fun iterator() {
        Assertions.assertFalse(PersistentSet.of<Int>().iterator().hasNext())
        Assertions.assertTrue(PersistentSet.of(1, 2).iterator().hasNext())
    }

    @Test
    fun add() {
        Assertions.assertThrows(UnsupportedOperationException::class.java) { PersistentSet.of(1).add(2) }
    }

    @Test
    fun addAll() {
        Assertions.assertThrows(UnsupportedOperationException::class.java) { PersistentSet.of(1).addAll(listOf(2, 3)) }
    }

    @Test
    fun clear() {
        Assertions.assertThrows(UnsupportedOperationException::class.java) { PersistentSet.of(1).clear() }
    }

    @Test
    fun retainAll() {
        Assertions.assertThrows(UnsupportedOperationException::class.java) {
            PersistentSet.of<Int>().retainAll(listOf())
        }
        Assertions.assertThrows(UnsupportedOperationException::class.java) {
            PersistentSet.of(1, 2, 3).retainAll(listOf())
        }
    }

    @Test
    fun removeAll() {
        Assertions.assertThrows(UnsupportedOperationException::class.java) {
            PersistentSet.of<Int>().removeAll(listOf())
        }
        Assertions.assertThrows(UnsupportedOperationException::class.java) {
            PersistentSet.of(1, 2, 3).removeAll(listOf())
        }
    }

    @Test
    fun remove() {
        Assertions.assertThrows(UnsupportedOperationException::class.java) { PersistentSet.of<Int>().remove(1) }
        Assertions.assertThrows(UnsupportedOperationException::class.java) { PersistentSet.of(1, 2, 3).remove(1) }
    }

    @Test
    fun testEquals() {
        val pset = PersistentSet.of(1, 2, 3)
        Assertions.assertTrue(pset.equals(pset))
        Assertions.assertTrue(pset.equals(PersistentSet.of(1, 2, 3)))
        Assertions.assertTrue(pset.equals(setOf(1, 2, 3)))
        Assertions.assertFalse(pset.equals(listOf<Int>()))
        Assertions.assertFalse(pset.equals(setOf<Int>()))
    }
}
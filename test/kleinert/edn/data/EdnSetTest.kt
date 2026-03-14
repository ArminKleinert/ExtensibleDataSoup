package kleinert.edn.data

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions
import java.util.*
import kotlin.collections.HashSet
import kotlin.collections.LinkedHashSet

class EdnSetTest {
    @Test
    fun getSize() {
        Assertions.assertEquals(0, EdnSet.of<Int>().size)
        Assertions.assertEquals(5, EdnSet.of(1, 2, 3, 4, 5).size)
        Assertions.assertEquals(1, EdnSet.of(1, 1, 1, 1, 1).size)
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
        val pset = EdnSet.of(1, 2, 3)
        Assertions.assertTrue(pset.equals(pset))
        Assertions.assertTrue(pset.equals(EdnSet.of(1, 2, 3)))
        Assertions.assertTrue(pset.equals(setOf(1, 2, 3)))
        Assertions.assertFalse(pset.equals(listOf<Int>()))
        Assertions.assertFalse(pset.equals(setOf<Int>()))
    }
}
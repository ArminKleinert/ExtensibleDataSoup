package kleinert.edn.data

import kleinert.edn.data.PersistentMap
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions
import java.util.*
import kotlin.NoSuchElementException

class PersistentMapTest {

    @Test
    fun getSorted() {
        Assertions.assertFalse(PersistentMap.of<Int, Int>().sorted)
        Assertions.assertFalse(PersistentMap.wrap(LinkedHashMap<Int, Int>(), ordered = true).sorted)
        Assertions.assertFalse(PersistentMap.wrap(HashMap<Int, Int>(), ordered = false).sorted)
        Assertions.assertTrue(PersistentMap.wrap(TreeMap<Int, Int>(), sorted = true).sorted)
    }

    @Test
    fun getOrdered() {
        Assertions.assertTrue(PersistentMap.of<Int, Int>().ordered)
        Assertions.assertTrue(PersistentMap.wrap(LinkedHashMap<Int, Int>(), ordered = true).ordered)
        Assertions.assertFalse(PersistentMap.wrap(HashMap<Int, Int>(), ordered = false).ordered)
        Assertions.assertFalse(PersistentMap.wrap(TreeMap<Int, Int>(), sorted = true).ordered)
    }

    @Test
    fun getEntries() {
        Assertions.assertEquals(setOf<Map.Entry<Int, Int>>(), PersistentMap.of<Int, Int>().entries)
        Assertions.assertEquals(mapOf(1 to 2, 3 to 4).entries, PersistentMap.of(1 to 2, 3 to 4).entries)
    }

    @Test
    fun getKeys() {
        Assertions.assertEquals(setOf<Int>(), PersistentMap.of<Int, Int>().keys)
        Assertions.assertEquals(setOf(1, 3), PersistentMap.of(1 to 2, 3 to 4).keys)
    }

    @Test
    fun getSize() {
        Assertions.assertEquals(0, PersistentMap.of<Int, Int>().size)
        Assertions.assertEquals(2, PersistentMap.of(1 to 2, 3 to 4).size)
    }

    @Test
    fun getValues() {
        Assertions.assertEquals(setOf<Int>(), PersistentMap.of<Int, Int>().values)
        Assertions.assertEquals(setOf(2, 4), PersistentMap.of(1 to 2, 3 to 4).values)
    }

    @Test
    fun get() {
        Assertions.assertNull(PersistentMap.of<Int, Int>().get(1))
        Assertions.assertNull(PersistentMap.of(1 to 2, 3 to 4).get(6))
        Assertions.assertEquals(2, PersistentMap.of(1 to 2, 3 to 4).get(1))
    }

    @Test
    fun containsValue() {
        Assertions.assertFalse(PersistentMap.of<Int, Int>().containsKey(2))
        Assertions.assertFalse(PersistentMap.of(1 to 2, 3 to 4).containsKey(2))
        Assertions.assertTrue(PersistentMap.of(1 to 2, 3 to 4).containsKey(1))
    }

    @Test
    fun containsKey() {
        Assertions.assertFalse(PersistentMap.of<Int, Int>().containsKey(1))
        Assertions.assertFalse(PersistentMap.of(1 to 2, 3 to 4).containsKey(5))
        Assertions.assertTrue(PersistentMap.of(1 to 2, 3 to 4).containsKey(1))
    }

    @Test
    fun isEmpty() {
        Assertions.assertTrue(PersistentMap.of<Int, Int>().isEmpty())
        Assertions.assertFalse(PersistentMap.of(1 to 2, 3 to 4).isEmpty())
    }

    @Test
    fun testEquals() {
        val pmap = PersistentMap.of(1 to 2, 3 to 4)
        Assertions.assertTrue(pmap.equals(pmap))
        Assertions.assertTrue(pmap.equals(PersistentMap.of(1 to 2, 3 to 4)))
        Assertions.assertTrue(pmap.equals(mapOf(1 to 2, 3 to 4)))
        Assertions.assertFalse(pmap.equals(mapOf<Int, Int>()))
    }
}
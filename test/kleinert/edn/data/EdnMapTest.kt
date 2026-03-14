package kleinert.edn.data

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions
import java.util.*

class EdnMapTest {
    @Test
    fun getEntries() {
        Assertions.assertEquals(setOf<Map.Entry<Int, Int>>(), EdnMap.of<Int, Int>().entries)
        Assertions.assertEquals(mapOf(1 to 2, 3 to 4).entries, EdnMap.of(1 to 2, 3 to 4).entries)
    }

    @Test
    fun getKeys() {
        Assertions.assertEquals(setOf<Int>(), EdnMap.of<Int, Int>().keys)
        Assertions.assertEquals(setOf(1, 3), EdnMap.of(1 to 2, 3 to 4).keys)
    }

    @Test
    fun getSize() {
        Assertions.assertEquals(0, EdnMap.of<Int, Int>().size)
        Assertions.assertEquals(2, EdnMap.of(1 to 2, 3 to 4).size)
    }

    @Test
    fun getValues() {
        Assertions.assertEquals(setOf<Int>(), EdnMap.of<Int, Int>().values)
        Assertions.assertEquals(setOf(2, 4), EdnMap.of(1 to 2, 3 to 4).values)
    }

    @Test
    fun get() {
        Assertions.assertNull(EdnMap.of<Int, Int>().get(1))
        Assertions.assertNull(EdnMap.of(1 to 2, 3 to 4).get(6))
        Assertions.assertEquals(2, EdnMap.of(1 to 2, 3 to 4).get(1))
    }

    @Test
    fun containsValue() {
        Assertions.assertFalse(EdnMap.of<Int, Int>().containsKey(2))
        Assertions.assertFalse(EdnMap.of(1 to 2, 3 to 4).containsKey(2))
        Assertions.assertTrue(EdnMap.of(1 to 2, 3 to 4).containsKey(1))
    }

    @Test
    fun containsKey() {
        Assertions.assertFalse(EdnMap.of<Int, Int>().containsKey(1))
        Assertions.assertFalse(EdnMap.of(1 to 2, 3 to 4).containsKey(5))
        Assertions.assertTrue(EdnMap.of(1 to 2, 3 to 4).containsKey(1))
    }

    @Test
    fun isEmpty() {
        Assertions.assertTrue(EdnMap.of<Int, Int>().isEmpty())
        Assertions.assertFalse(EdnMap.of(1 to 2, 3 to 4).isEmpty())
    }

    @Test
    fun testEquals() {
        val pmap = EdnMap.of(1 to 2, 3 to 4)
        Assertions.assertTrue(pmap.equals(pmap))
        Assertions.assertTrue(pmap.equals(EdnMap.of(1 to 2, 3 to 4)))
        Assertions.assertTrue(pmap.equals(mapOf(1 to 2, 3 to 4)))
        Assertions.assertFalse(pmap.equals(mapOf<Int, Int>()))
    }
}
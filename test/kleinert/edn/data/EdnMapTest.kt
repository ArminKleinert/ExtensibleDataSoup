package kleinert.edn.data

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions
import java.util.*
import kotlin.collections.toList

class EdnMapTest {
    private fun <K, V> sequencedMapOf(vararg xs: Pair<K, V>): SequencedMap<K, V> {
        val lhm = LinkedHashMap<K, V>()
        for ((k, v) in xs) {
            lhm[k] = v
        }
        return lhm
    }

    @Test
    fun fromSequencedCollection() {
        // From Map, order doesn't matter.
        Assertions.assertEquals(mapOf(1 to 2, 3 to 4), EdnMap.create(sequencedMapOf(1 to 2, 3 to 4)))
        Assertions.assertEquals(mapOf(2 to 3, 1 to 4), EdnMap.create(sequencedMapOf(2 to 3, 1 to 4)))

        // From Map, order does matter.
        Assertions.assertEquals(listOf(1 to 2, 3 to 4), EdnMap.create(sequencedMapOf(1 to 2, 3 to 4)).toList())
        Assertions.assertEquals(listOf(2 to 3, 1 to 4), EdnMap.create(sequencedMapOf(2 to 3, 1 to 4)).toList())


        // From List, order does matter.
        Assertions.assertEquals(mapOf(1 to 2, 3 to 4), EdnMap.create(listOf(1 to 2, 3 to 4)))
        Assertions.assertEquals(mapOf(2 to 3, 1 to 4), EdnMap.create(listOf(2 to 3, 1 to 4)))

        // From SequencedSet, order does matter.
        Assertions.assertEquals(mapOf(1 to 2, 3 to 4), EdnMap.create(EdnSet.of(1 to 2, 3 to 4)))
        Assertions.assertEquals(mapOf(2 to 3, 1 to 4), EdnMap.create(EdnSet.of(2 to 3, 1 to 4)))

        val lhs = LinkedHashMap<Int, Int>()
        val list = (0..19991).chunked(2).map { (k, v) -> k to v }.shuffled()
        lhs.putAll(list)
        Assertions.assertEquals(list, EdnMap.create(lhs).toList())
        Assertions.assertEquals(list, EdnMap.create(list).toList())
    }

    @Test
    fun fromSelf() {
        val l = EdnMap.of(1 to 2, 3 to 4, 5 to 6)
        Assertions.assertSame(l, EdnMap.create(l))
    }

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
        Assertions.assertNull(EdnMap.of<Int, Int>()[1])
        Assertions.assertNull(EdnMap.of(1 to 2, 3 to 4)[6])
        Assertions.assertEquals(2, EdnMap.of(1 to 2, 3 to 4)[1])
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
        Assertions.assertSame(pmap, pmap)
        Assertions.assertEquals(pmap, EdnMap.of(1 to 2, 3 to 4))
        Assertions.assertEquals(pmap, mapOf(1 to 2, 3 to 4))
        Assertions.assertNotEquals(pmap, mapOf<Int, Int>())
    }
}
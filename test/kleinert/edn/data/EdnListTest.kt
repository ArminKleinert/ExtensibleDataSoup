package kleinert.edn.data

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions

class EdnListTest {
    @Test
    fun fromSequencedCollection() {
        Assertions.assertEquals(listOf(1, 2, 3), EdnList.create(EdnSet.of(1, 2, 3)))
        Assertions.assertEquals(listOf(2, 3, 1), EdnList.create(EdnSet.of(2, 3, 1)))

        val lhs = LinkedHashSet<Int>()
        lhs.addAll((0..19990).filter { it.mod(2) == 0 })
        Assertions.assertEquals(lhs.toList(), EdnList.create(lhs))
    }

    @Test
    fun fromSelf() {
        val l = EdnList.of(1, 2, 3, 4, 5)
        Assertions.assertSame(l, EdnList.create(l))
    }

    @Test
    fun getSize() {
        Assertions.assertEquals(0, EdnList.of<Int>().size)
        Assertions.assertEquals(5, EdnList.of(1, 2, 3, 4, 5).size)
        Assertions.assertEquals(0, EdnList.create<Int>(listOf()).size)
        Assertions.assertEquals(5, EdnList.create(listOf(1, 2, 3, 4, 5)).size)
    }


    @Test
    fun get() {
        Assertions.assertThrows(IndexOutOfBoundsException::class.java) { EdnList.of<Int>().get(0) }
        Assertions.assertThrows(IndexOutOfBoundsException::class.java) { EdnList.of(1, 2, 3).get(3) }
        Assertions.assertEquals(5, EdnList.of(1, 2, 3, 4, 5).get(4))

        Assertions.assertThrows(IndexOutOfBoundsException::class.java) { EdnList.create<Int>(listOf()).get(0) }
        Assertions.assertThrows(IndexOutOfBoundsException::class.java) { EdnList.create(listOf(1, 2, 3)).get(3) }
        Assertions.assertEquals(5, EdnList.create(listOf(1, 2, 3, 4, 5)).get(4))
    }

    @Test
    fun isEmpty() {
        Assertions.assertTrue(EdnList.of<Int>().isEmpty())
        Assertions.assertFalse(EdnList.of(1, 2, 3, 4, 5).isEmpty())

        Assertions.assertTrue(EdnList.create<Int>(listOf()).isEmpty())
        Assertions.assertFalse(EdnList.create(listOf(1, 2, 3, 4, 5)).isEmpty())
    }

    @Test
    operator fun iterator() {
        Assertions.assertEquals(listOf("1", "2"), EdnList.of(1, 2).map(Int::toString))
        Assertions.assertFalse(EdnList.of<Int>().iterator().hasNext())
        Assertions.assertTrue(EdnList.of(1, 2).iterator().hasNext())

        Assertions.assertEquals(listOf("1", "2"), EdnList.of(1, 2).map(Int::toString))
        Assertions.assertFalse(EdnList.create<Int>(listOf()).iterator().hasNext())
        Assertions.assertTrue(EdnList.create(listOf(1, 2)).iterator().hasNext())
    }

    @Test
    fun listIterator() {
        Assertions.assertTrue(EdnList.of(1, 2).listIterator().hasNext())
        Assertions.assertFalse(EdnList.of(1, 2).listIterator(2).hasNext())

        Assertions.assertTrue(EdnList.create(listOf(1, 2)).listIterator().hasNext())
        Assertions.assertFalse(EdnList.create(listOf(1, 2)).listIterator(2).hasNext())
    }

    @Test
    fun subList() {
        Assertions.assertEquals(listOf(2, 3), EdnList.of(1, 2, 3).subList(1, 3))
        Assertions.assertEquals(listOf(2, 3), EdnList.create(listOf(1, 2, 3)).subList(1, 3))
    }

    @Test
    fun lastIndexOf() {
        Assertions.assertEquals(-1, EdnList.of(1, 2, 3, 2, 1).lastIndexOf(4))
        Assertions.assertEquals(4, EdnList.of(1, 2, 3, 2, 1).lastIndexOf(1))

        Assertions.assertEquals(-1, EdnList.create(listOf(1, 2, 3, 2, 1)).lastIndexOf(4))
        Assertions.assertEquals(4, EdnList.create(listOf(1, 2, 3, 2, 1)).lastIndexOf(1))
    }

    @Test
    fun indexOf() {
        Assertions.assertEquals(-1, EdnList.of(1, 2, 3, 2, 1).indexOf(4))
        Assertions.assertEquals(0, EdnList.of(1, 2, 3, 2, 1).indexOf(1))

        Assertions.assertEquals(-1, EdnList.create(listOf(1, 2, 3, 2, 1)).indexOf(4))
        Assertions.assertEquals(0, EdnList.create(listOf(1, 2, 3, 2, 1)).indexOf(1))
    }

    @Test
    fun containsAll() {
        Assertions.assertTrue(EdnList.of(1, 2, 3, 2, 1).containsAll(listOf()))
        Assertions.assertTrue(EdnList.of(1, 2, 3, 2, 1).containsAll(listOf(1, 3, 2)))
        Assertions.assertFalse(EdnList.of(1, 2, 3, 2, 1).containsAll(listOf(1, 5)))

        Assertions.assertTrue(EdnList.create(listOf(1, 2, 3, 2, 1)).containsAll(listOf()))
        Assertions.assertTrue(EdnList.create(listOf(1, 2, 3, 2, 1)).containsAll(listOf(1, 3, 2)))
        Assertions.assertFalse(EdnList.create(listOf(1, 2, 3, 2, 1)).containsAll(listOf(1, 5)))
    }

    @Test
    fun contains() {
        Assertions.assertTrue(EdnList.of(1, 2, 3, 2, 1).contains(1))
        Assertions.assertTrue(EdnList.of(1, 2, 3, 2, 1).contains(3))
        Assertions.assertFalse(EdnList.of(1, 2, 3, 2, 1).contains(4))

        Assertions.assertTrue(EdnList.create(listOf(1, 2, 3, 2, 1)).contains(1))
        Assertions.assertTrue(EdnList.create(listOf(1, 2, 3, 2, 1)).contains(3))
        Assertions.assertFalse(EdnList.create(listOf(1, 2, 3, 2, 1)).contains(4))
    }

    @Test
    fun testEquals() {
        run {
            val list = EdnList.of(1, 2, 3, 2, 1)
            Assertions.assertTrue(list.equals(list))
            Assertions.assertTrue(list.equals(EdnList.of(1, 2, 3, 2, 1)))
            Assertions.assertTrue(list.equals(EdnList.create(listOf(1, 2, 3, 2, 1))))
            Assertions.assertTrue(list.equals(listOf(1, 2, 3, 2, 1)))
            Assertions.assertFalse(list.equals(listOf<Int>()))
        }
        run {
            val list = EdnList.create(listOf(1, 2, 3, 2, 1))
            Assertions.assertTrue(list.equals(list))
            Assertions.assertTrue(list.equals(EdnList.of(1, 2, 3, 2, 1)))
            Assertions.assertTrue(list.equals(EdnList.create(listOf(1, 2, 3, 2, 1))))
            Assertions.assertTrue(list.equals(listOf(1, 2, 3, 2, 1)))
            Assertions.assertFalse(list.equals(listOf<Int>()))
        }
    }
}
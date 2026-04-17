package kleinert.edn.data

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions

class EdnVectorTest {
    @Test
    fun fromSequencedCollection() {
        Assertions.assertEquals(listOf(1, 2, 3), EdnVector.create(EdnSet.of(1, 2, 3)))
        Assertions.assertEquals(listOf(2, 3, 1), EdnVector.create(EdnSet.of(2, 3, 1)))

        val lhs = LinkedHashSet<Int>()
        lhs.addAll((0..19990).filter { it.mod(2) == 0 })
        Assertions.assertEquals(lhs.toList(), EdnVector.create(lhs))
    }

    @Test
    fun fromSelf() {
        val l = EdnVector.of(1, 2, 3, 4, 5)
        Assertions.assertSame(l, EdnVector.create(l))
    }

    @Test
    fun getSize() {
        Assertions.assertEquals(0, EdnVector.of<Int>().size)
        Assertions.assertEquals(5, EdnVector.of(1, 2, 3, 4, 5).size)
        Assertions.assertEquals(0, EdnVector.create<Int>(listOf()).size)
        Assertions.assertEquals(5, EdnVector.create(listOf(1, 2, 3, 4, 5)).size)
    }


    @Test
    fun get() {
        Assertions.assertThrows(IndexOutOfBoundsException::class.java) { EdnVector.of<Int>()[0] }
        Assertions.assertThrows(IndexOutOfBoundsException::class.java) { EdnVector.of(1, 2, 3)[3] }
        Assertions.assertEquals(5, EdnVector.of(1, 2, 3, 4, 5)[4])

        Assertions.assertThrows(IndexOutOfBoundsException::class.java) { EdnVector.create<Int>(listOf())[0] }
        Assertions.assertThrows(IndexOutOfBoundsException::class.java) { EdnVector.create(listOf(1, 2, 3))[3] }
        Assertions.assertEquals(5, EdnVector.create(listOf(1, 2, 3, 4, 5))[4])
    }

    @Test
    fun isEmpty() {
        Assertions.assertTrue(EdnVector.of<Int>().isEmpty())
        Assertions.assertFalse(EdnVector.of(1, 2, 3, 4, 5).isEmpty())

        Assertions.assertTrue(EdnVector.create<Int>(listOf()).isEmpty())
        Assertions.assertFalse(EdnVector.create(listOf(1, 2, 3, 4, 5)).isEmpty())
    }

    @Test
    operator fun iterator() {
        Assertions.assertEquals(listOf("1", "2"), EdnVector.of(1, 2).map(Int::toString))
        Assertions.assertFalse(EdnVector.of<Int>().iterator().hasNext())
        Assertions.assertTrue(EdnVector.of(1, 2).iterator().hasNext())

        Assertions.assertEquals(listOf("1", "2"), EdnVector.of(1, 2).map(Int::toString))
        Assertions.assertFalse(EdnVector.create<Int>(listOf()).iterator().hasNext())
        Assertions.assertTrue(EdnVector.create(listOf(1, 2)).iterator().hasNext())
    }

    @Test
    fun listIterator() {
        Assertions.assertTrue(EdnVector.of(1, 2).listIterator().hasNext())
        Assertions.assertFalse(EdnVector.of(1, 2).listIterator(2).hasNext())

        Assertions.assertTrue(EdnVector.create(listOf(1, 2)).listIterator().hasNext())
        Assertions.assertFalse(EdnVector.create(listOf(1, 2)).listIterator(2).hasNext())
    }

    @Test
    fun subList() {
        Assertions.assertEquals(listOf(2, 3), EdnVector.of(1, 2, 3).subList(1, 3))
        Assertions.assertEquals(listOf(2, 3), EdnVector.create(listOf(1, 2, 3)).subList(1, 3))
    }

    @Test
    fun lastIndexOf() {
        Assertions.assertEquals(-1, EdnVector.of(1, 2, 3, 2, 1).lastIndexOf(4))
        Assertions.assertEquals(4, EdnVector.of(1, 2, 3, 2, 1).lastIndexOf(1))

        Assertions.assertEquals(-1, EdnVector.create(listOf(1, 2, 3, 2, 1)).lastIndexOf(4))
        Assertions.assertEquals(4, EdnVector.create(listOf(1, 2, 3, 2, 1)).lastIndexOf(1))
    }

    @Test
    fun indexOf() {
        Assertions.assertEquals(-1, EdnVector.of(1, 2, 3, 2, 1).indexOf(4))
        Assertions.assertEquals(0, EdnVector.of(1, 2, 3, 2, 1).indexOf(1))

        Assertions.assertEquals(-1, EdnVector.create(listOf(1, 2, 3, 2, 1)).indexOf(4))
        Assertions.assertEquals(0, EdnVector.create(listOf(1, 2, 3, 2, 1)).indexOf(1))
    }

    @Test
    fun containsAll() {
        Assertions.assertTrue(EdnVector.of(1, 2, 3, 2, 1).containsAll(listOf()))
        Assertions.assertTrue(EdnVector.of(1, 2, 3, 2, 1).containsAll(listOf(1, 3, 2)))
        Assertions.assertFalse(EdnVector.of(1, 2, 3, 2, 1).containsAll(listOf(1, 5)))

        Assertions.assertTrue(EdnVector.create(listOf(1, 2, 3, 2, 1)).containsAll(listOf()))
        Assertions.assertTrue(EdnVector.create(listOf(1, 2, 3, 2, 1)).containsAll(listOf(1, 3, 2)))
        Assertions.assertFalse(EdnVector.create(listOf(1, 2, 3, 2, 1)).containsAll(listOf(1, 5)))
    }

    @Test
    fun contains() {
        Assertions.assertTrue(EdnVector.of(1, 2, 3, 2, 1).contains(1))
        Assertions.assertTrue(EdnVector.of(1, 2, 3, 2, 1).contains(3))
        Assertions.assertFalse(EdnVector.of(1, 2, 3, 2, 1).contains(4))

        Assertions.assertTrue(EdnVector.create(listOf(1, 2, 3, 2, 1)).contains(1))
        Assertions.assertTrue(EdnVector.create(listOf(1, 2, 3, 2, 1)).contains(3))
        Assertions.assertFalse(EdnVector.create(listOf(1, 2, 3, 2, 1)).contains(4))
    }

    @Test
    fun testEquals() {
        run {
            val list = EdnVector.of(1, 2, 3, 2, 1)
            Assertions.assertEquals(list, list)
            Assertions.assertEquals(list, EdnVector.of(1, 2, 3, 2, 1))
            Assertions.assertEquals(list, EdnVector.create(listOf(1, 2, 3, 2, 1)))
            Assertions.assertEquals(list, listOf(1, 2, 3, 2, 1))
            Assertions.assertNotEquals(list, listOf<Int>())
        }
        run {
            val list = EdnVector.create(listOf(1, 2, 3, 2, 1))
            Assertions.assertEquals(list, list)
            Assertions.assertEquals(list, EdnVector.of(1, 2, 3, 2, 1))
            Assertions.assertEquals(list, EdnVector.create(listOf(1, 2, 3, 2, 1)))
            Assertions.assertEquals(list, listOf(1, 2, 3, 2, 1))
            Assertions.assertNotEquals(list, listOf<Int>())
        }
    }
}
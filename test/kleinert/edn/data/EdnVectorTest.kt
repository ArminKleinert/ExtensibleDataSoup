package kleinert.edn.data

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions

class EdnVectorTest {

    @Test
    fun getSize() {
        Assertions.assertEquals(0, EdnVector.of<Int>().size)
        Assertions.assertEquals(5, EdnVector.of(1, 2, 3, 4, 5).size)
    }

    @Test
    fun get() {
        Assertions.assertThrows(IndexOutOfBoundsException::class.java) { EdnVector.of<Int>().get(0) }
        Assertions.assertThrows(IndexOutOfBoundsException::class.java) { EdnVector.of(1, 2, 3).get(3) }
        Assertions.assertEquals(5, EdnVector.of(1, 2, 3, 4, 5).get(4))
    }

    @Test
    fun isEmpty() {
        Assertions.assertTrue(EdnVector.of<Int>().isEmpty())
        Assertions.assertFalse(EdnVector.of(1, 2, 3, 4, 5).isEmpty())
    }

    @Test
    operator fun iterator() {
        Assertions.assertEquals(listOf("1", "2"), EdnVector.of(1, 2).map(Int::toString))
        Assertions.assertFalse(EdnVector.of<Int>().iterator().hasNext())
        Assertions.assertTrue(EdnVector.of(1, 2).iterator().hasNext())
    }

    @Test
    fun listIterator() {
        Assertions.assertTrue(EdnVector.of(1, 2).listIterator().hasNext())
        Assertions.assertFalse(EdnVector.of(1, 2).listIterator(2).hasNext())
    }

    @Test
    fun subList() {
        Assertions.assertEquals(listOf(2, 3), EdnVector.of(1, 2, 3).subList(1, 3))
    }

    @Test
    fun lastIndexOf() {
        Assertions.assertEquals(-1, EdnVector.of(1, 2, 3, 2, 1).lastIndexOf(4))
        Assertions.assertEquals(4, EdnVector.of(1, 2, 3, 2, 1).lastIndexOf(1))
    }

    @Test
    fun indexOf() {
        Assertions.assertEquals(-1, EdnVector.of(1, 2, 3, 2, 1).indexOf(4))
        Assertions.assertEquals(0, EdnVector.of(1, 2, 3, 2, 1).indexOf(1))
    }

    @Test
    fun containsAll() {
        Assertions.assertTrue(EdnVector.of(1, 2, 3, 2, 1).containsAll(listOf()))
        Assertions.assertTrue(EdnVector.of(1, 2, 3, 2, 1).containsAll(listOf(1, 3, 2)))
        Assertions.assertFalse(EdnVector.of(1, 2, 3, 2, 1).containsAll(listOf(1, 5)))
    }

    @Test
    fun contains() {
        Assertions.assertTrue(EdnVector.of(1, 2, 3, 2, 1).contains(1))
        Assertions.assertTrue(EdnVector.of(1, 2, 3, 2, 1).contains(3))
        Assertions.assertFalse(EdnVector.of(1, 2, 3, 2, 1).contains(4))
    }

    @Test
    fun testEquals() {
        val list = EdnVector.of(1, 2, 3, 2, 1)
        Assertions.assertTrue(list.equals(list))
        Assertions.assertTrue(list.equals(EdnVector.of(1, 2, 3, 2, 1)))
        Assertions.assertTrue(list.equals(listOf(1, 2, 3, 2, 1)))
        Assertions.assertFalse(list.equals(listOf<Int>()))
    }
}
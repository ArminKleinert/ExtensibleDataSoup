package kleinert.edn.data

import kleinert.edn.data.PersistentList
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions

class PersistentListTest {

    @Test
    fun getSize() {
        Assertions.assertEquals(0, PersistentList.of<Int>().size)
        Assertions.assertEquals(5, PersistentList.of(1, 2, 3, 4, 5).size)
    }

    @Test
    fun clear() {
        Assertions.assertThrows(UnsupportedOperationException::class.java) { PersistentList.of(1).clear() }
    }

    @Test
    fun addAll() {
        Assertions.assertThrows(UnsupportedOperationException::class.java) { PersistentList.of(1).addAll(listOf(2, 3)) }
    }


    @Test
    fun add() {
        Assertions.assertThrows(UnsupportedOperationException::class.java) { PersistentList.of(1).add(2) }
    }


    @Test
    fun get() {
        Assertions.assertThrows(IndexOutOfBoundsException::class.java) { PersistentList.of<Int>().get(0) }
        Assertions.assertThrows(IndexOutOfBoundsException::class.java) { PersistentList.of(1, 2, 3).get(3) }
        Assertions.assertEquals(5, PersistentList.of(1, 2, 3, 4, 5).get(4))
    }

    @Test
    fun isEmpty() {
        Assertions.assertTrue(PersistentList.of<Int>().isEmpty())
        Assertions.assertFalse(PersistentList.of(1, 2, 3, 4, 5).isEmpty())
    }

    @Test
    operator fun iterator() {
        Assertions.assertEquals(listOf("1", "2"), PersistentList.of(1, 2).map(Int::toString))
        Assertions.assertFalse(PersistentList.of<Int>().iterator().hasNext())
        Assertions.assertTrue(PersistentList.of(1, 2).iterator().hasNext())
    }

    @Test
    fun listIterator() {
        Assertions.assertTrue(PersistentList.of(1, 2).listIterator().hasNext())
        Assertions.assertFalse(PersistentList.of(1, 2).listIterator(2).hasNext())
    }

    @Test
    fun removeAt() {
        Assertions.assertThrows(UnsupportedOperationException::class.java) { PersistentList.of<Int>().removeAt(0) }
        Assertions.assertThrows(UnsupportedOperationException::class.java) { PersistentList.of(1, 2, 3).removeAt(1) }
    }

    @Test
    fun subList() {
        Assertions.assertEquals(listOf(2, 3), PersistentList.of(1, 2, 3).subList(1, 3))
    }

    @Test
    fun set() {
        Assertions.assertThrows(UnsupportedOperationException::class.java) { PersistentList.of<Int>().set(0, 5) }
        Assertions.assertThrows(UnsupportedOperationException::class.java) { PersistentList.of(1, 2, 3).set(1, 9) }
    }

    @Test
    fun retainAll() {
        Assertions.assertThrows(UnsupportedOperationException::class.java) {
            PersistentList.of<Int>().retainAll(listOf())
        }
        Assertions.assertThrows(UnsupportedOperationException::class.java) {
            PersistentList.of(1, 2, 3).retainAll(listOf())
        }
    }

    @Test
    fun removeAll() {
        Assertions.assertThrows(UnsupportedOperationException::class.java) {
            PersistentList.of<Int>().removeAll(listOf())
        }
        Assertions.assertThrows(UnsupportedOperationException::class.java) {
            PersistentList.of(1, 2, 3).removeAll(listOf())
        }
    }

    @Test
    fun remove() {
        Assertions.assertThrows(UnsupportedOperationException::class.java) { PersistentList.of<Int>().remove(1) }
        Assertions.assertThrows(UnsupportedOperationException::class.java) { PersistentList.of(1, 2, 3).remove(1) }
    }

    @Test
    fun lastIndexOf() {
        Assertions.assertEquals(-1, PersistentList.of(1, 2, 3, 2, 1).lastIndexOf(4))
        Assertions.assertEquals(4, PersistentList.of(1, 2, 3, 2, 1).lastIndexOf(1))
    }

    @Test
    fun indexOf() {
        Assertions.assertEquals(-1, PersistentList.of(1, 2, 3, 2, 1).indexOf(4))
        Assertions.assertEquals(0, PersistentList.of(1, 2, 3, 2, 1).indexOf(1))
    }

    @Test
    fun containsAll() {
        Assertions.assertTrue(PersistentList.of(1, 2, 3, 2, 1).containsAll(listOf()))
        Assertions.assertTrue(PersistentList.of(1, 2, 3, 2, 1).containsAll(listOf(1, 3, 2)))
        Assertions.assertFalse(PersistentList.of(1, 2, 3, 2, 1).containsAll(listOf(1, 5)))
    }

    @Test
    fun contains() {
        Assertions.assertTrue(PersistentList.of(1, 2, 3, 2, 1).contains(1))
        Assertions.assertTrue(PersistentList.of(1, 2, 3, 2, 1).contains(3))
        Assertions.assertFalse(PersistentList.of(1, 2, 3, 2, 1).contains(4))
    }

    @Test
    fun testEquals() {
        val list = PersistentList.of(1, 2, 3, 2, 1)
        Assertions.assertTrue(list.equals(list))
        Assertions.assertTrue(list.equals(PersistentList.of(1, 2, 3, 2, 1)))
        Assertions.assertTrue(list.equals(listOf(1, 2, 3, 2, 1)))
        Assertions.assertFalse(list.equals(listOf<Int>()))
    }
}
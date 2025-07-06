package kleinert.soap

import kleinert.soap.data.PackedList
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PackedListTest {
    @Test
    fun testEmpty() {
        run {
            val lst = PackedList<Int>(0, 0, listOf())
            Assertions.assertTrue(lst.frozen)
            Assertions.assertTrue(lst.isEmpty())
            Assertions.assertEquals(listOf<List<Int>>(), lst)
            Assertions.assertEquals(listOf<List<Int>>(), lst.unpack())
            Assertions.assertEquals(listOf<Int>(), lst.flatten())
            Assertions.assertEquals(0, lst.size)
            Assertions.assertEquals(0, lst.packedSize)
            Assertions.assertEquals(0, lst.subListSize)
            Assertions.assertThrows(IndexOutOfBoundsException::class.java) { lst[0] }
            Assertions.assertThrows(IndexOutOfBoundsException::class.java) { lst[0, 0] }
            Assertions.assertFalse(lst.listIterator().hasNext())
            Assertions.assertFalse(lst.contains(listOf(55)))
            Assertions.assertFalse(lst.containsAll(listOf(listOf(55), listOf(1,2))))
            Assertions.assertThrows(IndexOutOfBoundsException::class.java) { lst.subList(1, 2) }
        }
        run {
            val lst = PackedList<Int>(listOf(listOf()))
            Assertions.assertTrue(lst.frozen)
            Assertions.assertTrue(lst.isEmpty())
            Assertions.assertEquals(listOf<List<Int>>(), lst)
            Assertions.assertEquals(listOf<List<Int>>(), lst.unpack())
            Assertions.assertEquals(listOf<Int>(), lst.flatten())
            Assertions.assertEquals(0, lst.size)
            Assertions.assertEquals(0, lst.packedSize)
            Assertions.assertEquals(0, lst.subListSize)
            Assertions.assertThrows(IndexOutOfBoundsException::class.java) { lst[0] }
            Assertions.assertThrows(IndexOutOfBoundsException::class.java) { lst[0, 0] }
            Assertions.assertFalse(lst.listIterator().hasNext())
            Assertions.assertFalse(lst.contains(listOf(55)))
            Assertions.assertFalse(lst.containsAll(listOf(listOf(55), listOf(1,2))))
            Assertions.assertThrows(IndexOutOfBoundsException::class.java) { lst.subList(1, 2) }
        }
        run {
            val lst = PackedList<Int>(listOf(listOf()), frozen = false)
            Assertions.assertFalse(lst.frozen)
            Assertions.assertThrows(IndexOutOfBoundsException::class.java) { lst[0] = listOf(1) }
            Assertions.assertThrows(IndexOutOfBoundsException::class.java) { lst[0, 0] = 0 }
            Assertions.assertThrows(IndexOutOfBoundsException::class.java) { lst.subList(1, 2)[0] = listOf(6) }
        }
    }

    @Test
    fun testSingleton() {
        run {
            val lst = PackedList(1, 1, listOf(55))
            Assertions.assertTrue(lst.frozen)
            Assertions.assertTrue(!lst.isEmpty())
            Assertions.assertEquals(listOf(listOf(55)), lst)
            Assertions.assertEquals(listOf(55), lst.flatten())
            Assertions.assertEquals(1, lst.size)
            Assertions.assertEquals(1, lst.packedSize)
            Assertions.assertEquals(1, lst.subListSize)
            Assertions.assertEquals(listOf(55), lst[0])
            Assertions.assertEquals(55, lst[0, 0])
            Assertions.assertThrows(IndexOutOfBoundsException::class.java) { lst[1] }
            Assertions.assertThrows(IndexOutOfBoundsException::class.java) { lst[1, 0] }
            Assertions.assertThrows(IndexOutOfBoundsException::class.java) { lst[0, 1] }
            Assertions.assertTrue(lst.listIterator().hasNext())
            Assertions.assertTrue(lst.contains(listOf(55)))
            Assertions.assertFalse(lst.contains(listOf(1,2)))
            Assertions.assertFalse(lst.containsAll(listOf(listOf(55), listOf(1,2))))
        }
        run {
            val lst = PackedList(listOf(listOf(55)))
            Assertions.assertTrue(lst.frozen)
            Assertions.assertTrue(!lst.isEmpty())
            Assertions.assertEquals(listOf(listOf(55)), lst)
            Assertions.assertEquals(listOf(55), lst.flatten())
            Assertions.assertEquals(1, lst.size)
            Assertions.assertEquals(1, lst.packedSize)
            Assertions.assertEquals(1, lst.subListSize)
            Assertions.assertEquals(listOf(55), lst[0])
            Assertions.assertEquals(55, lst[0, 0])
            Assertions.assertThrows(IndexOutOfBoundsException::class.java) { lst[1] }
            Assertions.assertThrows(IndexOutOfBoundsException::class.java) { lst[1, 0] }
            Assertions.assertThrows(IndexOutOfBoundsException::class.java) { lst[0, 1] }
            Assertions.assertTrue(lst.listIterator().hasNext())
            Assertions.assertTrue(lst.contains(listOf(55)))
            Assertions.assertFalse(lst.containsAll(listOf(listOf(55), listOf(1,2))))
        }
        run {
            val lst = PackedList(listOf(listOf(55)), frozen = false)
            Assertions.assertFalse(lst.frozen)

            lst[0] = listOf(1)
            Assertions.assertEquals(listOf(listOf(1)), lst)

            lst[0, 0] = 0
            Assertions.assertEquals(listOf(listOf(0)), lst)

            Assertions.assertThrows(IndexOutOfBoundsException::class.java) { lst.subList(1, 2)[0] = listOf(6) }
        }
    }

    @Test
    fun testSimple() {
        run {
            val lst = PackedList(2, 2, listOf(1,2,3,4))
            Assertions.assertTrue(lst.frozen)
            Assertions.assertTrue(!lst.isEmpty())
            Assertions.assertEquals(listOf(listOf(1,2), listOf(3,4)), lst)
            Assertions.assertEquals(listOf(1,2,3,4), lst.flatten())
            Assertions.assertEquals(2, lst.size)
            Assertions.assertEquals(4, lst.packedSize)
            Assertions.assertEquals(2, lst.subListSize)
            Assertions.assertEquals(listOf(1,2), lst[0])
            Assertions.assertEquals(2, lst[0, 1])
            Assertions.assertTrue(lst.listIterator().hasNext())
            Assertions.assertTrue(lst.contains(listOf(1,2)))
            Assertions.assertTrue(lst.contains(listOf(3,4)))
            Assertions.assertTrue(lst.containsAll(listOf(listOf(1,2), listOf(3,4))))
            Assertions.assertEquals(listOf(listOf(3,4)), lst.subList(1, 2))
        }
        run {
            val lst = PackedList(listOf(listOf(1,2), listOf(3,4)))
            Assertions.assertTrue(lst.frozen)
            Assertions.assertTrue(!lst.isEmpty())
            Assertions.assertEquals(listOf(listOf(1,2), listOf(3,4)), lst)
            Assertions.assertEquals(listOf(1,2,3,4), lst.flatten())
            Assertions.assertEquals(2, lst.size)
            Assertions.assertEquals(4, lst.packedSize)
            Assertions.assertEquals(2, lst.subListSize)
            Assertions.assertEquals(listOf(1,2), lst[0])
            Assertions.assertEquals(2, lst[0, 1])
            Assertions.assertTrue(lst.listIterator().hasNext())
            Assertions.assertTrue(lst.contains(listOf(1,2)))
            Assertions.assertTrue(lst.containsAll(listOf(listOf(1,2), listOf(3,4))))
            Assertions.assertEquals(listOf(listOf(3,4)), lst.subList(1, 2))
        }
        run {
            val lst = PackedList(listOf(listOf(1,2), listOf(3,4)), frozen = false)
            Assertions.assertFalse(lst.frozen)

            lst[0] = listOf(3, 0)
            Assertions.assertEquals(listOf(listOf(3, 0), listOf(3, 4)), lst)

            lst[0, 0] = 5
            Assertions.assertEquals(listOf(listOf(5, 0), listOf(3, 4)), lst)

            lst.subList(1, 2)[0] = listOf(6, 5)
            Assertions.assertEquals(listOf(listOf(5, 0), listOf(6,5)), lst)
        }
    }

    @Test
    fun test3x2() {
        run {
            val lst = PackedList(3, 2, listOf(1,2,3,4,5,6))
            Assertions.assertTrue(lst.frozen)
            Assertions.assertTrue(!lst.isEmpty())
            Assertions.assertEquals(listOf(listOf(1,2), listOf(3,4), listOf(5,6)), lst)
            Assertions.assertEquals(listOf(listOf(1,2), listOf(3,4), listOf(5,6)), lst.unpack())
            Assertions.assertEquals(listOf(1,2,3,4,5,6), lst.flatten())
            Assertions.assertEquals(3, lst.size)
            Assertions.assertEquals(6, lst.packedSize)
            Assertions.assertEquals(2, lst.subListSize)
            Assertions.assertEquals(listOf(1,2), lst[0])
            Assertions.assertEquals(2, lst[0, 1])
            Assertions.assertTrue(lst.listIterator().hasNext())
            Assertions.assertTrue(lst.contains(listOf(1,2)))
            Assertions.assertTrue(lst.containsAll(listOf(listOf(1,2), listOf(3,4))))
            Assertions.assertEquals(listOf(listOf(3,4)), lst.subList(1, 2))
        }
        run {
            val lst = PackedList(listOf(listOf(1,2), listOf(3,4), listOf(5,6)))
            Assertions.assertTrue(lst.frozen)
            Assertions.assertTrue(!lst.isEmpty())
            Assertions.assertEquals(listOf(listOf(1,2), listOf(3,4), listOf(5,6)), lst)
            Assertions.assertEquals(listOf(1,2,3,4,5,6), lst.flatten())
            Assertions.assertEquals(3, lst.size)
            Assertions.assertEquals(6, lst.packedSize)
            Assertions.assertEquals(2, lst.subListSize)
            Assertions.assertEquals(listOf(1,2), lst[0])
            Assertions.assertEquals(2, lst[0, 1])
            Assertions.assertTrue(lst.listIterator().hasNext())
            Assertions.assertTrue(lst.contains(listOf(1,2)))
            Assertions.assertTrue(lst.containsAll(listOf(listOf(1,2), listOf(3,4))))
            Assertions.assertEquals(listOf(listOf(3,4)), lst.subList(1, 2))
        }
        run {
            val lst = PackedList(listOf(listOf(1,2), listOf(3,4), listOf(5,6)), frozen = false)
            Assertions.assertFalse(lst.frozen)

            lst[0] = listOf(3, 0)
            Assertions.assertEquals(listOf(listOf(3, 0), listOf(3, 4), listOf(5,6)), lst)

            lst[0, 0] = 5
            Assertions.assertEquals(listOf(listOf(5, 0), listOf(3, 4), listOf(5,6)), lst)

            lst.subList(1, 2)[0] = listOf(6, 5)
            Assertions.assertEquals(listOf(listOf(5, 0), listOf(6,5), listOf(5,6)), lst)
        }
    }
}
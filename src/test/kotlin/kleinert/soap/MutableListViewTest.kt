package kleinert.soap

import kleinert.soap.data.MutableListView
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MutableListViewTest {
    @Test
    fun testErrors() {
        val lst = ArrayList(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
        Assertions.assertThrows(IllegalArgumentException::class.java) { MutableListView(1, 0, lst) }
        Assertions.assertThrows(IndexOutOfBoundsException::class.java) { MutableListView(-1, 0, lst) }
        Assertions.assertThrows(IndexOutOfBoundsException::class.java) { MutableListView(0, lst.size + 1, lst) }
    }

    @Test
    fun testEmpty() {
        run {
            val lst = ArrayList<Int>()
            Assertions.assertTrue(MutableListView(0, lst.size, lst).isEmpty())
            Assertions.assertTrue(MutableListView(0, lst.size, lst).size == lst.size)
            Assertions.assertEquals(lst, MutableListView(0, lst.size, lst))

        }
        run {
            val lst = ArrayList(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
            Assertions.assertTrue(MutableListView(0, 0, lst).isEmpty())
            Assertions.assertTrue(MutableListView(5, 5, lst).isEmpty())
            Assertions.assertEquals(listOf<Int>(), MutableListView(0, 0, lst))
        }
    }

    @Test
    fun testSimple() {
        run {
            val lst = ArrayList(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
            Assertions.assertEquals(lst, MutableListView(0, lst.size, lst))
            Assertions.assertTrue(MutableListView(0, lst.size, lst).size == lst.size)
            Assertions.assertFalse(MutableListView(0, lst.size, lst).isEmpty())
            Assertions.assertEquals(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9), MutableListView(0, lst.size - 1, lst))
            Assertions.assertEquals(listOf(3, 4), MutableListView(2, 4, lst))
        }
    }

    @Test
    fun testSet() {
        run {
            val lst = ArrayList(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
            val subList = MutableListView(1, 4, lst)
            Assertions.assertEquals(listOf(2, 3, 4), subList)
            Assertions.assertEquals(lst[1], subList[0])

            lst[2] = 55
            Assertions.assertEquals(lst[2], subList[1])

            Assertions.assertThrows(IndexOutOfBoundsException::class.java) { subList.set(3, 8) }

            val temp = subList.set(2, 19)
            Assertions.assertEquals(4, temp)
            Assertions.assertEquals(19, subList[2])
            Assertions.assertEquals(19, lst[3])
        }
    }

    @Test
    fun testSublist() {
        run {
            val lst = ArrayList(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
            val subList = MutableListView(0, 5, lst)
            Assertions.assertEquals(listOf(1, 2, 3, 4, 5), subList)
            Assertions.assertSame(subList, subList.subList(0, 5))
            Assertions.assertEquals(listOf(1, 2), subList.subList(0, 2))
            Assertions.assertEquals(listOf(3, 4), subList.subList(2, 4))
            Assertions.assertEquals(listOf<Int>(), subList.subList(2, 2))
        }
    }
}

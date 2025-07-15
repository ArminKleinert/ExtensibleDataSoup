package kleinert.soap.edn


import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class EDNReaderListsVectorsTest {
    @Test
    fun parseEmptyList() {
        // Normal
        EDN.read("()").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertTrue((it as List<*>).isEmpty())
        }
        // Normal
        EDN.read("[]").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertTrue((it as List<*>).isEmpty())
        }

        // Whitespace does not matter
        EDN.read("(  )").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertTrue((it as List<*>).isEmpty())
        }
        EDN.read("(\t \n)").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertTrue((it as List<*>).isEmpty())
        }
        EDN.read("(\n)").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertTrue((it as List<*>).isEmpty())
        }

        // Whitespace does not matter
        EDN.read("[  ]").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertTrue((it as List<*>).isEmpty())
        }
        EDN.read("[\t \n]").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertTrue((it as List<*>).isEmpty())
        }
        EDN.read("[\n]").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertTrue((it as List<*>).isEmpty())
        }
    }

    @Test
    fun parseBasicList() {
        EDN.read("(1)").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertEquals(listOf(1L), (it as List<*>))
        }
        EDN.read("[1]").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertEquals(listOf(1L), (it as List<*>))
        }
        EDN.read("(1 2 3)").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertEquals(listOf(1L, 2L, 3L), (it as List<*>))
        }
        EDN.read("[1 2 3]").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertEquals(listOf(1L, 2L, 3L), (it as List<*>))
        }
    }

    @Test
    fun parseNestedList() {
        EDN.read("((1))").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertEquals(listOf(listOf(1L)), (it as List<*>))
        }
        EDN.read("([1])").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertEquals(listOf(listOf(1L)), (it as List<*>))
        }
        EDN.read("[(1)]").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertEquals(listOf(listOf(1L)), (it as List<*>))
        }
        EDN.read("[(1)]").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertEquals(listOf(listOf(1L)), (it as List<*>))
        }
        EDN.read("(1 (2 3))").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertEquals(listOf(1L, listOf(2L, 3L)), (it as List<*>))
        }
        EDN.read("[1 (2 3)]").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertEquals(listOf(1L, listOf(2L, 3L)), (it as List<*>))
        }
    }

    @Test
    fun parseWithConverter() {
        run {
            val options = EDN.defaultOptions.copy(listToPersistentListConverter = {LinkedList (it)})
            val parsed = EDN.read("(1 2)", options)
            Assertions.assertInstanceOf(LinkedList::class.java, parsed)
            Assertions.assertEquals(listOf(1L, 2L), parsed)
        }
        run {
            val options = EDN.defaultOptions.copy(listToPersistentVectorConverter = {LinkedList (it)})
            val parsed = EDN.read("[1 2]", options)
            Assertions.assertInstanceOf(LinkedList::class.java, parsed)
            Assertions.assertEquals(listOf(1L, 2L), parsed)
        }
    }

    @Test
    fun invalidTest() {
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("(") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("[") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read(")") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("]") }
    }
}

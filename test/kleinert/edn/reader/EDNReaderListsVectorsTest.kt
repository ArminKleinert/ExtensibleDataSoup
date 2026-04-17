package kleinert.edn.reader

import kleinert.edn.EDN
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EDNReaderListsVectorsTest {
    @Test
    fun parseEmptyList() {
        // Normal
        run {
            val it = EDN.read("()")
            Assertions.assertTrue(it is List<*>)
            Assertions.assertTrue((it as List<*>).isEmpty())
        }
        // Normal
        run {
            val it = EDN.read("[]")
            Assertions.assertTrue(it is List<*>)
            Assertions.assertTrue((it as List<*>).isEmpty())
        }

        // Whitespace does not matter
        run {
            val it = EDN.read("(  )")
            Assertions.assertTrue(it is List<*>)
            Assertions.assertTrue((it as List<*>).isEmpty())
        }
        run {
            val it = EDN.read("(\t \n)")
            Assertions.assertTrue(it is List<*>)
            Assertions.assertTrue((it as List<*>).isEmpty())
        }
        run {
            val it = EDN.read("(\n)")
            Assertions.assertTrue(it is List<*>)
            Assertions.assertTrue((it as List<*>).isEmpty())
        }

        // Whitespace does not matter
        run {
            val it = EDN.read("[  ]")
            Assertions.assertTrue(it is List<*>)
            Assertions.assertTrue((it as List<*>).isEmpty())
        }
        run {
            val it = EDN.read("[\t \n]")
            Assertions.assertTrue(it is List<*>)
            Assertions.assertTrue((it as List<*>).isEmpty())
        }
        run {
            val it = EDN.read("[\n]")
            Assertions.assertTrue(it is List<*>)
            Assertions.assertTrue((it as List<*>).isEmpty())
        }
    }

    @Test
    fun parseBasicList() {
        run {
            val it = EDN.read("(1)")
            Assertions.assertTrue(it is List<*>)
            Assertions.assertEquals(listOf(1L), (it as List<*>))
        }
        run {
            val it = EDN.read("[1]")
            Assertions.assertTrue(it is List<*>)
            Assertions.assertEquals(listOf(1L), (it as List<*>))
        }
        run {
            val it = EDN.read("(1 2 3)")
            Assertions.assertTrue(it is List<*>)
            Assertions.assertEquals(listOf(1L, 2L, 3L), (it as List<*>))
        }
        run {
            val it = EDN.read("[1 2 3]")
            Assertions.assertTrue(it is List<*>)
            Assertions.assertEquals(listOf(1L, 2L, 3L), (it as List<*>))
        }
    }

    @Test
    fun parseNestedList() {
        run {
            val it = EDN.read("((1))")
            Assertions.assertTrue(it is List<*>)
            Assertions.assertEquals(listOf(listOf(1L)), (it as List<*>))
        }
        run {
            val it = EDN.read("([1])")
            Assertions.assertTrue(it is List<*>)
            Assertions.assertEquals(listOf(listOf(1L)), (it as List<*>))
        }
        run {
            val it = EDN.read("[(1)]")
            Assertions.assertTrue(it is List<*>)
            Assertions.assertEquals(listOf(listOf(1L)), (it as List<*>))
        }
        run {
            val it = EDN.read("[(1)]")
            Assertions.assertTrue(it is List<*>)
            Assertions.assertEquals(listOf(listOf(1L)), (it as List<*>))
        }
        run {
            val it = EDN.read("(1 (2 3))")
            Assertions.assertTrue(it is List<*>)
            Assertions.assertEquals(listOf(1L, listOf(2L, 3L)), (it as List<*>))
        }
        run {
            val it = EDN.read("[1 (2 3)]")
            Assertions.assertTrue(it is List<*>)
            Assertions.assertEquals(listOf(1L, listOf(2L, 3L)), (it as List<*>))
        }
    }

    @Test
    fun parseWithConverter() {
        run {
            val options = EDN.defaultOptions.copy(listToEdnListConverter = { ArrayList(it) })
            val parsed = EDN.read("(1 2)", options)
            Assertions.assertTrue(parsed is ArrayList<*>)
            Assertions.assertEquals(listOf(1L, 2L), parsed)
        }
        run {
            val options = EDN.defaultOptions.copy(listToEdnVectorConverter = { ArrayList(it) })
            val parsed = EDN.read("[1 2]", options)
            Assertions.assertTrue(parsed is ArrayList<*>)
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

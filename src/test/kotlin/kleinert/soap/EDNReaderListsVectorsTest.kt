package kleinert.soap


import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EDNReaderListsVectorsTest {
    private fun soap(s: String): Any? {
        return EDNSoapReader.readString(s, EDNSoapOptions.defaultOptions)
    }

    private fun soapE(s: String): Any? {
        return EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions)
    }

    @Test
    fun parseEmptyList() {
        // Normal
        soap("()").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertTrue((it as List<*>).isEmpty())
        }
        // Normal
        soap("[]").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertTrue((it as List<*>).isEmpty())
        }

        // Whitespace does not matter
        soap("(  )").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertTrue((it as List<*>).isEmpty())
        }
        soap("(\t \n)").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertTrue((it as List<*>).isEmpty())
        }
        soap("(\n)").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertTrue((it as List<*>).isEmpty())
        }

        // Whitespace does not matter
        soap("[  ]").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertTrue((it as List<*>).isEmpty())
        }
        soap("[\t \n]").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertTrue((it as List<*>).isEmpty())
        }
        soap("[\n]").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertTrue((it as List<*>).isEmpty())
        }
    }

    @Test
    fun parseBasicList() {
        soap("(1)").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertEquals(listOf(1L), (it as List<*>))
        }
        soap("[1]").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertEquals(listOf(1L), (it as List<*>))
        }
        soap("(1 2 3)").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertEquals(listOf(1L, 2L, 3L), (it as List<*>))
        }
        soap("[1 2 3]").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertEquals(listOf(1L, 2L, 3L), (it as List<*>))
        }
    }

    @Test
    fun parseNestedList() {
        soap("((1))").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertEquals(listOf(listOf(1L)), (it as List<*>))
        }
        soap("([1])").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertEquals(listOf(listOf(1L)), (it as List<*>))
        }
        soap("[(1)]").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertEquals(listOf(listOf(1L)), (it as List<*>))
        }
        soap("[(1)]").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertEquals(listOf(listOf(1L)), (it as List<*>))
        }
        soap("(1 (2 3))").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertEquals(listOf(1L, listOf(2L, 3L)), (it as List<*>))
        }
        soap("[1 (2 3)]").let {
            Assertions.assertInstanceOf(List::class.java, it)
            Assertions.assertEquals(listOf(1L, listOf(2L, 3L)), (it as List<*>))
        }
    }

    @Test
    fun invalidTest() {
        Assertions.assertThrows(EdnReaderException::class.java) { soap("(") }
        Assertions.assertThrows(EdnReaderException::class.java) { soap("[") }
        Assertions.assertThrows(EdnReaderException::class.java) { soap(")") }
        Assertions.assertThrows(EdnReaderException::class.java) { soap("]") }
    }
}

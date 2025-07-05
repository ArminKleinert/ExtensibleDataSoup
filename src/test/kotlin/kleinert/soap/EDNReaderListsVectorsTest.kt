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
            Assertions.assertFalse((it as Iterable<*>).iterator().hasNext())
        }
        // Normal
        soap("[]").let {
            Assertions.assertTrue(it is List<*>)
            Assertions.assertTrue((it as List<*>).isEmpty())
        }

        // Whitespace does not matter
        soap("(  )").let {
            Assertions.assertTrue(it is Iterable<*>)
            Assertions.assertFalse((it as Iterable<*>).iterator().hasNext())
        }
        soap("(\t \n)").let {
            Assertions.assertTrue(it is Iterable<*>)
            Assertions.assertFalse((it as Iterable<*>).iterator().hasNext())
        }
        soap("(\n)").let {
            Assertions.assertTrue(it is Iterable<*>)
            Assertions.assertFalse((it as Iterable<*>).iterator().hasNext())
        }

        // Whitespace does not matter
        soap("[  ]").let {
            Assertions.assertTrue(it is List<*>)
            Assertions.assertTrue((it as List<*>).isEmpty())
        }
        soap("[\t \n]").let {
            Assertions.assertTrue(it is List<*>)
            Assertions.assertTrue((it as List<*>).isEmpty())
        }
        soap("[\n]").let {
            Assertions.assertTrue(it is List<*>)
            Assertions.assertTrue((it as List<*>).isEmpty())
        }
    }

    @Test
    fun parseBasicList() {
    }
}
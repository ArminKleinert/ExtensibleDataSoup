package kleinert.soap


import kleinert.soap.data.Symbol
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EDNReaderSetsMapsTest {
    private fun soap(s: String): Any? {
        return EDNSoapReader.readString(s, EDNSoapOptions.defaultOptions)
    }

    private fun soapE(s: String): Any? {
        return EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions)
    }

    @Test
    fun parseEmpty() {
        soap("{}").let {
            Assertions.assertInstanceOf(Map::class.java, it)
            Assertions.assertTrue((it as Map<*, *>).isEmpty())
        }
        soap("#{}").let {
            Assertions.assertInstanceOf(Set::class.java, it)
            Assertions.assertTrue((it as Set<*>).isEmpty())
        }
    }

    @Test
    fun parseBasicList() {
        soap("{a b c d}").let {
            Assertions.assertInstanceOf(Map::class.java, it)
            Assertions.assertEquals(it,
                mapOf(Symbol.symbol("a") to Symbol.symbol("b"), Symbol.symbol("c") to Symbol.symbol("d")))
        }
        soap("{1 2 3 4}").let {
            Assertions.assertEquals(mapOf(1L to 2L, 3L to 4L), it)
        }
        soap("#{1 2 3 4}").let {
            Assertions.assertInstanceOf(Set::class.java, it)
            Assertions.assertEquals(setOf(1L, 2L, 3L, 4L), (it as Set<*>))
        }
    }

    @Test
    fun invalidTest() {
        Assertions.assertThrows(EdnReaderException::class.java) { soap("{") }
        Assertions.assertThrows(EdnReaderException::class.java) { soap("#{") }
        Assertions.assertThrows(EdnReaderException::class.java) { soap("}") }

        Assertions.assertThrows(EdnReaderException::class.java) { soap("{1 2 3}") } // Odd number of elements
        Assertions.assertThrows(EdnReaderException::class.java) { soap("{1 2 1 3}") } // Duplicate key
        Assertions.assertThrows(EdnReaderException::class.java) { soap("#{1 2 1}") } // Duplicate element
    }
}
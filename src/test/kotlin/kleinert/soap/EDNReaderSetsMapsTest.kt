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
            println("${mapOf(1 to 2, 3 to 4)} ${mapOf(1 to 2, 3 to 4).javaClass} ${(it as Map<*, *>)} ${(it as Map<*, *>).javaClass}")
            println("${mapOf(1 to 2, 3 to 4) == mapOf(1 to 2, 3 to 4)}")
            Assertions.assertEquals(it,
                mapOf(Symbol.symbol("a") to Symbol.symbol("b"), Symbol.symbol("c") to Symbol.symbol("d")))
        }
        soap("{1 2 3 4}").let {
            Assertions.assertInstanceOf(Map::class.java, it)
            println("${mapOf(1 to 2, 3 to 4)} ${mapOf(1 to 2, 3 to 4).javaClass} ${(it as Map<*, *>)} ${(it as Map<*, *>).javaClass}")
            println("${mapOf(1 to 2, 3 to 4) == mapOf(1 to 2, 3 to 4)}")
            Assertions.assertEquals(it, mapOf(1 to 2, 3 to 4))
            Assertions.assertEquals(mapOf(1 to 2, 3 to 4), it)
        }
        soap("#{1 2 3 4}").let {
            Assertions.assertInstanceOf(Set::class.java, it)
            Assertions.assertEquals(setOf(1, 2, 3, 4), (it as Set<*>))
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
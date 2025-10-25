package kleinert.edn.reader

import kleinert.edn.EDN
import kleinert.edn.EDNSoapOptions
import kleinert.edn.ExtendedEDNDecoders
import kleinert.edn.data.Keyword
import kleinert.edn.data.PersistentList
import kleinert.edn.data.Symbol
import kleinert.edn.reader.EdnReaderException
import kleinert.edn.reader.EdnReaderException.EdnClassConversionError
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.*

class EDNReaderSetsMapsTest {
    @Test
    fun parseEmpty() {
        EDN.read("{}").let {
            Assertions.assertTrue(it is Map<*, *>)
            Assertions.assertTrue((it as Map<*, *>).isEmpty())
        }
        EDN.read("#{}").let {
            Assertions.assertTrue(it is Set<*>)
            Assertions.assertTrue((it as Set<*>).isEmpty())
        }
    }

    @Test
    fun parseBasicList() {
        EDN.read("{a b c d}").let {
            Assertions.assertTrue(it is Map<*, *>)
            Assertions.assertEquals(
                it,
                mapOf(Symbol.symbol("a") to Symbol.symbol("b"), Symbol.symbol("c") to Symbol.symbol("d"))
            )
        }
        EDN.read("{1 2 3 4}").let {
            Assertions.assertEquals(mapOf(1L to 2L, 3L to 4L), it)
        }
        EDN.read("#{1 2 3 4}").let {
            Assertions.assertTrue(it is Set<*>)
            Assertions.assertEquals(setOf(1L, 2L, 3L, 4L), (it as Set<*>))
        }
    }

    @Test
    fun parseWithConverter() {
        run {
            val options = EDN.defaultOptions.copy(mapToPersistentMapConverter = { IdentityHashMap(it) })
            val parsed = EDN.read("{1 2}", options)
            Assertions.assertTrue(parsed is IdentityHashMap<*, *>)
            Assertions.assertEquals(mapOf(1L to 2L), parsed)
        }
        run {
            val options = EDN.defaultOptions.copy(setToPersistentSetConverter = { TreeSet<Long>() })
            val parsed = EDN.read("#{1 2}", options)
            Assertions.assertTrue(parsed is TreeSet<*>)
            Assertions.assertEquals(setOf<Long>(), parsed)
        }
    }

    @Test
    fun invalidTest() {
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("{") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("#{") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("}") }

        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("{1 2 3}") } // Odd number of elements
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("{1 2 1 3}") } // Duplicate key
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("#{1 2 1}") } // Duplicate element
    }
}
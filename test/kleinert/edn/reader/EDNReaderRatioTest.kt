package kleinert.edn.reader

import kleinert.edn.EDN
import kleinert.edn.data.Ratio
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EDNReaderRatioTest {
    @Test
    fun parseRatioFail() {
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("0/1") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("+0/1") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("-0/1") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("1/1") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("+1/1") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("-1/1") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("1/2") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("-1/2") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("1/2567") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("-1/2567") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("123456789/2567") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("+123456789/2567") }
        Assertions.assertThrows(EdnReaderException::class.java) { EDN.read("-123456789/2567") }
    }

    @Test
    fun parseRatio() {
        val opt = EDN.defaultOptions.copy(allowRatios = true)

        EDN.read("0/1", opt).let {
            Assertions.assertTrue(it is Ratio)
            Assertions.assertEquals(Ratio.ZERO, it)
        }
        EDN.read("+0/1", opt).let {
            Assertions.assertTrue(it is Ratio)
            Assertions.assertEquals(Ratio.ZERO, it)
        }
        EDN.read("-0/1", opt).let {
            Assertions.assertTrue(it is Ratio)
            Assertions.assertEquals(Ratio.ZERO, it)
        }

        EDN.read("1/1", opt).let {
            Assertions.assertTrue(it is Ratio)
            Assertions.assertEquals(Ratio.valueOf(1), it)
        }
        EDN.read("+1/1", opt).let {
            Assertions.assertTrue(it is Ratio)
            Assertions.assertEquals(Ratio.valueOf(1), it)
        }
        EDN.read("-1/1", opt).let {
            Assertions.assertTrue(it is Ratio)
            Assertions.assertEquals(Ratio.valueOf(1).negate(), it)
        }
        EDN.read("1/2", opt).let {
            Assertions.assertTrue(it is Ratio)
            Assertions.assertEquals(Ratio.valueOf(1, 2), it)
        }
        EDN.read("-1/2", opt).let {
            Assertions.assertTrue(it is Ratio)
            Assertions.assertEquals(Ratio.valueOf(1, 2).negate(), it)
        }

        EDN.read("1/2567", opt).let {
            Assertions.assertTrue(it is Ratio)
            Assertions.assertEquals(Ratio.valueOf(1, 2567), it)
        }
        EDN.read("-1/2567", opt).let {
            Assertions.assertTrue(it is Ratio)
            Assertions.assertEquals(Ratio.valueOf(1, 2567).negate(), it)
        }

        EDN.read("123456789/2567", opt).let {
            Assertions.assertTrue(it is Ratio)
            Assertions.assertEquals(Ratio.valueOf(123456789, 2567), it)
        }
        EDN.read("+123456789/2567", opt).let {
            Assertions.assertTrue(it is Ratio)
            Assertions.assertEquals(Ratio.valueOf(123456789, 2567), it)
        }
        EDN.read("-123456789/2567", opt).let {
            Assertions.assertTrue(it is Ratio)
            Assertions.assertEquals(Ratio.valueOf(123456789, 2567).negate(), it)
        }
    }
}

package kleinert.edn.data

import kleinert.edn.data.Char32
import kleinert.edn.data.toChar32
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Char32Test {
    @Test
    fun testConstructor() {
        Assertions.assertThrows(IllegalArgumentException::class.java) { Char32(Char32.MIN_VALUE.code - 1) }
        Assertions.assertThrows(IllegalArgumentException::class.java) { Char32(Char32.MAX_VALUE.code + 1) }

        Assertions.assertEquals(Char32(0), Char32(0))
        Assertions.assertEquals(Char32('A'.code), Char32.valueOf('A'))
        Assertions.assertEquals(Char32(0x0001F546), Char32.valueOf(0x0001F546u))
        Assertions.assertEquals(Char32(0x0001F546), Char32.valueOf("\uD83D\uDD46"))
    }

    @Test
    fun dec() {
        Assertions.assertThrows(IllegalArgumentException::class.java) { Char32(Char32.MIN_VALUE.code).dec() }
        Assertions.assertEquals(Char32('A'.code), Char32.valueOf('B').dec())
        Assertions.assertEquals(Char32('A'.code), Char32('B'.code).dec())
    }

    @Test
    fun inc() {
        Assertions.assertThrows(IllegalArgumentException::class.java) { Char32(Char32.MAX_VALUE.code).inc() }
        Assertions.assertEquals(Char32('B'.code), Char32.valueOf('A').inc())
        Assertions.assertEquals(Char32('B'.code), Char32('A'.code).inc())
    }

    @Test
    fun minus() {
        Assertions.assertThrows(IllegalArgumentException::class.java) { Char32(Char32.MIN_VALUE.code) - 1 }
        Assertions.assertThrows(IllegalArgumentException::class.java) { Char32(Char32.MIN_VALUE.code) - 'A' }
        Assertions.assertThrows(IllegalArgumentException::class.java) { Char32(Char32.MIN_VALUE.code) - Char32(1) }

        Assertions.assertEquals(Char32(0), Char32('A'.code) - 65)
        Assertions.assertEquals(Char32(0), Char32('A'.code) - 'A')
        Assertions.assertEquals(Char32(0), Char32('A'.code) - Char32(65))
    }

    @Test
    fun plus() {
        Assertions.assertThrows(IllegalArgumentException::class.java) { Char32(Char32.MAX_VALUE.code) + 1 }
        Assertions.assertThrows(IllegalArgumentException::class.java) { Char32(Char32.MAX_VALUE.code) + 'A' }
        Assertions.assertThrows(IllegalArgumentException::class.java) { Char32(Char32.MAX_VALUE.code) + Char32(1) }

        Assertions.assertEquals(Char32('A'.code), Char32(0) + 65)
        Assertions.assertEquals(Char32('A'.code), Char32(0) + 'A')
        Assertions.assertEquals(Char32('A'.code), Char32(0) + Char32(65))
        Char(0)
    }

    @Test
    fun toChar() {
        Assertions.assertThrows(IllegalArgumentException::class.java) { (Char32(Char.MAX_VALUE.code) + 1).toChar() }
        Assertions.assertEquals('A', Char32.valueOf('A').toChar())
        Assertions.assertEquals('A', Char32('A'.code).toChar())
    }

    @Test
    fun toByte() {
        Assertions.assertThrows(IllegalArgumentException::class.java) { (Char32(Byte.MAX_VALUE.toInt()) + 1).toByte() }
        Assertions.assertEquals(65.toByte(), Char32.valueOf('A').toByte())
        Assertions.assertEquals(65.toByte(), Char32('A'.code).toByte())
    }

    @Test
    fun toShort() {
        Assertions.assertThrows(IllegalArgumentException::class.java) { (Char32(Short.MAX_VALUE.toInt()) + 1).toShort() }
        Assertions.assertEquals(65.toShort(), Char32.valueOf('A').toShort())
        Assertions.assertEquals(65.toShort(), Char32('A'.code).toShort())
    }

    @Test
    fun toInt() {
        Assertions.assertEquals(65, Char32.valueOf('A').toInt())
        Assertions.assertEquals(65, Char32('A'.code).toInt())
    }

    @Test
    fun toLong() {
        Assertions.assertEquals(65L, Char32.valueOf('A').toLong())
        Assertions.assertEquals(65L, Char32('A'.code).toLong())
    }

    @Test
    fun toFloat() {
        Assertions.assertEquals(65.0f, Char32.valueOf('A').toFloat())
        Assertions.assertEquals(65.0f, Char32('A'.code).toFloat())
    }

    @Test
    fun toDouble() {
        Assertions.assertEquals(65.0, Char32.valueOf('A').toDouble())
        Assertions.assertEquals(65.0, Char32('A'.code).toDouble())
    }

    @Test
    fun testToString() {
        Assertions.assertEquals("A", Char32.valueOf('A').toString())
        Assertions.assertEquals("\uD83D\uDD46", Char32(0x0001F546).toString()) // White latin cross
        Assertions.assertEquals("\uD83C\uDF81", Char32(0x0001F381).toString()) // Wrapped present 🎁
    }

    @Test
    fun compareTo() {
        Assertions.assertEquals(-1, Char32.valueOf('A').compareTo(Char32.valueOf('B')))
        Assertions.assertEquals(0, Char32.valueOf('A').compareTo(Char32.valueOf('A')))
        Assertions.assertEquals(1, Char32.valueOf('B').compareTo(Char32.valueOf('A')))

        Assertions.assertTrue(Char32.valueOf('A') < Char32.valueOf('B'))
        Assertions.assertTrue(Char32.valueOf('B') > Char32.valueOf('A'))
    }

    @Test
    fun rangeTo() {
        Assertions.assertEquals(
            listOf(Char32.valueOf('A'), Char32.valueOf('B'), Char32.valueOf('C')),
            Char32.valueOf('A').rangeTo(Char32.valueOf('C')).toList()
        )
        Assertions.assertEquals(
            listOf(Char32.valueOf('A'), Char32.valueOf('B'), Char32.valueOf('C')),
            (Char32.valueOf('A')..Char32.valueOf('C')).toList()
        )
    }

    @Test
    fun rangeUntil() {
        Assertions.assertEquals(
            listOf(Char32.valueOf('A'), Char32.valueOf('B')),
            Char32.valueOf('A').rangeUntil(Char32.valueOf('C')).toList()
        )
        Assertions.assertEquals(
            listOf(Char32.valueOf('A'), Char32.valueOf('B')),
            (Char32.valueOf('A')..<Char32.valueOf('C')).toList()
        )
    }

    @Test
    fun getCode() {
        Assertions.assertEquals(65, Char32('A'.code).code)
    }

    @Test
    fun charToChar32() {
        Assertions.assertEquals(Char32('A'.code), 'A'.toChar32())
    }

    @Test
    fun byteToChar32() {
        Assertions.assertEquals(Char32('A'.code), 65.toByte().toChar32())
    }

    @Test
    fun shortToChar32() {
        Assertions.assertEquals(Char32('A'.code), 65.toShort().toChar32())
    }

    @Test
    fun intToChar32() {
        Assertions.assertEquals(Char32('A'.code), 65.toChar32())
    }

    @Test
    fun longToChar32() {
        Assertions.assertEquals(Char32('A'.code), 65L.toChar32())
    }
}
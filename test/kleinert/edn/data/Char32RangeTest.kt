package kleinert.edn.data

import kleinert.edn.data.Char32
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Char32RangeTest {
    @Test
    fun getEndExclusive() {
        Assertions.assertEquals(11, (Char32(0)..Char32(10)).endExclusive.code)
    }

    @Test
    fun contains() {
        Assertions.assertFalse((Char32(0)..Char32(10)).contains(Char32(11)))

        Assertions.assertTrue((Char32(0)..Char32(10)).contains(Char32(0)))
        Assertions.assertTrue((Char32(0)..Char32(10)).contains(Char32(5)))
        Assertions.assertTrue((Char32(0)..Char32(10)).contains(Char32(10)))
    }

    @Test
    fun isEmpty() {
        Assertions.assertFalse((Char32(0)..Char32(10)).isEmpty())
        Assertions.assertTrue((Char32(1)..Char32(0)).isEmpty())
    }

    @Test
    fun testEquals() {
        // Not equal
        Assertions.assertNotEquals(Char32(0)..Char32(9), Char32(0)..Char32(10))

        // Componentwise equality
        Assertions.assertEquals(Char32(0)..Char32(9), Char32(0)..Char32(9))

        // Both are empty
        Assertions.assertEquals(Char32(9)..Char32(0), Char32(10)..Char32(5))
    }

    @Test
    operator fun iterator() {
        for ((index, char32) in (Char32(0)..Char32(9)).withIndex()) {
            Assertions.assertEquals(index, char32.code)
        }

        val c32iter = (Char32(0)..Char32(9)).iterator()
        for (i in 0..9) {
            val current = c32iter.next()
            Assertions.assertEquals(i, current.code)
            Assertions.assertEquals(Char32(i), current)
        }

        val c32iter2 = (Char32(0)..<Char32(9)).iterator()
        for (i in 0..8) {
            val current = c32iter2.nextChar32()
            Assertions.assertEquals(i, current.code)
        }

        val c32iter3 = (Char32(0)..<Char32(9)).iterator()
        for (i in 0..8) {
            val current = c32iter3.nextInt()
            Assertions.assertEquals(i, current)
        }

        val c32iter4 = (Char32.valueOf('A')..Char32.valueOf('Z')).iterator()
        for (chr in ('A'..'Z')) {
            val current = c32iter4.nextChar()
            Assertions.assertEquals(chr, current)
        }
    }
}
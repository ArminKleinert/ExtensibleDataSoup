package kleinert.soap.data

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Char32ProgressionTest {
    @Test
    fun getFirst() {
        val c32p = Char32Progression(Char32(0), Char32(9), 1)
        Assertions.assertEquals(0, c32p.first.code)

        val c32p2 = Char32Progression(Char32(0), Char32(9), 2)
        Assertions.assertEquals(0, c32p2.first.code)

        val c32p3 = Char32Progression(Char32(9), Char32(1), -1)
        Assertions.assertEquals(9, c32p3.first.code)
    }

    @Test
    fun getLast() {
        val c32p = Char32Progression(Char32(0), Char32(9), 1)
        Assertions.assertEquals(9, c32p.last.code)

        val c32p2 = Char32Progression(Char32(0), Char32(9), 2)
        Assertions.assertEquals(8, c32p2.last.code)

        val c32p3 = Char32Progression(Char32(9), Char32(1), -1)
        Assertions.assertEquals(1, c32p3.last.code)
    }

    @Test
    fun getStart() {
        val c32p = Char32Progression(Char32(0), Char32(9), 1)
        Assertions.assertEquals(0, c32p.start.code)

        val c32p2 = Char32Progression(Char32(0), Char32(9), 2)
        Assertions.assertEquals(0, c32p2.start.code)

        val c32p3 = Char32Progression(Char32(9), Char32(1), -1)
        Assertions.assertEquals(9, c32p3.start.code)
    }

    @Test
    fun getEndInclusive() {
        val c32p = Char32Progression(Char32(0), Char32(9), 1)
        Assertions.assertEquals(9, c32p.endInclusive.code)

        val c32p2 = Char32Progression(Char32(0), Char32(9), 2)
        Assertions.assertEquals(9, c32p2.endInclusive.code)

        val c32p3 = Char32Progression(Char32(9), Char32(1), -1)
        Assertions.assertEquals(1, c32p3.endInclusive.code)
    }

    @Test
    fun getStep() {
        val c32p = Char32Progression(Char32(0), Char32(9), 1)
        Assertions.assertEquals(1, c32p.step)

        val c32p2 = Char32Progression(Char32(0), Char32(9), 2)
        Assertions.assertEquals(2, c32p2.step)

        val c32p3 = Char32Progression(Char32(9), Char32(1), -1)
        Assertions.assertEquals(-1, c32p3.step)
    }

    @Test
    operator fun iterator() {
        for ((index, char32) in Char32Progression(Char32(0), Char32(9), 1).withIndex()) {
            Assertions.assertEquals(index, char32.code)
        }

        val c32iter = Char32Progression(Char32(9), Char32(0), -1).iterator()
        for (i in 9 downTo 0) {
            val current = c32iter.next()
            Assertions.assertEquals(i, current.code)
            Assertions.assertEquals(Char32(i), current)
        }

        val c32iter2 = Char32Progression(Char32(9), Char32(0), -1).iterator()
        for (i in 9 downTo 0) {
            val current = c32iter2.nextChar32()
            Assertions.assertEquals(i, current.code)
            Assertions.assertEquals(Char32(i), current)
        }

        val c32iter3 = Char32Progression(Char32(9), Char32(0), -1).iterator()
        for (i in 9 downTo 0) {
            val current = c32iter3.nextInt()
            Assertions.assertEquals(i, current)
        }
    }

    @Test
    fun isEmpty() {
        Assertions.assertFalse(Char32Progression(Char32(0), Char32(9), 1).isEmpty())
        Assertions.assertFalse(Char32Progression(Char32(0), Char32(0), 1).isEmpty())
        Assertions.assertFalse(Char32Progression(Char32(0), Char32(0), -1).isEmpty())

        Assertions.assertTrue(Char32Progression(Char32(1), Char32(0), 1).isEmpty())
        Assertions.assertTrue(Char32Progression(Char32(0), Char32(1), -1).isEmpty())
    }

    @Test
    fun testEquals() {
        // Not equal
        Assertions.assertNotEquals(
            Char32Progression(Char32(0), Char32(9), 1),
            Char32Progression(Char32(0), Char32(10), 3)
        )

        // Componentwise equality
        Assertions.assertEquals(
            Char32Progression(Char32(0), Char32(9), 1),
            Char32Progression(Char32(0), Char32(9), 1)
        )

        // Both are empty
        Assertions.assertEquals(
            Char32Progression(Char32(1), Char32(0), 1),
            Char32Progression(Char32(3), Char32(0), 1)
        )
    }
}
package kleinert.soap.cons

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ConsTest {
    @Test
    fun of() {
        assertInstanceOf(Cons::class.java, Cons.of<Boolean>())
        assertInstanceOf(Cons::class.java, Cons.of(true))
        assertInstanceOf(Cons::class.java, Cons.of(true, false))

        assertEquals(Cons.of<Boolean>(), Cons.of<Boolean>())
        assertEquals(VList.of<Boolean>(), Cons.of<Boolean>())
        assertEquals(CdrCodedList.of<Boolean>(), Cons.of<Boolean>())
        assertEquals(nullCons<Boolean>(), Cons.of<Boolean>())
        assertEquals(listOf<Boolean>(), Cons.of<Boolean>())

        assertEquals(Cons.of(true), Cons.of(true))
        assertEquals(VList.of(true), Cons.of(true))
        assertEquals(CdrCodedList.of(true), Cons.of(true))
        assertEquals(ConsCell(true, nullCons()), Cons.of(true))
        assertEquals(listOf(true), Cons.of(true))

        assertEquals(Cons.of(true, false), Cons.of(true, false))
        assertEquals(VList.of(true, false), Cons.of(true, false))
        assertEquals(CdrCodedList.of(true, false), Cons.of(true, false))
        assertEquals(ConsCell(true, ConsCell(false, nullCons())), Cons.of(true, false))
        assertEquals(listOf(true, false), Cons.of(true, false))
    }

    @Test
    fun fromIterable() {
        assertInstanceOf(Cons::class.java, Cons.from<Boolean>(arrayOf()))
        assertInstanceOf(Cons::class.java, Cons.from<Boolean>(listOf()))
        assertInstanceOf(Cons::class.java, Cons.from(sequenceOf<Boolean>().asIterable()))

        assertEquals(nullCons<Boolean>(), Cons.from<Boolean>(arrayOf()))
        assertEquals(nullCons<Boolean>(), Cons.from<Boolean>(listOf()))
        assertEquals(nullCons<Boolean>(), Cons.from(sequenceOf<Boolean>().asIterable()))

        assertInstanceOf(Cons::class.java, Cons.from(arrayOf(true)))
        assertInstanceOf(Cons::class.java, Cons.from(listOf(true)))
        assertInstanceOf(Cons::class.java, Cons.from(sequenceOf(true).asIterable()))

        assertEquals(Cons.of(true), Cons.from(arrayOf(true)))
        assertEquals(Cons.of(true), Cons.from(listOf(true)))
        assertEquals(Cons.of(true), Cons.from(sequenceOf(true).asIterable()))

        assertInstanceOf(Cons::class.java, Cons.from(arrayOf(true, false)))
        assertInstanceOf(Cons::class.java, Cons.from(listOf(true, false)))
        assertInstanceOf(Cons::class.java, Cons.from(sequenceOf(true, false).asIterable()))

        assertEquals(Cons.of(true, false), Cons.from(arrayOf(true, false)))
        assertEquals(Cons.of(true, false), Cons.from(listOf(true, false)))
        assertEquals(Cons.of(true, false), Cons.from(sequenceOf(true, false).asIterable()))
    }

    @Test
    fun wrapList() {
        assertInstanceOf(CdrCodedList::class.java, Cons.wrapList<Boolean>(listOf()))
        assertEquals(CdrCodedList<Boolean>(), Cons.wrapList<Boolean>(listOf()))

        assertInstanceOf(CdrCodedList::class.java, Cons.wrapList(listOf(true)))
        assertEquals(CdrCodedList.of(true), Cons.wrapList(listOf(true)))

        assertInstanceOf(CdrCodedList::class.java, Cons.wrapList(listOf(true, false)))
        assertEquals(CdrCodedList.of(true, false), Cons.wrapList(listOf(true, false)))
    }

    @Test
    fun randomAccess() {
        assertInstanceOf(RandomAccess::class.java, Cons.randomAccess<Boolean>(listOf()))
        assertInstanceOf(Cons::class.java, Cons.randomAccess<Boolean>(listOf()))
        assertEquals(Cons.of<Boolean>(), Cons.randomAccess<Boolean>(listOf()))

        assertInstanceOf(RandomAccess::class.java, Cons.randomAccess(listOf(true)))
        assertInstanceOf(Cons::class.java, Cons.randomAccess<Boolean>(listOf()))
        assertEquals(Cons.of(true), Cons.randomAccess(listOf(true)))

        assertInstanceOf(RandomAccess::class.java, Cons.randomAccess(listOf(true, false)))
        assertInstanceOf(Cons::class.java, Cons.randomAccess<Boolean>(listOf()))
        assertEquals(Cons.of(true, false), Cons.randomAccess(listOf(true, false)))
    }

    @Test
    fun log2Access() {
        assertInstanceOf(Cons::class.java, Cons.log2Access<Boolean>(listOf()))
        assertEquals(Cons.of<Boolean>(), Cons.log2Access<Boolean>(listOf()))

        assertInstanceOf(Cons::class.java, Cons.log2Access<Boolean>(listOf()))
        assertEquals(Cons.of(true), Cons.log2Access(listOf(true)))

        assertInstanceOf(Cons::class.java, Cons.log2Access<Boolean>(listOf()))
        assertEquals(Cons.of(true, false), Cons.log2Access(listOf(true, false)))
    }

    @Test
    fun singlyLinked() {
        assertInstanceOf(NullCons::class.java, Cons.singlyLinked<Boolean>(listOf()))
        assertEquals(nullCons<Boolean>(), Cons.singlyLinked<Boolean>(listOf()))

        assertInstanceOf(Cons::class.java, Cons.singlyLinked<Boolean>(listOf()))
        assertEquals(nullCons<Boolean>().cons(true), Cons.singlyLinked(listOf(true)))

        assertInstanceOf(Cons::class.java, Cons.singlyLinked<Boolean>(listOf()))
        assertEquals(nullCons<Boolean>().cons(false).cons(true), Cons.singlyLinked(listOf(true, false)))
    }

    @Test
    fun equalsConsOf() {
        assertEquals(Cons.of<Boolean>(), CdrCodedList.of<Boolean>())
        assertEquals(Cons.of(true, false), CdrCodedList.of(true, false))

        assertEquals(Cons.of<Boolean>(), nullCons<Boolean>())
        assertEquals(Cons.of(true, false), Cons.singlyLinked(listOf(true, false)))
        assertEquals(Cons.of(true, false), nullCons<Boolean>().cons(false).cons(true))

        assertEquals(Cons.of<Boolean>(), VList.of<Boolean>())
        assertEquals(Cons.of(true, false), VList.of(true, false))

        assertEquals(
            Cons.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
            ConsPair.concat(Cons.from(1..5), Cons.from(6..10))
        )
    }

    @Test
    fun equalsCdrCodedListOf() {
        assertEquals(CdrCodedList.of<Boolean>(), CdrCodedList.of<Boolean>())
        assertEquals(CdrCodedList.of(true, false), CdrCodedList.of(true, false))

        assertEquals(CdrCodedList.of<Boolean>(), nullCons<Boolean>())
        assertEquals(CdrCodedList.of(true, false), Cons.singlyLinked(listOf(true, false)))
        assertEquals(CdrCodedList.of(true, false), nullCons<Boolean>().cons(false).cons(true))

        assertEquals(CdrCodedList.of<Boolean>(), VList.of<Boolean>())
        assertEquals(CdrCodedList.of(true, false), VList.of(true, false))

        assertEquals(
            CdrCodedList.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
            ConsPair.concat(Cons.from(1..5), Cons.from(6..10))
        )
    }

    @Test
    fun equalsEmptyCons() {
        assertEquals(nullCons<Boolean>(), CdrCodedList.of<Boolean>())
        assertEquals(nullCons<Boolean>(), nullCons<Boolean>())
        assertEquals(nullCons<Boolean>(), VList.of<Boolean>())
    }

    @Test
    fun equalsCell() {
        val trueFalse = ConsCell(true, ConsCell(false, nullCons()))
        assertEquals(trueFalse, CdrCodedList.of(true, false))
        assertEquals(trueFalse, Cons.singlyLinked(listOf(true, false)))
        assertEquals(trueFalse, nullCons<Boolean>().cons(false).cons(true))
        assertEquals(trueFalse, VList.of(true, false))

        val oneToTen = ConsCell(
            1,
            ConsCell(
                2,
                ConsCell(
                    3,
                    ConsCell(
                        4,
                        ConsCell(5, ConsCell(6, ConsCell(7, ConsCell(8, ConsCell(9, ConsCell(10, nullCons()))))))
                    )
                )
            )
        )
        assertEquals(oneToTen, ConsPair.concat(Cons.from(1..5), Cons.from(6..10)))
    }

    @Test
    fun equalsVListOf() {
        assertEquals(VList.of<Boolean>(), CdrCodedList.of<Boolean>())
        assertEquals(VList.of(true, false), CdrCodedList.of(true, false))

        assertEquals(VList.of<Boolean>(), nullCons<Boolean>())
        assertEquals(VList.of(true, false), Cons.singlyLinked(listOf(true, false)))
        assertEquals(VList.of(true, false), nullCons<Boolean>().cons(false).cons(true))

        assertEquals(VList.of<Boolean>(), VList.of<Boolean>())
        assertEquals(VList.of(true, false), VList.of(true, false))

        assertEquals(
            VList.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
            ConsPair.concat(Cons.from(1..5), Cons.from(6..10))
        )
    }

    @Test
    fun equalsListOf() {
        assertEquals(listOf<Boolean>(), CdrCodedList.of<Boolean>())
        assertEquals(listOf(true, false), CdrCodedList.of(true, false))

        assertEquals(listOf<Boolean>(), nullCons<Boolean>())
        assertEquals(listOf(true, false), Cons.singlyLinked(listOf(true, false)))
        assertEquals(listOf(true, false), nullCons<Boolean>().cons(false).cons(true))

        assertEquals(listOf<Boolean>(), VList.of<Boolean>())
        assertEquals(listOf(true, false), VList.of(true, false))

        assertEquals(
            listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
            ConsPair.concat(Cons.from(1..5), Cons.from(6..10))
        )
    }
}

package kleinert.soap.cons

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ImmutableListTest {
    @Test
    fun of() {
        assertInstanceOf(ImmutableLazyList::class.java, ImmutableLazyList.of<Boolean>())
        assertInstanceOf(ImmutableLazyList::class.java, ImmutableLazyList.of(true))
        assertInstanceOf(ImmutableLazyList::class.java, ImmutableLazyList.of(true, false))

        assertEquals(ImmutableLazyList.of<Boolean>(), ImmutableLazyList.of<Boolean>())
        assertEquals(VList.of<Boolean>(), ImmutableLazyList.of<Boolean>())
        assertEquals(CdrCodedList.of<Boolean>(), ImmutableLazyList.of<Boolean>())
        assertEquals(nullCons<Boolean>(), ImmutableLazyList.of<Boolean>())
        assertEquals(listOf<Boolean>(), ImmutableLazyList.of<Boolean>())

        assertEquals(ImmutableLazyList.of(true), ImmutableLazyList.of(true))
        assertEquals(VList.of(true), ImmutableLazyList.of(true))
        assertEquals(CdrCodedList.of(true), ImmutableLazyList.of(true))
        assertEquals(ConsCell(true, nullCons()), ImmutableLazyList.of(true))
        assertEquals(listOf(true), ImmutableLazyList.of(true))

        assertEquals(ImmutableLazyList.of(true, false), ImmutableLazyList.of(true, false))
        assertEquals(VList.of(true, false), ImmutableLazyList.of(true, false))
        assertEquals(CdrCodedList.of(true, false), ImmutableLazyList.of(true, false))
        assertEquals(ConsCell(true, ConsCell(false, nullCons())), ImmutableLazyList.of(true, false))
        assertEquals(listOf(true, false), ImmutableLazyList.of(true, false))
    }

    @Test
    fun fromIterable() {
        assertInstanceOf(ImmutableLazyList::class.java, ImmutableLazyList.from<Boolean>(arrayOf()))
        assertInstanceOf(ImmutableLazyList::class.java, ImmutableLazyList.from<Boolean>(listOf()))
        assertInstanceOf(ImmutableLazyList::class.java, ImmutableLazyList.from(sequenceOf<Boolean>().asIterable()))

        assertEquals(nullCons<Boolean>(), ImmutableLazyList.from<Boolean>(arrayOf()))
        assertEquals(nullCons<Boolean>(), ImmutableLazyList.from<Boolean>(listOf()))
        assertEquals(nullCons<Boolean>(), ImmutableLazyList.from(sequenceOf<Boolean>().asIterable()))

        assertInstanceOf(ImmutableLazyList::class.java, ImmutableLazyList.from(arrayOf(true)))
        assertInstanceOf(ImmutableLazyList::class.java, ImmutableLazyList.from(listOf(true)))
        assertInstanceOf(ImmutableLazyList::class.java, ImmutableLazyList.from(sequenceOf(true).asIterable()))

        assertEquals(ImmutableLazyList.of(true), ImmutableLazyList.from(arrayOf(true)))
        assertEquals(ImmutableLazyList.of(true), ImmutableLazyList.from(listOf(true)))
        assertEquals(ImmutableLazyList.of(true), ImmutableLazyList.from(sequenceOf(true).asIterable()))

        assertInstanceOf(ImmutableLazyList::class.java, ImmutableLazyList.from(arrayOf(true, false)))
        assertInstanceOf(ImmutableLazyList::class.java, ImmutableLazyList.from(listOf(true, false)))
        assertInstanceOf(ImmutableLazyList::class.java, ImmutableLazyList.from(sequenceOf(true, false).asIterable()))

        assertEquals(ImmutableLazyList.of(true, false), ImmutableLazyList.from(arrayOf(true, false)))
        assertEquals(ImmutableLazyList.of(true, false), ImmutableLazyList.from(listOf(true, false)))
        assertEquals(ImmutableLazyList.of(true, false), ImmutableLazyList.from(sequenceOf(true, false).asIterable()))
    }

    @Test
    fun wrapList() {
        assertInstanceOf(CdrCodedList::class.java, ImmutableLazyList.wrapList<Boolean>(listOf()))
        assertEquals(CdrCodedList<Boolean>(), ImmutableLazyList.wrapList<Boolean>(listOf()))

        assertInstanceOf(CdrCodedList::class.java, ImmutableLazyList.wrapList(listOf(true)))
        assertEquals(CdrCodedList.of(true), ImmutableLazyList.wrapList(listOf(true)))

        assertInstanceOf(CdrCodedList::class.java, ImmutableLazyList.wrapList(listOf(true, false)))
        assertEquals(CdrCodedList.of(true, false), ImmutableLazyList.wrapList(listOf(true, false)))
    }

    @Test
    fun randomAccess() {
        assertInstanceOf(RandomAccess::class.java, ImmutableLazyList.randomAccess<Boolean>(listOf()))
        assertInstanceOf(ImmutableLazyList::class.java, ImmutableLazyList.randomAccess<Boolean>(listOf()))
        assertEquals(ImmutableLazyList.of<Boolean>(), ImmutableLazyList.randomAccess<Boolean>(listOf()))

        assertInstanceOf(RandomAccess::class.java, ImmutableLazyList.randomAccess(listOf(true)))
        assertInstanceOf(ImmutableLazyList::class.java, ImmutableLazyList.randomAccess<Boolean>(listOf()))
        assertEquals(ImmutableLazyList.of(true), ImmutableLazyList.randomAccess(listOf(true)))

        assertInstanceOf(RandomAccess::class.java, ImmutableLazyList.randomAccess(listOf(true, false)))
        assertInstanceOf(ImmutableLazyList::class.java, ImmutableLazyList.randomAccess<Boolean>(listOf()))
        assertEquals(ImmutableLazyList.of(true, false), ImmutableLazyList.randomAccess(listOf(true, false)))
    }

    @Test
    fun log2Access() {
        assertInstanceOf(ImmutableLazyList::class.java, ImmutableLazyList.log2Access<Boolean>(listOf()))
        assertEquals(ImmutableLazyList.of<Boolean>(), ImmutableLazyList.log2Access<Boolean>(listOf()))

        assertInstanceOf(ImmutableLazyList::class.java, ImmutableLazyList.log2Access<Boolean>(listOf()))
        assertEquals(ImmutableLazyList.of(true), ImmutableLazyList.log2Access(listOf(true)))

        assertInstanceOf(ImmutableLazyList::class.java, ImmutableLazyList.log2Access<Boolean>(listOf()))
        assertEquals(ImmutableLazyList.of(true, false), ImmutableLazyList.log2Access(listOf(true, false)))
    }

    @Test
    fun singlyLinked() {
        assertInstanceOf(NullCons::class.java, ImmutableLazyList.singlyLinked<Boolean>(listOf()))
        assertEquals(nullCons<Boolean>(), ImmutableLazyList.singlyLinked<Boolean>(listOf()))

        assertInstanceOf(ImmutableLazyList::class.java, ImmutableLazyList.singlyLinked<Boolean>(listOf()))
        assertEquals(nullCons<Boolean>().cons(true), ImmutableLazyList.singlyLinked(listOf(true)))

        assertInstanceOf(ImmutableLazyList::class.java, ImmutableLazyList.singlyLinked<Boolean>(listOf()))
        assertEquals(nullCons<Boolean>().cons(false).cons(true), ImmutableLazyList.singlyLinked(listOf(true, false)))
    }

    @Test
    fun equalsConsOf() {
        assertEquals(ImmutableLazyList.of<Boolean>(), CdrCodedList.of<Boolean>())
        assertEquals(ImmutableLazyList.of(true, false), CdrCodedList.of(true, false))

        assertEquals(ImmutableLazyList.of<Boolean>(), nullCons<Boolean>())
        assertEquals(ImmutableLazyList.of(true, false), ImmutableLazyList.singlyLinked(listOf(true, false)))
        assertEquals(ImmutableLazyList.of(true, false), nullCons<Boolean>().cons(false).cons(true))

        assertEquals(ImmutableLazyList.of<Boolean>(), VList.of<Boolean>())
        assertEquals(ImmutableLazyList.of(true, false), VList.of(true, false))

        assertEquals(
            ImmutableLazyList.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
            ListPair.concat(ImmutableLazyList.from(1..5), ImmutableLazyList.from(6..10))
        )
    }

    @Test
    fun equalsCdrCodedListOf() {
        assertEquals(CdrCodedList.of<Boolean>(), CdrCodedList.of<Boolean>())
        assertEquals(CdrCodedList.of(true, false), CdrCodedList.of(true, false))

        assertEquals(CdrCodedList.of<Boolean>(), nullCons<Boolean>())
        assertEquals(CdrCodedList.of(true, false), ImmutableLazyList.singlyLinked(listOf(true, false)))
        assertEquals(CdrCodedList.of(true, false), nullCons<Boolean>().cons(false).cons(true))

        assertEquals(CdrCodedList.of<Boolean>(), VList.of<Boolean>())
        assertEquals(CdrCodedList.of(true, false), VList.of(true, false))

        assertEquals(
            CdrCodedList.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
            ListPair.concat(ImmutableLazyList.from(1..5), ImmutableLazyList.from(6..10))
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
        assertEquals(trueFalse, ImmutableLazyList.singlyLinked(listOf(true, false)))
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
        assertEquals(oneToTen, ListPair.concat(ImmutableLazyList.from(1..5), ImmutableLazyList.from(6..10)))
    }

    @Test
    fun equalsVListOf() {
        assertEquals(VList.of<Boolean>(), CdrCodedList.of<Boolean>())
        assertEquals(VList.of(true, false), CdrCodedList.of(true, false))

        assertEquals(VList.of<Boolean>(), nullCons<Boolean>())
        assertEquals(VList.of(true, false), ImmutableLazyList.singlyLinked(listOf(true, false)))
        assertEquals(VList.of(true, false), nullCons<Boolean>().cons(false).cons(true))

        assertEquals(VList.of<Boolean>(), VList.of<Boolean>())
        assertEquals(VList.of(true, false), VList.of(true, false))

        assertEquals(
            VList.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
            ListPair.concat(ImmutableLazyList.from(1..5), ImmutableLazyList.from(6..10))
        )
    }

    @Test
    fun equalsListOf() {
        assertEquals(listOf<Boolean>(), CdrCodedList.of<Boolean>())
        assertEquals(listOf(true, false), CdrCodedList.of(true, false))

        assertEquals(listOf<Boolean>(), nullCons<Boolean>())
        assertEquals(listOf(true, false), ImmutableLazyList.singlyLinked(listOf(true, false)))
        assertEquals(listOf(true, false), nullCons<Boolean>().cons(false).cons(true))

        assertEquals(listOf<Boolean>(), VList.of<Boolean>())
        assertEquals(listOf(true, false), VList.of(true, false))

        assertEquals(
            listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
            ListPair.concat(ImmutableLazyList.from(1..5), ImmutableLazyList.from(6..10))
        )
    }
}

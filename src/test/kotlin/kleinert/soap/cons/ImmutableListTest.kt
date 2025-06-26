package kleinert.soap.cons
//
//import org.junit.jupiter.api.Assertions.*
//import org.junit.jupiter.api.Test
//
//class ImmutableListTest {
//    @Test
//    fun of() {
//        assertInstanceOf(PersistentList::class.java, PersistentList.of<Boolean>())
//        assertInstanceOf(PersistentList::class.java, PersistentList.of(true))
//        assertInstanceOf(PersistentList::class.java, PersistentList.of(true, false))
//
//        assertEquals(PersistentList.of<Boolean>(), PersistentList.of<Boolean>())
//        assertEquals(VList.of<Boolean>(), PersistentList.of<Boolean>())
//        assertEquals(PersistentWrapper.of<Boolean>(), PersistentList.of<Boolean>())
//        assertEquals(nullCons<Boolean>(), PersistentList.of<Boolean>())
//        assertEquals(listOf<Boolean>(), PersistentList.of<Boolean>())
//
//        assertEquals(PersistentList.of(true), PersistentList.of(true))
//        assertEquals(VList.of(true), PersistentList.of(true))
//        assertEquals(PersistentWrapper.of(true), PersistentList.of(true))
//        assertEquals(PersistentListHead(true, nullCons()), PersistentList.of(true))
//        assertEquals(listOf(true), PersistentList.of(true))
//
//        assertEquals(PersistentList.of(true, false), PersistentList.of(true, false))
//        assertEquals(VList.of(true, false), PersistentList.of(true, false))
//        assertEquals(PersistentWrapper.of(true, false), PersistentList.of(true, false))
//        assertEquals(PersistentListHead(true, PersistentListHead(false, nullCons())), PersistentList.of(true, false))
//        assertEquals(listOf(true, false), PersistentList.of(true, false))
//    }
//
//    @Test
//    fun fromIterable() {
//        assertInstanceOf(PersistentList::class.java, PersistentList.from<Boolean>(arrayOf()))
//        assertInstanceOf(PersistentList::class.java, PersistentList.from<Boolean>(listOf()))
//        assertInstanceOf(PersistentList::class.java, PersistentList.from(sequenceOf<Boolean>().asIterable()))
//
//        assertEquals(nullCons<Boolean>(), PersistentList.from<Boolean>(arrayOf()))
//        assertEquals(nullCons<Boolean>(), PersistentList.from<Boolean>(listOf()))
//        assertEquals(nullCons<Boolean>(), PersistentList.from(sequenceOf<Boolean>().asIterable()))
//
//        assertInstanceOf(PersistentList::class.java, PersistentList.from(arrayOf(true)))
//        assertInstanceOf(PersistentList::class.java, PersistentList.from(listOf(true)))
//        assertInstanceOf(PersistentList::class.java, PersistentList.from(sequenceOf(true).asIterable()))
//
//        assertEquals(PersistentList.of(true), PersistentList.from(arrayOf(true)))
//        assertEquals(PersistentList.of(true), PersistentList.from(listOf(true)))
//        assertEquals(PersistentList.of(true), PersistentList.from(sequenceOf(true).asIterable()))
//
//        assertInstanceOf(PersistentList::class.java, PersistentList.from(arrayOf(true, false)))
//        assertInstanceOf(PersistentList::class.java, PersistentList.from(listOf(true, false)))
//        assertInstanceOf(PersistentList::class.java, PersistentList.from(sequenceOf(true, false).asIterable()))
//
//        assertEquals(PersistentList.of(true, false), PersistentList.from(arrayOf(true, false)))
//        assertEquals(PersistentList.of(true, false), PersistentList.from(listOf(true, false)))
//        assertEquals(PersistentList.of(true, false), PersistentList.from(sequenceOf(true, false).asIterable()))
//    }
//
//    @Test
//    fun wrapList() {
//        assertInstanceOf(PersistentWrapper::class.java, PersistentList.wrapList<Boolean>(listOf()))
//        assertEquals(PersistentWrapper<Boolean>(), PersistentList.wrapList<Boolean>(listOf()))
//
//        assertInstanceOf(PersistentWrapper::class.java, PersistentList.wrapList(listOf(true)))
//        assertEquals(PersistentWrapper.of(true), PersistentList.wrapList(listOf(true)))
//
//        assertInstanceOf(PersistentWrapper::class.java, PersistentList.wrapList(listOf(true, false)))
//        assertEquals(PersistentWrapper.of(true, false), PersistentList.wrapList(listOf(true, false)))
//    }
//
//    @Test
//    fun randomAccess() {
//        assertInstanceOf(RandomAccess::class.java, PersistentList.randomAccess<Boolean>(listOf()))
//        assertInstanceOf(PersistentList::class.java, PersistentList.randomAccess<Boolean>(listOf()))
//        assertEquals(PersistentList.of<Boolean>(), PersistentList.randomAccess<Boolean>(listOf()))
//
//        assertInstanceOf(RandomAccess::class.java, PersistentList.randomAccess(listOf(true)))
//        assertInstanceOf(PersistentList::class.java, PersistentList.randomAccess<Boolean>(listOf()))
//        assertEquals(PersistentList.of(true), PersistentList.randomAccess(listOf(true)))
//
//        assertInstanceOf(RandomAccess::class.java, PersistentList.randomAccess(listOf(true, false)))
//        assertInstanceOf(PersistentList::class.java, PersistentList.randomAccess<Boolean>(listOf()))
//        assertEquals(PersistentList.of(true, false), PersistentList.randomAccess(listOf(true, false)))
//    }
//
//    @Test
//    fun log2Access() {
//        assertInstanceOf(PersistentList::class.java, PersistentList.log2Access<Boolean>(listOf()))
//        assertEquals(PersistentList.of<Boolean>(), PersistentList.log2Access<Boolean>(listOf()))
//
//        assertInstanceOf(PersistentList::class.java, PersistentList.log2Access<Boolean>(listOf()))
//        assertEquals(PersistentList.of(true), PersistentList.log2Access(listOf(true)))
//
//        assertInstanceOf(PersistentList::class.java, PersistentList.log2Access<Boolean>(listOf()))
//        assertEquals(PersistentList.of(true, false), PersistentList.log2Access(listOf(true, false)))
//    }
//
//    @Test
//    fun singlyLinked() {
//        assertInstanceOf(EmptyList::class.java, PersistentList.singlyLinked<Boolean>(listOf()))
//        assertEquals(nullCons<Boolean>(), PersistentList.singlyLinked<Boolean>(listOf()))
//
//        assertInstanceOf(PersistentList::class.java, PersistentList.singlyLinked<Boolean>(listOf()))
//        assertEquals(nullCons<Boolean>().cons(true), PersistentList.singlyLinked(listOf(true)))
//
//        assertInstanceOf(PersistentList::class.java, PersistentList.singlyLinked<Boolean>(listOf()))
//        assertEquals(nullCons<Boolean>().cons(false).cons(true), PersistentList.singlyLinked(listOf(true, false)))
//    }
//
//    @Test
//    fun equalsConsOf() {
//        assertEquals(PersistentList.of<Boolean>(), PersistentWrapper.of<Boolean>())
//        assertEquals(PersistentList.of(true, false), PersistentWrapper.of(true, false))
//
//        assertEquals(PersistentList.of<Boolean>(), nullCons<Boolean>())
//        assertEquals(PersistentList.of(true, false), PersistentList.singlyLinked(listOf(true, false)))
//        assertEquals(PersistentList.of(true, false), nullCons<Boolean>().cons(false).cons(true))
//
//        assertEquals(PersistentList.of<Boolean>(), VList.of<Boolean>())
//        assertEquals(PersistentList.of(true, false), VList.of(true, false))
//
//        assertEquals(
//            PersistentList.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
//            ListPair.concat(PersistentList.from(1..5), PersistentList.from(6..10))
//        )
//    }
//
//    @Test
//    fun equalsCdrCodedListOf() {
//        assertEquals(PersistentWrapper.of<Boolean>(), PersistentWrapper.of<Boolean>())
//        assertEquals(PersistentWrapper.of(true, false), PersistentWrapper.of(true, false))
//
//        assertEquals(PersistentWrapper.of<Boolean>(), nullCons<Boolean>())
//        assertEquals(PersistentWrapper.of(true, false), PersistentList.singlyLinked(listOf(true, false)))
//        assertEquals(PersistentWrapper.of(true, false), nullCons<Boolean>().cons(false).cons(true))
//
//        assertEquals(PersistentWrapper.of<Boolean>(), VList.of<Boolean>())
//        assertEquals(PersistentWrapper.of(true, false), VList.of(true, false))
//
//        assertEquals(
//            PersistentWrapper.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
//            ListPair.concat(PersistentList.from(1..5), PersistentList.from(6..10))
//        )
//    }
//
//    @Test
//    fun equalsEmptyCons() {
//        assertEquals(nullCons<Boolean>(), PersistentWrapper.of<Boolean>())
//        assertEquals(nullCons<Boolean>(), nullCons<Boolean>())
//        assertEquals(nullCons<Boolean>(), VList.of<Boolean>())
//    }
//
//    @Test
//    fun equalsCell() {
//        val trueFalse = PersistentListHead(true, PersistentListHead(false, nullCons()))
//        assertEquals(trueFalse, PersistentWrapper.of(true, false))
//        assertEquals(trueFalse, PersistentList.singlyLinked(listOf(true, false)))
//        assertEquals(trueFalse, nullCons<Boolean>().cons(false).cons(true))
//        assertEquals(trueFalse, VList.of(true, false))
//
//        val oneToTen = PersistentListHead(
//            1,
//            PersistentListHead(
//                2,
//                PersistentListHead(
//                    3,
//                    PersistentListHead(
//                        4,
//                        PersistentListHead(5, PersistentListHead(6, PersistentListHead(7, PersistentListHead(8, PersistentListHead(9, PersistentListHead(10, nullCons()))))))
//                    )
//                )
//            )
//        )
//        assertEquals(oneToTen, ListPair.concat(PersistentList.from(1..5), PersistentList.from(6..10)))
//    }
//
//    @Test
//    fun equalsVListOf() {
//        assertEquals(VList.of<Boolean>(), PersistentWrapper.of<Boolean>())
//        assertEquals(VList.of(true, false), PersistentWrapper.of(true, false))
//
//        assertEquals(VList.of<Boolean>(), nullCons<Boolean>())
//        assertEquals(VList.of(true, false), PersistentList.singlyLinked(listOf(true, false)))
//        assertEquals(VList.of(true, false), nullCons<Boolean>().cons(false).cons(true))
//
//        assertEquals(VList.of<Boolean>(), VList.of<Boolean>())
//        assertEquals(VList.of(true, false), VList.of(true, false))
//
//        assertEquals(
//            VList.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
//            ListPair.concat(PersistentList.from(1..5), PersistentList.from(6..10))
//        )
//    }
//
//    @Test
//    fun equalsListOf() {
//        assertEquals(listOf<Boolean>(), PersistentWrapper.of<Boolean>())
//        assertEquals(listOf(true, false), PersistentWrapper.of(true, false))
//
//        assertEquals(listOf<Boolean>(), nullCons<Boolean>())
//        assertEquals(listOf(true, false), PersistentList.singlyLinked(listOf(true, false)))
//        assertEquals(listOf(true, false), nullCons<Boolean>().cons(false).cons(true))
//
//        assertEquals(listOf<Boolean>(), VList.of<Boolean>())
//        assertEquals(listOf(true, false), VList.of(true, false))
//
//        assertEquals(
//            listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
//            ListPair.concat(PersistentList.from(1..5), PersistentList.from(6..10))
//        )
//    }
//}

import kleinert.soap.*
import kleinert.soap.data.PackedList
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.jvm.functions.FunctionN

fun examples1() {
    fun testFunDefault(s: String) {
        val data = EDNSoapReader.readString(s)
        println("\"" + data + "\" " + (data?.javaClass ?: "null"))
    }

    fun testFunExtend(s: String) {
        val data = EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions)
        println("\"" + data + "\" " + (data?.javaClass ?: "null"))
    }
    testFunDefault("\\a") // "a Class Char"

    // Unicode sequences in strings work.
    testFunDefault("\"\\uD83C\\uDF81\"") // "🎁" class java.lang.String
    testFunExtend("\"\\x0001F381\"") // "🎁" class java.lang.String

    // UTF-32 evaluates to strings.
    testFunDefault("\\u0660") // "٠" class java.lang.Character
    testFunExtend("#\\u0660") // "٠" class java.lang.String
    testFunExtend("#\\u0001F381") // "🎁" class java.lang.String

    // Discard is right-associative.
    testFunDefault("#_ #_ \"1\" \\a 1") // "1" class java.lang.Long
    testFunDefault("#_#_ \"1\" \\a 1") // "1" class java.lang.Long

    // Comments are ignored
    testFunDefault(";bc\n123") // "123" class java.lang.Long

    testFunDefault("#uuid \"f81d4fae-7dec-11d0-a765-00a0c91e6bf6\"") // "f81d4fae-7dec-11d0-a765-00a0c91e6bf6" class java.util.UUID

    testFunDefault("#inst \"1985-04-12T23:20:50.52Z\"") // "1985-04-12T23:20:50.520Z" class java.time.Instant

    testFunDefault("1,")
    testFunDefault("1 2")

    testFunDefault("[1]")
}

fun examples2() {
    fun mapOrListToPair(elem: Any?): Any = when (elem) {
        is Map<*, *> -> (elem["first"] to elem["second"])
        is List<*> -> elem[0] to elem[1]
        else -> throw IllegalArgumentException()
    }

    run {
        val decoders = mapOf("my/pair" to ::mapOrListToPair)
        println(
            EDNSoapReader.readString(
                "[ #my/pair {\"first\" 4 \"second\" 5} #my/pair [4 5] ] ",
                EDNSoapOptions.defaultOptions.copy(ednClassDecoders = decoders)
            )
        )
    } // Output: [(4, 5), (4, 5)]

    run {
        // Allowing more freedom in naming here.
        val decoders = mapOf("pair" to ::mapOrListToPair)
        println(
            EDNSoapReader.readString(
                "[ #pair {\"first\" 4 \"second\" 5} #pair [4 5] ] ",
                EDNSoapOptions.defaultOptions.copy(allowMoreEncoderDecoderNames = true, ednClassDecoders = decoders)
            )
        )
    } // Output: [(4, 5), (4, 5)]
}

fun main(args: Array<String>) {
    //examples1()
    //examples2()
//    println(EDNSoapReader.readString("(#_\\a)"))
//    println(EDNSoapReader.readString("[#_\\a]"))
//    println(EDNSoapReader.readString("#{#_\\a}"))
//    println(EDNSoapReader.readString("{1 #_\\a}"))

//    run {
//        val vl = EDNSoapReader.readString("(1 2 3)")
//        println(vl is VList<*>)
//        println(vl)
//        println(EDNSoapWriter.encode(vl))
//    }
//    run {
//        val vl = EDNSoapReader.readString("(\"ab\" \"cd\")")
//        println(vl is VList<*>)
//        println(vl)
//        println(EDNSoapWriter.encode(vl))
//    }
//    val al = ArrayList(listOf(1, 2, 3))
//    al.component1()
//
//    run {
//        println(BitSet.valueOf(longArrayOf(42, 12)))
//    }

//    run {
//        val vl = VList.of<Int>(1, 2, 3, 4, 5, 6)
//        val vld = vl.drop(2)
//        println("$vl $vld")
//    }
//
//    run {
//        val vl = VList.of(1, 2, 3, 4, 5)
//        val vld = vl.reversed()
//        println("$vl $vld ${vld.javaClass}")
//    }
//
//    run {
//        val vl: Cons<Int> = VList.of(1, 2, 3, 4, 5)
//        val vld = vl.reversed()
//        println("$vl $vld ${vld.javaClass}")
//    }
//
//    run {
//        val vl: List<Int> = VList.of(1, 2, 3, 4, 5)
//        val vld = vl.reversed()
//        println("$vl $vld ${vld.javaClass}")
//    }

//    run {
//        val vl = VList.of(1, 2, 3)
//        val vln = vl.prepend(listOf(6, 7))
//        println("$vl $vln ${vln.javaClass}")
//    }

//    run {
//        val bigList = Cons.wrapList((1..100000).toList())
//        val biggerList = bigList + bigList + bigList
//        //val l = LazyCons.take(1, LazyCons.map({it+1}, Cons.fromIterable(1..10000000000)))
//        val l = LazyCons.take(4, LazyCons.filter({it > 100 && it % 2 == 0}, LazyCons.drop(4, LazyCons.map({ it + 1 }, biggerList))))
//        println(l)
//        println("Result: " + l.toList())
//    }

//    run {
//        val biggerList = LazyCons.of((1..500).iterator())
//        val l = LazyCons.take(4, LazyCons.filter({it > 100 && it % 2 == 0}, LazyCons.drop(4, LazyCons.map({ it + 1 }, biggerList))))
//        println(l)
//        println("Result: " + l.toList())
//        println(biggerList.toList())
//    }

//    run {
//        val lst = Cons.concat(
//            Cons.of(1, 2, 3, 4, 5), Cons.of(6, 7, 8, 9, 10),
//            Cons.of(11, 12, 13, 14, 15), Cons.of(16, 17, 18, 19, 20)
//        )
//        println(lst)
//    }
//
//    run {
//        val lst = PersistentList.of(1, 2, 3, 4, 5)
//        println("????")
//        val seq = LazyList.cycle(lst)
//        println("!!!!")
//        println(seq.take(22))
//        println(seq.take(22))
//        println(seq.take(22).javaClass)
//        println(seq)
//    }
//
//    run {
//        val seq = LazyList.repeat(1)
//        println(seq.take(22))
//    }
//
//    run {
//        val seq = LazyList.repeatedly { Random.nextInt(0..9) }
//        println(seq.take(22))
//    }
//
//    run {
//        val seq = Cons.from(sequenceOf(1, 2, 3, 4, 5))
//        println(seq)
//    }
//
//    run {
//        val seq = LazyList.iterate({ it * 2 }, 1L)
//        println(seq.take(33))
//    }

//    run {
//        val obj = listOf(
//            1,
//            2.0,
//            BigInteger.valueOf(100),
//            BigDecimal.valueOf(100),
//            "abc",
//            '9',
//            VList.of(1, 2, 3),
//            sequenceOf(1, 2, 3),
//            true,
//            null
//        )
//        println(
//            EDNSoapWriter.pprintS(
//                obj,
//                options = EDNSoapOptions.defaultOptions.copy(decodingSequenceSeparator = " ")
//            )
//        )
//    }

//    run {
//        data class AO(val n: Int)
//
//        val input = "#test/AO 99"
//        val options = EDNSoapOptions.defaultOptions.copy(
//            //allowMoreEncoderDecoderNames = true,
//            ednClassDecoders = mapOf(
//                "test/AO" to { AO((it as Number).toInt()) }
//            ), ednClassEncoders = mapOf(
//                AO::class.java to { "test/AO" to mapOf(Keyword.keyword(":n") to (it as AO).n) }
//            ))
//        println(input)
//        val data = EDNSoapReader.readString(input, options)
//        println(data)
//        val output = EDNSoapWriter.pprintln(data, options = options)
//    }

//    run {
//        val seq = LazyList.distinct(LazyList.of(1,2,3,3,2,1,4))
//        println(LazyList.distinct(seq))
//    }
    run {
        val lst = PackedList(3, 2, listOf(1,2,3,4,5,6))
        println(lst.unpack())
        println(lst.unpack().indexOf(listOf(3,4)))
        println(lst.indexOf(listOf(3,4)))
        println(lst[1])
        println(lst[1, 1])
    }
    run {
        val text = "#array2d [[1 2] [3 4] [5 6]]"
        val parsed = EDNSoapReader.readString(text, EDNSoapOptions.extendedReaderOptions(EDNSoapOptions.allDecoders))
        println(parsed?.javaClass ?: "null")
        println(parsed)
    }
    run{
        println("5E-1".toDouble())
    }
}

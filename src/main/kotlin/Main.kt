import kleinert.soap.EDNSoapOptions
import kleinert.soap.EDNSoapReader
import kleinert.soap.EDNSoapWriter
import kleinert.soap.VList
import java.util.*

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
    fun mapOrListToPair(elem: Any?): Any? = when (elem) {
        is Map<*, *> -> (elem["first"] to elem["second"])
        is List<*> -> elem[0] to elem[1]
        else -> throw IllegalArgumentException()
    }

    run {
        val decoders = mapOf("my/pair" to ::mapOrListToPair)
        println(EDNSoapReader.readString(
            "[ #my/pair {\"first\" 4 \"second\" 5} #my/pair [4 5] ] ",
            EDNSoapOptions.defaultOptions.copy(ednClassDecoders = decoders)
        ))
    } // Output: [(4, 5), (4, 5)]

    run {
        // Allowing more freedom in naming here.
        val decoders = mapOf("pair" to ::mapOrListToPair)
        println(EDNSoapReader.readString(
            "[ #pair {\"first\" 4 \"second\" 5} #pair [4 5] ] ",
            EDNSoapOptions.defaultOptions.copy(allowMoreEncoderDecoderNames = true, ednClassDecoders = decoders)
        ))
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

    run {
        val vl = VList.of<Int>(1, 2, 3, 4, 5, 6)
        val vld = vl.drop(2)
        println("$vl $vld")
    }
}

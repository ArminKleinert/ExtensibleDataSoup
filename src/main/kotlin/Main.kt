import kleinert.soap.data.Keyword
import kleinert.soap.data.Complex
import kleinert.soap.data.Char32
import kleinert.soap.data.PersistentList
import kleinert.soap.data.PersistentVector
import kleinert.soap.data.PersistentSet
import kleinert.soap.data.PersistentMap
import kleinert.soap.data.IObj
import kleinert.soap.data.Ratio
import kleinert.soap.data.Symbol
import kleinert.soap.edn.EDN
import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant
import java.util.UUID
import java.util.LinkedList

fun f() {
    EDN.pprintln(Symbol.symbol("symbol"))                       // symbol
    EDN.pprintln(Symbol.symbol("namespace/symbol"))             // namespace/symbol
    EDN.pprintln(Keyword["keyword"])                            // :keyword
    EDN.pprintln(Keyword["namespace/keyword"])                  // :namespace/keyword

    EDN.pprintln("string")                                      // "string"
    EDN.pprintln('c')                                           // \c
    EDN.pprintln('☦')                                           // \u2626

    // Without special options, Char32 is printed as String.
    EDN.pprintln(Char32(0x0001F381))                            // "🎁"

    // PersistentLists, Sequences and other Iterables (which are not Lists, Sets or Maps) are printed with ().
    EDN.pprintln(PersistentList.of(1, 2, 3))                    // (1, 2, 3)
    EDN.pprintln(sequenceOf(1, 2, 3))                           // (1, 2, 3)
    EDN.pprintln(1..3)                                          // (1, 2, 3)

    // Vectors, Arrays and Lists (other than PersistentList) are all printed with [].
    EDN.pprintln(PersistentVector.of(1, 2, 3))                  // [1, 2, 3]
    EDN.pprintln(listOf(1, 2, 3))                               // [1, 2, 3]
    EDN.pprintln(intArrayOf(1, 2, 3))                           // [1, 2, 3]

    // PersistentSet and other Sets are treated the same
    EDN.pprintln(PersistentSet.of(1, 2, 3))                     // #{1, 2, 3}
    EDN.pprintln(setOf(1, 2, 3))                                // #{1, 2, 3}

    // PersistentMap and other Maps are treated the same
    EDN.pprintln(PersistentMap.of(1 to 2, 3 to 4))              // {1 2, 3 4}
    EDN.pprintln(mapOf(1 to 2, 3 to 4))                         // {1 2, 3 4}

    EDN.pprintln(0xC0FFEE)                                      // 12648430
    EDN.pprintln(12648430)                                      // 12648430
    EDN.pprintln(BigInteger.valueOf(12648430))                  // 12648430N
    EDN.pprintln(5.0)                                           // 5.0
    EDN.pprintln(BigDecimal.valueOf(5.0))                       // 5.0M
    EDN.pprintln(Ratio.valueOf(5, 6))                           // 5/6

    EDN.pprintln(null)                                          // nil
    EDN.pprintln(true)                                          // true
    EDN.pprintln(false)                                         // false

    EDN.pprintln(UUID.fromString("f81d4fae-7dec-11d0-a765-00a0c91e6bf6")) // #uuid "f81d4fae-7dec-11d0-a765-00a0c91e6bf6"
    EDN.pprintln(Instant.parse("1985-04-12T23:20:50.52Z"))      // #inst "1985-04-12T23:20:50.520Z"

    EDN.pprintln(Double.POSITIVE_INFINITY)                      // ##INF
    EDN.pprintln(Double.NEGATIVE_INFINITY)                      // ##-INF
    EDN.pprintln(Double.NaN)                                    // ##NaN

    EDN.pprintln(IObj.valueOf(mapOf(12 to 13), 159))            // ^{12 13} 159
}

fun testReader() {
    println(EDN.read("symbol")) // Symbol without namespace
    println(EDN.read("namespace/symbol")) // Symbol
    println(EDN.read(":keyword")) // Keyword without namespace
    println(EDN.read(":namespace/symbol")) // Keyword
    println(EDN.read("\"string\"")) // String
    println(EDN.read("\\c")) // Character
    println(EDN.read("\\o41"))
    println(EDN.read("\\u2626"))

    println(EDN.read("\\newline"))
    println(EDN.read("\\space"))
    println(EDN.read("\\tab"))
    println(EDN.read("\\backspace"))
    println(EDN.read("\\formfeed"))
    println(EDN.read("\\return"))

    println(EDN.read("(list elements)")) // List
    println(EDN.read("[vector elements]")) // Vector
    println(EDN.read("#{set elements}")) // Set
    println(EDN.read("{map-key map-value}")) // Map

    println(EDN.read("0xC0FFEE")) // Long
    println(EDN.read("12648430")) // The same Long
    println(EDN.read("12648430N")) // The same as BigInt
    println(EDN.read("5.0")) // Double
    println(EDN.read("5.0M")) // BigDecimal
    println(EDN.read("5/6")) // Ratio

    println(EDN.read("nil")) // Null
    println(EDN.read("false")) // False
    println(EDN.read("true")) // True

    println(EDN.read("#uuid \"f81d4fae-7dec-11d0-a765-00a0c91e6bf6\"")) // UUID
    println(EDN.read("#inst \"1985-04-12T23:20:50.52Z\"")) // Instant

    println(EDN.read("##INF")) // Infinity
    println(EDN.read("##-INF")) // Negative infinity
    println(EDN.read("##NaN")) // NaN
    println(EDN.read("##-NaN")) // The cooler NaN

    println(EDN.read("^:a abc")) // Meta
}

fun testReaderExtended() {
    /*
    Normally, complex numbers are not allowed, but can be enabled.
     */
    var options = EDN.defaultOptions.copy(allowComplexNumberLiterals = true)
    println(EDN.read("5+7i", options)) // Complex

    // Scheme supports '\xXXXXXXXX' character codes, but the EDN format doesn't.
    // With this option, these char literals can be enabled. They have the type "Char32".
    options = EDN.defaultOptions.copy(allowSchemeUTF32Codes = true)
    println(EDN.read("\\x0001F381", options))

    options = EDN.defaultOptions.copy(allowDispatchChars = true)
    println(EDN.read("#\\o41", options))
    println(EDN.read("#\\u0001F381", options))
    println(EDN.read("#\\x0001F381", options))

    options = EDN.defaultOptions.copy(allowNumericSuffixes = true)
    println(EDN.read("123_i8", options)) // Byte
    println(EDN.read("123_i16", options)) // Short
    println(EDN.read("123_i32", options)) // Int
    println(EDN.read("123_i64", options)) // Long
    println(EDN.read("123L", options)) // Long

    options = EDN.defaultOptions.copy(allowUTFSymbols = true)
    println(EDN.read("\uD83C\uDF81", options)) // Symbol
    println(EDN.read(":\uD83C\uDF81", options)) // Keyword

    println(EDN.read("(1 2 3)") is PersistentList<*>)
    options = EDN.defaultOptions.copy(listToPersistentListConverter = { LinkedList(it) })
    println(EDN.read("(1 2 3)", options) is LinkedList<*>)
    options = EDN.defaultOptions.copy(listToPersistentListConverter = { ArrayList(it) })
    println(EDN.read("(1 2 3)", options) is LinkedList<*>)

    println(EDN.read("[1 2 3]") is PersistentVector<*>)
    options = EDN.defaultOptions.copy(listToPersistentVectorConverter = { LinkedList(it) })
    println(EDN.read("[1 2 3]", options) is LinkedList<*>)
    options = EDN.defaultOptions.copy(listToPersistentVectorConverter = { ArrayList(it) })
    println(EDN.read("[1 2 3]", options) is LinkedList<*>)

    println(EDN.read("{1 2 3 4}") is LinkedHashMap<*, *>)
    options = EDN.defaultOptions.copy(mapToPersistentMapConverter = { HashMap(it) })
    println(EDN.read("{1 2 3 4}", options) is PersistentMap<*, *>)

    println(EDN.read("#{1 2 3 4}") is LinkedHashSet<*>)
    options = EDN.defaultOptions.copy(setToPersistentSetConverter = { HashSet(it) })
    println(EDN.read("#{1 2 3 4}", options) is LinkedHashSet<*>)
}

fun testDecoder() {
    data class Dice(val sides: Int)

    val anyToDice = { it: Any? ->
        if (it is List<*> && it.size == 1 && it[0] is Number) Dice((it[0] as Number).toInt())
        else null
    }

    println(EDN.read("#my/dice [6]", EDN.defaultOptions.copy(ednClassDecoders = mapOf("my/dice" to anyToDice))))

    val opts =
        EDN.defaultOptions.copy(allowMoreEncoderDecoderNames = true, ednClassDecoders = mapOf("dice" to anyToDice))
    println(EDN.read("#dice [6]", opts))
}

fun testWriter() {
    EDN.pprintln(Symbol.symbol("symbol"))                       // symbol
    EDN.pprintln(Symbol.symbol("namespace/symbol"))             // namespace/symbol
    EDN.pprintln(Keyword["keyword"])                            // :keyword
    EDN.pprintln(Keyword["namespace/keyword"])                  // :namespace/keyword

    EDN.pprintln("string")                                      // "string"
    EDN.pprintln('c')                                           // \c
    EDN.pprintln('☦')                                           // \u2626

    // Without special options, Char32 is printed as String.
    EDN.pprintln(Char32(0x0001F381))                            // "🎁"

    // PersistentLists, Sequences and other Iterables (which are not Lists, Sets or Maps) are printed with ().
    EDN.pprintln(PersistentList.of(1, 2, 3))                    // (1, 2, 3)
    EDN.pprintln(sequenceOf(1, 2, 3))                           // (1, 2, 3)
    EDN.pprintln(1..3)                                          // (1, 2, 3)

    // Vectors, Arrays and Lists (other than PersistentList) are all printed with [].
    EDN.pprintln(PersistentVector.of(1, 2, 3))                  // [1, 2, 3]
    EDN.pprintln(listOf(1, 2, 3))                               // [1, 2, 3]
    EDN.pprintln(intArrayOf(1, 2, 3))                           // [1, 2, 3]

    // PersistentSet and other Sets are treated the same
    EDN.pprintln(PersistentSet.of(1, 2, 3))                     // #{1, 2, 3}
    EDN.pprintln(setOf(1, 2, 3))                                // #{1, 2, 3}

    // PersistentMap and other Maps are treated the same
    EDN.pprintln(PersistentMap.of(1 to 2, 3 to 4))              // {1 2, 3 4}
    EDN.pprintln(mapOf(1 to 2, 3 to 4))                         // {1 2, 3 4}

    EDN.pprintln(0xC0FFEE)                                      // 12648430
    EDN.pprintln(12648430)                                      // 12648430
    EDN.pprintln(BigInteger.valueOf(12648430))                  // 12648430N
    EDN.pprintln(5.0f)                                          // 5.0
    EDN.pprintln(5.0)                                           // 5.0
    EDN.pprintln(BigDecimal.valueOf(5.0))                       // 5.0M
    EDN.pprintln(Ratio.valueOf(5, 6))                           // 5/6

    // Complex numbers use their default "toString()" method.
    EDN.pprintln(Complex.valueOf(1.0, 0.5))                     // +1+0.5i

    EDN.pprintln(null)                                          // nil
    EDN.pprintln(true)                                          // true
    EDN.pprintln(false)                                         // false

    EDN.pprintln(UUID.fromString("f81d4fae-7dec-11d0-a765-00a0c91e6bf6")) // #uuid "f81d4fae-7dec-11d0-a765-00a0c91e6bf6"
    EDN.pprintln(Instant.parse("1985-04-12T23:20:50.52Z"))      // #inst "1985-04-12T23:20:50.520Z"

    EDN.pprintln(Double.POSITIVE_INFINITY)                      // ##INF
    EDN.pprintln(Float.POSITIVE_INFINITY)                       // ##INF
    EDN.pprintln(Double.NEGATIVE_INFINITY)                      // ##-INF
    EDN.pprintln(Float.NEGATIVE_INFINITY)                       // ##-INF
    EDN.pprintln(Double.NaN)                                    // ##NaN
    EDN.pprintln(-Double.NaN)                                   // ##NaN

    EDN.pprintln(IObj.valueOf("meta", 159))                     // ^{:tag "meta"} 159
    EDN.pprintln(IObj.valueOf(Symbol.symbol("meta"), 159))      // ^{:tag meta} 159
    EDN.pprintln(IObj.valueOf(Keyword["meta"], 159))            // ^{:meta true} 159
    EDN.pprintln(IObj.valueOf(mapOf(12 to 13), 159))            // ^{12 13} 159
}

fun testEncoder() {
    data class Dice(val sides: Int)

    val encoders: List<Pair<Class<*>, (Any) -> Pair<String, Any?>?>> = listOf(
        Dice::class.java to { "my/dice" to listOf((it as Dice).sides) }
    )
    EDN.pprintln(Dice(20), options = EDN.defaultOptions.copy(ednClassEncoders = encoders))
}

fun main() {
    testReader()
    testEncoder()
    println("\uD83D\uDE28")
}

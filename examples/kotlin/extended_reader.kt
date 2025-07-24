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
